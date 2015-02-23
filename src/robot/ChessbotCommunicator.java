package robot;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.Hashtable;

import robot.*;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.AtCommand;
import com.rapplogic.xbee.api.AtCommandResponse;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.wpan.NodeDiscover;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;
import com.rapplogic.xbee.api.zigbee.ZNetTxStatusResponse;
import com.rapplogic.xbee.util.ByteUtils;


public class ChessbotCommunicator extends Thread
{
    public static void main(String[] args) throws XBeeException, InterruptedException  
    {
        PropertyConfigurator.configure("log4j.properties");
        ChessbotCommunicator comms =  new ChessbotCommunicator("/dev/tty.usbserial-A800f3fq", 57600);
        comms.initializeCommunication(5, 5000);

        comms.endCommunication();
    }

    private final static Logger log = Logger.getLogger(NodeDiscoverTest.class);
    
    private XBee xbee = new XBee();

    private ArrayList<XBeeAddress64> nodeAddresses = new ArrayList<XBeeAddress64>();
    private ArrayList<XBeeAddress64> undiscoveredBots = new ArrayList<XBeeAddress64>();
    private Hashtable<Integer, XBeeAddress64> botIDLookupTable = new Hashtable<Integer, XBeeAddress64>();    

    private int baudrate;
    private String comPort;

	private PacketListener listenForIncomingBotIDs = new PacketListener()
	{
		public void processResponse(XBeeResponse response) 
		{
			if (response.getApiId() == ApiId.ZNET_RX_RESPONSE) 
			{
				ZNetRxResponse rx = (ZNetRxResponse) response;
				log.debug("RX response is: " + rx);
				botIDLookupTable.put(rx.getData()[0], rx.getRemoteAddress64());
			} 
		}
	};

	private PacketListener listenForIncomingNodes = new PacketListener()
	{
		public void processResponse(XBeeResponse response) 
		{
			if (response.getApiId() == ApiId.AT_RESPONSE) 
			{
				NodeDiscover nd = NodeDiscover.parse((AtCommandResponse)response);
				log.debug("Node discover response is: " + nd);
				nodeAddresses.add(nd.getNodeAddress64());
			}
		}
	};

    public ChessbotCommunicator(String _comport, int _baudRate) 
    {
        try 
        {
            xbee.open(_comport, _baudRate);
        }
        catch (XBeeException e) 
        {
            System.out.println("\n[CommunicatorAPI-Constructor]: Cannot open comport: " + _comport);
            e.printStackTrace();
        }

        setComPort(_comport);
    }

    public void run()
    {
    	while(true)
    	{

        	try
        	{
        		Thread.sleep(10);
        	}
        	catch(InterruptedException ex){}
    	}
    }

    public int initializeCommunication(int numOfRetries, int nodeDiscoveryTimeout) throws XBeeException, InterruptedException
    {
    	discoverNodeAddresses(nodeDiscoveryTimeout);
    	discoverBotAddresses();

    	int i = 0;
    	if(undiscoveredBots.size() != 0)

    	{
	    	log.debug("Undiscovered Bots:" +  undiscoveredBots);

	        while(i < numOfRetries)
	        {
	        	log.debug("Retrying");
	        	i++;

	        	discoverBotAddresses();
	        	if(undiscoveredBots.size() == 0)
	        	{
	        		break;
	        	}
	        }
    	}

        log.debug("Number of Retries was " + i);
        log.debug("Node Addresses" + nodeAddresses);
        log.debug("Bot Id lookup Tables" + botIDLookupTable);
        if(undiscoveredBots.size() != 0)
        {
        	log.debug(undiscoveredBots.size() + " Undiscovered Bot(s): " +  undiscoveredBots);
        }
        else
        {
        	log.debug("All bots discovered");
        }

        return undiscoveredBots.size();
    }

    public void discoverNodeAddresses(int nodeDiscoveryTimeout) throws XBeeException, InterruptedException
    {
    	nodeAddresses.clear();

		log.debug("Sending node discover command");
		xbee.sendAsynchronous(new AtCommand("ND"));

		xbee.addPacketListener(listenForIncomingNodes);
		Thread.sleep(nodeDiscoveryTimeout);		
		xbee.removePacketListener(listenForIncomingNodes);

		log.debug("Discovered " + (nodeAddresses.size() - 1) + " Nodes");
		checkForUndiscoveredBots();
		
    }

    public void discoverBotAddresses() throws XBeeException, InterruptedException
    {    	

		for(int i = 0; i < undiscoveredBots.size(); i++)
		{
			sendCommandAndWaitForAck(undiscoveredBots.get(i), new ReadBotIDCommand(0), 5000);
		}

		xbee.addPacketListener(listenForIncomingBotIDs);

		for(int i = 0; i < undiscoveredBots.size(); i++)
		{
			sendCommandAndWaitForAck(undiscoveredBots.get(i), new ExecuteCommand(0), 5000);
		}

		Thread.sleep(500);
		xbee.removePacketListener(listenForIncomingBotIDs);

		checkForUndiscoveredBots();
    }

    public void checkForUndiscoveredBots()
    {
    	undiscoveredBots.clear();

    	for(int i = 1; i < nodeAddresses.size(); i++)
    	{
    		if(!(botIDLookupTable.contains(nodeAddresses.get(i))))
    		{
    			undiscoveredBots.add(nodeAddresses.get(i));
    		}
    	}
    }

    public void sendCommand(XBeeAddress64 address, Command cmd) throws XBeeException, InterruptedException
    {
    	ZNetTxRequest tx = new ZNetTxRequest(address, cmd.generatePayload());
    	tx.setFrameId(0);
    	xbee.sendAsynchronous(tx);
    }

    public void sendCommand(Command cmd) throws XBeeException, InterruptedException
    {
    	ZNetTxRequest tx = new ZNetTxRequest(botIDLookupTable.get(cmd.getRobotID()), cmd.generatePayload());
    	tx.setFrameId(0);
    	xbee.sendAsynchronous(tx);
    }

    public void sendCommandAndWaitForAck(XBeeAddress64 address, Command cmd, int timeout) throws XBeeException, InterruptedException
    {
    	ZNetTxRequest tx = new ZNetTxRequest(address, cmd.generatePayload());
    	tx.setFrameId(xbee.getNextFrameId());
    	ZNetTxStatusResponse ACK = (ZNetTxStatusResponse) xbee.sendSynchronous(tx, timeout);

    	if(ACK.getDeliveryStatus() == ZNetTxStatusResponse.DeliveryStatus.SUCCESS)
    	{
    		log.debug("Success: " + address);
    	}
    	else
    	{
    		log.debug("Failure: " + address);
    	}
    }

    public void sendCommandAndWaitForAck(Command cmd, int timeout) throws XBeeException, InterruptedException
    {
    	ZNetTxRequest tx = new ZNetTxRequest(botIDLookupTable.get(cmd.getRobotID()), cmd.generatePayload());
    	tx.setFrameId(xbee.getNextFrameId());
    	ZNetTxStatusResponse ACK = (ZNetTxStatusResponse) xbee.sendSynchronous(tx, timeout);

    	if(ACK.getDeliveryStatus() == ZNetTxStatusResponse.DeliveryStatus.SUCCESS)
    	{
    		log.debug("Success: " + botIDLookupTable.get(cmd.getRobotID()));
    	}
    	else
    	{
    		log.debug("Failure: " + botIDLookupTable.get(cmd.getRobotID()));
    	}
    }

    public Hashtable<Integer, XBeeAddress64> returnBotIDLookupTable()
    {
    	return botIDLookupTable;
    }

    public void setBaud(int _baud)
    {
        baudrate = _baud;
    }

    public int returnBaudrate()
    {
        return baudrate;
    } 

    public ArrayList<XBeeAddress64> returnNodeAddresses()
    {
    	return nodeAddresses;
    }

    public void setComPort(String _comport)
    {
        comPort = _comport;
    }

    public String returnComPort()
    {
        return comPort;
    }

    public void endCommunication()
    {
        xbee.close();
    }
}
