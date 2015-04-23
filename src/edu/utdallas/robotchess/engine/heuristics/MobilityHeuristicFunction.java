package edu.utdallas.robotchess.engine.heuristics;

import java.util.*;

import edu.utdallas.robotchess.game.*;

/*
 * Basic mobility is simply the number of moves available to the player at a given time.
 *
 * There are several possibilities suggested for improving on this:
 *  - Bishop/Knight mobility may be prioritized over Rooks (on opening especially)
 *  - forward mobility prioritized over backward mobility
 *  - for rooks, vertical mobility prioritized over horizontal mobility
 *  - though not a legal move, it is useful to count moves to the square
 *    of a friendly piece (i.e. for protection)
 */
public class MobilityHeuristicFunction implements HeuristicFunction
{
    @Override
    public double h(ChessGame game)
    {
        List<ChessPiece> activePieces = game.getActivePieces();

        double mobilityValue = 0.0;
        Team activeTeam = game.getActiveTeam();

        for (int i = 0; i < activePieces.size(); i++) {
            ChessPiece piece = activePieces.get(i);

            if (piece.getTeam() == activeTeam) {
                ArrayList<Square> moveLocations = game.generateMoveLocations(piece);
                mobilityValue += moveLocations.size();
            }
        }

        mobilityValue *= activeTeam.getDirectionalValue();

        return normalize(mobilityValue);
    }

    private double normalize(double value)
    {
        return value / 100.0;
    }
}
