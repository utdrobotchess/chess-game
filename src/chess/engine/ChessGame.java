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
    private boolean castEmpasse;
    private int bothSideCastled = 0;
    private int rookLocation;
    private int castlingLocation;
    private ChessBoard itsBoard;
    private boolean capturePiece = false;
    private GameState itsState;
    private ChessPiece capturedPiece;
    private ChessPiece captured;
    private Square square;
    private boolean castlingAttempt;
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
        itsChessPieces.add(piece);
        //logger.log(Level.FINE, "{0} added to chess game", piece);
    }
    protected void splitInTeam(){
        for(int i = 0; i < itsChessPieces.size(); i++){
            if(itsChessPieces.get(i).getTeam() == Team.GREEN)
                whitePieces.add(itsChessPieces.get(i));
            else
                blackPieces.add(itsChessPieces.get(i));
        }
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
    public GameState selectPiece(int selectionLocation){
         // Retrieve the selected square
        Square selectionSquare = itsBoard.getSquareAt(selectionLocation);
        //  Get the selected chess piece on the selected square
        ChessPiece occupant = selectionSquare.getOccupant();
        // Make a list of all possible moves
        ArrayList<Square> possibleMoveLocations;
        itsState.clearEnPassantPairs();
        itsState.clearMovePairs();
        
        // Update
        updatePossibleMoveLocations();
        
        // Set what square the selected piece is at
        itsState.setSelectedPieceIndex(selectionLocation);
        // Retrieve all possiblem moves
        possibleMoveLocations = occupant.getPossibleMoveLocations();
        // Save the possible moves
        itsState.setPossibleMoveIndexes(possibleMoveLocations);
        
        strList = convertToStringArray();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(strList[i][j] == null)
                    strList[i][j] = " ";
                System.out.print(strList[i][j] + " ");
            }
            System.out.println();
        }
      //  convert();
        printEntireMove();
        return itsState;
    }
    
   /* protected void convertFromStringArray(String[][] strArray){
        for(int i = 0; i < strArray.length ;i++){
            for(int j = 0; j < strArray.length; j++){
                strArray[i][j]
            }
        }
    }*/
    protected String[][] convertToStringArray(){
        String[][] strArray = new String[8][8];
        for(int i = 0; i < itsChessPieces.size() ;i++){
            int sqLocation = itsChessPieces.get(i).getNumericalLocation();
                strArray[(int)Math.floor(sqLocation/8)][sqLocation % 8] = itsChessPieces.get(i).toString();
        }
        return strArray;
    }
    
    
    
    // This function simulates that if a piece is moved, is the king still in
    // check.
    protected boolean TestForCheck(int origin, int destination){
        boolean isValid;
        
        int savedOrigin = origin;
        int savedDest = destination;
        // Return the king's location to the previous if castling is invalid
 
        if(checkForImpasse(destination)){
            movePiece(itsState.getEnPassantPairs().get(0), itsState.getEnPassantPairs().get(1));
        }
        ArrayList<Integer> movePairs;
        movePairs = addMovePairs(destination);
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
        isValid = !testForBeingChecked();
        undoMove(savedDest, savedOrigin);
        return isValid;
    }
    public boolean isKingInCheck(int origin, int destination){
        boolean isValid;
        castlingAttempt = false;
        // Castling
        if(isCastlingInvoked(destination) && itsBoard.getSquareAt(origin).getOccupant() instanceof King){
            
            // If castling succeeds
            if(checkForCastle(origin, destination)){
                setCastled(true);
                movePiece(rookLocation, castlingLocation);
            }
            else if(castlingAttempt){
                itsState.toggleActiveTeam();
            }
        }
        else{
            if(isBeingPromoted()){
                itsState.setPawnPromotion(true);
            }
            else if(checkForImpasse(destination)){
                movePiece(itsState.getEnPassantPairs().get(0), itsState.getEnPassantPairs().get(1));
            }
            ArrayList<Integer> movePairs;
            movePairs = addMovePairs(destination);
            itsState.setMovePairs(movePairs);
            // An opponent piece is captured
            if(movePairs.get(1) == - 1){
                capturePieceLocatedNumericallyAt(destination);
            }

            // Process the moves
            for (int i = 0; i < movePairs.size(); i += 2) {
                movePiece(movePairs.get(i), movePairs.get(i+1)); 
            }
        }
        
        // Update after these moves
        updatePossibleMoveLocations();
        
        // Check if the king is not in check after the move
        isValid = !testForBeingChecked();
        // The a valid move has been executed.
        if(isValid){
            if(capturedPiece != null){
                captured = capturedPiece;
                if(capturedPiece.getTeam() == Team.GREEN)
                    whitePieces.remove(capturedPiece);
                else
                    blackPieces.remove(capturedPiece);
                itsChessPieces.remove(capturedPiece);
            }
            setCapture(false);
            updateKingStatus();
            // update game status
            updateStatus(destination);
        }
        return isValid;
    }
    
    
    public void updateKingStatus(){
        testForCheck();
        testForStalemate();
        if(itsState.isDraw())
            System.out.println("draw");
        if(itsState.isCheck()){
            testForCheckMate();
            if(itsState.isCheckmate())
                System.out.println("checkmate");
            else
                System.out.println("check");
        }
    }
    private void updateStatus(int dest){
        itsState.setSelectedPieceIndex(-1);
        itsState.clearPossibleMoveIndexes();
        itsState.toggleActiveTeam();
        itsBoard.getSquareAt(dest).getOccupant().incrementNumberOfPriorMoves();
    }
    private void capturePieceLocatedNumericallyAt(int destination){
        setCapture(true);
        
        // Save the piece and its location
        capturedPiece = itsBoard.getSquareAt(destination).getOccupant();
        square = capturedPiece.getLocation();
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

        // Move the selected piece
        // From here
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
        } else {
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
        }
    }

    private void testForCheck() {
        int kingLocation = getKingLocation();

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
    
    private int getKingLocation() {
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
        splitInTeam();
        return itsChessPieces;
    }

    // Assume a check is a checkmate. Find a opponent piece to stop the check
    // for a checkmate to be invalid.
    protected void testForCheckMate(){
        int kingLocation = getKingLocation();
        ArrayList<ChessPiece> teamPiece = new ArrayList<>();
        ArrayList<ChessPiece> opponentPiece = new ArrayList<>();
        ArrayList<Square> teamPieceMoveList;
        ArrayList<Square> opponentMoveList;
        itsState.setCheckmate(true);
        // List of all pieces
        for (int i = 0; i < itsChessPieces.size(); i++) {
            ChessPiece piece = itsChessPieces.get(i);

            // Player's own pieces
            if (piece.getTeam() == itsState.getActiveTeam() && piece.getPossibleMoveLocations().contains(itsBoard.getSquareAt(kingLocation))) { 
                        teamPiece.add(piece);    
            }
            
            // Enemy pieces
            else if (piece.getTeam() != itsState.getActiveTeam()){
                opponentPiece.add(piece);
            }
        }
        boolean preventedByAPiece = false;
        int here; int there;
        // Number of team pieces
        opponentPiece.remove(itsBoard.getSquareAt(kingLocation).getOccupant());
        for(int w = 0; w < teamPiece.size() && !preventedByAPiece; w++){
            // Each team piece's move list
            
            teamPieceMoveList = teamPiece.get(w).getPossibleMoveLocations();
            // Number of opponent members
            for(int x = 0; x < opponentPiece.size() && !preventedByAPiece; x++){
                // Each opponent piece's possible move list
                boolean captureAttempt = false;
                opponentMoveList = opponentPiece.get(x).getPossibleMoveLocations();
                // Find the square that is both in the opponent's and team's possible move list
                for(int y = 0; y < teamPieceMoveList.size() && !preventedByAPiece; y++){
                    for(int z = 0; z < opponentMoveList.size(); z++){
                        // If the current opponent piece is unable to block the attacker piece's path or even capture that piece,
                        // it is a checkmate
                        // Otherwise, break out of the loop to move on to the next piece
                        if(teamPieceMoveList.get(y) == opponentMoveList.get(z)){
                            
                            there = teamPieceMoveList.get(y).getNumericalLocation();
                            here = opponentPiece.get(x).getNumericalLocation();
                            if(canOpponentPreventBeingInCheck(here, there)){
                                preventedByAPiece = true;
                                break;
                            }
                        }
                        // If an opponent can capture the attacker
                        else if(teamPiece.get(w).getLocation() == opponentMoveList.get(z) && !captureAttempt){
                            captureAttempt = true;
                            there = teamPiece.get(w).getNumericalLocation();
                            here = opponentPiece.get(x).getNumericalLocation();
                            if(canOpponentPreventBeingInCheck(here, there)){
                                preventedByAPiece = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(preventedByAPiece){
            itsState.setCheckmate(false);
        }
        // If there is no team piece to protect the king, check
        // if the king can move at all when in check
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
        int kingLocation = getKingLocation();
        ChessPiece theirKing = itsBoard.getSquareAt(kingLocation).getOccupant();
        
        // Get the new list of possible moves
        numberOfPossibleMove = theirKing.getPossibleMoveLocations();
        
        if (!theirKing.getPossibleMoveLocations().isEmpty()){
            for(int x = 0; x < numberOfPossibleMove.size(); x++){
                int kingPossibleMove = numberOfPossibleMove.get(x).getNumericalLocation();
                selectPiece(kingLocation);
                itsState.toggleActiveTeam();
                
                // King can move and, therefore, not stuck
                if(TestForCheck(kingLocation, kingPossibleMove)){
                    stillStuck = false;
                    itsState.toggleActiveTeam();
                    break;
                }
                itsState.toggleActiveTeam();
            }
        }
       return stillStuck;
    }
    
    protected boolean canOpponentPreventBeingInCheck(int defender, int attacker){
        boolean canPreventAttack;
        itsState.setSelectedPieceIndex(-1); 
        // Deselect all the possible move
        itsState.clearPossibleMoveIndexes();  
        
        selectPiece(defender);
        // king is in checkmate
        itsState.toggleActiveTeam();
        canPreventAttack = TestForCheck(defender, attacker);
        itsState.toggleActiveTeam();
        return canPreventAttack;
        
    }
    
    // Undo the move execute. The player will move again
    public void undoMove(int destination, int origin){
        
        // Put a captured piece back into the game
        movePiece(destination, origin);
        if(isCaptured()){
           capturedPiece.setLocation(square);
           setCapture(false);
        }
        
        updatePossibleMoveLocations();
        itsState.setSelectedPieceIndex(-1); 
        itsState.clearPossibleMoveIndexes();
    }
 
    public ChessPiece getCapturedChessPiece(){
        return capturedPiece;
    }
    public boolean isCaptured(){
        return capturePiece;
    }
    protected void setCapture(boolean x){
        capturedPiece = null;
        
        capturePiece = x;
    }
    public void sendPrisoner(){
        captured = null;
    }
    // If the king is the only piece remaining in the team and is stuck,
    // it is a stalemate
    private void testForStalemate(){
        if(itsState.getActiveTeam() != Team.GREEN){
            if(whitePieces.size() == 1 && isEnemyKingStuck() && !itsState.isCheck()){
                itsState.setDraw(true);
            }
        }
        else{
            if(blackPieces.size() == 1 && isEnemyKingStuck() && !itsState.isCheck()){
                itsState.setDraw(true);
            }
        }
    }
    protected boolean checkForCastle(int kingLocation, int dest){
        
        ChessPiece rook;
        Square kingSquare = itsBoard.getSquareAt(kingLocation);
        Square destSquare = itsBoard.getSquareAt(dest);
        ChessPiece king = kingSquare.getOccupant();
        int castleDirection;
        ArrayList<ChessPiece> opponentPieces;
        
        if(king.getTeam() == Team.GREEN){
            opponentPieces = blackPieces;
        }
        else{
            opponentPieces = whitePieces;
        }
        
        if(!(bothSideCastled != 3 )){
            return false;
        }
        if(itsState.isCheck()){
            castlingAttempt = true;
         //   castlingAttempt = true;
            return false;
        }
        
        //***** Check if the destination square will invoke castling********
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
        
        // Get the square which is beside the king's square
        Square castlingSquare = kingSquare.getNeighborInDirection(castleDirection);
        castlingLocation = castlingSquare.getNumericalLocation();

        // ******** Check if either squares are not being attacked ********
        for (int i = 0; i < opponentPieces.size(); i++){
            ArrayList<Square> pieceMove = opponentPieces.get(i).getPossibleMoveLocations();
            for (int j = 0; j < pieceMove.size(); j++)
                if(pieceMove.get(j) == castlingSquare || pieceMove.get(j) == destSquare){
                    System.out.println("Cannot castle: square under attack");
                    castlingAttempt = true;
                    return false;
                }
        }

        rookLocation = rook.getNumericalLocation();
        return true;
    }
    protected boolean checkForImpasse(int dest){
        castEmpasse = false;
        Square destSquare = itsBoard.getSquareAt(dest);
        Square piece = itsBoard.getSquareAt(itsState.getSelectedPieceIndex());
        int leftDirection;
        int rightDirection;
        int rearDirection;
        
        // Check if the piece is a pawn
        if(!(piece.getOccupant() instanceof Pawn))
            return false;
        
        // Check if the pawn is on the correct row to cast empasse
        if(piece.getOccupant().getTeam() == Team.GREEN){
            if(dest < 32 && dest >= 40)
                return false;
            leftDirection = 5;
            rightDirection = 3;
            rearDirection = 0;
        }
        else{
            if(dest < 24 && dest >= 32)
                return false;
            leftDirection = 7;
            rightDirection = 1;
            rearDirection = 4;
        }
        
        // Check if the destination square is not occupied and in the square
        // where empasse will execute
        if(!destSquare.isOccupied()){
            if(itsBoard.getSquareAt(dest) ==  piece.getNeighborInDirection(leftDirection) ||
                    itsBoard.getSquareAt(dest) ==  piece.getNeighborInDirection(rightDirection)){
                ArrayList<Integer> emPassePairs = new ArrayList<>();
                pawnLocation = destSquare.getNeighborInDirection(rearDirection).getNumericalLocation();

                // Remove the opponent's pawn
                emPassePairs.add(pawnLocation);
                emPassePairs.add(-1);

                itsState.setEnPassantPairs(emPassePairs);
                capturePieceLocatedNumericallyAt(pawnLocation);
                castEmpasse = true;
                return true;
            }
        } 
        return false;
    }
    public int getRookLocation(){
        return rookLocation;
    }
    public boolean hasCastled(){
        return hasCastled;
    }
    public boolean hasCastedEmpasse(){
        return castEmpasse;
    }
    public int getCastlingLocation(){
        return castlingLocation;
    }
    public void setCastled(boolean castled){
        bothSideCastled++;
        hasCastled = castled;
    }
    protected boolean isCastlingInvoked( int dest){
        if(dest == 2 || dest == 6 || dest == 58 || dest == 62)
            return true;
        else 
            return false;
    }
    public int getPawnLocation(){
        return pawnLocation;
    }
    public boolean castlingAttempted(){
        return castlingAttempt;
    }
    public ChessPiece getCapturedPiece(){
        return captured;
    }
    protected boolean isBeingPromoted(){
        Square mainSquare = itsBoard.getSquareAt(itsState.getSelectedPieceIndex());
        ChessPiece piece = mainSquare.getOccupant();
        if(piece instanceof Pawn){
            if(piece.getTeam() == Team.GREEN && mainSquare.getNumericalLocation() >= 48 
                    && mainSquare.getNumericalLocation()<56)
                return true;
            if(piece.getTeam() == Team.ORANGE && mainSquare.getNumericalLocation() >= 8 
                    && mainSquare.getNumericalLocation()<16)
                return true;
        }
        return false;
    }
    public void addPromotedPiece(ChessPiece p){
        if(p.getTeam() == Team.GREEN)
            whitePieces.add(p);
        else
            blackPieces.add(p);
        itsChessPieces.add(p);
    }
    public void printEntireMove(){
        String list = "";
        for(int i = 0; i < itsChessPieces.size(); i++){
            // Chess Piece
            ArrayList<Square> possibleMove = itsChessPieces.get(i).getPossibleMoveLocations();
            
            for(int j = 0; j < possibleMove.size(); j++){
                if(possibleMove.get(j).getNumericalLocation() != -1){
                    
                    list += (int)Math.floor(itsChessPieces.get(i).getNumericalLocation() / 8) + "" +
                    (itsChessPieces.get(i).getNumericalLocation()% 8);
                // Check if the opponent piece is on the move location
                    if(itsBoard.getSquareAt(possibleMove.get(j).getNumericalLocation()).isOccupied()){
                        list += (int)Math.floor(possibleMove.get(j).getNumericalLocation()/8) + "" 
                                + (possibleMove.get(j).getNumericalLocation() % 8) + 
                                itsBoard.getSquareAt(possibleMove.get(j).getNumericalLocation()).getOccupant().toString();
                    }

                    else{
                         list += (int)Math.floor(possibleMove.get(j).getNumericalLocation()/8) + "" 
                                + (possibleMove.get(j).getNumericalLocation() % 8 ) + " ";
                    }
                }
            }
        }
    }
}