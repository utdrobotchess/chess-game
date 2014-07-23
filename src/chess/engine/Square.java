/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.logging.*;

public abstract class Square implements Comparable<Square> {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private ChessPiece itsOccupant;
    private int itsNumericalLocation;

    /* Directional neighbors defined as follows:
    *  7   0   1
    *  6  ***  2
    *  5   4   3
    */
    private final Square[] itsNeighbors = new Square[8];

    protected void assignNeighborInDirection(Square newNeighbor, int direction) {
        itsNeighbors[direction] = newNeighbor;

        logger.log(Level.FINER, "{0} assigned as neighbor to {1} in direction {2}",
                   new Object[] {newNeighbor, this, direction});
    }

	/*
	 * Allows for comparisons and sorting of Squares based on the numberical value
	 * of their location.
	 */
    @Override
    public int compareTo(Square secondSquare) {
        if (itsNumericalLocation < secondSquare.getNumericalLocation()) {
            return -1;
        } else if(itsNumericalLocation == secondSquare.getNumericalLocation()) {
            return 0;
        } else {
            return 1;
        }
    }

    protected int getNumericalLocation() {
        return itsNumericalLocation;
    }

    protected Square getNeighborInDirection(int direction) {
        return itsNeighbors[direction];
    }

    protected ChessPiece getOccupant() {
        return itsOccupant;
    }

    protected boolean isOccupied() {
        if (itsOccupant instanceof NullChessPiece) {
            return false;
        } else {
            return true;
        }
    }

    protected void setNumericalLocation(int location) {
        itsNumericalLocation = location;

        logger.log(Level.FINER, "{0} location set to {1}",
                   new Object[] {this, location});
    }

    protected void setOccupant(ChessPiece occupant) {
        itsOccupant = occupant;

        logger.log(Level.FINER, "{0} occupant set to {1}",
                   new Object[] {this, occupant});
    }

    @Override
    public String toString(){
        return "Square at " + itsNumericalLocation;
    }
}
