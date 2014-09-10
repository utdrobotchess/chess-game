package chess.communication;
import java.util.Scanner;

import XBeeApI.XBeeException;

public class Terminal 
{
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		/*	Test Program: Write any test programs for this class in the main method.	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		public static void main(String[] args) throws XBeeException
		{
			//Use the following method to test making commands from console
			  WriteFromTerminal();
		}
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Variables	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		private static Scanner keyboard;
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Methods		*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		public static void WriteFromTerminal() throws XBeeException
		{
			boolean doneWritingCommands = false;
			String input;
			
			keyboard = new Scanner(System.in);
				PrintCommandOptions();
				input = keyboard.next();
				while(!doneWritingCommands)
				{
					if(input.equalsIgnoreCase("write"))
					{
						ConstructCommandFromTerminal();
						PrintCommandOptions();
						input = keyboard.next();
					}
					else if(input.equalsIgnoreCase("rcmode"))
					{
						int botId;
						System.out.println("Type in robot identification number.");
						System.out.println(" Enter:	");
						botId = keyboard.nextInt();
						Command.RemoteControlMode(botId);
						PrintCommandOptions();
						input = keyboard.next();
					}
					else if(input.equalsIgnoreCase("print"))
					{
						if(Command.commandBuffer.size() != 0)
							Command.PrintCommandBuffer();
						else
							System.out.println("\n	No commands have been stored.");
						PrintCommandOptions();
						input = keyboard.next();
					}
					else if(input.equalsIgnoreCase("send"))
					{
						Command.SendCommands();
						PrintCommandOptions();
						input = keyboard.next();
					}
					else if(input.equalsIgnoreCase("done"))
						doneWritingCommands = true;
					else
					{
						System.out.println("/t[Terminal-WriteFromTerminal] Incorrect entry. Common this is sad, even for you.");
						PrintCommandOptions();
						input = keyboard.next();
					}
				}
			keyboard.close();
		}
		
		public static void ConstructCommandFromTerminal()
		{
			boolean done = false; int count = 1;
			char[] buffer = new char[6]; 
			String command;int input;
		
			System.out.print("Enter Command: ");
			command = keyboard.next();
			System.out.println("\n Type in hexidecimal without '0x', enter '101' to end.");
			System.out.print("\tEnter botId: ");
			input = keyboard.nextInt(16);
			buffer[0] = (char)input;
			while(!done)
			{
				if(input != 0x101)
				{
					System.out.println("\n Type in hexidecimal without '0x', enter '101' to end.");
					System.out.print("\tEnter param"+count+": ");
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
			Command.ConstructCommand(command,buffer[0],buffer[1],buffer[2],buffer[3],buffer[4],buffer[5]);
			Command.PrintCommandBuffer();	//This is for debugging purposes.
		}
		
		private static void PrintCommandOptions()
		{
			System.out.println("Type in 'write' to write a command, 'rcmode' to active RC-mode, ");
			System.out.println("\t'send' to send a command, 'print' to print, and 'done'\n\t when you finished.");
			System.out.print(" Enter: ");
		}
}
