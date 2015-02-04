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
        setLocation(location);
        setPossibleMoves(new ArrayList<Square>());
        setTeamFromInitialLocation(location);
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
