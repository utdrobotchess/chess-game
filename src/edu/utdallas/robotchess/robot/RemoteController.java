package edu.utdallas.robotchess.robot;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class RemoteController extends Thread
{
    public static Controller controller;
    private ChessbotCommunicator comm;
    boolean keepAlive;

    public RemoteController(ChessbotCommunicator comm)
    {
        this.comm = comm;

        try {
            Controllers.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        Controllers.poll();

        if (Controllers.getControllerCount() != 0) {
            controller = Controllers.getController(0);
            System.out.println("Found controller");
        } else {
            System.out.println("Could not find any controllers");
        }
    }

    public void run()
    {
        if(controller == null) {
            System.out.println("Cannot start RemoteController Thread since no Remote Controller found");
            return;
        }

        ZeroJoyStick();

        while (keepAlive) {
            Command cmd = new RCCommand(0, ComputeWheelVelocities());
            comm.sendCommand(cmd);

            try { Thread.sleep(100); }
            catch (InterruptedException ex){}
        }

        System.out.println("Terminating RemoteController Thread");
    }

    public void terminate()
    {
        keepAlive = false;
    }

    public int[] ComputeWheelVelocities()
    {
        int[] wheelVelPayload = new int[4];
        int direction, rotation, forwardVel, rotationVel;

        controller.poll();

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
