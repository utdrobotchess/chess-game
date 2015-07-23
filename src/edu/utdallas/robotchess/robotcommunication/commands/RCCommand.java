package edu.utdallas.robotchess.robotcommunication.commands;

public class RCCommand extends Command
{
    int velocities[];

    public RCCommand(int robotID, int velocities[])
    {
        commandID = 0x4;
        payloadLength = 0x5;
        requiresACK = false;

        this.robotID = robotID;
        this.velocities = velocities;
    }

    @Override
    public int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        payload[0] = commandID;

        for (int i = 0; i < 4; i++)
            payload[i + 1] = velocities[i];

        return payload;
    }

    @Override
    public String toString()
    {
        if (velocities.length != 4)
            return "Incorrect Array Length for Velocities";

        return String.format("RC Command: (Robot ID %d) (Command ID %d) [%3d %3d %3d %3d]",
                robotID, commandID ,velocities[0], velocities[1], velocities[2], velocities[3]);
    }
}
