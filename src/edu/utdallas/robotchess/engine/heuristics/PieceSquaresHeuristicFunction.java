package edu.utdallas.robotchess.engine.heuristics;

import java.util.*;

import edu.utdallas.robotchess.game.*;

public class PieceSquaresHeuristicFunction implements HeuristicFunction
{

    final private int[] pawnTable = {0 ,  0,  0,  0,  0,  0,  0,  0,
                                     50, 50, 50, 50, 50, 50, 50, 50,
                                     10, 10, 20, 30, 30, 20, 10, 10,
                                     5 ,  5, 10, 25, 25, 10,  5,  5,
                                     0 ,  0,  0, 20, 20,  0,  0,  0,
                                     5 , -5,-10,  0,  0,-10, -5,  5,
                                     5 , 10, 10,-20,-20, 10, 10,  5,
                                     0 ,  0,  0,  0,  0,  0,  0,  0};

    final private int[] knightTable = {-50,-40,-30,-30,-30,-30,-40,-50,
                                       -40,-20,  0,  0,  0,  0,-20,-40,
                                       -30,  0, 10, 15, 15, 10,  0,-30,
                                       -30,  5, 15, 20, 20, 15,  5,-30,
                                       -30,  0, 15, 20, 20, 15,  0,-30,
                                       -30,  5, 10, 15, 15, 10,  5,-30,
                                       -40,-20,  0,  5,  5,  0,-20,-40,
                                       -50,-40,-30,-30,-30,-30,-40,-50};

    final private int[] bishopTable = {-20,-10,-10,-10,-10,-10,-10,-20,
                                       -10,  0,  0,  0,  0,  0,  0,-10,
                                       -10,  0,  5, 10, 10,  5,  0,-10,
                                       -10,  5,  5, 10, 10,  5,  5,-10,
                                       -10,  0, 10, 10, 10, 10,  0,-10,
                                       -10, 10, 10, 10, 10, 10, 10,-10,
                                       -10,  5,  0,  0,  0,  0,  5,-10,
                                       -20,-10,-10,-10,-10,-10,-10,-20};

    final private int[] rookTable = {0 ,  0,  0,  0,  0,  0,  0,  0,
                                     5 , 10, 10, 10, 10, 10, 10,  5,
                                     -5,  0,  0,  0,  0,  0,  0, -5,
                                     -5,  0,  0,  0,  0,  0,  0, -5,
                                     -5,  0,  0,  0,  0,  0,  0, -5,
                                     -5,  0,  0,  0,  0,  0,  0, -5,
                                     -5,  0,  0,  0,  0,  0,  0, -5,
                                     0 ,  0,  0,  5,  5,  0,  0,  0};

    final private int[] queenTable = {-20,-10,-10, -5, -5,-10,-10,-20,
                                      -10,  0,  0,  0,  0,  0,  0,-10,
                                      -10,  0,  5,  5,  5,  5,  0,-10,
                                      -5 ,  0,  5,  5,  5,  5,  0, -5,
                                      0  ,  0,  5,  5,  5,  5,  0, -5,
                                      -10,  5,  5,  5,  5,  5,  0,-10,
                                      -10,  0,  5,  0,  0,  0,  0,-10,
                                      -20,-10,-10, -5, -5,-10,-10,-20};

    final private int[] kingTable = {-30,-40,-40,-50,-50,-40,-40,-30,
                                     -30,-40,-40,-50,-50,-40,-40,-30,
                                     -30,-40,-40,-50,-50,-40,-40,-30,
                                     -30,-40,-40,-50,-50,-40,-40,-30,
                                     -20,-30,-30,-40,-40,-30,-30,-20,
                                     -10,-20,-20,-20,-20,-20,-20,-10,
                                     20 , 20,  0,  0,  0,  0, 20, 20,
                                     20 , 30, 10,  0,  0, 10, 30, 20};

    @Override
    public double h(ChessGame game)
    {
        List<ChessPiece> activePieces = game.getActivePieces();

        double value = 0.0;

        for (int i = 0; i < activePieces.size(); i++) {
            ChessPiece piece = activePieces.get(i);
            Team team = piece.getTeam();
            double multiplier = team.getDirectionalValue();
            value += (multiplier * getPieceSquareValue(piece));
        }

        value = normalize(value);

        return value;
    }

    private int getPieceSquareValue(ChessPiece piece)
    {
        int location = piece.getIntLocation();
        Team team = piece.getTeam();

        if (team == Team.GREEN)
            location = 63 - location;

        if (piece instanceof Pawn)
            return pawnTable[location];

        if (piece instanceof Knight)
            return knightTable[location];

        if (piece instanceof Bishop)
            return bishopTable[location];

        if (piece instanceof Rook)
            return rookTable[location];

        if (piece instanceof Queen)
            return queenTable[location];

        if (piece instanceof King)
            return kingTable[location];

        return 0;
    }

    private double normalize(double value)
    {
        return value / 1000.0;
    }
}
