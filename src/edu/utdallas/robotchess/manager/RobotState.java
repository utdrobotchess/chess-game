/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.manager;

import java.util.LinkedList;
import java.util.Queue;

import edu.utdallas.robotchess.robot.Command;
import edu.utdallas.robotchess.robot.Motion;
import edu.utdallas.robotchess.robot.Response;

public class RobotState
{
    private boolean ready;
    private boolean rcMode;
    private int boardColumns;
    private int boardRows;

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

    public synchronized int getBoardColumns()
    {
        return boardColumns;
    }

    public synchronized int getBoardRows()
    {
        return boardRows;
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

    public synchronized boolean isRCMode()
    {
        return rcMode;
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

    public synchronized void setBoardColumns(int columns)
    {
        this.boardColumns = columns;
    }

    public synchronized void setBoardRows(int rows)
    {
        this.boardRows = rows;
    }

    public synchronized void setReady(boolean ready)
    {
        this.ready = ready;
    }

    public synchronized void setRCMode(boolean rcMode)
    {
        this.rcMode = rcMode;
    }
}
