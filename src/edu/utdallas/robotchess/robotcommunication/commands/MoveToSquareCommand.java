package edu.utdallas.robotchess.robotcommunication.commands;

public class MoveToSquareCommand extends Command
{
    final int MAX_DESTINATION = 63;
    final int MIN_DESTINATION = 0;
    private int[] destinations;

    public MoveToSquareCommand(int robotID, int[] destinations)
    {
        commandID = 0x3;
        payloadLength = 0x1 + destinations.length;
        this.robotID = robotID;
        this.destinations = destinations;
    }

    @Override
    public int[] generatePayload()
    {
        for(int i = 0; i < destinations.length; i++)
            if (destinations[i] > MAX_DESTINATION || destinations[i] < MIN_DESTINATION)
                return null;

        int payload[] = new int[payloadLength];

        payload[0] = commandID;

        for(int i = 0; i < destinations.length; i++)
            payload[i + 1] = destinations[i];

        return payload;
    }

    @Override
    public String toString()
    {
        String str = String.format("Move To Square Command: (Robot ID %d) (Command ID %d) [", 
                robotID, commandID);

        for (int i = 0; i < destinations.length; i++)
            str += destinations[i] + " ";

        str += "]";

        return str;
    }
}
