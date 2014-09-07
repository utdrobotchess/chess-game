package chess.engine;

/**
 *
 * @author Alexandre
 */
public class NullSquare extends Square {
   // private static final Logger logger = ChessLogger.getInstance().logger;
    private static NullSquare instance = null;

    private NullSquare() {  }

    protected static NullSquare generateNullSquare() {
        if (instance == null) {
            instance = new NullSquare();
            instance.setNumericalLocation(-1);
            instance.setOccupant(NullChessPiece.spawnAt(instance));

            for (int i = 0; i < 8; i++) {
                instance.assignNeighborInDirection(instance, i);
            }
        }

       // logger.log(Level.FINE, "Null square generated");

		return instance;
    }
}