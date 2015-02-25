/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class MeasureSquareStateCommand extends Command
{
    public MeasureSquareStateCommand(int robotID)
    {
       commandID = 0x5; 
       payloadLength = 0x1;
       
       this.robotID = robotID;
    }

    @Override
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        payload[0] = commandID;
        return null;
    }
}
