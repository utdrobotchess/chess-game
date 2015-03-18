/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.engine.heuristics;

import edu.utdallas.robotchess.game.Pawn;
import edu.utdallas.robotchess.game.Knight;
import edu.utdallas.robotchess.game.Bishop;
import edu.utdallas.robotchess.game.Rook;
import edu.utdallas.robotchess.game.Queen;
import edu.utdallas.robotchess.game.ChessPiece;

import edu.utdallas.robotchess.engine.State;
import edu.utdallas.robotchess.engine.ChessGameState;

import java.util.List;

public class MaterialHeuristicFunction implements HeuristicFunction
{
    private final double PAWN_VALUE = 1.0;
    private final double KNIGHT_VALUE = 3.0;
    private final double BISHOP_VALUE = 3.25;
    private final double ROOK_VALUE = 5.0;
    private final double QUEEN_VALUE = 9.0;

    @Override
    public double h(State state)
    {
        ChessGameState gameState = (ChessGameState) state;
        
        List<ChessPiece> activePieces = gameState.getActivePieces();

        double materialValue = 0.0;

        for (int i = 0; i < activePieces.size(); i++) {
            ChessPiece piece = activePieces.get(i);
            double multiplier = piece.getTeam() == gameState.getActiveTeam() ? 1.0 : -1.0;
            materialValue += multiplier * getPieceValue(piece);
        }
        
        normalize(materialValue);

        return materialValue;
    }
    
    private double getPieceValue(ChessPiece piece)
    {
        if (piece instanceof Pawn)
            return PAWN_VALUE;

        if (piece instanceof Knight)
            return KNIGHT_VALUE;

        if (piece instanceof Bishop)
            return BISHOP_VALUE;

        if (piece instanceof Rook)
            return ROOK_VALUE;

        if (piece instanceof Queen)
            return QUEEN_VALUE;

        return 0.0;
    }
    
    private double normalize(double value)
    {
        final double MAX = 8 * PAWN_VALUE +
            2 * KNIGHT_VALUE +
            2 * BISHOP_VALUE +
            2 * ROOK_VALUE +
            2 * QUEEN_VALUE;
        
        return value / MAX;
    }
}
