/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import chess.engine.*;
import java.util.logging.*;

/*
 * Makes up the 36 spaces that border and surround a normal chessboard
 */
public class PerimeterSquare extends Square {
//    private static final Logger logger = ChessLogger.getInstance().logger;

    private PerimeterSquare() {
        super();
    }

    protected static PerimeterSquare generatePerimeterSquareAt(int location) {
        
        // Create PerimeterSquare objects
        PerimeterSquare perimSq = new PerimeterSquare();
        perimSq.setNumericalLocation(location);
        perimSq.setOccupant(NullChessPiece.spawnAt(perimSq));

        //logger.log(Level.FINE, "Perimeter square generated at {0}", location);
        return perimSq;
    }
}
