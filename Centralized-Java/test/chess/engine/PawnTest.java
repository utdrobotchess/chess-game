/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chess.engine;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author Owner
 */
public class PawnTest {
    
    public PawnTest() {
    }
    
    @Test
    public void testSpawn() {
        Pawn p1 = Pawn.spawnPawnAt(8);
        Pawn p2 = Pawn.spawnPawnAt(12);
        Pawn p3 = Pawn.spawnPawnAt(50);
        Pawn p4 = Pawn.spawnPawnAt(53);
        
        assertEquals(p1.getLocation(), 8);
        assertEquals(p2.getLocation(), 12);
        assertEquals(p3.getLocation(), 50);
        assertEquals(p4.getLocation(), 53);
        
        assertEquals(p1.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(p2.getTeam(), ChessPiece.Team.GREEN);
        assertEquals(p3.getTeam(), ChessPiece.Team.ORANGE);
        assertEquals(p4.getTeam(), ChessPiece.Team.ORANGE);
        
        assertEquals(p1.getNumberOfPriorMoves(), 0);
    }
    
    @Test
    public void testPossibleOpeningMoves() {
        Pawn p1 = Pawn.spawnPawnAt(9);
        Pawn p2 = Pawn.spawnPawnAt(51);
        
        ArrayList<Integer> p1ExpectedMoveLocations = new ArrayList<>();
        p1ExpectedMoveLocations.add(17);
        p1ExpectedMoveLocations.add(25);
        
        ArrayList<Integer> p2ExpectedMoveLocations = new ArrayList<>();
        p2ExpectedMoveLocations.add(35);
        p2ExpectedMoveLocations.add(43);
        
        assertEquals(p1.getPossibleMoveLocations(), p1ExpectedMoveLocations);
        assertEquals(p2.getPossibleMoveLocations(), p2ExpectedMoveLocations);
    }
}
