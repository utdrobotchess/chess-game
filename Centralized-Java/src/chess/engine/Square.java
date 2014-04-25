package chess.engine;

/**
 *
 * @author Ryan J. Marcotte
 */
public abstract class Square {
    private int itsLocation;
    private Square[] itsNeighbors = new Square[8];
    
    protected void assignNeighborInDirection(Square newNeighbor, int direction) {
        itsNeighbors[direction] = newNeighbor;
    }
    
    protected int getLocation() {
        return itsLocation;
    }
    
    protected Square getNeighborInDirection(int direction) {
        return itsNeighbors[direction];
    }
    
    protected void setLocation(int location) {
        itsLocation = location;
    }
}
