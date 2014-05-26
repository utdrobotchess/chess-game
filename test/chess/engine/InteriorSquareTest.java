package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the functionality of an InteriorSquare, including generation
 * @author Ryan J. Marcotte
 */
public class InteriorSquareTest {
    @Test
    public void testGenerateInteriorSquareAt() {
        Square sq1 = InteriorSquare.generateInteriorSquareAt(8);
        Square sq2 = InteriorSquare.generateInteriorSquareAt(42);
        
        assertEquals(sq1.getNumericalLocation(), 8);
        assertEquals(sq2.getNumericalLocation(), 42);
    }
    
}
