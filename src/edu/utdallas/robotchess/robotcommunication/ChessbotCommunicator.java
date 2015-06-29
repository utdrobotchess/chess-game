package edu.utdallas.robotchess.robotcommunication;

import edu.utdallas.robotchess.robotcommunication.commands.*;
import edu.utdallas.robotchess.robotcommunication.responses.*;

import org.apache.log4j.*;
import gnu.io.CommPortIdentifier;
import java.util.Enumeration;
import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.zigbee.*;

public class ChessbotCommunicator
{
    private final static Logger log = Logger.getLogger(ChessbotCommunicator.class);

    private static ChessbotCommunicator instance = null;

    private XBee xbee = new XBee();
    private BotFinder botFinder;

    private int baudrate;
    private String comport;

	private PacketListener listenForIncomingResponses = new PacketListener()
	{
		public void processResponse(XBeeResponse response)
		{
			if (response.getApiId() == ApiId.ZNET_RX_RESPONSE)
            {
                @SuppressWarnings("unused")
				ZNetRxResponse rx = (ZNetRxResponse) response; //TODO: Do something with response
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
        PropertyConfigurator.configure("log/log4j.properties");

        this.baudrate = 57600; // check xbee for this in the future
        SearchForXbeeOnComports();

        if(xbee == null)
        {
            log.debug("Cannot run ChessbotCommunicator Thread since no XBee on Comport");
            return;
        }

        botFinder = new BotFinder(xbee, this); //This may be a strange way of doing this....
        botFinder.start();

        xbee.addPacketListener(listenForIncomingResponses);
    }

    public void endCommnication()
    {
        xbee.close();
    }

    public void sendCommand(Command cmd)
    {
        int[] payload = cmd.generatePayload();
        XBeeAddress64 addr = new XBeeAddress64();

        if(cmd.GetXbeeAddress() != null)
            addr = cmd.GetXbeeAddress();
        else
            addr = botFinder.GetBotAddresses().get(cmd.getRobotID());

        if(addr == null)
        {
            log.debug("Cannot send packet. It has a null address");
            return;
        }

        ZNetTxRequest tx = new ZNetTxRequest(addr, payload);

        if(cmd.RequiresACK())
        {
            tx.setFrameId(xbee.getNextFrameId());

            for(int i = 0; i < cmd.getRetries(); i++)
            {
                try
                {
                    ZNetTxStatusResponse ACK = (ZNetTxStatusResponse) xbee.sendSynchronous(tx, cmd.getTimeout());

                    if(ACK.getDeliveryStatus() == ZNetTxStatusResponse.DeliveryStatus.SUCCESS)
                        break;
                }
                catch(XBeeException e) { log.debug("Couldn't send packet to Coordinator XBee. Make sure it is plugged in"); }
            }

        }
        else
        {
            tx.setFrameId(0);
            try { xbee.sendAsynchronous(tx); }
            catch(XBeeException e) { log.debug("Couldn't send packet to Coordinator XBee. Make sure it is plugged in"); }
        }

        log.debug("Sent Command");
    }

    public void SearchForXbeeOnComports()
    {
        @SuppressWarnings("unchecked")
        Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();

        String osName = System.getProperty("os.name");
        String portName = null;

        boolean foundXbee = false;

        log.debug("Searching Comports for XBee");

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
                    this.comport = pid.getName();
                    try
                    {
                        xbee.open(this.comport, this.baudrate);
                        log.debug("Found XBee on comport" + this.comport);
                        foundXbee = true;
                        break;
                    }
                    catch(XBeeException e)
                    {
                        log.debug("Did not find XBee on comport " + this.comport);
                    }

                }
            }
        }

        if(!foundXbee)
        {
            log.debug("Couldn't find Xbee on any available COMPORT");
            xbee = null;
            this.comport = null;
        }
    }
}
