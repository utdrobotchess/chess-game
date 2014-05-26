package chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the functionality of a PerimeterSquare, including generation
 * @author Ryan J. Marcotte 
 */
public class PerimeterSquareTest {

    @Test
    public void testGeneratePerimeterSquare() {
        PerimeterSquare perimSq1 = PerimeterSquare.generatePerimeterSquareAt(75);
        PerimeterSquare perimSq2 = PerimeterSquare.generatePerimeterSquareAt(95);
        
        assertEquals(perimSq1.getNumericalLocation(), 75);
        assertEquals(perimSq2.getNumericalLocation(), 95);
    }
}
