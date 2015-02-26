package robot;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import robot.*;
import manager.RobotState;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.AtCommand;
import com.rapplogic.xbee.api.AtCommandResponse;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeRequest;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.wpan.NodeDiscover;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;
import com.rapplogic.xbee.api.zigbee.ZNetTxStatusResponse;
import com.rapplogic.xbee.util.ByteUtils;


public class ChessbotCommunicator extends Thread
{
    private final static Logger log = Logger.getLogger(ChessbotCommunicator.class);
    
    private XBee xbee = new XBee();

    private ArrayList<XBeeAddress64> nodeAddresses = new ArrayList<XBeeAddress64>();
    private ArrayList<XBeeAddress64> undiscoveredBots = new ArrayList<XBeeAddress64>();
    private ArrayList<XBeeAddress64> botIDLookupList = new ArrayList<XBeeAddress64>(32);
    
    private RobotState robotState;

    private int baudrate;
    private String comPort;
    
	private PacketListener listenForIncomingResponses = new PacketListener()
	{
		public void processResponse(XBeeResponse response) 
		{
			if (response.getApiId() == ApiId.ZNET_RX_RESPONSE) 
			{
				ZNetRxResponse rx = (ZNetRxResponse) response;
				log.debug("RX response is: " + rx);
				robotState.addNewResponse(new Response(rx.getData(), botIDLookupList.indexOf(rx.getRemoteAddress64())));
			}
		}
	};

	private PacketListener listenForIncomingBotIDs = new PacketListener()
	{
		public void processResponse(XBeeResponse response) 
		{
			if (response.getApiId() == ApiId.ZNET_RX_RESPONSE) 
			{
				ZNetRxResponse rx = (ZNetRxResponse) response;
				log.debug("RX response is: " + rx);
				botIDLookupList.set(rx.getData()[1], rx.getRemoteAddress64());
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

    public ChessbotCommunicator(RobotState robotState, String comport, int baudRate) 
    {
        PropertyConfigurator.configure("log/log4j.properties");
        
        this.robotState = robotState;
        
        try 
        {
            xbee.open(comport, baudRate);
        }
        catch (XBeeException e) 
        {
            log.debug("\n[CommunicatorAPI-Constructor]: Cannot open comport: " + comport);
            e.printStackTrace();
        }

        for(int i = 0; i < 32; i++)
        {
        	botIDLookupList.add(null);
        }
        
        setComPort(comport);
    }

    public void run(int numOfNodes) 
    {
    	if(robotState == null)
    		return;

        try {
            initializeCommunication(3, 10000, numOfNodes);
            robotState.setReady(true);
            xbee.addPacketListener(listenForIncomingResponses);
        } catch (Exception ex) {

        }

    	while (true) {
			boolean waitForMovement = false;
			
    		if(robotState.isCommandAvailable()) {
    			Command cmd = robotState.pollNextCommand();
                cmd.setRobotID(3); //This is temporary, since it means that all messages will be sent to robot 3. 

                try {
                    if (cmd instanceof RCCommand) {
                        sendCommand(cmd);
                    } else {
                        sendCommandAndWaitForAck(cmd, 5000, 3);
                        System.out.printf("%x\n", cmd.generatePayload()[0]);
                    }
                } catch (Exception ex) {
                    log.debug(ex);
                }
                    
                if (cmd instanceof MoveToSquareCommand)
                    waitForMovement = true;

                if (cmd instanceof ExecuteCommand && waitForMovement) {
    				robotState.setReady(false);
    				long startTime = System.currentTimeMillis();
    				long timeout = 15000;
    				Response expectedResponse = new Response(cmd.generatePayload(),
                                                             cmd.getRobotID());
    				
    				waitForMovement = false;
    				
    				while ((System.currentTimeMillis() - startTime) < timeout) {
                        // i don't expect this if statement to ever be true
                        // should probably be a equals() instead of ==
    					if (robotState.peekNextResponse() == expectedResponse) {
    						robotState.pollNextResponse();
    						robotState.setReady(true);
    						break;
    					}

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {

                        }
    				}
    			}
			} 

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                
            }
	        	
	        if (robotState.getCloseCommunication()) {
	        	endCommunication();
	        	robotState.setCloseCommunication(false);
	        	return;
	        }
    	}
    }

    public int initializeCommunication(int numOfRetries,
                                       int nodeDiscoveryTimeout,
                                       int numOfNodes)
        throws XBeeException, InterruptedException
    {
    	discoverNodeAddresses(nodeDiscoveryTimeout, numOfNodes);
    	discoverBotAddresses(numOfRetries);
    	
        log.debug("Node Addresses: " + nodeAddresses);
        log.debug("Bot Id lookup Table: " + botIDLookupList);

        if(undiscoveredBots.size() != 0)
        	log.warn(undiscoveredBots.size() + " Undiscovered Bot(s): "
                     +  undiscoveredBots);
        else
        	log.debug("All bots discovered");

        return undiscoveredBots.size();
    }

    public void discoverNodeAddresses(int nodeDiscoveryTimeout, int numOfNodes)
        throws XBeeException, InterruptedException
    {
    	nodeAddresses.clear();
    	numOfNodes = numOfNodes + 1;
    	
		log.debug("Sending node discover command");
		xbee.sendAsynchronous(new AtCommand("ND"));

		xbee.addPacketListener(listenForIncomingNodes);
		
		long startTime = System.currentTimeMillis();
		
		while ((nodeAddresses.size() < numOfNodes) &&
              (System.currentTimeMillis() - startTime) < nodeDiscoveryTimeout)
			Thread.sleep(100);
		
		xbee.removePacketListener(listenForIncomingNodes);

		log.debug("Discovered " + (nodeAddresses.size() - 1) + " Node(s)");
    }

    public void discoverBotAddresses(int numOfRetries)
        throws XBeeException, InterruptedException
    {    	
    	checkForUndiscoveredBots();
    	
		for(int i = 0; i < undiscoveredBots.size(); i++)
			sendCommandAndWaitForAck(undiscoveredBots.get(i),
                                     new ReadBotIDCommand(0),
                                     5000, numOfRetries);

		xbee.addPacketListener(listenForIncomingBotIDs);

		for(int i = 0; i < undiscoveredBots.size(); i++)
			sendCommandAndWaitForAck(undiscoveredBots.get(i),
                                     new ExecuteCommand(0),
                                     5000, numOfRetries);

		Thread.sleep(500);
		xbee.removePacketListener(listenForIncomingBotIDs);
		
		checkForUndiscoveredBots();
    }

    public void checkForUndiscoveredBots()
    {
    	undiscoveredBots.clear();

    	for(int i = 1; i < nodeAddresses.size(); i++)
    		if(!botIDLookupList.contains(nodeAddresses.get(i)))
    			undiscoveredBots.add(nodeAddresses.get(i));
    }

    public void sendCommand(XBeeAddress64 address, Command cmd)
        throws XBeeException, InterruptedException
    {
    	ZNetTxRequest tx = new ZNetTxRequest(address, cmd.generatePayload());
    	tx.setFrameId(0);
    	xbee.sendAsynchronous(tx);
    }

    public void sendCommand(Command cmd)
        throws XBeeException, InterruptedException
    {
    	ZNetTxRequest tx = new ZNetTxRequest(botIDLookupList.get(cmd.getRobotID()), 
                                             cmd.generatePayload());
    	tx.setFrameId(0);
    	xbee.sendAsynchronous(tx);
    }

    public void sendCommandAndWaitForAck(XBeeAddress64 address, Command cmd,
                                         int timeout, int numOfRetries)
        throws XBeeException, InterruptedException
    {
    	ZNetTxRequest tx = new ZNetTxRequest(address, cmd.generatePayload());
    	tx.setFrameId(xbee.getNextFrameId());
    	
    	for(int i = 0; i < numOfRetries; i++) {
            ZNetTxStatusResponse ACK = (ZNetTxStatusResponse) xbee.sendSynchronous(tx, timeout);

    		if(ACK.getDeliveryStatus() == ZNetTxStatusResponse.DeliveryStatus.SUCCESS) {
    			log.debug("Success " + address +  " with Payload "
                          + cmd.generatePayload());
    			if(i != 0)
    				log.debug("Success with " + numOfRetries + " Retry(ies)");

    			return;
    		} else {
	    		log.debug("Failure: " + address);
	    		log.debug("retrying...");
	    	}
    	}
    	
		log.warn("Failure with " + numOfRetries + " Retry(ies)");
    }

    public void sendCommandAndWaitForAck(Command cmd, int timeout, int numOfRetries)
        throws XBeeException, InterruptedException
    {
    	ZNetTxRequest tx = new ZNetTxRequest(botIDLookupList.get(cmd.getRobotID()), 
                                             cmd.generatePayload());
    	tx.setFrameId(xbee.getNextFrameId());

    	for(int i = 0; i < numOfRetries; i++) {
            ZNetTxStatusResponse ACK = (ZNetTxStatusResponse) xbee.sendSynchronous(tx, timeout);

    		if(ACK.getDeliveryStatus() == ZNetTxStatusResponse.DeliveryStatus.SUCCESS) {
    			log.debug("Success: " + botIDLookupList.get(cmd.getRobotID()));
    			if(i != 0)
    				log.debug("Success with " + numOfRetries + " Retry(ies)");

    			return;
    		} else {
	    		log.debug("Failure: " + botIDLookupList.get(cmd.getRobotID()));
	    		log.debug("retrying...");
	    	}
    	}
    	
		log.warn("Failure with " + numOfRetries + " Retry(ies)");
    }
    
    public void setRobotState(RobotState _robotState)
    {
    	robotState = _robotState;
    }

    public ArrayList<XBeeAddress64> returnbotIDLookupList()
    {
    	return botIDLookupList;
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
