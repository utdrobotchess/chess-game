package RobotChess.Communication;

import gnu.io.*;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

	@SuppressWarnings("unused")
	public class Communicator implements SerialPortEventListener
	{
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		/*	Test Program: Write any test programs in the main method.	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		public static void main(String[] args) throws Exception 
		{
			Communicator main = new Communicator();
			main.InitializeCommunication();
			char[] crossSquare1  = {0x01, 0x01, 0x07, 0x00, 0x00, 0x00, 0x00, 0x75};
			char[] crossSquare2  = {0x1c, 0x01, 0x07, 0x00, 0x00, 0x00, 0x00, 0x75};
			char[] center1  = {0x01, 0x03, 0x2D, 0x02, 0x00, 0x02, 0x00, 0x75};
			char[] center2  = {0x1c, 0x03, 0x2D, 0x02, 0x00, 0x02, 0x00, 0x75};
			char[] execute1 = {0x01, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x75};
			char[] execute2 = {0x1c, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x75};
			char[] rotate1 = {0x01, 0x02, 0x00, 0x00, 0xB4, 0x00, 0x00, 0x75};
			char[] rotate2 = {0x1c, 0x02, 0x00, 0x00, 0xB4, 0x00, 0x00, 0x75};
			
			SendMessage(crossSquare2);
			SendMessage(rotate2);
			SendMessage(crossSquare2);
			SendMessage(rotate2);
			SendMessage(center2);
			
			long startTime = System.currentTimeMillis(); 
			while((System.currentTimeMillis() - startTime < 250))
			{}
			
			SendMessage(crossSquare1);
			SendMessage(rotate1);
			SendMessage(crossSquare1);
			SendMessage(rotate1);
			SendMessage(center1);
			
			
			SendMessage(execute1);
			SendMessage(execute2);

			
			
			main.EndCommunication();
		}		
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Variables	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		public SerialPort serialPort;
		
		// The Port we're normally going to use.
		private static final String PORT_NAMES[] = {"/dev/ttyUSB0"/*for Linux*/,
			 										"COM26"/*for Windows*/};
		public static BufferedReader input;
		public static OutputStream output;
		
		static String receivedMessage = " ";
		
		// Time (in Milliseconds) to block while waiting for port to open
		public static final int TIME_OUT = 0;
		public static final int BAUD_RATE = 57600;
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Methods	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		
		public static void SendMessage(char[] bytearr)
		{
			for(int i = 0; i < 8; i++)
				Communicator.writeByte(bytearr[i]);
			//System.out.print("S: ");					//These are for testing purposes.
			//String str = String.format("%040x", new BigInteger(1,new String(bytearr).getBytes()));
			//PrintString(str.substring(24, str.length()));
		}
		
		private static synchronized void writeByte(char data)
		{
			try
			{
				output.write(data);
			}
			catch(Exception e)
			{
				System.out.println("[GLaDOS] couldn't write to port. You weren't even testing for that.");
			}
		}
		
		private static void PrintString(String tempString) 
		{
			System.out.println(tempString);
		}
		
		public void InitializeCommunication()
		{
			CommPortIdentifier portID = null;
			
			@SuppressWarnings("rawtypes")
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
			
			//First, Find an instance of serial port as set in PORT_NAMES.
			while(portEnum.hasMoreElements())
			{
				CommPortIdentifier currentPortID = (CommPortIdentifier) portEnum.nextElement();
				for(String portName: PORT_NAMES)
				{
					if (currentPortID.getName().equals(portName))
					{
						portID = currentPortID;
						break;
					}
				}
			}
			
			if(portID == null)
			{
				System.out.println("[GLaDOS] specific COM port not found.");
			}
			
			try
			{
				// open the serial port, and use class for the appName.
				serialPort = (SerialPort) portID.open(this.getClass().getName(),TIME_OUT);
				
				// port parameters
				serialPort.setSerialPortParams(BAUD_RATE,SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
											   SerialPort.PARITY_NONE);
				
				// Open the streams
				input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
				output = serialPort.getOutputStream();
				
				//add event listeners
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
			}
			catch (Exception e)
			{
				System.out.println("[GlaDOS] Initialization problem. Debug on your own recognisense.");
				System.out.println(e.toString());
			}
		}
		
		public synchronized void serialEvent(SerialPortEvent oEvent)
		{
			if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
			{
				try
				{
					receivedMessage = input.readLine();
				}
				catch(Exception e)
				{}
			}
		}
			
		public synchronized void EndCommunication()
		{
			if (serialPort != null)
			{
				serialPort.removeEventListener();
				serialPort.close();
			}
		}
	}
