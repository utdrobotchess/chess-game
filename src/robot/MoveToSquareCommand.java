/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class MoveToSquareCommand extends Command
{
    final int MAX_DESTINATION = 63;
    final int MIN_DESTINATION = 0;
    private int destination;

    public MoveToSquareCommand(int destination)
    {
        commandID = 0x9;
        payloadLength = 0x2;

        this.robotID = -1;
        this.destination = destination;
    }

    public MoveToSquareCommand(int robotID, int destination)
    {
        commandID = 0x9;
        payloadLength = 0x2;
        this.robotID = robotID;
        this.destination = destination;
    }

    @Override
    protected int[] generatePayload()
    {
        if (destination > MAX_DESTINATION || destination < MIN_DESTINATION)
            return null;

        int payload[] = new int[payloadLength];

        payload[0] = commandID;
        payload[1] = destination;

        return payload;
    }

    @Override
    public String toString()
    {
        return "Move to " + destination;
    }
}
