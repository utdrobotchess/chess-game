package chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author Alexandre
 */
public class Pawn extends ChessPiece{
    private static final String white = prefix + "WPawn.png";
    private static final String black = prefix + "BPawn.png";
    private static ImageIcon WPawnPic = new ImageIcon(white);
    private static ImageIcon BPawnPic = new ImageIcon(black);
    private ArrayList<Square> possibleMoveLocations = new ArrayList<>();
    
    // Create a new pawn, assigning it to a team and set its initial location
    protected static Pawn spawnAt(Square location) {
       // System.out.println(location);
        Pawn p = new Pawn();
        p.setTeamFromInitialLocation(location);
        p.setLocation(location);
        p.setInitialNumericalLocation();
        assignStringName(p);
        return p;
    }
    
    
    @Override
    protected ArrayList<Square> generatePossibleMoveLocations() {
        possibleMoveLocations.clear();
        addPossibleForwardMoves();
        addPossibleCapturingMoves();

        Collections.sort(possibleMoveLocations);

        return possibleMoveLocations;
    }

    private void addPossibleForwardMoves() {
        int possibleNumberOfForwardSquares = 1;
        if(hasNotMoved())
            possibleNumberOfForwardSquares = 2;
        addPossibleMoveLocationsInDirection(possibleMoveLocations,
                                            determineForwardDirectionFromTeam(),
                                            possibleNumberOfForwardSquares);
                
        Square testSquare= this.getLocation();
        ChessPiece occupant = testSquare.getOccupant();
        int front = determineForwardDirectionFromTeam();
        // Get the pawn's frontal neighbor
        testSquare = testSquare.getNeighborInDirection(front); 
        
        // Check if the neighbor has a piece
        if (testSquare.isOccupied()) {
            // Get the opponent's piece;
            ChessPiece occupant2 = testSquare.getOccupant();
            // Check if the neighbor is on the other team
            if (occupant.getTeam() != occupant2.getTeam()) {
                // If so, remove its location from the possible move location list
                possibleMoveLocations.remove(testSquare);
            }
        }
        
        // Not allowed to capture pieces in front
        Square testSquare2 = this.getLocation();
        occupant = testSquare2.getOccupant();
        testSquare2 = testSquare2.getNeighborInDirection(front).getNeighborInDirection(front); 
        // Check if the neighbor has a piece
        if (testSquare2.isOccupied() && occupant.hasNotMoved()) {
            // Get the opponent's piece;
            ChessPiece occupant2 = testSquare2.getOccupant();
            // Check if the neighbor is on the other team
            if (occupant.getTeam() != occupant2.getTeam()) {
                // If so, remove its location from the possible move location list
                possibleMoveLocations.remove(testSquare2);
            }
        }
        
        //impasse
        Square testSquare3 = getLocation();
        occupant = testSquare3.getOccupant();
        
        int forwardDirection = determineForwardDirectionFromTeam();
        Square leftSide;
        Square rightSide;
        if(occupant.getTeam() == Team.GREEN){
            leftSide = testSquare3.getNeighborInDirection((forwardDirection + 1));
            rightSide = testSquare3.getNeighborInDirection((forwardDirection + 7) % 8);
        }
        else{
            leftSide = testSquare3.getNeighborInDirection((forwardDirection + 7) % 8);
            rightSide = testSquare3.getNeighborInDirection((forwardDirection + 1));
        }
                    
        if(testSquare3.getNeighborInDirection(6).isOccupied()){
            ChessPiece occupant2 = testSquare3.getNeighborInDirection(6).getOccupant();
            
            //Condition: the capturing pawn and the targeting pawn have to be on different team.
            //         : the targeted pawn made a double-step
            //         : the capturing pawn must captures the targeted pawn immediately
            if(occupant.getTeam() != occupant2.getTeam()
                    && occupant2.getCurrentNumber() - occupant2.getNumberOfTurn() == 0
                    && Math.abs(occupant2.getNumericalLocation() - occupant2.getInitialNumericalLocation()) == 16){
                possibleMoveLocations.add(leftSide);
            }
        }
        
        if(testSquare3.getNeighborInDirection(2).isOccupied()){
            ChessPiece occupant2 = testSquare3.getNeighborInDirection(2).getOccupant();
            if(occupant.getTeam() != occupant2.getTeam() 
                    && occupant2.getCurrentNumber() - occupant2.getNumberOfTurn() == 0
                    && Math.abs(occupant2.getNumericalLocation() - occupant2.getInitialNumericalLocation()) == 16){
                possibleMoveLocations.add(rightSide);
            }
        }
    }

    private void addPossibleCapturingMoves() {
        Square testSquare;
        ChessPiece occupant;
        Square currentLocation = getLocation();
        int forwardDirection = determineForwardDirectionFromTeam();

        Square[] possibleCaptureSquares = {currentLocation.getNeighborInDirection(forwardDirection + 1),
                                           currentLocation.getNeighborInDirection((forwardDirection + 7) % 8)};

        for (int i = 0; i < possibleCaptureSquares.length; i++) {
            testSquare = possibleCaptureSquares[i];
            if (testSquare.isOccupied()) {
                occupant = testSquare.getOccupant();

                if (occupant.getTeam() != getTeam()) {
                    possibleMoveLocations.add(testSquare);
                }
            }
        }
    }

    private int determineForwardDirectionFromTeam() {
        if (getTeam() == Team.GREEN) {
            return 4;
        } else {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return ID;
    }
    public static void assignStringName(Pawn p){
        if(p.getTeam() == Team.ORANGE){
            p.setImage(WPawnPic);
            p.setName("P");
        }
        else{
            p.setImage(BPawnPic);
            p.setName("p");
        }
    }
}
