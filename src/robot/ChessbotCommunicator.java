package robot;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;

import manager.RobotState;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;
import com.rapplogic.xbee.api.zigbee.ZNetTxStatusResponse;


public class ChessbotCommunicator extends Thread
{
    private final static Logger log = Logger.getLogger(ChessbotCommunicator.class);

    private XBee xbee = new XBee();
    private RobotState robotState;
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
				robotState.addNewResponse(new Response(rx.getData(),
                                                       botFinder.GetBotAddresses().indexOf(rx.getRemoteAddress64())));
			}
		}
	};

    public ChessbotCommunicator(RobotState robotState, int baud)
    {
        PropertyConfigurator.configure("log/log4j.properties");

        log.debug("Searching Comports for XBee");
        SearchForXbeeOnComports();

        try
        {
            xbee.open(this.comport, baud);
            log.debug("Found XBee on comport");
        }
        catch (XBeeException e)
        {
            log.debug("Couldn't find Xbee on any available COMPORT");
        }

        botFinder = new BotFinder(xbee, robotState);
        this.baudrate = baud;
        this.robotState = robotState;
    }

    @Override
    public void run()
    {
        log.debug("Running ChessbotCommunicator Thread");

    	if(robotState == null)
    		return;

        botFinder.start();
        robotState.setReady(true);

        xbee.addPacketListener(listenForIncomingResponses);

    	while (keepAlive)
        {
    		if(robotState.isCommandAvailable())
            {
    			Command cmd = robotState.pollNextCommand();
                sendCommand(cmd);
            }

            try { Thread.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }

    	}
        terminate();
    }

    public void terminate()
    {
        botFinder.terminate();
        xbee.close();
        keepAlive = false;
    }

    public void WaitForFinishedMovement(Command cmd)
    {
        long startTime = System.currentTimeMillis();
        long timeout = 15000;
        Response expectedResponse = new Response(cmd.generatePayload(), cmd.getRobotID());

        while ((System.currentTimeMillis() - startTime) < timeout)
        {
            if (robotState.peekNextResponse() == expectedResponse)
            {
                robotState.pollNextResponse();
                break;
            }

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
        Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        String osName = System.getProperty("os.name");
        String portName = null;

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
                    this.comport = pid.getName();
            }
        }

    }
}
