package edu.utdallas.robotchess.robotcommunication.commands;

import com.rapplogic.xbee.api.XBeeAddress64;

public abstract class Command
{
    public int commandID;
    public int payloadLength;
    public int robotID;
    public int timeout = 5000;
    public int retries = 3;
    public boolean requiresACK = true;
    public XBeeAddress64 xbeeAddress;

    public abstract int[] generatePayload();

    public XBeeAddress64 GetXbeeAddress()
    {
        return this.xbeeAddress;
    }

    public void setRobotID(int robotID)
    {
        this.robotID = robotID;
    }

    public int getRobotID()
    {
        return robotID;
    }

    public boolean RequiresACK()
    {
        return requiresACK;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public int getRetries()
    {
        return retries;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public void setRetries(int retries)
    {
        this.retries = retries;
    }
}
