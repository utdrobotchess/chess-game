package RobotChess.Communication;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Command 
{
	public static void main(String[] args) throws Exception 
	{
		ConstructCommand("crossSquare",(char)0x1d,(char)0x07);
		ConstructCommand("crossSquare",(char)0x01,(char)0x07);
		ConstructCommand("execute", (char)0x1d);
		ConstructCommand("execute", (char)0x01);
		//PrintCommandBuffer();
		SendCommands();
		
		//WriteFromConsole();
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Variables	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		public static ArrayList<String> commandName =  new ArrayList<String>();
		public static ArrayList<char[]> commandBuffer = new ArrayList<char[]>();
	
		private static char checkSum = 0x75;
		
		private static Scanner keyboard;
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Methods		*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
	
		public static void WriteFromConsole()
		{
			boolean doneWritingCommands = false;
			String input;
			
			keyboard = new Scanner(System.in);
				System.out.println("Type in 'write' to write a command, 'send' to send a command, \n	'print' to print, and 'done' when you finished.");
				System.out.print(" Enter: ");
				input = keyboard.next();
				while(!doneWritingCommands)
				{
					if(input.equalsIgnoreCase("write"))
					{
						ConstructCommandFromConsole();
						System.out.println("\nType in 'write' to write a command, 'send' to send a command, \n	'print' to print, and 'done' when you finished.");
						System.out.print(" Enter: ");
						input = keyboard.next();
					}
					else if(input.equalsIgnoreCase("print"))
					{
						if(commandBuffer.size() != 0)
							PrintCommandBuffer();
						else
							System.out.println("\n	No commands have been stored.");
						System.out.println("\nType in 'write' to write a command, 'send' to send a command, \n	'print' to print, and 'done' when you finished.");
						System.out.print(" Enter: ");
						input = keyboard.next();
					}
					else if(input.equalsIgnoreCase("send"))
					{
						SendCommands();
						System.out.println("\nType in 'write' to write a command, 'send' to send a command, \n	'print' to print, and 'done' when you finished.");
						System.out.print(" Enter: ");
						input = keyboard.next();
					}
					else if(input.equalsIgnoreCase("done"))
						doneWritingCommands = true;
					else
					{
						System.out.println("	[GLaDOS] Incorrect entry. Common this is sad, even for you.");
						System.out.println("\nType in 'write' to write a command, 'send' to send a command, \n	'print' to print, and 'done' when you finished.");
						System.out.print(" Enter: ");
						input = keyboard.next();
					}
				}
			keyboard.close();
		}
		
		public static void ConstructCommand(String command,char botId,char param1,char param2,char param3,char param4,char param5)
		{
		
			switch(command.toLowerCase())
			{	
				case("crosssquare"):
					if (param1>=0x01&&param1<=0x07)
					{
						commandName.add("crossSquare");
						char[] temp = {botId,0x01,param1,0x00,0x00,0x00,0x00,checkSum};
						commandBuffer.add(temp);
					}
					else
					{
						System.out.println("[Invalid crossSquare command] check parameters:\n	(commandName, botId, newBotId)");
						System.out.println("	Note:	you can only a max of 7 squares at a time."); 
					}
					break;
				case("rotate"):
					if ((param1==0x00||param1== 0x2d) && (param2>=0x00&&param2<=0xFF)&&(param3>=0x00&&param3<=0xFF))
					{
						commandName.add("rotate");
						char[] temp = {botId,0x02,param1,param2,param3,0x00,0x00,checkSum};
						commandBuffer.add(temp);
					}
					else
					{
						System.out.println("[Invalid rotate command] check parameters:\n	(commandName, botId, direcion, MSB of degree, LSB of degree)");
						System.out.println("	Note:	direction refers to the sign, either 0 for positive,and - for negative.."); 
					}
					break;
				case("center"):
					if ((param1==0x00||param1==0x2d)&&(param2>=0x00&&param2<=0x04)&&(param3==0x00||param1==0x2d)&&(param4>=0x00&&param4<=0x04))
					{
						commandName.add("center");
						char[] temp = {botId,0x03,param1,param2,param3,param4,param5,checkSum};
						commandBuffer.add(temp);
					}
					else
					{
						System.out.println("[Invalid center command] check parameters:\n	(commandName, botId, direction, amount, direction, amount)");
						System.out.println("	Note:	direction refers to the sign, either 0 for positive,and - for negative.");
						System.out.println("		amount ranges from 0 to 4, because these refer to multiples of 45.");
					}
					break;
				case("writebotid"):
					if (param1>=0x00&&param1<=0xFE)
					{
						commandName.add("writeBotId");
						char[] temp = {botId,0x01,param1,0x00,0x00,0x00,0x00,checkSum};
						commandBuffer.add(temp);
					}
					else
					{
						System.out.println("[Invalid writeBotId command] check parameters:\n	(commandName, botId, newBotId)");
						System.out.println("	Note:	bot identification can be any value between 0 and 254");
					}
					break;
				case("measuresquarestate"):
					{
						commandName.add("measureSquareState");
						char[] temp = {botId,0x05,0x00,0x00,0x00,0x00,0x00,checkSum};
						commandBuffer.add(temp);
					}
					break;
				case("unwind"):
					{
						commandName.add("unwind");
						char[] temp = {botId,0x06,0x00,0x00,0x00,0x00,0x00,checkSum};
						commandBuffer.add(temp);
					}
					break;
				case("aligntoedge"):
					{
						commandName.add("alignToEdge");
						char[] temp = {botId,0x07,0x00,0x00,0x00,0x00,0x00,checkSum};
						commandBuffer.add(temp);
					}
					break;
				case("movedistance"):
					if ((param1==0x00||param1==0x2d)&&(param2>=0x00&&param2<=0xFF)&&(param3>=0x00&&param3<=0xFF)&&(param4>=0x00&&param4<=0xFF)&&(param5>=0x00&&param5<=0xFF))
					{
						commandName.add("moveDistance");
						char[] temp = {botId,0x08,param1,param2,param3,param4,param5,checkSum};
						commandBuffer.add(temp);
					}
					break;
				case("execute"):
					{
						commandName.add("execute");
						char[] temp = {botId,0xFF,0x00,0x00,0x00,0x00,0x00,checkSum};
						commandBuffer.add(temp);
					}
					break;
				default:
					System.out.println("	[Invalid command] Check the command you entered.\n");
					System.out.println("	 [GLaDOS] And you thought you were smart.\n 		  I find great pleasure watching your stupidity.\n");
			}
		}
	
		public static void ConstructCommand(String command, char botId, char param1,char param2, char param3, char param4)
		{
			ConstructCommand(command,botId,param1,param2,param3,param4,(char)0);
		}
		
		public static void ConstructCommand(String command, char botId, char param1,char param2, char param3)
		{
			ConstructCommand(command,botId,param1,param2,param3,(char)0x00,(char)0x00);
		}
		
		public static void ConstructCommand(String command, char botId, char param1)
		{
			ConstructCommand(command,botId,param1,(char)0x00,(char)0x00,(char)0x00,(char)0x00);
		}
		
		public static void ConstructCommand(String command, char botId)
		{
			ConstructCommand(command,botId,(char)0x00,(char)0x00,(char)0x00,(char)0x00,(char)0x00);
		}
		
		public static void ConstructCommandFromConsole()
		{
			boolean done = false; int count = 1;
			char[] buffer = new char[6]; 
			String command;int input;
		
			System.out.print("Enter Command: ");
			command = keyboard.next();
			System.out.println("\n Type in hexidecimal without '0x', enter '000' to end.");
			System.out.print("  Enter botId: ");
			input = keyboard.nextInt(16);
			buffer[0] = (char)input;
			while(!done)
			{
				if(input != 0x101)
				{
					System.out.println("\n Type in hexidecimal without '0x', enter '101' to end.");
					System.out.print("  Enter param"+count+": ");
					input = keyboard.nextInt(16);
					buffer[count] = (char)input;
					count++;
				}
				else
				{
					buffer[count-1] = 0x00;
					for(int index = count; index < 6; index++)
						buffer[index] = (char)0x00;
					done = true;
				}
			}
			ConstructCommand(command,buffer[0],buffer[1],buffer[2],buffer[3],buffer[4],buffer[5]);
			PrintCommandBuffer();	//This is for debugging purposes.
		}
		
		public static void SendCommands()
		{
			Communicator xbee = new Communicator();
			xbee.InitializeCommunication();
				for(int index = 0; index < commandBuffer.size(); index++)
					Communicator.SendMessage(commandBuffer.get(index));
			xbee.EndCommunication();
			commandName.clear();
			commandBuffer.clear();
		}
		
		public static void PrintCommandBuffer()
		{
			for(int index = 0; index < commandBuffer.size(); index++)	
			{
				String str = new String(commandBuffer.get(index));
				str = String.format("%040x", new BigInteger(1, str.getBytes()));	
				System.out.println("\n"+commandName.get(index)+":\n	"+str.substring(24, str.length()));
			}
		}
		
		public static void RCMode()
		{
			RemoteController controller = new RemoteController ();
			controller.InitializeControllers();
			RemoteController.FindAvailableControllers();
			
			
		}
}
