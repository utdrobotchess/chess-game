package chess.engine;

import java.util.*;

/**
 * An imaginary chess piece that is used as the occupant of a square when a real
 * chess piece does not occupy that square
 * @author Ryan J. Marcotte
 */
public class NullChessPiece extends ChessPiece {
    private NullChessPiece() {
        super();
    }
    
    protected static NullChessPiece spawnAt(Square location) {
        NullChessPiece piece = new NullChessPiece();
        piece.setLocation(location);
        return piece;
    }
    
    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        return new ArrayList<>();
    }
}
