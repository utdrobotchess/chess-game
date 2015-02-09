/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import java.util.Collections;
import java.util.ArrayList;

public class Rook extends ChessPiece
{
    public Rook(Square location)
    {
        super(location);
    }
    
    public Rook(Square location, int id)
    {
        super(location, id);
    }

    public Rook(Square location, int id, Team team, boolean active)
    {
        super(location, id, team, active);
    }

    protected ChessPiece copyPiece()
    {
        ChessPiece copiedPiece = new Rook(getLocation(), getID(), getTeam(), isActive());
        return copiedPiece;
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        for (int i = 0; i < 7; i += 2)
            addMovesInDirection(moveList, i, 8);

        Collections.sort(moveList);
        
        return moveList;
    }
}
