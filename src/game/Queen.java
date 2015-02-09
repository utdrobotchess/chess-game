/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import java.util.ArrayList;
import java.util.Collections;

public class Queen extends ChessPiece
{
    public Queen(Square location)
    {
        super(location);
    }
    
    public Queen(Square location, int id)
    {
        super(location, id); 
    }

    public Queen(Square location, int id, Team team, boolean active)
    {
        super(location, id, team, active);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPiece = new Queen(getLocation(), getID(), getTeam(), isActive());
        return copiedPiece;
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        for (int i = 0; i < 8; i++)
            addMovesInDirection(moveList, i, 8);

        Collections.sort(moveList);

        return moveList;
    }
}
