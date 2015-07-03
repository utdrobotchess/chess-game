package edu.utdallas.robotchess.robotcommunication;

import edu.utdallas.robotchess.robotcommunication.commands.Command;
import edu.utdallas.robotchess.robotcommunication.commands.ReadBotIDCommand;

import org.apache.log4j.*;

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

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;

public class ChessbotCommunicator
{
    private final static Logger log = Logger.getLogger(ChessbotCommunicator.class);
    private static ChessbotCommunicator instance = null;
    private XBee xbee;

    private ArrayList<XBeeAddress64> nodeAddresses = new ArrayList<XBeeAddress64>();
    private ArrayList<XBeeAddress64> botAddresses = new ArrayList<XBeeAddress64>();

    private PacketListener nodeDiscoverResponseListener = new PacketListener()
    {
        public void processResponse(XBeeResponse response)
        {
            if (response.getApiId() == ApiId.AT_RESPONSE)
            {
                NodeDiscover nd = NodeDiscover.parse((AtCommandResponse)response);
                XBeeAddress64 addr = nd.getNodeAddress64();

                if(!nodeAddresses.contains(addr))
                {
                    nodeAddresses.add(addr);
                    log.debug(nd);
                }
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
                        botAddresses.set(payload[1], addr);
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

        for(int i = 0; i < 32; i++)
            botAddresses.add(null);
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
            //restarting the program.
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
                return false;
            }
        }
        else
            return false;
    }

    public int returnNumberofConnectedChessbots()
    {
        return 0; //still need to implement this
    }

    public void endCommnication()
    {
        xbee.close(); //There is an issue with this method crashing the program
    }

    public ArrayList<XBeeAddress64> GetBotAddresses()
    {
        return botAddresses;
    }

    private void discoverBots()
    {
        ArrayList<XBeeAddress64> undiscoveredBots = new ArrayList<XBeeAddress64>();

        for(int i = 0; i < nodeAddresses.size(); i++)
            if(!botAddresses.contains(nodeAddresses.get(i)))
                undiscoveredBots.add(nodeAddresses.get(i));

        for(int i = 0; i < undiscoveredBots.size(); i++)
        {
            XBeeAddress64 addr = undiscoveredBots.get(i);
            ReadBotIDCommand cmd = new ReadBotIDCommand(addr);

            sendCommand(cmd);
        }
    }


    public void sendCommand(Command cmd)
    {
        int[] payload = cmd.generatePayload();
        XBeeAddress64 addr = new XBeeAddress64();

        if(cmd.GetXbeeAddress() != null)
            addr = cmd.GetXbeeAddress();
        else
            // addr = botFinder.GetBotAddresses().get(cmd.getRobotID());

        if(addr == null)
        {
            log.debug("Cannot send packet. It has a null address");
            return;
        }

        ZNetTxRequest tx = new ZNetTxRequest(addr, payload);

        if(cmd.RequiresACK())
        {
            for(int i = 0; i < cmd.getRetries(); i++)
            {
                try
                {
                    ZNetTxStatusResponse ACK = (ZNetTxStatusResponse) xbee.sendSynchronous(tx, cmd.getTimeout());

                    if(ACK.getDeliveryStatus() == ZNetTxStatusResponse.DeliveryStatus.SUCCESS)
                        break;
                }
                catch(XBeeException e) { log.debug("Couldn't send packet to Coordinator XBee. Make sure it is connected"); }
            }

        }
        else
        {
            tx.setFrameId(0);
            try { xbee.sendAsynchronous(tx); }
            catch(XBeeException e) { log.debug("Couldn't send packet to Coordinator XBee. Make sure it is connected"); }
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
                    try
                    {
                        xbee.open(comport, 57600);
                        foundXbee = true;
                        break;
                    }
                    catch(XBeeException e)
                    {
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
