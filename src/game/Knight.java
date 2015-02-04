/**
 *
 * @author Ryan J. Marcotte
 */

package game;

import java.util.ArrayList;
import java.util.Collections;

public class Knight extends ChessPiece
{
    public Knight(Square location)
    {
        setLocation(location);
        setPossibleMoves(new ArrayList<Square>());
        setTeamFromInitialLocation(location);
    }

    protected ArrayList<Square> generateMoveLocations()
    {
        ArrayList<Square> moveList = new ArrayList<>();
        Square currentLocation = getLocation();

        for (int i = 0; i < 8; i += 2) {
            Square neighbor = currentLocation.getNeighbor(i);

            if (neighbor == null)
                continue;
            
            Square twoAwayNeighbor = neighbor.getNeighbor(i);

            if (twoAwayNeighbor == null)
                continue;
            
            Square potentialMove;

            for (int j = 2; j < 7; j += 4) {
                potentialMove = twoAwayNeighbor.getNeighbor((i + j) % 8);

                if (potentialMove == null)
                    continue;

                if (potentialMove.isOccupied()) {
                    ChessPiece occupant = potentialMove.getOccupant();
                    Team theirTeam = occupant.getTeam();
                    Team ourTeam = getTeam();

                    if (theirTeam == ourTeam)
                        continue;
                }

                moveList.add(potentialMove);
            }
        }

        Collections.sort(moveList);
        
        return moveList;
    }
}
