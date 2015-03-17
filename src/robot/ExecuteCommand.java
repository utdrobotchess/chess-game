package robot;

import com.rapplogic.xbee.api.XBeeAddress64;

public class ExecuteCommand extends Command
{
    public ExecuteCommand(int robotID)
    {
        commandID = 0xff;
        payloadLength = 0x1;
        this.robotID = robotID;
    }

    public ExecuteCommand(XBeeAddress64 xbeeAddress)
    {
        commandID = 0xff;
        payloadLength = 0x1;
        this.xbeeAddress = xbeeAddress;
    }

    @Override
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        payload[0] = commandID;

        return payload;
    }
}
