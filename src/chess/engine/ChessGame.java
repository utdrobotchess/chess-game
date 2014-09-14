package chess.engine;

import java.util.ArrayList;

/**
 *
 * @author Alexandre
 */
public class ChessGame {
    private ArrayList<ChessPiece> itsChessPieces = new ArrayList<>();
    private ArrayList<ChessPiece> whitePieces = new ArrayList<>();
    private ArrayList<ChessPiece> blackPieces = new ArrayList<>();
    private String[][] strList = new String[8][8];
    private boolean hasCastled;
    private int pawnLocation;
    private boolean castEnpassant;
    private int bothSideCastled = 0;
    private int rookLocation;
    private int castlingLocation;
    private ChessBoard itsBoard;
    private boolean capturePiece = false;
    private GameState itsState;
    private ChessPiece CapturedPiece;
    private Square square;
    public static ChessGame setupGame() {
        ChessGame game = new ChessGame();
        // Begin building a chess game
        System.out.println("Setting up the game");
        ChessGameBuilder.build(game);
        return game;
    }
    protected void addBuiltChessBoard(ChessBoard builtChessBoard) {
        itsBoard = builtChessBoard;
        //logger.log(Level.FINE, "Chess board added to chess game");
    }
    protected void addChessPiece(ChessPiece piece) {
        
        if(piece.getTeam() == Team.ORANGE){
            whitePieces.add(piece);
        }
        else
            blackPieces.add(piece);
        itsChessPieces.add(piece);
        //logger.log(Level.FINE, "{0} added to chess game", piece);
    }
    public void removeChessPiece(ChessPiece piece){
        if(piece.getTeam() == Team.ORANGE){
            whitePieces.remove(piece);
        }
        else{
            blackPieces.remove(piece);
        }
        itsChessPieces.remove(piece);
    }
    public ChessBoard getBoard() {
        return itsBoard;
    }
   
    public GameState getState() {
        return itsState;
    }

    protected void setState(GameState state) {
        itsState = state;
    }
    public GameState selectPieceAtLocation(int selectionLocation){
         // Retrieve the selected square
        Square selectionSquare = itsBoard.getSquareAt(selectionLocation);
        //  Get the selected chess piece on the selected square
        ChessPiece occupant = selectionSquare.getOccupant();
        
        itsState.clearEnPassantPairs();
        itsState.clearMovePairs();
        // Update
        updatePossibleMoveLocations();
        // Set what square the selected piece is at
        itsState.setSelectedPieceIndex(selectionLocation);
        
        // Retrieve all possiblem moves
        ArrayList<Square> possibleMoveLocations = occupant.getPossibleMoveLocations();
        // Save the list of possible moves for the selected piece.
        itsState.setPossibleMoveIndexes(possibleMoveLocations);
        // print all possible moves from all pieces
       // printEntireMove();
        return itsState;
    }
    
   /* protected void convertFromStringArray(String[][] strArray){
        for(int i = 0; i < strArray.length ;i++){
            for(int j = 0; j < strArray.length; j++){
                strArray[i][j]
            }
        }
    }*/
    public String[][]  convertToStringArray(){
        for(int i = 0; i < itsChessPieces.size(); i++){
            int sqLocation = itsChessPieces.get(i).getNumericalLocation();
                strList[(int)Math.floor(sqLocation/8)][sqLocation % 8] = itsChessPieces.get(i).toString();
        }
        return strList;
    }
    
    // This function simulates that (while a king is in check) if a team piece is moved,
    // will the king still be in check.
    private boolean isKingInStillCheck(int origin, int destination){
        
        int savedOrigin = origin;
        int savedDest = destination;
        ChessPiece piece = itsBoard.getSquareAt(origin).getOccupant();
        
        // Return false if the king tries to castle since the king is already in
        // check

        if(piece instanceof King && isCastlingInvoked(destination)){
            itsState.setSelectedPieceIndex(-1);
            itsState.clearPossibleMoveIndexes();
            return false;
        }
        
        
        else if(castEnpassant){
            castEnpasssant(destination);
            capturePieceLocatedNumericallyAt(pawnLocation);
            movePiece(itsState.getEnPassantPairs().get(0), itsState.getEnPassantPairs().get(1));
            castEnpassant = false;
        }
        
        ArrayList<Integer> movePairs = addMovePairs(destination);
        itsState.setMovePairs(movePairs);
        
        // An opponent piece is captured
        if(movePairs.get(1) == - 1){
            capturePieceLocatedNumericallyAt(destination);
        }

        // Process the moves
        for (int i = 0; i < movePairs.size(); i += 2) {
            movePiece(movePairs.get(i), movePairs.get(i+1)); 
        }
       
        // Update after these moves
        updatePossibleMoveLocations();
        
        // Check if the king is not in check after the move
        boolean isValid = !testForBeingChecked();
        
        // Undo the move each time since the move is only a test
        undoMove(savedDest, savedOrigin);
        return isValid;
    }
    
    // Before processing the move, make sure the king is not in check after
    // the move is made. Otherwise, undo the move.
    public boolean isKingInCheck(int origin, int destination){
        
        // Get the selected piece
        ChessPiece piece = itsBoard.getSquareAt(origin).getOccupant();
        
        // Determine if the king will castle
        if(piece instanceof King && isCastlingInvoked(destination)){
            
            // If castling succeeds
            if(checkForCastle(origin, destination)){
                setCastled(true);
                movePiece(rookLocation, castlingLocation);
            }
            
            // Move the king back to its original square location
            else{
                movePiece(origin, destination); 
                itsState.setSelectedPieceIndex(-1);
                itsState.clearPossibleMoveIndexes();
                return false;
            }
        }

        if(isEnpassantInvoked(piece, destination)){
            castEnpasssant(destination);
            capturePieceLocatedNumericallyAt(pawnLocation);
            movePiece(itsState.getEnPassantPairs().get(0),itsState.getEnPassantPairs().get(1));
        }
        
        ArrayList<Integer>movePairs = addMovePairs(destination);
        itsState.setMovePairs(movePairs);

        // An opponent piece is captured
        if(movePairs.get(1) == - 1){
            capturePieceLocatedNumericallyAt(destination);
        }

        // Process the moves
        for (int i = 0; i < movePairs.size(); i += 2) {
            movePiece(movePairs.get(i), movePairs.get(i+1)); 
        }
        
        // Update after these moves
        updatePossibleMoveLocations();
        
        // A valid move has been executed.
        if(!testForBeingChecked()){
            if(CapturedPiece != null){
                // Remove a captured piece from the chess piece list
                removeChessPiece(CapturedPiece);
            }
            // Pawn promotion
            if(isPawnBeingPromoted(destination)){
                itsState.setPawnPromotion(true);
            }          
            return true;
        }
        else
            return false;
    }
    
    // Update after a move is made
    public void updateStatus(int dest){
        itsState.setSelectedPieceIndex(-1);
        itsState.clearPossibleMoveIndexes();  
        itsBoard.getSquareAt(dest).getOccupant().incrementNumberOfPriorMoves();
        castEnpassant = false;
        updatePossibleMoveLocations();
        testForCheck();
        testForStalemate();
        if(itsState.isCheck()){
            testForCheckMate();
        }
      //  convertToStringArray();
        itsState.toggleActiveTeam();
    }
    
    // Save the captured piece if the undo function is invoked.
    private void capturePieceLocatedNumericallyAt(int destination){
        setCapture(true);
        // Save the piece and its location
        CapturedPiece = itsBoard.getSquareAt(destination).getOccupant();
        square = CapturedPiece.getLocation();
    }
    
    private ArrayList<Integer> addMovePairs(int destination) {
        ArrayList<Integer> movePairs = new ArrayList<>();
        Square destinationSquare = itsBoard.getSquareAt(destination);
        // A piece is captured
        if (destinationSquare.isOccupied()) {
            // From here
            movePairs.add(destination);
            
            // To there
            movePairs.add(-1);
        }

        // Move the selected piece from here...
        movePairs.add(itsState.getSelectedPieceIndex());
        
        // To there
        movePairs.add(destination);

        return movePairs;
    }
    
    // Execute the move
    private void movePiece(int origin, int destination) {
        final int FIRST_PERIM_INDEX = 64;
        final int FINAL_PERIM_INDEX = 100;

        // original square
        Square originSquare = itsBoard.getSquareAt(origin);
        
        Square destinationSquare = NullSquare.generateNullSquare();
        
        // Get the piece from the original square
        ChessPiece movingPiece = originSquare.getOccupant();

        if (destination == -1) {
            
            // The squares surrounding the normal chess board
            for (int i = FIRST_PERIM_INDEX; i < FINAL_PERIM_INDEX; i++) {
                
                // Capture a piece
                if (!itsBoard.getSquareAt(i).isOccupied()) {
                    
                    destinationSquare = itsBoard.getSquareAt(i);
                    break;
                }
            }
        }
        else {
            destinationSquare = itsBoard.getSquareAt(destination);
        }

        // Move from original location to its destination
        movingPiece.setLocation(destinationSquare);
    }
    
    // Update all possible move for each pieces.
    public void updatePossibleMoveLocations() {
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);
            piece.setPossibleMoveLocations(piece.generatePossibleMoveLocations());
           // Test
          //  System.out.println(piece + " " + piece.getPossibleMoveLocations());
        }
    }

    private void testForCheck() {
        int kingLocation = getEnemyKingLocation();

        // All available pieces
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);

            // Piece's turn
            if (piece.getTeam() == itsState.getActiveTeam()) {
                // Get all possible moves 
                ArrayList<Square> moveLocations = piece.getPossibleMoveLocations();

                // Check if there is a move that attacks the enemy king
                for (int j = 0; j < moveLocations.size(); j++) {
                    if (moveLocations.get(j).getNumericalLocation() == kingLocation) {
                        itsState.setCheck(true);
                        return;
                    }
                }
            }
        }
        itsState.setCheck(false);
    }
    private boolean testForBeingChecked() {
        int kingLocation = getOurKingLocation();
        
        // All available pieces
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);

            // Get opponent's pieces
            if (piece.getTeam() != itsState.getActiveTeam() && (piece.getNumericalLocation() >= 0 &&
                    piece.getNumericalLocation() < 64)) {
                 
                // Get all possible moves 
                ArrayList<Square> moveLocations = piece.getPossibleMoveLocations();
                // Check if there is a piece attacking our king
                for (int j = 0; j < moveLocations.size(); j++) {
                    if (moveLocations.get(j).getNumericalLocation() == kingLocation) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private int getEnemyKingLocation() {
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);

            // Enemy king is found
            if (piece.getTeam() != itsState.getActiveTeam() && piece instanceof King) {
                return piece.getNumericalLocation();
            }
        }

        return -1;
    }
    
    private int getOurKingLocation() {
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);

            // Our king is found
            if (piece.getTeam() == itsState.getActiveTeam() && piece instanceof King) {
                return piece.getNumericalLocation();
            }
        }

        return -1;
    }
    public ArrayList<ChessPiece> setUpPieces() {
        return itsChessPieces;
    }

    // Assume a check is a checkmate. Find a opponent piece to stop the check
    // for a checkmate to be invalid.
    protected void testForCheckMate(){
        int kingLocation = getEnemyKingLocation();
        ArrayList<ChessPiece> teamPiece = new ArrayList<>();
        ArrayList<ChessPiece> opponentPiece = new ArrayList<>();
        ArrayList<Square> teamPieceMoveList;
        ArrayList<Square> opponentMoveList;
        itsState.setCheckmate(true);
        // List of all pieces
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);

            // Player's own pieces
            if (piece.getTeam() == itsState.getActiveTeam() && 
                    piece.getPossibleMoveLocations().contains(itsBoard.getSquareAt(kingLocation))) { 
                teamPiece.add(piece);    
            }
            
            // Enemy pieces
            else if (piece.getTeam() != itsState.getActiveTeam()){
                opponentPiece.add(piece);
  
            }
        }
        boolean preventedByAPiece = false;
        boolean shouldCheckMove = false;
        int here = -1; int there = -1;
        
        // Number of team pieces
        opponentPiece.remove(itsBoard.getSquareAt(kingLocation).getOccupant());
        
        // Break out of the loop if a checkmate fails
        outerLoop:
        for(int w = 0; w < teamPiece.size(); w++){
            // Each team piece's move list
            teamPieceMoveList = teamPiece.get(w).getPossibleMoveLocations();
            // Number of opponent members
            for(int x = 0; x < opponentPiece.size(); x++){
                // Each opponent piece's possible move list
                boolean captureAttempt = false;
                opponentMoveList = opponentPiece.get(x).getPossibleMoveLocations();
                // Find the square that is both in the opponent's and team's possible move list
                for(int y = 0; y < teamPieceMoveList.size(); y++){
                    for(int z = 0; z < opponentMoveList.size(); z++){
                        
                        // Cehck if an opponent can block the team piece from attack
                        if(teamPieceMoveList.get(y) == opponentMoveList.get(z)){
                            there = teamPieceMoveList.get(y).getNumericalLocation();
                            here = opponentPiece.get(x).getNumericalLocation();
                            shouldCheckMove = true;
                        }
                        // Check if an opponent can capture the attacking team piece
                        else if(!captureAttempt && 
                                teamPiece.get(w).getLocation() == opponentMoveList.get(z) 
                                || isEnpassantInvoked(opponentPiece.get(x), opponentMoveList.get(z).getNumericalLocation())){
                            there = teamPiece.get(w).getNumericalLocation();
                            here = opponentPiece.get(x).getNumericalLocation();
                            captureAttempt = true;
                            shouldCheckMove = true;
                        }
                        
                        // If either above is true, then check if their king is still
                        // safe 
                        if(shouldCheckMove){
                            if(canOpponentPreventBeingInCheck(here, there)){
                                preventedByAPiece = true;
                                // Test
                              //  System.out.println(opponentPiece.get(x) + " can prevent " + teamPiece.get(w) + " from checkmate at square: " + there);
                                break outerLoop;
                            }
                            captureAttempt = false;
                        }
                    }
                }
            }
        }
        
        if(preventedByAPiece){
            itsState.setCheckmate(false);
        }
        // If there is no opponent piece to protect the king, test
        // if the king can move at all while in check
        else if(isEnemyKingStuck()){
            itsState.setCheckmate(true);
        }
        else
            itsState.setCheckmate(false);
    }
    
    // Check that the king can't move anywhere for checkmate or stalemate
    protected boolean isEnemyKingStuck(){
        boolean stillStuck = true;
        ArrayList<Square> numberOfPossibleMove;
        int kingLocation = getEnemyKingLocation();
        ChessPiece theirKing = itsBoard.getSquareAt(kingLocation).getOccupant();
        
        // Get a copy of list of possible moves
        numberOfPossibleMove = theirKing.getPossibleMoveLocations();
        
        if (!theirKing.getPossibleMoveLocations().isEmpty()){
            for(int x = 0; x < numberOfPossibleMove.size(); x++){
                int kingPossibleMove = numberOfPossibleMove.get(x).getNumericalLocation();
                
                selectPieceAtLocation(kingLocation);
                itsState.toggleActiveTeam();
                
                // King can move and, therefore, not stuck
                if(isKingInStillCheck(kingLocation, kingPossibleMove)){
                    stillStuck = false;
                  //  Test
                  //  System.out.println(theirKing + " can still move to square(s) " + numberOfPossibleMove);
                    itsState.toggleActiveTeam();
                    break;
                }
                itsState.toggleActiveTeam();
            }
        }
       return stillStuck;
    }
    
    // Check if an opponent piece can stop the checkmate
    protected boolean canOpponentPreventBeingInCheck(int defender, int attacker){        
        selectPieceAtLocation(defender);
        // Check if the king is still safe or not
        itsState.toggleActiveTeam();
        boolean canPreventAttack = isKingInStillCheck(defender, attacker);
        itsState.toggleActiveTeam();
        return canPreventAttack;
    }
    
    // Undo the move execution. The player will move again
    public void undoMove(int destination, int origin){
        
        // Put a captured piece back into the game
        movePiece(destination, origin);
        if(isCaptured()){
            castEnpassant = false;
           CapturedPiece.setLocation(square);
           setCapture(false);
        }
        updatePossibleMoveLocations();
        itsState.setSelectedPieceIndex(-1); 
        itsState.clearPossibleMoveIndexes();
    }
 
    // Check if a piece is captured
    private boolean isCaptured(){
        return capturePiece;
    }
    
    // Determine if a piece is captured
    public void setCapture(boolean isCaptured){
        if(!isCaptured)
            CapturedPiece = null;
        capturePiece = isCaptured;
    }
    
    public ChessPiece getCapturedPiece(){
        return CapturedPiece;
    }
    
    // If the king is the only piece with possible moves but can't move 
    // because doing so will result in check, it is a stalemate.
    private void testForStalemate(){
        // White Team
        if(itsState.getActiveTeam() == Team.GREEN){
            for(int i = 0; i < whitePieces.size(); i++){
                // Check if any pieces beside the king can move
                if(!(whitePieces.get(i) instanceof King) &&
                        !whitePieces.get(i).getPossibleMoveLocations().isEmpty()){
                    return;
                }
            }
            if(isEnemyKingStuck() && !itsState.isCheck())
                itsState.setDraw(true);
        }
        // Black team
        else{
            for(int i = 0; i < blackPieces.size(); i++){
            // Check if any pieces beside the king can move
                if(!(blackPieces.get(i) instanceof King) &&
                        !blackPieces.get(i).getPossibleMoveLocations().isEmpty()){
                    return;
                }
            }
            if(isEnemyKingStuck() && !itsState.isCheck())
                itsState.setDraw(true);
        }
    }
    
    // King can castled if:
    // The king is not in check
    // The king has not already castled
    // The squares are not attacked
    protected boolean checkForCastle(int kingLocation, int dest){
        
        // Check if both side castled or is in check
        if(bothSideCastled == 3 || itsState.isCheck() ){
            return false;
        }
        
        ChessPiece rook;
        Square kingSquare = itsBoard.getSquareAt(kingLocation);
        Square destSquare = itsBoard.getSquareAt(dest);
        ChessPiece king = kingSquare.getOccupant();
        
        int castleDirection;
        
        //***** Determine which square invoked castling ********
        // Which side will the king castle
        int whichSide = kingSquare.compareTo(destSquare);
        
        // Left side
        if(whichSide == 1 &&(kingLocation - dest == 2)){
            castleDirection = 6;
            rook = destSquare.getNeighborInDirection(6).getNeighborInDirection(6).getOccupant();
        }
        
        // Right side
        else if(whichSide == -1 && (dest - kingLocation == 2)){
            castleDirection = 2;
            rook = destSquare.getNeighborInDirection(2).getOccupant();
        }
        else{
            return false;
        }
        
        // The square next to the king's square
        Square castlingSquare = kingSquare.getNeighborInDirection(castleDirection);
        castlingLocation = castlingSquare.getNumericalLocation();

        // ******** Check if either squares are not being attacked ********
        ArrayList<ChessPiece> opponentPieces;
        
        if(king.getTeam() == Team.ORANGE)
            opponentPieces = blackPieces;
        else
            opponentPieces = whitePieces;
        
        for (int i = 0; i < opponentPieces.size(); i++){
            ArrayList<Square> pieceMove = opponentPieces.get(i).getPossibleMoveLocations();
            for (int j = 0; j < pieceMove.size(); j++)
                if(pieceMove.get(j) == castlingSquare || pieceMove.get(j) == destSquare){
                    System.out.println("Cannot castle: square under attack");
                    return false;
                }
        }

        rookLocation = rook.getNumericalLocation();
        return true;
    }
    // ********************** Methods related to castling *********************
    protected boolean isCastlingInvoked( int dest){
        ChessPiece king = itsBoard.getSquareAt(getOurKingLocation()).getOccupant();
        if(king.hasNotMoved() && (dest == 2 || dest == 6 || dest == 58 || dest == 62))
            return true;
        else 
            return false;
    }
    public boolean hasCastled(){
        return hasCastled;
    }
    public int getRookLocation(){
        return rookLocation;
    }
    public void setCastled(boolean castled){
        bothSideCastled++;
        hasCastled = castled;
    }
    public int getCastlingLocation(){
        return castlingLocation;
    }
    
    // ********************* Methods related to Enpassant *****************
    private boolean isEnpassantInvoked(ChessPiece piece, int destination){
        if(piece instanceof Pawn){
            Square leftSide;
            Square rightSide;
            if(piece.getTeam() == Team.GREEN){
                leftSide = piece.getLocation().getNeighborInDirection(5);
                rightSide = piece.getLocation().getNeighborInDirection(3);
            }
            else{
                leftSide = piece.getLocation().getNeighborInDirection(7);
                rightSide = piece.getLocation().getNeighborInDirection(1);
            }

            if((piece.getPossibleMoveLocations().contains(leftSide) && !leftSide.isOccupied())
                    && leftSide.getNumericalLocation() == destination){
                setPawnLocation(piece.getLocation().getNeighborInDirection(6).getNumericalLocation());
                castEnpassant = true;
                return true;
            }
            else if(piece.getPossibleMoveLocations().contains(rightSide) && !rightSide.isOccupied()
                    && rightSide.getNumericalLocation() == destination){
                setPawnLocation(piece.getLocation().getNeighborInDirection(2).getNumericalLocation());
                castEnpassant = true;
                return true;
            }
            else 
                return false;
        }
        else
            return false;
    }
    // If the opponent pawn just double jumped from the previous move
    // and is now beside the capturing team piece. The team piece can cast
    // enpassant
    protected void castEnpasssant(int dest){
        ArrayList<Integer> enpassantPairs = new ArrayList<>();
        // Remove the opponent's pawn
        enpassantPairs.add(getPawnLocation());
        enpassantPairs.add(-1);

        itsState.setEnPassantPairs(enpassantPairs);
    }
    public boolean hasCastedEnpassant(){
        return castEnpassant;
    }
    protected void setPawnLocation(int location){
        pawnLocation = location;
    }
    public int getPawnLocation(){
        return pawnLocation;
    }
  
    // ************** Methods related to pawn promotion **********************
    protected boolean isPawnBeingPromoted(int destination){
        Square originSquare = itsBoard.getSquareAt(destination);
        int originSquareNumericalLocation = originSquare.getNumericalLocation();
        ChessPiece piece = originSquare.getOccupant();
        if(piece instanceof Pawn){
            if(piece.getTeam() == Team.GREEN && originSquareNumericalLocation >= 56 
                    && originSquareNumericalLocation< 64)
                return true;
            if(piece.getTeam() == Team.ORANGE && originSquareNumericalLocation >= 0 
                    && originSquareNumericalLocation < 8)
                return true;
        }
        return false;
    }
    public void updatePawnPromotion(ChessPiece pawn, int dest){
        removeChessPiece(pawn);
        addChessPiece(itsBoard.getSquareAt(dest).getOccupant());
        itsState.setPawnPromotion(false);
        updatePossibleMoveLocations();
    }
    
    public void printEntireMove(){
        String list = "";
        for(int i = 0; i < itsChessPieces.size(); i++){
            int chessPieceRow = (int)Math.floor(itsChessPieces.get(i).getNumericalLocation() / 8);
            int chessPieceColumn = itsChessPieces.get(i).getNumericalLocation()% 8;
            ArrayList<Square> possibleMove = itsChessPieces.get(i).getPossibleMoveLocations();
            
            for(int j = 0; j < possibleMove.size(); j++){
                Square testSquare = possibleMove.get(j);
                int testSquareRow = (int)Math.floor(testSquare.getNumericalLocation()/8);
                int testSquareColumn = possibleMove.get(j).getNumericalLocation() % 8;
                
                list += chessPieceRow + "" + chessPieceColumn;
                
                // Check if the opponent piece is on the move location
                if(testSquare.isOccupied()){
                    list += testSquareRow + "" + testSquareColumn + testSquare.getOccupant().toString();
                }

                else{
                    list += testSquareRow + "" + testSquareColumn;
                }
                System.out.print(" ");
            }
        }
        System.out.println(list);
    }
}