/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class SquareTest {
    
    public SquareTest() {
    }

    @Test
    public void testAssignNeighborInDirection() {
        Square interSq1 = InteriorSquare.generateInteriorSquareAt(21);
        Square interSq2 = InteriorSquare.generateInteriorSquareAt(22);
        Square interSq3 = InteriorSquare.generateInteriorSquareAt(38);
        Square interSq4 = InteriorSquare.generateInteriorSquareAt(46);
        Square interSq5 = InteriorSquare.generateInteriorSquareAt(56);
        Square interSq6 = InteriorSquare.generateInteriorSquareAt(49);
        
        interSq1.assignNeighborInDirection(interSq2, 2);
        interSq2.assignNeighborInDirection(interSq1, 6);
        interSq3.assignNeighborInDirection(interSq4, 4);
        interSq4.assignNeighborInDirection(interSq3, 0);
        interSq5.assignNeighborInDirection(interSq6, 1);
        interSq6.assignNeighborInDirection(interSq5, 5);
        
        assertEquals(interSq1.getNeighborInDirection(2), interSq2);
        assertEquals(interSq2.getNeighborInDirection(6), interSq1);
        assertEquals(interSq3.getNeighborInDirection(4), interSq4);
        assertEquals(interSq4.getNeighborInDirection(0), interSq3);
        assertEquals(interSq5.getNeighborInDirection(1), interSq6);
        assertEquals(interSq6.getNeighborInDirection(5), interSq5);
    }
    
    @Test
    public void testMixedAssignNeighborInDirection() {
        Square interSq = InteriorSquare.generateInteriorSquareAt(7);
        Square perimSq = PerimeterSquare.generatePerimeterSquareAt(75);
        Square nullSq = NullSquare.generateNullSquare();
        
        interSq.assignNeighborInDirection(perimSq, 2);
        perimSq.assignNeighborInDirection(interSq, 6);
        perimSq.assignNeighborInDirection(nullSq, 2);
        
        assertEquals(interSq.getNeighborInDirection(2), perimSq);
        assertEquals(perimSq.getNeighborInDirection(6), interSq);
        assertEquals(perimSq.getNeighborInDirection(2), nullSq);
    }
}
