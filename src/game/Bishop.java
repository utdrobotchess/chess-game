/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import java.util.Collections;
import java.util.ArrayList;

public class Bishop extends ChessPiece
{
    public Bishop(Square location)
    {
        super(location);
    }
    
    public Bishop(Square location, int id)
    {
        super(location, id);
    }

    public Bishop(Square location, int id, Team team, boolean active)
    {
        super(location, id, team, active);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPiece = new Bishop(getLocation(), getID(), getTeam(), isActive());
        return copiedPiece;
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        for (int i = 1; i < 8; i += 2)
            addMovesInDirection(moveList, i, 8);

        Collections.sort(moveList);

        return moveList;
    }
}
