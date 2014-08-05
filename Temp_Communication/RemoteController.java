package RobotChess.Communication;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class RemoteController 
{
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		/*	Test Program: Write any test programs in the main method.	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		public static void main(String[] arguments)
		{
			RemoteController xboxController = new RemoteController();
			xboxController.InitializeControllers();
			FindAvailableControllers();
			//SetControllerToSystem(0);
			PrintControllerJoyStickAndButtonMap();
			//DisplayXYValues();
			SendMoveCommands((char)0x01);
		}

	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Variables	*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		private static Controller controller;
		
	//----------------------------------------------------------------------------------------------------------------------------------------------------	
		/*	Methods		*/
	//----------------------------------------------------------------------------------------------------------------------------------------------------
		public void InitializeControllers()
		{
			try 
			{
				Controllers.create();
			} 
			catch (LWJGLException e) 
			{
				System.out.println("\n Problem initializing controllers.");
				System.exit(0);
			}
			Controllers.poll();
		}
		
		public static void SetControllerToSystem(int controllerIndex)
		{
			controller = Controllers.getController(controllerIndex);
			ZeroJoyStick();
		}
		
		public static void SendMoveCommands(char botId)
		{
			Communicator xbee =new Communicator();
			xbee.InitializeCommunication();
			
			char direction = 0x00,rotation = 0x00,directionMagnitude = 0x00,rotationMagnitude = 0x00;
			
			long startTime = System.currentTimeMillis();
			while(!controller.isButtonPressed(7))
			{
				controller.poll();
				ZeroJoyStick();
				if(System.currentTimeMillis()-startTime > 100)
				{
					if(controller.getAxisValue(0) < 0) //forward
						direction = 0x01; 
					else							   //backward
						direction = 0x00;
					
					if(controller.getAxisValue(3) < 0) //left
						rotation = 0x01; 
					else 							   //right
						rotation = 0x00;

					directionMagnitude = (char)Math.abs(controller.getAxisValue(0)*255);
					rotationMagnitude = (char)Math.abs(controller.getAxisValue(3)*255);
					System.out.println((int)directionMagnitude+"	"+(int)rotationMagnitude);
					char[]temp = {botId, direction, directionMagnitude, rotation, rotationMagnitude, 0x00, 0x00, 0x75};
					Communicator.SendMessage(temp);	
					startTime = System.currentTimeMillis();
					ZeroJoyStick();
				}	
			}
			char[]temp = {botId, 0x00, 0x00, 0x00, 0x00, 0xff, 0xff, 0x75};
			Communicator.SendMessage(temp);
			xbee.EndCommunication();
		}

		public static void DisplayXYValues()
		{
			System.out.println("\nAxis Values:");
			
			long startTime = System.currentTimeMillis();
			while(!controller.isButtonPressed(7)&&System.currentTimeMillis()-startTime<1000)
			{
				controller.poll();
				if(System.currentTimeMillis()-startTime==100)
				{
					System.out.print("  X_1:  "+String.format("%.2f",controller.getAxisValue(1))+",");
					System.out.print("   Y_1:  "+String.format("%.2f",controller.getAxisValue(0))+",");
					System.out.print("   X_2:  "+String.format("%.2f",controller.getAxisValue(3))+",");
					System.out.print("   Y_2:  "+String.format("%.2f",controller.getAxisValue(2))+"\n");
					startTime = System.currentTimeMillis();
					ZeroJoyStick();
				}
					
			}
		}
		
		public static void FindAvailableControllers()
		{	
			if(Controllers.getControllerCount() > 0)
			{
				for(int index = 0;index < Controllers.getControllerCount();index++)
				{
					controller = Controllers.getController(index);
					System.out.println("\n Controller found at index_"+index+": "+ controller.getName()+"\n");
				}
			}
			else
			{
				System.out.println("\n Could not Find any controllers.");
				System.exit(0);
			}
		}
		
		public static void PrintControllerJoyStickAndButtonMap()
		{
			String[] controllerName = {"A_Button","B_Button","X_Button","Y_Button",
									"Left_Bumper","Right_Bumper","Select_Button",
									"Start_Button","Lower_Joystick(Push Down)",
									"Upper_Joystick(Push Down)"};
			for(int index = 0;index < controller.getAxisCount();index++)
			{
				if(index != 2 && index != 3)
					System.out.printf("%10s %18s","	"+controller.getAxisName(index),"at index_"+index+"\n");
				else
					System.out.printf("%10s %14s","	"+controller.getAxisName(index),"at index_"+index+"\n");
			}	
			
			System.out.println();
			for(int index = 0;index < controller.getButtonCount();index++)
				System.out.printf("%10s %15s %s","	"+controller.getButtonName(index),"at index_"+index,"  "+controllerName[index]+"\n");
		}
		
		private static void ZeroJoyStick()
		{
			while(controller.getAxisValue(0) != 0.0 && controller.getAxisValue(1) != 0.0 && controller.getAxisValue(2) != 0.0 && controller.getAxisValue(3) != 0.0)
			{
				controller.poll();
				for(int index = 0;index < controller.getAxisCount();index++)
					controller.setDeadZone(index,(float)0.3);
			}
		}
		
		
}
