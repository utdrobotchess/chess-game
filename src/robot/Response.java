/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

public class Response
{
    private int responseID;
    private int[] payload;
    private int robotID;
    
    public Response(int[] payload, int robotID)
    {
    	this.payload = payload;
    	this.robotID = robotID;
    	this.responseID = payload[0];
    }
    
    public int[] getPayload()
    {
    	return this.payload;
    }
    
    public int getresponseID()
    {
    	return this.responseID;
    }

    public int getRobotID()
    {
        return this.robotID;
    }

    public int[] putPayload()
    {
    	return this.payload;
    }
    
    public int putresponseID()
    {
    	return this.responseID;
    }

    public int putRobotID()
    {
        return this.robotID;
    }
}
