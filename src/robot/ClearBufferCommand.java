package robot;

public class ClearBufferCommand extends Command
{
    public ClearBufferCommand(int robotID)
    {
        commandID = 0xfe;
        payloadLength = 0x1;
        this.robotID = robotID;
    }

    @Override
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        payload[0] = commandID;

        return payload;
    }
}
