/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

import com.rapplogic.xbee.api.XBeeAddress64;

public abstract class Command
{
    protected int commandID;
    protected int payloadLength;
    protected int robotID;
    protected int timeout = 5000;
    protected int retries = 3;
    protected boolean requiresACK = true;
    protected XBeeAddress64 xbeeAddress;

    protected abstract int[] generatePayload();

    protected XBeeAddress64 GetXbeeAddress()
    {
        return this.xbeeAddress;
    }

    protected void setRobotID(int robotID)
    {
        this.robotID = robotID;
    }

    protected int getRobotID()
    {
        return robotID;
    }

    protected boolean RequiresACK()
    {
        return requiresACK;
    }

    protected int getTimeout()
    {
        return timeout;
    }

    protected int getRetries()
    {
        return retries;
    }

    protected void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    protected void setRetries(int retries)
    {
        this.retries = retries;
    }
}
