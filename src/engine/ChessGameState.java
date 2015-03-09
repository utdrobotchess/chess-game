/**
 *
 * @author Ryan J. Marcotte
 */

package engine;

import game.ChessGame;
import game.ChessPiece;
import game.Team;

import java.util.ArrayList;

public class ChessGameState implements State
{
    ChessGame game;

    public ChessGameState(ChessGame game)
    {
        this.game = game;
    }
    
    public ArrayList<ChessPiece> getActivePieces()
    {
        ArrayList<ChessPiece> activePieces = new ArrayList<>();
        ChessPiece[] allPieces = game.getAllPieces();

        for (int i = 0; i < allPieces.length; i++)
            if (allPieces[i].isActive())
                activePieces.add(allPieces[i]); 
        
        return activePieces;
    }
    
    public Team getActiveTeam()
    {
        return game.getActiveTeam();
    }
}
