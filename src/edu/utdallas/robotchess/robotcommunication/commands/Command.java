package edu.utdallas.robotchess.robotcommunication.commands;

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

    public int[] generatePayload()
    {
        return null;
    }

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

    public boolean getACK()
    {
        return requiresACK;
    }

    public void setACK(boolean ack)
    {
        requiresACK = ack;
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

    @Override
    public abstract String toString();
}
