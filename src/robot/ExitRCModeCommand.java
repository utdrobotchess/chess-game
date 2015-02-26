package robot;

public class ExitRCModeCommand extends Command
{
    public ExitRCModeCommand(int robotID)
    {
        commandID = 0;
        payloadLength = 0x5;
        
        this.robotID = robotID;
    }
    
    @Override
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        for (int i = 0; i < 4; i++)
            payload[i] = 0;

        payload[4] = 0xff;

        return payload;
    }
}
