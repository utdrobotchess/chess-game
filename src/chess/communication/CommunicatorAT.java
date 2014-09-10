package RobotChess_Communication;

import gnu.io.*;

import java.io.*;
import java.util.*;

	/*	Note: CommunicatorAT will automatically read and continuously try to read any incoming messages 
	  		  when it is initialized.
	*/
	public class CommunicatorAT implements SerialPortEventListener
	{
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		/*	Test Program: Write any test programs for this class in the main method.	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		public static void main(String[] args) throws Exception 
		{
			//Run this program to test AT Communication.
				/*CommunicatorAT main = new CommunicatorAT();
				main.InitializeCommunication("COM12", 57600);
					char[] crossSquare1 = {0x01, 0x01, 0x07, 0x00, 0x00, 0x00, 0x00, 0x75};
					char[] rotate1 		= {0x01, 0x02, 0x00, 0x00, 0xB4, 0x00, 0x00, 0x75};
					char[] center1  	= {0x01, 0x03, 0x2D, 0x02, 0x00, 0x02, 0x00, 0x75};
					char[] execute1 	= {0x01, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x75};
					char[] crossSquare2 = {0x1c, 0x01, 0x07, 0x00, 0x00, 0x00, 0x00, 0x75};
					char[] rotate2 		= {0x1c, 0x02, 0x00, 0x00, 0xB4, 0x00, 0x00, 0x75};
					char[] center2  	= {0x1c, 0x03, 0x2D, 0x02, 0x00, 0x02, 0x00, 0x75};
					char[] execute2 	= {0x1c, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x75};
				
					long startTime = System.currentTimeMillis(); 
					while((System.currentTimeMillis() - startTime < 1000))
					{
						main.SendMessage(crossSquare1);
						main.SendMessage(rotate1);
						main.SendMessage(crossSquare1);
						main.SendMessage(rotate1);
						main.SendMessage(center1);
					
						if(System.currentTimeMillis() - startTime > 250)
						{
							main.SendMessage(crossSquare2);
							main.SendMessage(rotate2);
							main.SendMessage(crossSquare2);
							main.SendMessage(rotate2);
							main.SendMessage(center2);
						
							main.SendMessage(execute1);
							main.SendMessage(execute2);
						}
					}	
					main.EndCommunication();*/
		}		
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Variables	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		public SerialPort serialPort;
		
		public static BufferedReader input;
		public static OutputStream output;
		
		// Time (in Milliseconds) to block while waiting for port to open
		public static final int TIME_OUT = 0;
		private static String COM;
		private static int BAUD_RATE;
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Methods	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		
		public void SendMessage(char[] bytearr)
		{
			for(int i = 0; i < 8; i++)
				writeByte(bytearr[i]);
			
			//----------------------------------//
			/*	These are for testing purposes.	*/
			//----------------------------------//
			//String str = String.format("%040x", new BigInteger(1,new String(bytearr).getBytes()));
			//System.out.println("S: "+str.substring(24, str.length()));	
		}
		
		private synchronized void writeByte(char data)
		{
			try
			{
				output.write(data);
			}
			catch(Exception e)
			{
				PrintString("[CommunicatorAT-writeByte] Couldn't write to port.");
			}
		}
		
		public void InitializeCommunication()
		{
			boolean comPortIsFound = false;
			CommPortIdentifier portID = null;
			
			@SuppressWarnings("rawtypes")
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
			
			//First, Find an instance of serial port as set in PORT_NAMES.
			while(portEnum.hasMoreElements()&&!comPortIsFound)
			{
				CommPortIdentifier currentPortID = (CommPortIdentifier) portEnum.nextElement();
				if (currentPortID.getName().equals(COM))
				{
						portID = currentPortID;
						comPortIsFound = true;
				}
				else
					PrintString("COM port "+currentPortID.getName()+" does match entered COM port "+COM);
			}
			
			if(portID == null)
			{
				PrintString("[CommunicatorAT-InitializeCommunication] Specific COM port not found.");
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
				PrintString("[CommunicatorAT-InitializeCommunication] Initialization problem.");
				System.exit(0);
			}
		}
		
		public synchronized void serialEvent(SerialPortEvent oEvent)
		{
			if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
			{
				try
				{
					String receivedMessage = input.readLine();
					PrintString(receivedMessage);
				}
				catch(Exception e)
				{
					PrintString("[CommunicatorAT-serialEvent] could not write to message.");
					System.exit(0);
				}
			}
		}
		
		private void PrintString(String tempString) 
		{
			System.out.println(tempString);
		}
		
		public static void SetUpCommunication(String com, int baud_rate)
		{
			COM = com;
			BAUD_RATE = baud_rate;
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