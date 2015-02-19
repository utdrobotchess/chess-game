package robot;

public class ExecuteCommand extends Command
{
    public ExecuteCommand(int robotID)
    {
        commandID = 0xff;
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
