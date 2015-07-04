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

import edu.utdallas.robotchess.robotcommunication.commands.Command;
import edu.utdallas.robotchess.robotcommunication.commands.ReadBotIDCommand;
import gnu.io.CommPortIdentifier;

public class ChessbotCommunicator
{
    private final static Logger log = Logger.getLogger(ChessbotCommunicator.class);
    private static ChessbotCommunicator instance = null;
    private XBee xbee;
    private ChessbotInfoArrayHandler chessbots;

    private Thread discoverChessbotThread = new Thread()
    {
        public void run(){
            try {
                xbee.sendAsynchronous(new AtCommand("ND"));
            } catch(XBeeException e) {
                log.debug("Couldn't send ND command", e);
            }

            xbee.addPacketListener(nodeDiscoverResponseListener);

            try {
                Thread.sleep(10000); //May need to change this time after testing
            } catch (InterruptedException e) {
                log.debug("discoverChessbotThread interrupted", e);
            }

            ArrayList<XBeeAddress64> undiscoveredBots = chessbots.getAddressesWithNullIds();

            for (XBeeAddress64 addr : undiscoveredBots) {
                ReadBotIDCommand cmd = new ReadBotIDCommand(addr);
                sendCommand(cmd); //Don't know whether or not to send synchrnous
            }
        }

    };

    private PacketListener nodeDiscoverResponseListener = new PacketListener()
    {
        public void processResponse(XBeeResponse response)
        {
            if (response.getApiId() == ApiId.AT_RESPONSE)
            {
                NodeDiscover nd = NodeDiscover.parse((AtCommandResponse)response);
                XBeeAddress64 addr = nd.getNodeAddress64();
                chessbots.add(addr);
            }
        }
    };

    private PacketListener transmitResponseListener = new PacketListener()
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
                        chessbots.add(addr, (Integer) payload[1]);
                        log.debug(rx);
                        break;
                }
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
            xbee.addPacketListener(transmitResponseListener);
            log.debug("Added a transmitResponseListener PacketListener");
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
        //may change this later for cleaner interface between this class and
        //manager class. Ideally, we want to create a panel that shows all of
        //the relevant information stored in the chessbots data structure.
        return chessbots;
    }

    public boolean allChessbotsConnected()
    {
        return chessbots.allChessbotsConnected();
    }

    public void discoverChessbots()
    {
        discoverChessbotThread.start();
    }

    public void sendCommand(Command cmd)
    {
        int[] payload = cmd.generatePayload();
        XBeeAddress64 addr = new XBeeAddress64();

        if(cmd.GetXbeeAddress() != null)
            addr = cmd.GetXbeeAddress();
        else
            addr = chessbots.getAddressFromId(cmd.getRobotID());

        if(addr == null)
        {
            log.debug("Cannot send packet. It has a null address");
            return;
        }

        ZNetTxRequest tx = new ZNetTxRequest(addr, payload);

        //I'm going to change how (below) is done. Probably, I will not use
        //retries and instead create a thread for each command that requires an
        //ACK. I'm only worried about how the Xbee class will internal handle
        //multiple threads writing at a time... I know that sendSynchronous()
        //is thread safe, but I don't know if threads will just end up exiting
        //without sending their information if another thread is currently
        //writing to the serial port.
        if(cmd.RequiresACK())
        {
            for(int i = 0; i < cmd.getRetries(); i++)
            {
                try {
                    ZNetTxStatusResponse ACK = (ZNetTxStatusResponse) xbee.sendSynchronous(tx, cmd.getTimeout());

                    if(ACK.getDeliveryStatus() == ZNetTxStatusResponse.DeliveryStatus.SUCCESS)
                        break;
                } catch(XBeeException e) {
                    log.debug("Couldn't send packet to Coordinator XBee. Make sure it is connected");
                }
            }

        }
        else
        {
            tx.setFrameId(0);
            try {
                xbee.sendAsynchronous(tx);
            } catch(XBeeException e) {
               log.debug("Couldn't send packet to Coordinator XBee. Make sure it is connected");
            }
        }

        log.debug("Sent Command");
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
}
