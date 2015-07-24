package edu.utdallas.robotchess.robotcommunication;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import edu.utdallas.robotchess.robotcommunication.commands.Command;
import edu.utdallas.robotchess.robotcommunication.commands.RCCommand;

//TODO: Copy over T-shirt Cannon Remote Controller and LWJGL libs. It is
//written in a much cleaner way and already has each button mapped. Just
//make it a thread.
public class RemoteController extends Thread
{
    public static Controller controller;
    private ChessbotCommunicator comm;
    protected final static Logger log = Logger.getLogger(RemoteController.class);
    boolean keepAlive = true;
    int botID;

    public RemoteController(ChessbotCommunicator comm, int botID)
    {
        PropertyConfigurator.configure("log/log4j.properties");

        this.comm = comm;
        this.botID = botID;

        try {
            Controllers.create();
        } catch (LWJGLException e) {
            log.info("Exception", e);
        }

        Controllers.poll();

        if (Controllers.getControllerCount() != 0) {
            controller = Controllers.getController(0);
            log.info("Found controller");
        }
        else
            log.info("Could not find any controllers");
    }

    //TODO: Check if botID exists before starting thread
    public void run()
    {
        if(controller == null) {
            log.info("Cannot start RemoteController Thread since no Remote Controller found");
            return;
        }

        ZeroJoyStick();

        while (keepAlive) {
            Command cmd = new RCCommand(botID, ComputeWheelVelocities());
            comm.sendCommand(cmd);

            try { Thread.sleep(100); }
            catch (InterruptedException ex){}
        }

        comm.sendCommand(new RCCommand(botID, new int[] {0, 0, 0, 0}));
            log.info("Terminating RemoteController Thread");
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
            direction = 0x01; //forward
        else
            direction = 0x00; //backward

        if(controller.getAxisValue(2) < 0)
            rotation = 0x01; //left
        else
            rotation = 0x00; //right

        forwardVel = (char)Math.abs(controller.getAxisValue(1) * 255);
        rotationVel = (char)Math.abs(controller.getAxisValue(2) * 255);

        if(controller.isButtonPressed(4))
            forwardVel *= .5;

        if(controller.isButtonPressed(5))
            rotationVel *= .5;

        wheelVelPayload = new int[] {direction, forwardVel, rotation, rotationVel};

        log.info(String.format("%x, %x, %x, %x\n", wheelVelPayload[0], wheelVelPayload[1], wheelVelPayload[2], wheelVelPayload[3]));
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
