/**
 *
 * @author Ryan J. Marcotte
 */

package robot;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class MotionPlannerTest
{
    public static void main(String[] args)
    {
        MotionPlannerTest test = new MotionPlannerTest();
        test.testSmallBoard();
    }
    
    //@Test
    public void testSimplePath()
    {
        int current[] = {0};
        int desired[] = {16};

        int expected[] = {0, 8, 16};
        
        MotionPlanner planner = new MotionPlanner(current, desired, 8, 8);

        ArrayList<Integer> plan = planner.plan();

        Assert.assertEquals(expected.length, plan.size());

        for (int i = 0; i < expected.length; i++)
            Assert.assertEquals(expected[i], plan.get(i).intValue());
    }

    //@Test
    public void testNoPath()
    {
        int current[] = {0, 1, 8, 9};
        int desired[] = {63, 1, 8, 9};

        int expected[] = {};
        
        MotionPlanner planner = new MotionPlanner(current, desired, 8, 8);

        ArrayList<Integer> plan = planner.plan();

        Assert.assertEquals(expected.length, plan.size());

        for (int i = 0; i < expected.length; i++)
            Assert.assertEquals(expected[i], plan.get(i).intValue());
    }

    //@Test
    public void testMoveToOccupied()
    {
        int current[] = {0, 20};
        int desired[] = {20, 20};
        int expected[] = {};
        
        MotionPlanner planner = new MotionPlanner(current, desired, 8, 8);

        ArrayList<Integer> plan = planner.plan();

        Assert.assertEquals(expected.length, plan.size());

        for (int i = 0; i < expected.length; i++)
            Assert.assertEquals(expected[i], plan.get(i).intValue());
    }

    //@Test
    public void testMoveAroundObstructions()
    {
        int current[] = {0, 8, 9, 10, 11, 12, 20, 28, 36, 33, 34, 35, 25};
        int desired[] = {26, 8, 9, 10, 11, 12, 20, 28, 36, 33, 34, 35, 25};
        int expected[] = {0, 1, 2, 3, 4, 13, 21, 29, 37, 44, 43, 42, 41, 32, 24, 17, 26};

        MotionPlanner planner = new MotionPlanner(current, desired, 8, 8);

        ArrayList<Integer> plan = planner.plan();

        Assert.assertEquals(expected.length, plan.size());

        for (int i = 0; i < expected.length; i++)
            Assert.assertEquals(expected[i], plan.get(i).intValue());
    }

    //@Test
    public void testSmallBoard()
    {
        int current[] = {0, 8, 9, 10, 11, 19, 27, 26, 25};
        int desired[] = {18, 8, 9, 10, 11, 19, 27, 26, 25};
        int expected[] = {0, 1, 2, 3, 12, 20, 28, 35, 34, 33, 24, 17, 18};
        
        MotionPlanner planner = new MotionPlanner(current, desired, 5, 5);

        ArrayList<Integer> plan = planner.plan();

        Assert.assertEquals(expected.length, plan.size());

        for (int i = 0; i < expected.length; i++)
            Assert.assertEquals(expected[i], plan.get(i).intValue());
    }

    @Test
    public void testCapture()
    {
        
    }

    //@Test
    public void testCastlingSimple()
    {

    }
}
