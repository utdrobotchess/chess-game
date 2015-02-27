package robot;

import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import manager.RobotState;

public class RemoteController extends Thread 
{
    public static Controller controller;
    RobotState robotState;

    public RemoteController(RobotState robotState)
    {
        this.robotState = robotState;

        try 
        {
            Controllers.create();
        } 
        catch (LWJGLException e) 
        {
            e.printStackTrace();
            System.exit(0);
        }

        Controllers.poll();

        if(Controllers.getControllerCount() != 0)
        {
            controller = Controllers.getController(0);
            System.out.println("Found controller");
        }
        else
        {
            System.out.println("Could not find any controllers");
        }
    }
    
    public void run()
    {
        robotState.addNewCommand(new RCModeCommand(4));
        robotState.addNewCommand(new ExecuteCommand(4));

        ZeroJoyStick();

        while (true) {
            robotState.addNewCommand(new RCCommand(4, ComputeWheelVelocities()));
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            
            }
        }
    }

    public int[] ComputeWheelVelocities()
    {
        int[] wheelVelPayload = new int[4];
        int direction, rotation, forwardVel, rotationVel;

        controller.poll();
        long startTime = System.currentTimeMillis();

        if(controller.getAxisValue(1) < 0)
        {
            direction = 0x01; //forward
        }
        else
        {
            direction = 0x00; //backward
        }

        if(controller.getAxisValue(2) < 0)
        {
            rotation = 0x01; //left
        }
        else
        {
            rotation = 0x00; //right
        }

        forwardVel = (char)Math.abs(controller.getAxisValue(1) * 255);
        rotationVel = (char)Math.abs(controller.getAxisValue(2) * 255);

        if(controller.isButtonPressed(4))
        {
            forwardVel *= .5;
        }

        if(controller.isButtonPressed(5))
        {
            rotationVel *= .5;
        }

        wheelVelPayload = new int[] {direction, forwardVel, rotation, rotationVel};
        
        System.out.printf("%x, %x, %x, %x\n", wheelVelPayload[0], wheelVelPayload[1], wheelVelPayload[2], wheelVelPayload[3]);
        return wheelVelPayload;
    }

    public static void ZeroJoyStick()
    {
        while(controller.getAxisValue(0) != 0.0 && controller.getAxisValue(1) != 0.0 && controller.getAxisValue(2) != 0.0 && controller.getAxisValue(3) != 0.0)
        {
            controller.poll();

            for(int i = 0; i < controller.getAxisCount(); i++)
                controller.setDeadZone(i, (float)0.3);
        }
    }
}
