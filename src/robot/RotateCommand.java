/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class RotateCommand extends Command
{
    final int MAX_DEGREES = 360;
    final int MIN_DEGREES = 0;
    final int QUANTIZATION_LEVEL = 45;
    private int degrees;
    
    public RotateCommand(int robotID, int degrees)
    {
        commandID = 0x2;
        payloadLength = 0x2;
        this.degrees = degrees;
        this.robotID = robotID;
    }

    @Override
    protected int[] generatePayload()
    {
        if (degrees > MAX_DEGREES ||
            degrees < MIN_DEGREES ||
            degrees % QUANTIZATION_LEVEL != 0)
            return null;
        
        int payload[] = new int[payloadLength];

        payload[0] = commandID;
        payload[1] = degrees / QUANTIZATION_LEVEL;
        
        return payload;
    }
}
