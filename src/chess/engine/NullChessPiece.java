package chess.engine;

import java.util.*;
import java.util.logging.*;

/*
 * An imaginary chess piece that is used as the occupant of a square when a real
 * chess piece does not occupy that square
 */
public class NullChessPiece extends ChessPiece {
  //  private static final Logger logger = ChessLogger.getInstance().logger;

    private NullChessPiece() {
        super();
    }

    protected static NullChessPiece spawnAt(Square location) {
        NullChessPiece piece = new NullChessPiece();

        piece.setLocation(location);

       /* logger.log(Level.FINE, "Null chess piece spawned at location {0}",
                   location.getNumericalLocation());*/

        return piece;
    }

   /* @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        return new ArrayList<>();
    }*/

    public String toString() {
        return "Null chesspiece";
    }

    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        return new ArrayList<>();
    }
}
