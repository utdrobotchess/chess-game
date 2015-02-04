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
        setLocation(location);
        setPossibleMoves(new ArrayList<Square>());
        setTeamFromInitialLocation(location);
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
