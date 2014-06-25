/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

public class NullSquareTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

    @Test
    public void testGenerateNullSquare() {
        logger.log(Level.WARNING, "Begin testGenerateNullSquare() - NullSquareTest");

        NullSquare nullSq = NullSquare.generateNullSquare();
        assertEquals(nullSq.getNumericalLocation(), -1);

        logger.log(Level.WARNING, "End testGenerateNullSquare() - NullSquareTest");
    }
}
