package edu.utdallas.robotchess.robot;

import java.util.*;

public class Path
{
    private int robotID;
    private ArrayList<Integer> path;

    public Path(int robotID)
    {
        this.robotID = robotID;
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
}
