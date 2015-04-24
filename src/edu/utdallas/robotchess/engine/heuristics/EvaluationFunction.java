package edu.utdallas.robotchess.engine.heuristics;

import edu.utdallas.robotchess.game.*;

public class EvaluationFunction
{
    public double f(ChessGame game)
    {
        MaterialHeuristicFunction material = new MaterialHeuristicFunction();
        double materialValue = material.h(game);

        MobilityHeuristicFunction mobility = new MobilityHeuristicFunction();
        double mobilityValue = mobility.h(game);

        PieceSquaresHeuristicFunction pieceSquares = new PieceSquaresHeuristicFunction();
        double pieceSquaresValue = pieceSquares.h(game);

        return 1.0 * materialValue + 1.0 * mobilityValue + 1.0 * pieceSquaresValue;
    }
}
