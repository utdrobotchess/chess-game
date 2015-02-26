package robot;

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
        robotState.addNewCommand(new RCModeCommand(3));

        while (true) {
            robotState.addNewCommand(new RCCommand(3, ComputeWheelVelocities()));
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

        if(controller.getAxisValue(0) < 0)
        {
            direction = 0x01; //forward
        }
        else
        {
            direction = 0x00; //backward
        }

        if(controller.getAxisValue(3) < 0)
        {
            rotation = 0x01; //left
        }
        else
        {
            rotation = 0x00; //right
        }

        forwardVel = (char)Math.abs(controller.getAxisValue(0) * 255);
        rotationVel = (char)Math.abs(controller.getAxisValue(3) * 255);

        if(controller.isButtonPressed(4))
        {
            forwardVel *= .5;
        }

        if(controller.isButtonPressed(5))
        {
            rotationVel *= .5;
        }

        wheelVelPayload = new int[] {direction, forwardVel, rotation, rotationVel};
        return wheelVelPayload;
    }

}
