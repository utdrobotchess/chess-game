/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

import com.rapplogic.xbee.api.XBeeAddress64;

public class ReadBotIDCommand extends Command
{

    public ReadBotIDCommand(int robotID)
    {
        commandID = 0xa;
        payloadLength = 0x1;
        this.robotID = robotID;
    }

    public ReadBotIDCommand(XBeeAddress64 xbeeAddress)
    {
        commandID = 0xa;
        payloadLength = 0x1;
        this.xbeeAddress = xbeeAddress;
    }

    @Override
    protected int[] generatePayload()
    {
        int payload[] = new int[payloadLength];

        payload[0] =  commandID;
        
        return payload;
    }
}
