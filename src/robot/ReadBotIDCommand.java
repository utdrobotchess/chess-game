/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class ReadBotIDCommand extends Command
{
    public ReadBotIDCommand(int robotID)
    {
        commandID = 0xa;
        payloadLength = 0x1;
        this.robotID = robotID;
    }

    @Override
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        payload[0] =  commandID;
        
        return payload;
    }
}
