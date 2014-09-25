package chess.gui;

import chess.engine.*;
import chess.AI.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Alexandre
 */
import javax.swing.JOptionPane;

public class Chess extends JFrame {

    ChessGame chess;
    String moveTemp;
    Square squareSelected;
    
    private ArrayList<ChessPiece> itsChessPieces = new ArrayList<>();
    private ArrayList<Square> possibleSquare = new ArrayList<>();
    static JPanel chessBoard = new JPanel(new GridLayout(8, 8));
    JPanel chessPanel = new JPanel(new BorderLayout());
    JFrame f = new JFrame();
    static JButton[] square = new JButton[64];
    private int source = 0;
    int[] flipBoard = new int[64];
    ImageIcon selectedPiece = new ImageIcon();
    private boolean selected = false;
    boolean clickable = false;
    boolean againstPlayer;
    JLabel whoseTurn = new JLabel("Choose Settings", JLabel.CENTER);
    JPanel parentPanel = new JPanel(new BorderLayout());
    JPanel rightSidePanel = new JPanel(new BorderLayout());
    JPanel gridLayoutPanel = new JPanel(new BorderLayout());
    gameSettings gameStartPanel = new gameSettings();
    gameData gameDataPanel = new gameData();
    JMenuBar menuBar = new JMenuBar();
    JMenuItem globalDepth1 = new JMenuItem("1");
    JMenuItem globalDepth3 = new JMenuItem("3");
    JMenuItem globalDepth4 = new JMenuItem("4");
    
    JPanel row = new JPanel(new GridLayout(8, 0));
    JPanel column = new JPanel(new GridLayout(0, 8));
    ChessGameAI gameAI = new ChessGameAI();
    JMenu file = new JMenu("Files"), view = new JMenu("View"), settings = new JMenu("Settings"), subMenu;
    public static void main(String[] args) {
       new Chess();
    }

    public Chess() {
        for (int i = 0; i < flipBoard.length; i++){
            flipBoard[i] = 63 - i;
        }
        Chess.MouseAdapter clicked = new Chess.MouseAdapter();
        file = new JMenu("File");
        view = new JMenu("View");
        settings = new JMenu("Game Settings");
        addMenuItems();
        
        // Start Game
        gameStartPanel.gameStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whoseTurn.setText("White's move");
                againstPlayer = gameStartPanel.playAgainstWho();
                gameStartPanel.setVisible(false);
                gameDataPanel.setVisible(true);
                rightSidePanel.add(gameDataPanel);
                clickable = true; // the squares are now clickable
                f.pack();
            }
        });
        for (int x = 0; x < 64; x++) {
            square[x] = new JButton();
            if((x<8)||(x>15&&x<24)||(x>31&&x<40)||(x>47&&x<56)){
                if(x%2==0){
                    square[x].setBackground(new Color(247,131,0));
                }
                else{
                    square[x].setBackground(new Color(3,73,0));
                }
            }
            if((x>7&&x<16)||(x>23&&x<32)||(x>39&&x<48)||(x>55&&x<=63)){
                if(x%2==0){
                    square[x].setBackground(new Color(3,73,0));
                }
                else{
                    square[x].setBackground(new Color(247,131,0));
                }
            }
            // Add the colored square to the board
            chessBoard.add(square[x]);
        }
        // Add the pieces to the square
        chess = ChessGame.setupGame();
        itsChessPieces = chess.setUpPieces();
        // Set up the chess pieces
        for (int j = 0; j < itsChessPieces.size(); j++) {
            square[itsChessPieces.get(j).getNumericalLocation()].setIcon(itsChessPieces.get(j).getImage());
        }

        for (int i = 0; i < 64; i++) {
            // Register the action listener.
            square[i].addMouseListener(clicked);
        }
        gameAI.setGameToAI(chess);
        // Start building a GUI
        parentPanel.add(menuBar, BorderLayout.NORTH);
        parentPanel.add(gridLayoutPanel);
        chessPanel.add(chessBoard, BorderLayout.CENTER);
        
        for(int i = 8; i >= 1; i--){
            row.add(new JLabel(String.valueOf(i)));
        }
        chessPanel.add(row, BorderLayout.WEST);
        for(int i = 0; i < 8; i++){
            JLabel label = new JLabel(String.valueOf((char)(i + 'A')), JLabel.CENTER);
            column.add(label);
        }
        chessPanel.add(whoseTurn, BorderLayout.NORTH);
        chessPanel.add(column, BorderLayout.SOUTH);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 100;
        rightSidePanel.add(gameStartPanel);
        gridLayoutPanel.add(chessPanel, BorderLayout.WEST);
        gridLayoutPanel.add(rightSidePanel, BorderLayout.EAST);
        f.add(parentPanel);
        f.setTitle("Chess");
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void addMenuItems() {
        menuBar.add(file);
        menuBar.add(settings);
        menuBar.add(view);
        JMenuItem newGame = new JMenuItem("New Game");
        
        
        // Restart Game
        newGame.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Restart the game?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
 
                if(result == 0){
                    
                    // Rebuild the chess game
                    chess = ChessGame.setupGame();
                    
                    // Gather the chess pieces
                    itsChessPieces = chess.setUpPieces();
                    
                    // Clear the board
                    for(int i = 0; i < 64; i++){
                        square[i].setIcon(null);
                    }
                    
                    // Set up chess pieces
                    for (int j = 0; j < itsChessPieces.size(); j++) {
                        square[itsChessPieces.get(j).getNumericalLocation()].setIcon(itsChessPieces.get(j).getImage());
                   }
                    whoseTurn.setText("Choose Settings");
                   gameDataPanel.setVisible(false);
                   gameDataPanel.clear();
                   gameStartPanel.setVisible(true);
                   rightSidePanel.add(gameStartPanel);
                   f.pack();
                   gameStartPanel.reset();
                   clickable = false;
                }
            }
            
        });
        file.add(newGame);
        Listener listener = new Listener();
        JMenu setGlobalDepth = new JMenu("Setting Global depth");
        
        globalDepth1.addActionListener(listener);
        globalDepth3.addActionListener(listener);
        globalDepth4.addActionListener(listener);
        
        settings.add(setGlobalDepth);
        setGlobalDepth.add(globalDepth1);
        setGlobalDepth.add(globalDepth3);
        setGlobalDepth.add(globalDepth4);
        
        JMenuItem config = new JMenuItem("configuration");
        settings.add(config);
        
        JMenuItem nothing = new JMenuItem("Blank");
        settings.add(new JCheckBox("Aggressive Play"));
        settings.add(nothing);
        
    }
    class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == globalDepth1){
                ChessGameAI.globalDepth = 1;
            }
            else if(e.getSource() == globalDepth3){
                ChessGameAI.globalDepth = 3;
            }
            else if(e.getSource() == globalDepth4){
                ChessGameAI.globalDepth = 4;
            }
            
        }
        
    }
    class MouseAdapter implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(clickable){
            for (int dest = 0; dest < 64; dest++) {
                // Search for the position of the square clicked
                if (e.getSource() == square[dest]) {
                    
                    // Retrieve the piece on that square
                    squareSelected = chess.getBoard().getSquareAt(dest);

                    // Player select a square that has his/her own team piece on the team's turn
                    if(!selected && squareSelected.isOccupied() && 
                            squareSelected.getOccupant().getTeam() == chess.getState().getActiveTeam()){
                       selectPiece(dest);
                    }
                    // Choose where the selected piece will move to
                    else if(selected){
                        
                        // Check if square selected is in the possible move list
                        if(chess.getState().getPossibleMoveIndexes().contains(dest)){
                            deselect();
                            // Check if the king is in check after
                            // player finishes a move
                            if(!chess.isKingInCheck(source, dest)){
                                System.out.println("check");
                                chess.undoMove(dest, source);
                            }
                            
                            // Process the move
                            else{
                                executeMove(dest);
                                
                                // Playing against an AI
                                if(!againstPlayer){
                                    toggle();
                                    gameAI.updateGameBoard(chess.convertToStringArray());
                                    ChessGameAI.flipBoard();
                                    long startTime=System.currentTimeMillis();
                                    moveTemp=ChessGameAI.alphaBeta(ChessGameAI.globalDepth, 1000000, -1000000, "", 0);
                                    ChessGameAI.makeMove(moveTemp);
                                    long endTime=System.currentTimeMillis();
                                  //  System.out.println("My Move: "+UI.moveDecoding(1,moveTemp.substring(0, 5))+" took: "+(endTime-startTime)+" milliseconds");

                                    //ChessGameAI.flipBoard();

                                 /*   for(int i=0 ;i<8 ;i++){
                                        System.out.println(Arrays.deepToString(ChessGameAI.BoardRepn[i]));
                                    }*/

                                    gameAI.updateGameStatus(chess);
                                    executeAIMove(moveTemp.substring(0, 5));
                                    toggle();
                                }
                                
                                // Playing against a player
                                else
                                    toggle();
                            }
                        }
                        else{
                            deselect();
                        }
                    }
                    break;
                }
            }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    public void toggle(){
        if(whoseTurn.getText().equals("White's move"))
            whoseTurn.setText("Black's move");
        else
            whoseTurn.setText("White's move");
    }
    public void executeAIMove(String move){
        int sourceX = (move.charAt(0) - 48) * 8;
        int sourceY = (move.charAt(1) - 48);
        int next = flipBoard[sourceX + sourceY];
        
        squareSelected = chess.getBoard().getSquareAt(next);
        selectPiece(next);
        
        source = next;
        int destX = (move.charAt(2) - 48) * 8;
        int destY = (move.charAt(3) - 48);
        deselect();
        chess.isKingInCheck(source, flipBoard[destX + destY]);
        executeMove(flipBoard[destX + destY]);
    //    gameAI.updateGameBoard(chess.convertToStringArray());
    }
    public void selectPiece(int dest){
         // Get the piece on the selected square
        chess.selectPieceAtLocation(dest);

        // Save the image of the piece and its location
        selectedPiece =  squareSelected.getOccupant().getImage();
        source = dest;
        possibleSquare = squareSelected.getOccupant().getPossibleMoveLocations();
        // Highlight possible moves
        square[source].setBackground(Color.blue);
        for(int i = 0; i < possibleSquare.size(); i++){
            if(possibleSquare.get(i).isOccupied())
                square[possibleSquare.get(i).getNumericalLocation()].setBackground(Color.RED);
            else
                square[possibleSquare.get(i).getNumericalLocation()].setBackground(Color.YELLOW);
        }

        // Now to move the piece to where
        selected = true;
    }
    
    public void executeMove(int next){
        
         // Check if castling is valid
        if(chess.hasCastled()){

            // Rook's new location
            square[chess.getCastlingLocation()].setIcon(chess.getBoard().
                    getSquareAt(chess.getCastlingLocation()).getOccupant().getImage());
            square[chess.getRookLocation()].setIcon(null);

            // King's new location
            square[source].setIcon(null);
            square[next].setIcon(selectedPiece);
            chess.setCastled(false);

            // Record the move to the data panel
            ChessPiece king = chess.getBoard().getSquareAt(source).getOccupant();
             int originKingLocation =chess.getBoard().getSquareAt(source).getNumericalLocation();
             int destKingLocation = chess.getBoard().getSquareAt(chess.getRookLocation()).getNumericalLocation();
             ChessPiece rook = chess.getBoard().getSquareAt(chess.getCastlingLocation()).getOccupant();
             int originRookLocation = chess.getBoard().getSquareAt(chess.getRookLocation()).getNumericalLocation();
             int destRookLocation = chess.getBoard().getSquareAt(chess.getCastlingLocation()).getNumericalLocation();
             
             gameDataPanel.updateMove(convertChartoStringName(king.toString()) + " castles from square " + originKingLocation
                + " move to square: " + destKingLocation + "\n" +
                     convertChartoStringName(rook.toString()) + " castles from square " + originRookLocation
                + " move to square: " + destRookLocation);
        }
        else {
            // Remove the captured pawn if enpassant is invoked
            if(chess.hasCastedEnpassant()){
                square[chess.getPawnLocation()].setIcon(null);
            }

            square[source].setIcon(null);
            square[next].setIcon(selectedPiece);

            ChessPiece originPiece = chess.getBoard().getSquareAt(next).getOccupant();

            // A piece is captured
            if(chess.getCapturedPiece() != null){
                
                // Update to the data panel
                if(chess.getCapturedPiece().getTeam() == Team.GREEN){
                    gameDataPanel.updateCapturedPiece(chess.getCapturedPiece().getImage(), 0);
                }
                else{
                    gameDataPanel.updateCapturedPiece(chess.getCapturedPiece().getImage(), 1);
                }

                gameDataPanel.updateMove(convertChartoStringName(originPiece.toString()) + 
                         " from square " + source +
                         " captures " + convertChartoStringName(chess.getCapturedPiece().toString())
                    + " at square " + next);
                gameDataPanel.convert(Integer.toString(source / 8), 
                        Integer.toString(source % 8), 
                        chess.getCapturedPiece().toString(),
                        Integer.toString(next / 8), 
                        Integer.toString(next % 8));
                chess.setCapture(false);
            }
            else{
                // Record the move to Display messages
                gameDataPanel.updateMove(convertChartoStringName(originPiece.toString()) + 
                        " from square: " + source
                    + " move to square " + next);

                // Record the move into Played Move
                gameDataPanel.convert(Integer.toString(source / 8), 
                        Integer.toString(source % 8), 
                        "",
                        Integer.toString(next / 8), 
                        Integer.toString(next % 8));
            }

            // Pawn gets promoted
            if(chess.getState().getPawnPromotion()){
                int promo = JOptionPane.showOptionDialog(null, "What will the pawn be promoted to?", 
                        "Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                        new Object[]{"Rook", "Knight", "Bishop", "Queen"}, "Rook");

                Square squareLocation = chess.getBoard().getSquareAt(next);
                ChessPiece oldPawn = squareLocation.getOccupant();
                if(promo == 0){
                    Rook rook = new Rook();
                    rook.setTeam(squareLocation.getOccupant().getTeam());
                    Rook.assignStringName(rook);
                    rook.setLocation(squareLocation);
                    square[next].setIcon(rook.getImage());
                }
                else if(promo == 1){
                    Knight knight = new Knight();
                    knight.setTeam(squareLocation.getOccupant().getTeam());
                    Knight.assignStringName(knight);
                    knight.setLocation(squareLocation);
                    square[next].setIcon(knight.getImage());
                }
                else if(promo == 2){
                    Bishop bishop = new Bishop();
                    bishop.setTeam(squareLocation.getOccupant().getTeam());
                    Bishop.assignStringName(bishop);
                    bishop.setLocation(squareLocation);
                    square[next].setIcon(bishop.getImage());
                }
                else if(promo == 3){
                    Queen queen = new Queen();
                    queen.setTeam(squareLocation.getOccupant().getTeam());
                    Queen.assignStringName(queen);
                    queen.setLocation(squareLocation);
                    square[next].setIcon(queen.getImage());
                }
                chess.updatePawnPromotion(oldPawn, next);
            }
        }
        chess.updateStatus(next);
            
        // End if there is a stalemate or a checkmate
        if(chess.getState().isCheckmate()){
            Team winner = chess.getBoard().getSquareAt(next).getOccupant().getTeam();
            gameDataPanel.updateMove("Winner: Team " + winner);
            JOptionPane.showMessageDialog(null, "CheckMate: Team " + winner + " wins");
            clickable = false;
        }
        else if(chess.getState().isDraw()){
            gameDataPanel.updateMove("Stalemate");
            JOptionPane.showMessageDialog(null, "Stalemate");
            clickable = false;
        }
    }
    public void processPromotion(ChessPiece p){
        
    }
    // Return the colors of possible moves to original colors.
    public void deselect() {
        for(int i = 0; i < possibleSquare.size(); i++){
            int x = possibleSquare.get(i).getNumericalLocation();
            if((x<8)||(x>15&&x<24)||(x>31&&x<40)||(x>47&&x<56)){
                if(x%2==0){
                    square[x].setBackground(new Color(247,131,0));
                }
                else{
                    square[x].setBackground(new Color(3,73,0));	 
                }
            }
            if((x>7&&x<16)||(x>23&&x<32)||(x>39&&x<48)||(x>55&&x<=63)){
                if(x%2==0){
                    square[x].setBackground(new Color(3,73,0));
                }
                else{
                    square[x].setBackground(new Color(247,131,0));
                }
            }
        }

        // Turn blue back to the original color
        if((source<8)||(source>15&&source<24)||(source>31&&source<40)||(source>47&&source<56)){
            if(source%2==0){
                square[source].setBackground(new Color(247,131,0));
            }
            else{
                square[source].setBackground(new Color(3,73,0));	
            }
        }

        if((source>7&&source<16)||(source>23&&source<32)||(source>39&&source<48)||(source>55&&source<=63)){
            if(source%2==0){
                square[source].setBackground(new Color(3,73,0));
            }
            else{
                square[source].setBackground(new Color(247,131,0));
            }
        }

        selected = false;
    }
    public String convertChartoStringName(String piece){
        switch (piece) {
            case "P":
                return "white Pawn";
            case "R":
                return "white Rook";
            case "K":
                return "white Knight";
            case "B":
                return "white Bishop";
            case "Q":
                return "white Queen";
            case "A":
                return "white King";
            case "p":
                return "black Pawn";
            case "r":
                return "black Rook";
            case "k":
                return "black Knight";
            case "b":
                return "black Bishop";
            case "q":
                return "black Queen";
            case "a":
                return "black King";
        }
        return "";
    }
}