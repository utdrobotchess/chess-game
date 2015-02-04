/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import java.util.ArrayList;
import java.util.Collections;

public class King extends ChessPiece
{
    public King(Square location)
    {
        setLocation(location);
        setPossibleMoves(new ArrayList<Square>());
        setTeamFromInitialLocation(location);
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();

        for (int i = 0; i < 8; i++)
            addMovesInDirection(moveList, i, 1);

        Collections.sort(moveList);

        return moveList;
    }
}
