/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class CrossSquaresCommand extends Command
{
    final int MAX_SQUARES = 7;
    final int MIN_SQUARES = 1;
    private int numberOfSquares;

    public CrossSquaresCommand(int robotID, int numberOfSquares)
    {
        commandID = 0x1;
        payloadLength = 0x2;
        this.numberOfSquares = numberOfSquares;
        this.robotID = robotID;
    }

    @Override
    protected int[] generatePayload()
    {
        if (numberOfSquares > MAX_SQUARES || numberOfSquares < MIN_SQUARES)
            return null;
            
        int payload[] = new int[payloadLength];

        payload[0] = commandID;
        payload[1] = numberOfSquares;

        return payload;
    }
}
