package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the functionality of a null square, including generation
 * @author Ryan J. Marcotte 
 */
public class NullSquareTest {
    
    @Test
    public void testGenerateNullSquare() {
        NullSquare nullSq = NullSquare.generateNullSquare();
        assertEquals(nullSq.getNumericalLocation(), -1);
    }
}
