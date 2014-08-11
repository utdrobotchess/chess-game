package RobotChess.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.wpan.TxStatusResponse;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;

	public class CommunicatorAPI
	{
	
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		/*	Test Program: Write any test programs for this class in the main method.	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		public static void main(String[] args)  	
		{

			try 
			{
				XBee xbee = new XBee();
				xbee.open(COM, BAUD_RATE);
			} 
			catch (XBeeException e) 
			{
				System.out.println("\nCould not initialize communication");
				System.exit(0);
			}
			catch(ClassCastException e)
			{
				System.out.println("\nClass Cast Error");
				System.exit(0);
			}
			
			CommunicatorAPI communicator = new CommunicatorAPI();
			communicator.InitializeCommunication();
		
			communicator.EndCommunication();
		}
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Variables	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		public static String COM = "COM26";
		public static int BAUD_RATE = 57600;
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Methods		*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		public void InitializeCommunication()
		{
			
			try 
			{
				BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
				int selectedNumber = 0;
				int[] radioFrequencyData = new int[] {1};	
				while(selectedNumber != 3)
				{
					System.out.print("Enter a number (1:Led On|2:Led Off|3: Quit): ");
					try 
					{
						selectedNumber = Integer.parseInt(input.readLine());
					} 
					catch (NumberFormatException e) 
					{
						System.out.println("\nThere was formatting problem.");
						continue;
					} 
					catch (IOException e) 
					{
						System.out.println("\nType in numbers only.");
						continue;
					}
					
					if(selectedNumber!=1 && selectedNumber!=2)
						continue;
				}
				radioFrequencyData[0] = selectedNumber;
		
				XBeeAddress64 remoteaddr = new XBeeAddress64(0x00,0x13,0xA2,0x00,0x40,0x86,0x96,0x38);
				ZNetTxRequest tx = new ZNetTxRequest(remoteaddr, radioFrequencyData);
				XBee xbee = new XBee();
				try
				{
					TxStatusResponse status = (TxStatusResponse) xbee.sendSynchronous(tx);
					if(status.isSuccess())
						System.out.println("Led is On");
					else
						System.out.println("Led is Off");
				}
				catch(ClassCastException e)
				{
					
				}
			} 
			catch (XBeeTimeoutException e) 
			{
				System.out.println("\nXBeeTimeoutException occured.");
				System.exit(0);
			} 
			catch (XBeeException e) 
			{
				System.out.println("\nXBeeException occured.");
				System.exit(0);
			}	
		}
		
		public void EndCommunication()
		{
			XBee xbee = new XBee();
			xbee.close();
		}
	}
