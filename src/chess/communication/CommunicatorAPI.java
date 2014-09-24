package chess.communication;

import org.apache.log4j.PropertyConfigurator;

import chess.communication.XBeeAPI.ApiId;
import chess.communication.XBeeAPI.AtCommand;
import chess.communication.XBeeAPI.AtCommandResponse;
import chess.communication.XBeeAPI.PacketListener;
import chess.communication.XBeeAPI.XBee;
import chess.communication.XBeeAPI.XBeeAddress64;
import chess.communication.XBeeAPI.XBeeException;
import chess.communication.XBeeAPI.XBeeResponse;
import chess.communication.XBeeAPI.wpan.NodeDiscover;
import chess.communication.XBeeAPI.zigbee.ZNetRxResponse;
import chess.communication.XBeeAPI.zigbee.ZNetTxRequest;
import chess.communication.XBeeAPI.util.ByteUtils;

public class CommunicatorAPI
{
    /*----------------------------------------------------------------------------------------------
    Test Program: Write any test programs for this class in the main method.    
    ----------------------------------------------------------------------------------------------*/
    public static void main(String[] args) throws XBeeException, InterruptedException  
    {
        PropertyConfigurator.configure("log4j.properties");
        CommunicatorAPI communicator =  new CommunicatorAPI("COM17");
        communicator.FindAllNodes();
        //communicator.GetBotAddresses(nodeAddresses);

        // Test to print all nodes in network
           System.out.println("After running FindAllNodes().");
           /*for(int i = 1; i < numOfConnectedNodes; i++)
               System.out.println(i+":  "+ByteUtils.toBase16(nodeAddresses[i]));*/
       
        //Test to print all nodes in network
            communicator.GetBotAddresses();
            System.out.println();
            for(int i = 0; i < botIdOrderedNodeAddresses.length; i++)
            System.out.println(i+": "+ByteUtils.toBase16(botIdOrderedNodeAddresses[i]));
    
            
        /*Test sending the ID query message
        int[] temp = new int[] {0x0A,0x00,0x00,0x00,0x00,0x00};
        communicator.SendMessage(temp,nodeAddresses[1],0);
        int[] execute = new int[] {0xff,0x00,0x00,0x00,0x00,0x00};
        communicator.SendMessage(execute,nodeAddresses[1],0);           
        */
          
        communicator.EndCommunication();
    }

    /*----------------------------------------------------------------------------------------------
    Class Member Variables
    ----------------------------------------------------------------------------------------------*/
    static XBee xbee = new XBee();

    private static String comPort;
    private static ZNetRxResponse rxPacket;
    private static int[] rxPacketData = new int [6]; 
    private static int numOfConnectedNodes = 0;
    
    private static int[][] nodeAddresses = new int[33][8];
    private static int[][] botIdOrderedNodeAddresses = new int[32][8];
      
    /*----------------------------------------------------------------------------------------------
    Class Member Methods
    ----------------------------------------------------------------------------------------------*/
    public CommunicatorAPI() 
    {
        try 
        {
            xbee.open(comPort, 57600);
        }
        catch (XBeeException e) 
        {
            System.out.println("\n[CommunicatorAPI-Constructor]: Cannot open comport: " + comPort);
            e.printStackTrace();
            System.exit(0);
        }
    }    
    
    public CommunicatorAPI(String _comport) 
    {
        try 
        {
            xbee.open(_comport, 57600);
        }
        catch (XBeeException e) 
        {
            System.out.println("\n[CommunicatorAPI-Constructor]: Cannot open comport: " + _comport);
            e.printStackTrace();
            System.exit(0);
        }

        SetComPort(_comport);
    }

    public synchronized void FindAllNodes() throws XBeeException, InterruptedException
    {
        xbee.sendSynchronous(new AtCommand("NT"));
        AtCommandResponse nodeTimeOut = (AtCommandResponse)xbee.getResponse();
        long nodeDiscoveryTimeOut = ByteUtils.convertMultiByteToInt(nodeTimeOut.getValue())*100;
        
        PacketListener scanForNodes = new PacketListener()
        {
            public void processResponse(XBeeResponse response)
            {
                if(response.getApiId() == ApiId.AT_RESPONSE)
                {
                    NodeDiscover nd = NodeDiscover.parse((AtCommandResponse)response);
                    nodeAddresses[numOfConnectedNodes] = nd.getNodeAddress64().getAddress();
                    numOfConnectedNodes++;
                }
            }
        };
        
        xbee.addPacketListener(scanForNodes);
        
        xbee.sendSynchronous(new AtCommand("ND"));
        Thread.sleep(nodeDiscoveryTimeOut);
    }
    
    public synchronized int[] ReadMessage() throws XBeeException, InterruptedException
    {
        rxPacketData = new int[] {0,0,0,0,0,0};
        PacketListener receivingMessage = new PacketListener()
        {
            public void processResponse(XBeeResponse response)
            {
                if(response.getApiId() == ApiId.ZNET_RX_RESPONSE)
                {
                    rxPacket = (ZNetRxResponse)response;
                    rxPacketData = rxPacket.getData();
                }
            }
        };
        
        xbee.addPacketListener(receivingMessage);
        Thread.sleep(250);
        
        return rxPacketData;
    }
    
    public int[][] GetBotAddresses() throws XBeeException, InterruptedException
    {
        int index = 1;
        
        System.out.println();
        for(index =1; index < numOfConnectedNodes;index++)
            System.out.println(index+": "+ByteUtils.toBase16(nodeAddresses[index]));
        
        System.out.println();
        for(index =1; index < numOfConnectedNodes;index++)
        {
            int[] idRequest = new int[] {0x0A};
            int[] execute = new int[] {0xff};
            SendMessage(idRequest,nodeAddresses[index]);
            SendMessage(execute,nodeAddresses[index]);
            
            int[] message = ReadMessage();
            
            while(message[0] == 0)
            {
                for(int correction = 0; correction < 3; correction++)
                {
                    if(message[0] == 0 && correction < 2)
                    {
                        SendMessage(idRequest,nodeAddresses[index]);
                        SendMessage(execute,nodeAddresses[index]);
                        message = ReadMessage();
                    }
                    else if(message[0] == 0 && correction == 2)
                    {
                        System.out.println("can't get botId from bot with address: "+ nodeAddresses[index]);
                        break;
                    }
                    else
                        break;
                }
            }
            
            System.out.println(index+": This is Bot_"+message[0]);
            botIdOrderedNodeAddresses[(message[0]-1)] = nodeAddresses[index];
            message = new int[] {0,0,0,0,0,0};
        }
        return botIdOrderedNodeAddresses;
    }
    public synchronized void SendMessage(int[] bytearr, int[] destinationAddress, int frameId)
    {
        int[] information = bytearr;
        XBeeAddress64 destination = new XBeeAddress64(destinationAddress);
        ZNetTxRequest request = new ZNetTxRequest(destination,information);
        request.setOption(ZNetTxRequest.Option.UNICAST);
        request.setFrameId(frameId);
        try
        {
            //System.out.println(ByteUtils.toBase16(bytearr)+" Sending...");
            xbee.sendSynchronous(request);
        } 
        catch (XBeeException e) 
        {
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    public synchronized void SendMessage(int[] bytearr, int[] destinationAddress)
    {
        int[] information = bytearr;
        XBeeAddress64 destination = new XBeeAddress64(destinationAddress);
        ZNetTxRequest request = new ZNetTxRequest(destination,information);
        request.setOption(ZNetTxRequest.Option.UNICAST);
        request.setFrameId(0);
        try
        {
            //System.out.println(ByteUtils.toBase16(bytearr)+" Sending...");
            xbee.sendAsynchronous(request);
        } 
        catch (XBeeException e) 
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void SetComPort(String _comport)
    {
        comPort = _comport;
    }
    
    public static String GetCurrentComPort()
    {
        return comPort;
    }
    
    public void EndCommunication()
    {
        xbee.close();
    }
}
