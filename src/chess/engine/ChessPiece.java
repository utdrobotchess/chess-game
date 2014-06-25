/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

public abstract class ChessPiece implements Comparable<ChessPiece> {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private static final int TOTAL_SQUARES = 64;
    private Square itsLocation;
    private Team itsTeam;
    private int itsID;
    private int itsNumberOfPriorMoves = 0;
    private ArrayList<Square> itsPossibleMoveLocations;

    public ChessPiece() {}

	/*
	 * Explores a Squares in a particular direction up to a particular depth. Searches
	 * until it reaches the edge of the board, an occupied square, or the specified depth.
	 */
    protected void addPossibleMoveLocationsInDirection(ArrayList<Square> possibleMoveLocations,
                                                       int direction, int depth) {
        Square testSquare = itsLocation;

        for (int i = 0; i < depth; i++) {
            testSquare = testSquare.getNeighborInDirection(direction);

            if (testSquare instanceof PerimeterSquare) {
                break; //search has reached the edge of the board
            }

            if (testSquare.isOccupied()) {
                if (testSquare.getOccupyingTeam() == itsTeam) {
                    break; //cannot move through square occupied by teammate -- end search
                } else {
                    possibleMoveLocations.add(testSquare);
                    break; //can capture opponent -- add location, end search
                }
            } else {
                possibleMoveLocations.add(testSquare); //unoccupied interior square
            }
        }

        logger.log(Level.FINE, "Possible move locations in direction {0} " +
                   "added to {1}", new Object[] {direction, this});
    }

    protected int getNumericalLocation() {
        return itsLocation.getNumericalLocation();
    }

    protected Square getLocation() {
        return itsLocation;
    }

    protected int getID() {
        return itsID;
    }

    protected ArrayList<Square> getPossibleMoveLocations() {
        return itsPossibleMoveLocations;
    }

    protected abstract ArrayList<Square> generatePossibleMoveLocations();

    protected Team getTeam() {
        return itsTeam;
    }

    protected boolean hasNotMoved() {
        return itsNumberOfPriorMoves == 0;
    }

	protected void incrementNumberOfPriorMoves() {
		itsNumberOfPriorMoves++;

        logger.log(Level.FINE, "Number of prior moves for {0} incremented to {1}",
                   new Object[] {this, itsNumberOfPriorMoves});
	}

    protected void setLocation(Square newLocation) {
    	if (itsLocation != null) {
			/* if it already occupied a square, reset the occupancy of that old square */
			itsLocation.setOccupancy(false);
			itsLocation.setOccupyingTeam(Team.NEUTRAL);
		}

		itsLocation = newLocation;
		itsLocation.setOccupancy(true);
        itsLocation.setOccupyingTeam(itsTeam);

        logger.log(Level.FINE, "Location of {0} set to {1}",
                   new Object[] {this, getNumericalLocation()});
    }

    protected void setPossibleMoveLocations(ArrayList<Square> possibleMoveLocations) {
        itsPossibleMoveLocations = possibleMoveLocations;

        logger.log(Level.FINE, "Possible move locations for {0} updated", this);
    }

    protected void setTeamFromInitialLocation(Square initialLocation) {
		/* if initial location is in top half of board (0-31), team is green */
		if (initialLocation.getNumericalLocation() < (TOTAL_SQUARES / 2)) {
            itsTeam = Team.GREEN;
        } else {
            /* otherwise, team is orange */
            itsTeam = Team.ORANGE;
        }
    }

    protected void setID(int newID) {
        itsID = newID;

        logger.log(Level.FINE, "ID of {0} set to {1}",
                   new Object[] {this, itsID});
    }

    @Override
    public int compareTo(ChessPiece secondPiece) {
        if (getNumericalLocation() < secondPiece.getNumericalLocation()) {
            return -1;
        } else if (getNumericalLocation() == secondPiece.getNumericalLocation()) {
            return 0;
        } else {
            return 1;
        }
    }
}
