package edu.utdallas.robotchess.robot;

import org.apache.log4j.*;
import gnu.io.CommPortIdentifier;
import java.util.Enumeration;
import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.zigbee.*;

public class ChessbotCommunicator extends Thread
{
    private final static Logger log = Logger.getLogger(ChessbotCommunicator.class);

    private XBee xbee = new XBee();
    private BotFinder botFinder;

    private boolean keepAlive = true;
    private int baudrate;
    private String comport;

	private PacketListener listenForIncomingResponses = new PacketListener()
	{
		public void processResponse(XBeeResponse response)
		{
			if (response.getApiId() == ApiId.ZNET_RX_RESPONSE)
            {
				ZNetRxResponse rx = (ZNetRxResponse) response;
				//robotState.addNewResponse(new Response(rx.getData(), botFinder.GetBotAddresses().indexOf(rx.getRemoteAddress64())));
			}
		}
	};

    public ChessbotCommunicator(int baud)
    {
        PropertyConfigurator.configure("log/log4j.properties");

        this.baudrate = baud;
        SearchForXbeeOnComports();
    }

    @Override
    public void run()
    {
        if(xbee == null)
        {
            log.debug("Cannot run ChessbotCommunicator Thread since no XBee on Comport");
            return;
        }

        log.debug("Running ChessbotCommunicator Thread");

        botFinder = new BotFinder(xbee);
        botFinder.start();

        xbee.addPacketListener(listenForIncomingResponses);

        while (keepAlive)
        {
            // if(robotState.isCommandAvailable())
            // {
            //     Command cmd = robotState.pollNextCommand();
            //     sendCommand(cmd);
            // }

            try { Thread.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        log.debug("Terminating ChessbotCommunicator Thread");

        botFinder.terminate();

        xbee.close();//TODO fix this method so that it doesn't crash our program.
    }

    public void terminate()
    {
        keepAlive = false;
    }

    public void WaitForFinishedMovement(Command cmd)
    {
        long startTime = System.currentTimeMillis();
        long timeout = 15000;
        Response expectedResponse = new Response(cmd.generatePayload(), cmd.getRobotID());

        while ((System.currentTimeMillis() - startTime) < timeout)
        {
            // if (robotState.peekNextResponse() == expectedResponse)
            // {
            //     robotState.pollNextResponse();
            //     break;
            // }

            try { Thread.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
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
