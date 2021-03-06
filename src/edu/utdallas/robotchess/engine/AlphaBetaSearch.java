package edu.utdallas.robotchess.engine;

import java.util.*;

import edu.utdallas.robotchess.game.*;
import edu.utdallas.robotchess.engine.heuristics.*;

public class AlphaBetaSearch
{
    final int MAX_DEPTH = 3;
    int currentDepth;

    public Move search(ChessGame game)
    {
        currentDepth = 0;

        Move action = null;
        Team activeTeam = game.getActiveTeam();
        double actionValue = (activeTeam == Team.GREEN) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        ArrayList<Move> possibleMoves = generateMoves(game);

        for (int i = 0; i < possibleMoves.size(); i++) {
            Move move = possibleMoves.get(i);
            ChessGame resultingGame = getResult(game, move);
            double value = (activeTeam == Team.GREEN) ?
                minValue(resultingGame,
                         Double.NEGATIVE_INFINITY,
                         Double.POSITIVE_INFINITY) :
                maxValue(resultingGame,
                         Double.NEGATIVE_INFINITY,
                         Double.POSITIVE_INFINITY);

            if (activeTeam == Team.GREEN && value > actionValue ||
                activeTeam == Team.ORANGE && value < actionValue) {
                actionValue = value;
                action = move;
            }
        }

        ChessGame resultingGame = getResult(game, action);

        return action;
    }

    protected double maxValue(ChessGame game, double alpha, double beta)
    {
        currentDepth++;

        if (cutoffTest(game)) {
            currentDepth--;
            return (new EvaluationFunction()).f(game);
        }

        double maxValue = Double.NEGATIVE_INFINITY;
        ArrayList<Move> possibleMoves = generateMoves(game);

        for (int i = 0; i < possibleMoves.size(); i++) {
            Move move = possibleMoves.get(i);
            ChessGame resultingGame = getResult(game, move);
            maxValue = Math.max(maxValue, minValue(resultingGame, alpha, beta));

            if (maxValue >= beta) {
                currentDepth--;
                return maxValue;
            }

            alpha = Math.max(alpha, maxValue);
        }

        currentDepth--;

        return maxValue;
    }

    protected double minValue(ChessGame game, double alpha, double beta)
    {
        currentDepth++;

        if (cutoffTest(game)) {
            currentDepth--;
            return (new EvaluationFunction()).f(game);
        }

        double minValue = Double.POSITIVE_INFINITY;
        ArrayList<Move> possibleMoves = generateMoves(game);

        for (int i = 0; i < possibleMoves.size(); i++) {
            Move move = possibleMoves.get(i);
            ChessGame resultingGame = getResult(game, move);
            minValue = Math.min(minValue, maxValue(resultingGame, alpha, beta));

            if (minValue <= alpha) {
                currentDepth--;
                return minValue;
            }

            beta = Math.min(beta, minValue);
        }

        currentDepth--;

        return minValue;
    }

    protected boolean cutoffTest(ChessGame game)
    {
        if (currentDepth >= MAX_DEPTH || game.isInCheckmate())
            return true;

        return false;
    }

    protected ArrayList<Move> generateMoves(ChessGame game)
    {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<ChessPiece> activePieces = game.getActivePieces();

        for (int i = 0; i < activePieces.size(); i++) {
            ChessPiece piece = activePieces.get(i);
            if (piece.getTeam() == game.getActiveTeam()) {
                ArrayList<Square> moveLocations = game.generateMoveLocations(piece);
                int pieceID = piece.getID();
                int origin = piece.getIntLocation();

                for (int j = 0; j < moveLocations.size(); j++) {
                    int destination = moveLocations.get(j).toInt();
                    moves.add(new Move(pieceID, origin, destination));
                }
            }
        }

        return moves;
    }

    protected ChessGame getResult(ChessGame game, Move move)
    {
        return game.copyGameAndMovePiece(move.pieceID, move.destination);
    }
}
