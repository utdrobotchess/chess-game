package edu.utdallas.robotchess.robot;

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
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        payload[0] = commandID;

        for (int i = 0; i < 4; i++)
            payload[i + 1] = velocities[i];

        return payload;
    }
}
