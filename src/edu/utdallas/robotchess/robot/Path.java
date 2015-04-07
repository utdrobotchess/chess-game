package edu.utdallas.robotchess.robot;

import java.util.*;

public class Path
{
    private int robotID;
    private int origin;
    private ArrayList<Integer> path;

    public Path(int robotID, int origin)
    {
        this.robotID = robotID;
        this.origin = origin;
        path = new ArrayList<>();
    }
    
    protected void add(int location)
    {
        path.add(location);
    }
    
    public ArrayList<Integer> getPath()
    {
        return path;
    }
    
    public int getRobotID()
    {
        return robotID;
    }

    public Command generateCommand()
    {
        int[] payload = new int[path.size()];

        for(int i = 0; i < path.size(); i++)
            payload[i] = path.get(i);

        return new MoveToSquareCommand(robotID, payload);
    }
 }
