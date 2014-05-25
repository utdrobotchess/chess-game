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
 * @author Owner
 */
public class PerimeterSquareTest {
    
    public PerimeterSquareTest() {
    }

    @Test
    public void testGeneratePerimeterSquare() {
        PerimeterSquare perimSq1 = PerimeterSquare.generatePerimeterSquareAt(75);
        PerimeterSquare perimSq2 = PerimeterSquare.generatePerimeterSquareAt(95);
        
        assertEquals(perimSq1.getLocation(), 75);
        assertEquals(perimSq2.getLocation(), 95);
    }
    
}
