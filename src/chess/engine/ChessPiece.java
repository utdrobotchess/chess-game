package chess.engine;

import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public abstract class ChessPiece implements Comparable<ChessPiece>  {
    final static protected String prefix = "C:\\Users\\Alexandre\\Documents\\NetBeansProjects\\Chess\\src\\chess\\engine\\";
    private static final int TOTAL_SQUARES = 64;  // For assigning team
    private Square itsLocation;
    private Team itsTeam;
    protected String ID;
    private int itsID;
    private int itsNumberOfPriorMoves = 0;
    static private int totalTurnNumber = 0;
    private int itsRecentTurnNumber = 0;
    private int initialNumericalLocation;
    private ArrayList<Square> itsPossibleMoveLocations;

    ImageIcon image ;
    public ChessPiece() {}

	/*
	 * Explores a Squares in a particular direction up to a particular depth. Searches
	 * until it reaches the edge of the board, an occupied square, or the specified depth.
	 */
    protected void addPossibleMoveLocationsInDirection(ArrayList<Square> possibleMoveLocations,
                                                       int direction, int depth) {
        ChessPiece occupant;
        // Its square ID
        Square testSquare = itsLocation;
        
        for (int i = 0; i < depth; i++) {
            // Get its direction
            testSquare = testSquare.getNeighborInDirection(direction);
            //System.out.println(testSquare.getNeighborInDirection(direction));
            if (testSquare instanceof PerimeterSquare) {
                break; //search has reached the edge of the board
            }
            // Test if the piece on the square friendly or not
            if (testSquare.isOccupied()) {
                occupant = testSquare.getOccupant();
                if (occupant.getTeam() == itsTeam) {
                    /* cannot move through square occupied by teammate -- end search */
                    break;
                } 
               
                else {
                    /* can capture opponent -- add location, end search */
                    possibleMoveLocations.add(testSquare);
                    break;
                }
            } else {
                /* unoccupied interior square */
                possibleMoveLocations.add(testSquare);
            }
        }
       // logger.log(Level.FINE, "Possible move locations in direction {0} " +
          //         "added to {1}", new Object[] {direction, this});
    }

    public int getNumericalLocation() {
        return itsLocation.getNumericalLocation();
    }

    protected Square getLocation() {
        return itsLocation;
    }
    protected void setInitialNumericalLocation(){
        initialNumericalLocation = itsLocation.getNumericalLocation();
    }

    protected int getInitialNumericalLocation(){
        return initialNumericalLocation;
    }
    protected int getID() {
        return itsID;
    }

    public ArrayList<Square> getPossibleMoveLocations() {
        return itsPossibleMoveLocations;
    }

    protected abstract ArrayList<Square> generatePossibleMoveLocations();

    public Team getTeam() {
        return itsTeam;
    }
    public void setTeam(Team color){
        itsTeam = color;
    }

    protected boolean hasNotMoved() {
        return itsNumberOfPriorMoves == 0;
    }
    
    protected void incrementNumberOfPriorMoves(){
        itsRecentTurnNumber = ++totalTurnNumber;
        itsNumberOfPriorMoves++;
        //logger.log(Level.FINE, "Number of prior moves for {0} incremented to {1}",
               //    new Object[] {this, itsNumberOfPriorMoves});
    }
    protected int getCurrentNumber(){
        return itsRecentTurnNumber;
    }
    
    protected int getNumberOfTurn(){
        return totalTurnNumber;
    }
    public void setLocation(Square newLocation) {
    	if (itsLocation != null) {
            /* if it already occupied a square, reset the occupancy of that old square */
            itsLocation.setOccupant(NullChessPiece.spawnAt(itsLocation));
        }

	itsLocation = newLocation;
	itsLocation.setOccupant(this);

      //  logger.log(Level.FINE, "Location of {0} set to {1}",
               //    new Object[] {this, getNumericalLocation()});
    }

    protected void setPossibleMoveLocations(ArrayList<Square> possibleMoveLocations) {
        itsPossibleMoveLocations = possibleMoveLocations;

       // logger.log(Level.FINE, "Possible move locations for {0} updated", this);
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

       /* logger.log(Level.FINE, "ID of {0} set to {1}",
                   new Object[] {this, itsID});*/
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
    protected void setImage(ImageIcon image){
        this.image = image;
    }
    public ImageIcon getImage(){
        return image;
    }
    public String setName(String ID){
        return this.ID = ID;
    }
}
