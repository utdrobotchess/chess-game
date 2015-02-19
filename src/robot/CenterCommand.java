/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class CenterCommand extends Command
{
    final int MAX_DEGREES = 360;
    final int MIN_DEGREES = 0;
    final int QUANTIZATION_LEVEL = 45;
    private int firstEdgeDegrees;
    private int secondEdgeDegrees;

    public CenterCommand(int robotID, int firstEdgeDegrees, int secondEdgeDegrees)
    {
        commandID = 0x3; 
        payloadLength = 0x3;
        this.firstEdgeDegrees = firstEdgeDegrees;
        this.secondEdgeDegrees = secondEdgeDegrees;
    }

    @Override
    protected int[] generatePayload()
    {
        if (firstEdgeDegrees > MAX_DEGREES || secondEdgeDegrees > MAX_DEGREES ||
            firstEdgeDegrees < MIN_DEGREES || secondEdgeDegrees < MIN_DEGREES ||
            firstEdgeDegrees % QUANTIZATION_LEVEL != 0 ||
            secondEdgeDegrees % QUANTIZATION_LEVEL != 0) 
            return null;

        int payload[] = new int[payloadLength];

        payload[0] = commandID;
        payload[1] = firstEdgeDegrees / QUANTIZATION_LEVEL;
        payload[2] = secondEdgeDegrees / QUANTIZATION_LEVEL;

        return payload;
    }
}
