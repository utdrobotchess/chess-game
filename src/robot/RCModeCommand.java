/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class RCModeCommand extends Command
{
    public RCModeCommand(int robotID)
    {
       commandID = 0xB; 
    }

    @Override
    protected int[] generatePayload()
    {
        return new int[] {commandID};
    }
}
