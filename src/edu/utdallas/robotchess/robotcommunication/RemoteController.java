package edu.utdallas.robotchess.robotcommunication;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import edu.utdallas.robotchess.robotcommunication.commands.Command;
import edu.utdallas.robotchess.robotcommunication.commands.RCCommand;
import edu.utdallas.robotchess.robotcommunication.commands.SmartCenterCommand;

//TODO: Should 'implement Runnable', however javac is not recognizing the
//start() method for RemoteController when I make it so
public class RemoteController extends Thread
{
    static Controller controller;
    Mapping buttonMap;
    ChessbotCommunicator comm;
    long smartCenterTime = System.currentTimeMillis();
    final static Logger log = Logger.getLogger(RemoteController.class);

    final long SMART_CENTER_TIMEOUT = 8000;

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
            buttonMap = new Mapping(controller);
        }
        else
            log.info("Could not find any controllers");
    }

    public void terminate()
    {
        keepAlive = false;
    }

    //TODO: Check if botID exists before starting thread
    @Override
    public void run()
    {
        if(controller == null) {
            log.info("Cannot start RemoteController Thread since no Remote Controller found");
            return;
        }

        calibrate();
        Command cmd;

        while (keepAlive) {
            controller.poll();

            handleButtonPress();

            cmd = new RCCommand(botID, ComputeWheelVelocities());
            comm.sendCommand(cmd);

            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                log.debug("Thread Interrupted", e);
            }
        }

        comm.sendCommand(new RCCommand(botID, new int[] {0, 0, 0, 0}));
        log.info("Terminating RemoteController Thread");
    }

    public void checkButtons()
    {
        for (int i = 0; i < controller.getButtonCount(); i++)
            if (controller.isButtonPressed(i))
                System.out.println("Button " + i + " is pressed");
    }

    private void handleButtonPress()
    {
        Command cmd;

        if (buttonMap.isButtonAPressed()) {
            smartCenterTime = System.currentTimeMillis();
            cmd = new SmartCenterCommand(botID);
            comm.sendCommand(cmd);

            //Don't want to send Chessbot any other message while it is
            //centering
            try {
                Thread.sleep(SMART_CENTER_TIMEOUT);
            } catch (InterruptedException e) {
                log.debug("Thread Interrupted", e);
            }
        }
    }

    public int[] ComputeWheelVelocities()
    {
        int[] wheelVelPayload = new int[4];
        int direction, rotation, forwardVel, rotationVel;

        forwardVel = (int)(controller.getYAxisValue() * 255);
        rotationVel = (int)(controller.getRXAxisValue() * 255);

        if(forwardVel < 0)
            direction = 0x01; //forward
        else
            direction = 0x00; //backward

        if(rotationVel < 0)
            rotation = 0x01; //left
        else
            rotation = 0x00; //right

        forwardVel = (char)Math.abs(forwardVel);
        rotationVel = (char)Math.abs(rotationVel);

        if(buttonMap.isLeftBumperPressed())
            forwardVel *= .5;

        if(buttonMap.isRightBumperPressed())
            rotationVel *= .5;

        wheelVelPayload = new int[] {direction, forwardVel, rotation, rotationVel};

        log.info(String.format("%x, %x, %x, %x\n", wheelVelPayload[0], wheelVelPayload[1], wheelVelPayload[2], wheelVelPayload[3]));

        return wheelVelPayload;
    }

    private void calibrate()
    {
        for (int i = 1; i < controller.getAxisCount(); i++)
            controller.setDeadZone(i, 0.2f);
    }

    class Mapping
    {
        ////////////////////////////////////////
        //   Buttons
        // 0) A
        // 1) B
        // 2) X
        // 3) Y
        // 4) left bumper
        // 5) right bumper
        // 6) start
        // 7) back
        // 8) left stick (press)
        // 9) right stick (press)

        int[] buttons = new int[10];
        Controller controller;
        boolean foundMapping = false;

        public Mapping(Controller controller)
        {
            this.controller = controller;
            createMapping();
        }

        private void createMapping()
        {
            String osName = System.getProperty("os.name");
            foundMapping = true;

            if (osName.equalsIgnoreCase("Mac OS X")) {
                buttons = new int[] {11, 12, 13, 14, 8, 9, 4, 5, 7, 6};
            }

            else if (osName.equalsIgnoreCase("Linux")) {
                buttons = new int[] {0, 1, 2, 3, 4, 5, -1, 8, 10, 11};
            }

            else if (osName.equalsIgnoreCase("windows"))
            {}

            else
                foundMapping = false;
        }

        public boolean isMappingFound()
        {
            return foundMapping;
        }

        public boolean isButtonAPressed()
        {
            return controller.isButtonPressed(buttons[0]);
        }

        public boolean isButtonBPressed()
        {
            return controller.isButtonPressed(buttons[1]);
        }

        public boolean isButtonXPressed()
        {
            return controller.isButtonPressed(buttons[2]);
        }

        public boolean isButtonYPressed()
        {
            return controller.isButtonPressed(buttons[3]);
        }

        public boolean isRightStickPressed()
        {
            return controller.isButtonPressed(buttons[8]);
        }

        public boolean isLeftBumperPressed()
        {
            return controller.isButtonPressed(buttons[4]);
        }

        public boolean isRightBumperPressed()
        {
            return controller.isButtonPressed(buttons[5]);
        }
    }

}
