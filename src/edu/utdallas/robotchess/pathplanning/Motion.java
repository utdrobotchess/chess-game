package edu.utdallas.robotchess.pathplanning;

public class Motion
{
    private int currentRobotPositions[];
    private int desiredRobotPositions[];

    public Motion(int current[], int desired[])
    {
        currentRobotPositions = current;
        desiredRobotPositions = desired;
    }

    public int[] getCurrent()
    {
        return currentRobotPositions;
    }

    public int[] getDesired()
    {
        return desiredRobotPositions;
    }

    @Override
    public String toString()
    {
        String output = "Current Positions: ";

        for (int i = 0; i < currentRobotPositions.length; i++)
            output += " " + currentRobotPositions[i];

        output += "\n";

        for (int i = 0; i < desiredRobotPositions.length; i++)
            output += " " + desiredRobotPositions[i];

        output += "\n";

        return output;
    }
}
