package edu.utdallas.robotchess.robotcommunication.commands;

public class SmartCenterCommand extends Command
{
    public SmartCenterCommand(int robotID)
    {
        commandID = 0x7;
        payloadLength = 0x1;

        this.robotID = robotID;
    }

    @Override
    public int[] generatePayload()
    {
        int payload[] = new int[payloadLength];
        payload[0] = commandID;

        return payload;
    }
}
