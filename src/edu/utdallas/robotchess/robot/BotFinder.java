package edu.utdallas.robotchess.robot;

import edu.utdallas.robotchess.manager.RobotState;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.AtCommand;
import com.rapplogic.xbee.api.AtCommandResponse;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.NodeDiscover;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;

import java.util.ArrayList;
import java.util.Hashtable;

public class BotFinder extends Thread
{
    private XBee xbee;
    private RobotState robotState;

    private boolean keepAlive = true;
    private long timeout = 30000;
    private final int pruningIndex = 2;

    private final static Logger log = Logger.getLogger(BotFinder.class);

    private ArrayList<XBeeAddress64> nodeAddresses = new ArrayList<XBeeAddress64>();
    private ArrayList<XBeeAddress64> currentNodeAddresses = new ArrayList<XBeeAddress64>();
    private ArrayList<XBeeAddress64> botAddresses = new ArrayList<XBeeAddress64>();

    private Hashtable<Integer, ArrayList<XBeeAddress64>> unresponsiveNodes = new Hashtable<Integer, ArrayList<XBeeAddress64>>();

	private PacketListener listenForIncomingNodes = new PacketListener()
	{
		public void processResponse(XBeeResponse response)
		{
			if (response.getApiId() == ApiId.AT_RESPONSE)
            {
				NodeDiscover nd = NodeDiscover.parse((AtCommandResponse)response);
                XBeeAddress64 addr = nd.getNodeAddress64();

                if(!nodeAddresses.contains(addr))
                    nodeAddresses.add(addr);

                if(!currentNodeAddresses.contains(addr))
                    currentNodeAddresses.add(addr);

                for(int i = 0; i < pruningIndex; i++)
                {
                    if(unresponsiveNodes.get(i).contains(addr))
                        unresponsiveNodes.get(i).remove(addr);
                }

                log.debug(nd);
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
                int[] payload = rx.getData();
                XBeeAddress64 addr = rx.getRemoteAddress64();

                if(payload[0] == 2)
                {
                    botAddresses.set(payload[1], addr);
                    log.debug(rx);
                }

			}
		}
	};

    public BotFinder(XBee xbee, RobotState robotState)
    {
        this.xbee = xbee;
        this.robotState = robotState;

        PropertyConfigurator.configure("log/log4j.properties");

        for(int i = 0; i < 32; i++)
            botAddresses.add(null);

        for(int i = 0; i < pruningIndex; i++)
            unresponsiveNodes.put(i, new ArrayList<XBeeAddress64>());
    }

    @Override
    public void run()
    {
        long startTime = System.currentTimeMillis();

        xbee.addPacketListener(listenForIncomingNodes);
        xbee.addPacketListener(listenForIncomingBotIDs);

        try{ xbee.sendAsynchronous(new AtCommand("ND")); }
        catch(XBeeException e) { log.debug("Couldn't send ND command"); }

        log.debug("Running Node Discovery Thread");

        while(keepAlive)
        {
            if(System.currentTimeMillis() - startTime > this.timeout)
            {
                pruneNodeAddressList();
                pruneBotAddressList();
                currentNodeAddresses.clear();

                try{ xbee.sendAsynchronous(new AtCommand("ND")); }
                catch(XBeeException e) { log.debug("Couldn't send ND command"); }

                startTime = System.currentTimeMillis();
            }

            discoverBots();

            try { Thread.sleep(10000); }
            catch (InterruptedException ex) { }

        }

        xbee.removePacketListener(listenForIncomingNodes);
        xbee.removePacketListener(listenForIncomingBotIDs);
    }

    public ArrayList<XBeeAddress64> GetBotAddresses()
    {
        return botAddresses;
    }

    public void terminate()
    {
        this.keepAlive = false;
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

            robotState.addNewCommand(cmd);
        }
    }

    private void pruneNodeAddressList()
    {
        ArrayList<XBeeAddress64> currentUnresponsiveNodes = new ArrayList<XBeeAddress64>();

        currentUnresponsiveNodes.addAll(nodeAddresses);
        currentUnresponsiveNodes.removeAll(currentNodeAddresses);

        for(int i = 0; i < currentUnresponsiveNodes.size(); i++)
        {
            for(int j = pruningIndex - 1; j >= 0; j--)
            {
                if(unresponsiveNodes.get(j).contains(currentUnresponsiveNodes.get(i)))
                {
                    unresponsiveNodes.get(j).remove(currentUnresponsiveNodes.get(i));

                    if(j == pruningIndex - 1)
                    {
                        nodeAddresses.remove(currentUnresponsiveNodes.get(i));
                        log.debug("Removing Node: " + currentUnresponsiveNodes.get(i));
                    }

                    else
                        unresponsiveNodes.get(j + 1).add(currentUnresponsiveNodes.get(i));

                    break;

                }
                else if(j == 0)
                    unresponsiveNodes.get(j).add(currentUnresponsiveNodes.get(i));

                log.debug(unresponsiveNodes.get(i));
            }
        }
    }

    private void pruneBotAddressList()
    {
        for(int i = 0; i < botAddresses.size(); i++)
            if(!nodeAddresses.contains(botAddresses.get(i)))
                botAddresses.set(i, null);
    }

    private void setTimeout(long timeout)
    {
        if(timeout > 0)
            this.timeout = timeout;
    }
}
