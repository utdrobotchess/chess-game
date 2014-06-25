/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

public class PerimeterSquareTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

    @Test
    public void testGeneratePerimeterSquare() {
        logger.log(Level.WARNING, "Begin testGeneratePerimeterSquare() - PerimeterSquareTest");

        PerimeterSquare perimSq1 = PerimeterSquare.generatePerimeterSquareAt(75);
        PerimeterSquare perimSq2 = PerimeterSquare.generatePerimeterSquareAt(95);

        assertEquals(perimSq1.getNumericalLocation(), 75);
        assertEquals(perimSq2.getNumericalLocation(), 95);

        logger.log(Level.WARNING, "End testGeneratePerimeterSquare() - PerimeterSquareTest");
    }
}
