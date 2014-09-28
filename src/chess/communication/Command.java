package chess.communication;

import java.math.BigInteger;
import java.util.ArrayList;

import chess.communication.XBeeAPI.XBeeException;
import chess.communication.XBeeAPI.util.ByteUtils;

public class Command 
{
	/*----------------------------------------------------------------------------------------------------------------------------------------------------
	Test Program: Write any test programs for this class in the main method.	
	----------------------------------------------------------------------------------------------------------------------------------------------------*/	
		public static void main(String[] args) throws XBeeException, InterruptedException
		{
			xbee.FindAllNodes();
			xbee.GetBotAddresses();
			//Use the following method to test get DestinationAddresses
			ArrayList <Integer> AvailableBotIndices = xbee.GetAvailableBotIndices();
			
				/*for(int index = 0; index < AvailableBotIndices.size(); index++)
				{*/
					//System.out.println(destinationAddresses[AvailableBots.get(index)]);
					ConstructCommand("aligntoedge");
					ConstructCommand("execute");
					PrintCommandBuffer();
					//System.out.println(ByteUtils.toBase16(xbee.botIdOrderedNodeAddresses[AvailableBotIndices.get(index)]));
					
				//}
				for(int index = 0; index < AvailableBotIndices.size(); index++)
				{
					//SendCommands(AvailableBotIndices.get(index));
					System.out.println(AvailableBotIndices.get(index));
				}
				
				//commandName.clear();
				//commandBuffer.clear();
			
			//Use the following methods to test ConstructCommand using AT mode
			  /*ConstructCommand("crossSquare",(char)0x1d,(char)0x07);
		  		ConstructCommand("crossSquare",(char)0x01,(char)0x07);
		  		ConstructCommand("execute", (char)0x1d);
		  		ConstructCommand("execute", (char)0x01);
		  		PrintCommandBuffer();
		  		SendCommands();
		  	  */

			//Use the following method to test remote control of the robots
			  //RemoteControlMode(0x01);
				
				CommunicatorAPI.EndCommunication();
		}
	
	/*----------------------------------------------------------------------------------------------------------------------------------------------------	
	Class Member Variables
	----------------------------------------------------------------------------------------------------------------------------------------------------*/
	public static ArrayList<String> commandName =  new ArrayList<String>();
	public static ArrayList<int[]> commandBuffer = new ArrayList<int[]>();
	
	public static CommunicatorAPI xbee = new CommunicatorAPI("COM17");
		
	/*----------------------------------------------------------------------------------------------------------------------------------------------------	
	Class Member Methods		
	----------------------------------------------------------------------------------------------------------------------------------------------------*/
		
	public static void ConstructCommand(String command,int param1,int param2,int param3,int param4,int param5)
	{
	    switch(command.toLowerCase())
		{	
		    case("crosssquare"):
			    if (param1>=0x01&&param1<=0x07)
			    {
			        commandName.add("crossSquare");
				    int[] temp = {0x01,param1};
				    commandBuffer.add(temp);
			    }
			    else
			    {
				    System.out.println("[Invalid crossSquare command] check parameters:\n\t(commandName, botId, newBotId)");
				    System.out.println("\tNote:\tyou can only a max of 7 squares at a time."); 
			    }
			    break;
			case("rotate"):
			    if ((param1==0x00||param1== 0x2d) && (param2>=0x00&&param2<=0xFF)&&(param3>=0x00&&param3<=0xFF))
			    {
			        commandName.add("rotate");
			        int[] temp = {0x02,param1,param2,param3};
			        commandBuffer.add(temp);
			    }
			    else
			    {
				    System.out.println("[Invalid rotate command] check parameters:\n\t(commandName, botId, direcion, MSB of degree, LSB of degree)");
				    System.out.println("\tNote:\tdirection refers to the sign, either 0 for positive,and - for negative.."); 
			    }
			    break;
			case("center"):
			    if ((param1==0x00||param1==0x2d)&&(param2>=0x00&&param2<=0x04)&&(param3==0x00||param1==0x2d)&&(param4>=0x00&&param4<=0x04))
				{
			        commandName.add("center");
					int[] temp = {0x03,param1,param2,param3,param4,param5};
					commandBuffer.add(temp);
				}
				else
				{
					System.out.println("[Invalid center command] check parameters:\n\t(commandName, botId, direction, amount, direction, amount)");
					System.out.println("\tNote:	direction refers to the sign, either 0 for positive,and - for negative.");
					System.out.println("\t\tamount ranges from 0 to 4, because these refer to multiples of 45.");
				}
				break;
			case("writebotid"):
				if (param1>=0x00&&param1<=0xFE)
				{
				    commandName.add("writeBotId");
					int[] temp = {0x01,param1};
					commandBuffer.add(temp);
				}
				else
				{
					System.out.println("[Invalid writeBotId command] check parameters:\n\t(commandName, botId, newBotId)");
					System.out.println("\tNote:\tbot identification can be any value between 0 and 254");
				}
				break;
			case("moveto"):
			    if(param1<=0x07&&param2<=0x07)
				{
					commandName.add("movetTo");
					int[] temp = {0x09,param1,param2};
					commandBuffer.add(temp);
				}
				else
				{
					System.out.println("[Invalid moveTo command] check parameters:\n\t(commandName, x , y)");
					System.out.println("\tNote:\tx and y can be any value between 0 and 7");
				}
				break;
			case("measuresquarestate"):
				{
					commandName.add("measureSquareState");
					int[] temp = {0x05};
					commandBuffer.add(temp);
				}
				break;
			case("unwind"):
				{
					commandName.add("unwind");
					int[] temp = {0x06};
					commandBuffer.add(temp);
				}
				break;
			case("aligntoedge"):
				{
					commandName.add("alignToEdge");
					int[] temp = {0x07};
					commandBuffer.add(temp);
				}
				break;
			case("movedistance"):
				if ((param1==0x00||param1==0x2d)&&(param2>=0x00&&param2<=0xFF)&&(param3>=0x00&&param3<=0xFF)&&(param4>=0x00&&param4<=0xFF)&&(param5>=0x00&&param5<=0xFF))
				{
					commandName.add("moveDistance");
					int[] temp = {0x08,param1,param2,param3,param4,param5};
					commandBuffer.add(temp);
				}
				break;
			case("execute"):
				{
					commandName.add("execute");
					int[] temp = {0xFF};
					commandBuffer.add(temp);
				}
				break;
			default:
				System.out.println("\t[Invalid command] Check the command you entered.\n");
		}
	}
	
	public static void ConstructCommand(String command, int param1, int param2, int param3, int param4)
	{
	     ConstructCommand(command,param1,param2,param3,param4,0x00);
	}
		
	public static void ConstructCommand(String command, int param1, int param2, int param3)
	{
	    ConstructCommand(command,param1,param2,param3,0x00,0x00);
	}
		
	public static void ConstructCommand(String command, int param1)
	{
	    ConstructCommand(command,param1,0x00,0x00,0x00,0x00);
	}
		
	public static void ConstructCommand(String command)
	{
	    ConstructCommand(command,0x00,0x00,0x00,0x00,0x00);
	}
		
	public static void SendCommands(int botAddressIndex) throws InterruptedException
	{ 
		for(int i = 0; i < commandBuffer.size(); i++)
		{
			xbee.SendMessage(commandBuffer.get(i),xbee.botIdOrderedNodeAddresses[botAddressIndex],1);
		}
	}
		
	public static void PrintCommandBuffer()
	{
	    for(int index = 0; index < commandBuffer.size(); index++)	
		{	
			System.out.println("\n"+commandName.get(index)+":\n	"+ByteUtils.toBase16(commandBuffer.get(index)));
		}
	}
}