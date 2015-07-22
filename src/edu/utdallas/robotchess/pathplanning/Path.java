package edu.utdallas.robotchess.pathplanning;

import java.util.ArrayList;

import edu.utdallas.robotchess.robotcommunication.commands.Command;
import edu.utdallas.robotchess.robotcommunication.commands.MoveToSquareCommand;

public class Path
{
    private int robotID;
    private int origin;
    private ArrayList<Integer> squareSequence;

    public Path(int robotID, int origin)
    {
        this.robotID = robotID;
        this.origin = origin;
        squareSequence = new ArrayList<>();
    }

    //TODO: enforce adding neighbors only, since none of this works if
    //you do not sequentially add neighor squares until the destination
    public void add(int location)
    {
        squareSequence.add(location);
    }

    //TODO: enforce adding neighbors only, since none of this works if
    //you do not sequentially add neighor squares until the destination
    public void setSquareSequence(ArrayList<Integer> squareSequence)
    {
        this.squareSequence = squareSequence;
    }

    public ArrayList<Integer> getPath()
    {
        return squareSequence;
    }

    public int getRobotID()
    {
        return robotID;
    }

    public Command generateCommand()
    {
        if (squareSequence.size() == 0)
            return null;

        ArrayList<Integer> payload = new ArrayList<>();

        int previousDirection = squareSequence.get(0) - origin;

        for(int i = 1; i < squareSequence.size(); i++) {
            int newDirection = squareSequence.get(i) - squareSequence.get(i - 1);
            if (newDirection != previousDirection) {
                payload.add(squareSequence.get(i - 1));
                previousDirection = newDirection;
            }
        }

        payload.add(squareSequence.get(squareSequence.size() - 1));

        int[] payloadArr = new int[payload.size()];
        for (int i = 0; i < payloadArr.length; i++)
            payloadArr[i] = payload.get(i);

        return new MoveToSquareCommand(robotID, payloadArr);
    }

    @Override
    public String toString()
    {
        String str = new String();
        str = String.format("<%d> (%d) [", robotID, origin);

        for (Integer i : squareSequence)
            str += String.format("%d ", i);

        str += String.format("]");
        return str;
    }
 }
