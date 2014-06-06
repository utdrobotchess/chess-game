package chess.engine;

import java.util.logging.*;

/**
 *
 * @author Ryan J. Marcotte
 */
public class ChessGameBuilder {
    private static final Logger logger = ChessLogger.getInstance().logger;

    private static ChessBoard board;

    private final static int[] ROOK_LOCATIONS = {0, 7, 56, 63};
    private final static int[] KNIGHT_LOCATIONS = {1, 6, 57, 62};
    private final static int[] BISHOP_LOCATIONS = {2, 5, 58, 61};
    private final static int[] QUEEN_LOCATIONS = {3, 59};
    private final static int[] KING_LOCATIONS = {4, 60};
    private final static int[] PAWN_LOCATIONS = {8, 9, 10, 11, 12, 13, 14, 15,
                                                48, 49, 50, 51, 52, 53, 54, 55};

    private ChessGameBuilder() { }

    protected static void build(ChessGame game) {
        board = ChessBoard.generateChessBoard();
        game.addBuiltChessBoard(board);
        game.setActiveTeam(Team.GREEN);

        addPieces(game);

        logger.log(Level.FINE, "Chess game built");
    }

    private static void addPieces(ChessGame game) {
        for(int i = 0; i < 4; i++) {
            Rook rook = Rook.spawnAt(board.getSquareAt(ROOK_LOCATIONS[i]));
            game.addChessPiece(rook);

            Knight knight = Knight.spawnAt(board.getSquareAt(KNIGHT_LOCATIONS[i]));
            game.addChessPiece(knight);

            Bishop bishop = Bishop.spawnAt(board.getSquareAt(BISHOP_LOCATIONS[i]));
            game.addChessPiece(bishop);
        }

        for(int i = 0; i < 2; i++) {
            Queen queen = Queen.spawnAt(board.getSquareAt(QUEEN_LOCATIONS[i]));
            game.addChessPiece(queen);

            King king = King.spawnAt(board.getSquareAt(KING_LOCATIONS[i]));
            game.addChessPiece(king);
        }

        for(int i = 0; i < 16; i++) {
            Pawn pawn = Pawn.spawnAt(board.getSquareAt(PAWN_LOCATIONS[i]));
            game.addChessPiece(pawn);
        }
    }
}
