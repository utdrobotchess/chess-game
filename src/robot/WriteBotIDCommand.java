/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class WriteBotIDCommand extends Command
{
    final int MIN_ID = 0;
    final int MAX_ID = 31;
    private int newRobotID;
    
    public WriteBotIDCommand(int currentRobotID, int newRobotID)
    {
        commandID = 0x1;
        payloadLength = 0x2;
        this.robotID = currentRobotID;
        this.newRobotID = newRobotID;
    }

    @Override
    protected int[] generatePayload()
    {
        if (newRobotID > MAX_ID || newRobotID < MIN_ID)
            return null;

        int payload[] = new int[payloadLength];

        payload[0] = commandID;
        payload[1] = newRobotID;

        return payload;
    }
}
