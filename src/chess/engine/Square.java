package chess.engine;

/**
 * Encompasses three types of squares: interior, perimeter, and null. Used as the 
 * basis for all location-based logic in the chess engine.
 * @author Ryan J. Marcotte
 */
public abstract class Square implements Comparable<Square> {
    private int itsLocation;
    private boolean itsOccupancy;
    private Team itsOccupyingTeam;
    
    /* Directional neighbors defined as follows:
    *  7   0   1
    *  6  ***  2
    *  5   4   3
    */
    private final Square[] itsNeighbors = new Square[8];
    
    protected void assignNeighborInDirection(Square newNeighbor, int direction) {
        itsNeighbors[direction] = newNeighbor;
    }
    
    @Override
    public int compareTo(Square secondSquare) {
        if(itsLocation < secondSquare.getLocation())
            return -1;
        else if(itsLocation == secondSquare.getLocation())
            return 0;
        else
            return 1;
    }

    protected int getLocation() {
        return itsLocation;
    }
    
    protected Square getNeighborInDirection(int direction) {
        return itsNeighbors[direction];
    }
    
    protected boolean isOccupied() {
        return itsOccupancy;
    }
    
    protected Team getOccupyingTeam() {
    	return itsOccupyingTeam;
    }

    protected void setLocation(int location) {
        itsLocation = location;
    }
    
    protected void setOccupancy(boolean occupancy) {
        itsOccupancy = occupancy;
    }

    protected void setOccupyingTeam(Team occupyingTeam) {
	itsOccupyingTeam = occupyingTeam;
    }
}
