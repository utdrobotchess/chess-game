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
public class NullSquareTest {
    
    public NullSquareTest() {
    }

    @Test
    public void testGenerateNullSquare() {
        NullSquare nullSq = NullSquare.generateNullSquare();
        assertEquals(nullSq.getNumericalLocation(), -1);
    }
    
}
