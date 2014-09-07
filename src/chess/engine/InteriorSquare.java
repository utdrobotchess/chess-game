
package chess.engine;

/**
 *
 * @author Alexandre
 */
public class InteriorSquare extends Square{
     protected static InteriorSquare generateInteriorSquareAt(int location) {
        InteriorSquare interSq = new InteriorSquare();
        interSq.setNumericalLocation(location);
        interSq.setOccupant(NullChessPiece.spawnAt(interSq));

       // logger.log(Level.FINE, "Interior square generated at {0}", location);

        return interSq;
    }
}
