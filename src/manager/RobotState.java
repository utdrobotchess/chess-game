/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

import java.util.LinkedList;
import java.util.Queue;

import robot.Command;
import robot.Motion;
import robot.Response;

public class RobotState
{
    private boolean ready;
    private boolean closeCommunication = false;

    private Queue<Command> commandQueue;
    private Queue<Motion> motionQueue;
    private Queue<Response> responseQueue;
    
    public RobotState()
    {
        commandQueue = new LinkedList<>();
        motionQueue = new LinkedList<>();
        responseQueue = new LinkedList<>();
    }

    public synchronized void addNewCommand(Command newCommand)
    {
        commandQueue.offer(newCommand);
    }

    public synchronized void addNewMotion(Motion newMotion)
    {
        motionQueue.offer(newMotion);
    }
    
    public synchronized void addNewResponse(Response newResponse)
    {
    	responseQueue.offer(newResponse);
    }

    public synchronized boolean isCommandAvailable()
    {
        return !commandQueue.isEmpty();
    }

    public synchronized boolean isMotionAvailable()
    {
        return !motionQueue.isEmpty();
    }
    
    public synchronized boolean isResponseAvailable()
    {
    	return !responseQueue.isEmpty();
    }

    public synchronized boolean isReady()
    {
        return ready;
    }

    public synchronized Command pollNextCommand()
    {
        return commandQueue.poll();
    }

    public synchronized Motion pollNextMotion()
    {
        return motionQueue.poll();
    }
    
    public synchronized Response pollNextResponse()
    {
    	return responseQueue.poll();
    }
    
    public synchronized Response peekNextResponse()
    {
    	return responseQueue.peek();
    }
    
    public synchronized boolean getCloseCommunication()
    {
    	return closeCommunication;
    }
    
    public synchronized void setCloseCommunication(boolean closeCommunication)
    {
    	this.closeCommunication = closeCommunication;
    }

    public synchronized void setReady(boolean ready)
    {
        this.ready = ready;
    }
}
