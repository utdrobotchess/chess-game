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
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		/*	Test Program: Write any test programs for this class in the main method.	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		public static void main(String[] args) throws XBeeException, InterruptedException  
		{
			
			PropertyConfigurator.configure("log4j.properties");
			CommunicatorAPI communicator =  new CommunicatorAPI();
			communicator.InitializeCommunication();
			
			//Use the following to test sending messages.
//				int[] temp = new int[] {0x0A,0x00,0x00,0x00,0x00,0x00};
//				communicator.SendMessage(temp,nodeAddresses[1],0);
//				int[] execute = new int[] {0xff,0x00,0x00,0x00,0x00,0x00};
//				communicator.SendMessage(execute,nodeAddresses[1],0);
			
			//Use the following method to test whether incoming messages are being read
//				communicator.ReadMessage();
//				communicator.ReadMessage();
				
			
			//Use the following method test for scanning
				/*nodeAddresses = communicator.FindAllNodes();
				for(int index = 0; index < 32; index++)
				{
					System.out.println(ByteUtils.toBase16(nodeAddresses[index]));
				}*/
				
			//Use the following test if the previous test works
				int[][] node = communicator.FindAllNodes();
				communicator.GetBotAddresses(node);
				for(int index = 0; index < 32; index++)
				{
					System.out.println(ByteUtils.toBase16(nodeAddresses[index]));
				}
				
			communicator.EndCommunication();
		}

	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Variables	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		static XBee xbee = new XBee();
		
		private static ZNetRxResponse rx;
		
		private static String COM = "COM17";
		
		private static int BAUD_RATE = 57600;
		
		public static int[][] nodeAddresses = new int[32][8];
		
		private static int rowIndex = 0;
		
		private static boolean doneReading;
		
		public  static int [] input;
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Methods		*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------			
		public void InitializeCommunication() 
		{	
			try 
			{
				xbee.open(COM, BAUD_RATE);
			} 
			catch (XBeeException e) 
			{
				System.out.println("\n[CommunicatorAPI-InitializeCommunication] problem opening COM ports.");
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		public void ReadMessage()
		{
			try 
			{
				XBeeResponse response = xbee.getResponse();
				boolean doneReading = false;
				while(!doneReading)
				{
					if(response.getApiId() == ApiId.ZNET_RX_RESPONSE)
					{
						rx = (ZNetRxResponse) response;
						input = rx.getData();
						System.out.println(input[0]);
						doneReading = true;
					}
					else;
						//System.out.println(response.);
				}
			} 
			catch (XBeeException e) 
			{
				System.out.println("\n[CommunicatorAPI-ReadMessage] Problem accessing recieved data.\n");
				e.printStackTrace();
			}
		}
		
		public void SendMessage(int[] bytearr, int[] destinationLow,int frameId)
		{
			int[] information = bytearr;
			XBeeAddress64 destination = new XBeeAddress64(destinationLow);
			ZNetTxRequest request = new ZNetTxRequest(destination,information);
			request.setOption(ZNetTxRequest.Option.UNICAST);
			request.setFrameId(frameId);
			try 
			{
				xbee.sendAsynchronous(request);
			} 
			catch (XBeeException e) 
			{
				System.out.println("\n[CommunincatorAPI-SendMessage] problem sending message.\n");
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		public static void SetUpCommunication(String com, int baud_rate)
		{
			COM = com;
			BAUD_RATE = baud_rate;
		}
		
		public int[][] FindAllNodes() throws XBeeException,InterruptedException
		{
			int[][] xbeeAddresses = new int [33][8];
			xbee.sendAsynchronous(new AtCommand("NT"));
			AtCommandResponse nodeTimeOut = (AtCommandResponse)xbee.getResponse();
			long nodeDiscoveryTimeOut = ByteUtils.convertMultiByteToInt(nodeTimeOut.getValue())*100;
			xbee.addPacketListener(
									new PacketListener()
									{
										public void processResponse(XBeeResponse response)
										{
											if(response.getApiId() == ApiId.AT_RESPONSE)
											{
												NodeDiscover nd = NodeDiscover.parse((AtCommandResponse)response);
												xbeeAddresses[rowIndex] = nd.getNodeAddress64().getAddress();
												rowIndex++;
											}
										}
									}
								  );
			xbee.sendAsynchronous(new AtCommand("ND"));
			Thread.sleep(nodeDiscoveryTimeOut);
			
			return xbeeAddresses;
		}
		
		public int[][] GetBotAddresses(int[][] addresses)
		{
			int index;
			for(index = 1; index < rowIndex; index++)
			{
				System.out.println(index +","+ rowIndex);
				int[] idRequest = new int[] {0x0A,0x00,0x00,0x00,0x00,0x00};
				SendMessage(idRequest,addresses[index],0);
				int[] execute = new int[] {0xff,0x00,0x00,0x00,0x00,0x00};
				SendMessage(execute,addresses[index],0);
				System.out.println(index);
				ReadMessage();
				System.out.println("n");
				//nodeAddresses[(input[0]-1)] = addresses[index];
			}
			
			return nodeAddresses;
		}
		
		public void EndCommunication()
		{
			xbee.close();
		}
}
