/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

import java.util.LinkedList;
import java.util.Queue;

import robot.Command;
import robot.Motion;

public class RobotState
{
    private boolean ready;
    private Queue<Command> commandQueue;
    private Queue<Motion> motionQueue;
    private int boardRows;
    private int boardColumns;
    
    public RobotState()
    {
        commandQueue = new LinkedList<>();
        motionQueue = new LinkedList<>();
    }

    public synchronized void addNewCommand(Command newCommand)
    {
        commandQueue.offer(newCommand);
    }

    public synchronized void addNewMotion(Motion newMotion)
    {
        motionQueue.offer(newMotion);
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

    public synchronized boolean isReady()
    {
        return ready;
    }

    public synchronized Command removeNextCommand()
    {
        return commandQueue.poll();
    }

    public synchronized Motion removeNextMotion()
    {
        return motionQueue.poll();
    }

    public synchronized void setReady(boolean ready)
    {
        this.ready = ready;
    }

    public synchronized void setBoardColumns(int boardColumns)
    {
        this.boardColumns = boardColumns;
    }

    public synchronized void setBoardRows(int boardRows)
    {
        this.boardRows = boardRows;
    }
}
