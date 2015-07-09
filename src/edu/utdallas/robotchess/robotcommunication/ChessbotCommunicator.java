package edu.utdallas.robotchess.robotcommunication;

import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.AtCommand;
import com.rapplogic.xbee.api.AtCommandResponse;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.NodeDiscover; //originally had wpan.NodeDiscover
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;
import com.rapplogic.xbee.api.zigbee.ZNetTxStatusResponse;
import com.rapplogic.xbee.util.ByteUtils;

import edu.utdallas.robotchess.robotcommunication.commands.Command;
import edu.utdallas.robotchess.robotcommunication.commands.ReadBotIDCommand;

import gnu.io.CommPortIdentifier;

public class ChessbotCommunicator
{
    private final static Logger log = Logger.getLogger(ChessbotCommunicator.class);
    private static ChessbotCommunicator instance = null;
    private XBee xbee;
    private ChessbotInfoArrayHandler chessbots;
    //I don't like using this flag, but it seemed necessary at the time
    private boolean discoveringChessbots = false;

    private PacketListener nodeDiscoverResponseListener = new PacketListener()
    {
        public void processResponse(XBeeResponse response)
        {
            if (response.getApiId() == ApiId.AT_RESPONSE)
            {
                NodeDiscover nd = NodeDiscover.parse((AtCommandResponse)response);
                XBeeAddress64 addr = nd.getNodeAddress64();
                log.debug("Found Address: " + addr);//temp
                chessbots.add(addr);
            }
        }
    };

    private PacketListener txResponseListener = new PacketListener()
    {
        public void processResponse(XBeeResponse response)
        {
            if (response.getApiId() == ApiId.ZNET_RX_RESPONSE)
            {
                ZNetRxResponse rx = (ZNetRxResponse) response;
                XBeeAddress64 addr = rx.getRemoteAddress64();

                int[] payload = rx.getData();

                switch (payload[0]) {
                    case 2:
                        log.debug("Adding ID: " + payload[1] + " to address " + addr);//temp
                        chessbots.add(addr, (Integer) payload[1]);
                        break;
                }

                chessbots.updateMessageReceived(addr, rx);
                log.debug(chessbots); //temp
            }
        }
    };

    public static ChessbotCommunicator create()
    {
        if (instance == null)
            instance = new ChessbotCommunicator();

        return instance;
    }

    private ChessbotCommunicator()
    {
        PropertyConfigurator.configure("log/log4j.properties"); //Should migrate this to all source code for logging
        xbee = new XBee();
        chessbots = new ChessbotInfoArrayHandler();
    }

    public boolean initializeCommunication()
    {
        if(isConnected())
            return true;

        boolean successful = SearchForXbeeOnComports();

        if(successful)
        {
            xbee.addPacketListener(txResponseListener);
            log.debug("Added a txResponseListener PacketListener");
            //There is a case here where the xbee could be connected, but we
            //fail to add the packetlistener. I haven't seen this happen, so
            //I'm more worried about avoiding adding duplicate packetListeners
            //than I am about the aforementioned case. However, if this does
            //happen, there is no way to add the packetListener without
            //restarting the program and trying to reconnect.
        }
        return successful;
    }

    public boolean isConnected()
    {
        if(xbee.isConnected())
        {
            try {
                xbee.sendSynchronous(new AtCommand("ID"), 500);
                return true;
            } catch(XBeeException e){
                //If the serial connection is up, but the xbee is not
                //connected, we should close the serial connection to allow
                //reconnection the next time we press the "Connect to Xbee"
                //button. We should use the method below, however it seems to
                //crash the program. See issue #2 on our github repo.

                //xbee.close()
                return false;
            }
        }
        else
            return false;
    }

    public void endCommnication()
    {
        //There is an issue with this method crashing the program. See issue #2
        //on our github repo
        xbee.close();
    }

    public ChessbotInfoArrayHandler getChessbotInfo()
    {
        return chessbots;
    }

    public boolean allChessbotsConnected()
    {
        return chessbots.allChessbotsConnected();
    }

    public boolean isDiscoveringChessbots()
    {
        return discoveringChessbots;
    }

    public void discoverChessbots()
    {
        if(discoveringChessbots)
            return;

        DiscoverChessbotThread discoverChessbotThread = new DiscoverChessbotThread();
        discoverChessbotThread.start();
    }

    class DiscoverChessbotThread extends Thread {

        @Override
        public void run() {
            int timeout = 0;
            discoveringChessbots = true;

            log.debug("Discovering..."); //temp

            try {
                @SuppressWarnings("deprecation")
                AtCommandResponse nodeTimeout = xbee.sendAtCommand(new AtCommand("NT"));
                timeout = ByteUtils.convertMultiByteToInt(nodeTimeout.getValue()) * 100;
            } catch(XBeeException e){
                log.debug("Couldn't send NT command", e);
            }

            log.debug("NT command successful. Timeout: " + (float)timeout/(float)1000 + " seconds"); //temp

            try {
                xbee.addPacketListener(nodeDiscoverResponseListener);
                xbee.sendSynchronous(new AtCommand("ND"), 5000);
            } catch(XBeeException e) {
                log.debug("Couldn't send ND command", e);
            }

            log.debug("ND command successful. Listening for responses..."); //temp

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                log.debug("discoverChessbotThread interrupted", e);
            }

            ArrayList<XBeeAddress64> undiscoveredBots = chessbots.getAddressesWithNullIds();
            log.debug("Undiscovered bots: " + undiscoveredBots); //temp

            for (XBeeAddress64 addr : undiscoveredBots) {
                ReadBotIDCommand cmd = new ReadBotIDCommand(addr);
                cmd.setACK(true);
                sendCommand(cmd);
            }

            xbee.removePacketListener(nodeDiscoverResponseListener); //This should be called any time the
                                                                     //thread stops (even if it is interrupted)
        }

    };


    public void sendCommand(Command cmd)
    {
        int[] payload = cmd.generatePayload();
        XBeeAddress64 addr = new XBeeAddress64();

        if(cmd.GetXbeeAddress() != null)
            addr = cmd.GetXbeeAddress();
        else
            addr = chessbots.getAddressFromId(cmd.getRobotID());

        if(addr == null) {
            log.debug("Cannot send packet. It has a null address");
            return;
        }

        ZNetTxRequest tx = new ZNetTxRequest(addr, payload);

        if(cmd.getACK()) {
            SendSynchronousThread t = new SendSynchronousThread(cmd, tx, addr);
            t.start();
        }
        else {
            tx.setFrameId(0);
            try {
                xbee.sendAsynchronous(tx);
            } catch(XBeeException e) {
                log.debug("Couldn't send packet to Coordinator XBee. Make sure it is connected");
                return;
            }
        }

        log.debug("Sent Command"); //temp
    }

    //Should be "implements Runnable". But when I correct it, java is unable to
    //find the start() method for this class. At this point, I'm not sure what
    //practical issues I'll run into by extending Thread instead of the correct
    //way
    private class SendSynchronousThread extends Thread
    {
        private Command cmd;
        private ZNetTxRequest tx;
        private XBeeAddress64 addr;

        public SendSynchronousThread(Command cmd, ZNetTxRequest tx, XBeeAddress64 addr) {
            this.cmd = cmd;
            this.tx = tx;
            this.addr = addr;
        }

        public void run() {
            tx.setFrameId(xbee.getNextFrameId());

            try {
                ZNetTxStatusResponse ACK = (ZNetTxStatusResponse) xbee.sendSynchronous(tx, cmd.getTimeout());
                boolean deliveryStatus = (ACK.getDeliveryStatus() == ZNetTxStatusResponse.DeliveryStatus.SUCCESS);
                log.debug("Got ACK. Delivery Status: " + deliveryStatus); //temp
                chessbots.updateMessageSent(addr, tx, deliveryStatus);
            } catch(XBeeException e) {
                log.debug("Couldn't send packet to Coordinator XBee. Make sure it is connected");
            }
        }
    }


    public boolean SearchForXbeeOnComports()
    {
        if(isConnected())
            return true;

        @SuppressWarnings("unchecked")
        Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();

        String osName = System.getProperty("os.name");
        String portName = null;
        boolean foundXbee = false;
        String comport;

        if (osName.equalsIgnoreCase("Mac OS X"))
            portName = "tty.usbserial";
        else if (osName.equalsIgnoreCase("Linux"))
            portName = "ttyUSB";

        if(portName != null)
        {
            while (portIdentifiers.hasMoreElements())
            {
                CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();

                if (pid.getPortType() == CommPortIdentifier.PORT_SERIAL && !pid.isCurrentlyOwned() && pid.getName().contains(portName))
                {
                    comport = pid.getName();
                    try {
                        xbee.open(comport, 57600);
                        foundXbee = true;
                        break;
                    } catch(XBeeException e) {
                        log.debug("Did not find XBee on comport " + comport);
                    }

                }
            }
        }

        if(!foundXbee)
            return false;
        else
            return true;
    }

    public long getBaudrate() {
        //TODO: Implement
        return 0;
    }

    public void setBaudrate(long baudrate) {
        //TODO: Implement
    }
}
