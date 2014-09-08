package chess.gui;

import chess.engine.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
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
    Square squareSelected;
    private ArrayList<ChessPiece> itsChessPieces = new ArrayList<>();
    private ArrayList<Square> possibleSquare = new ArrayList<>();
    static JPanel chessBoard = new JPanel(new GridLayout(8, 8));
    JFrame f = new JFrame();
    static JButton[] square = new JButton[64];
    private int index = 0;
    ImageIcon selectedPiece = new ImageIcon();
    private boolean selected = false;
    boolean clickable = false;
    JPanel parentPanel = new JPanel(new BorderLayout());
    JPanel rightSidePanel = new JPanel(new BorderLayout());
    JPanel gridLayoutPanel = new JPanel(new BorderLayout());
    gameSettings gameStartPanel = new gameSettings();
    gameData gameDataPanel = new gameData();
    JMenuBar menuBar = new JMenuBar();
  
    JMenu file = new JMenu("Files"), view = new JMenu("View"), settings = new JMenu("Settings"), subMenu;
    JMenuItem menuItem;

    public static void main(String[] args) {
       new Chess();
    }

    public Chess() {
        Chess.MouseAdapter clicked = new Chess.MouseAdapter();
        file = new JMenu("File");
        view = new JMenu("View");
        settings = new JMenu("Settings");
        addMenuItems();
        gameStartPanel.gameStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStartPanel.setVisible(false);
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

        // Start building a GUI
        parentPanel.add(menuBar, BorderLayout.NORTH);
        parentPanel.add(gridLayoutPanel);
        gridLayoutPanel.add(chessBoard, BorderLayout.WEST);
        rightSidePanel.add(gameStartPanel);

        gridLayoutPanel.add(rightSidePanel, BorderLayout.CENTER);
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

        menuItem = new JMenuItem("New Game");
        menuItem.addActionListener(new ActionListener(){

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
                    gameStartPanel.setVisible(true);
                    gameDataPanel.setVisible(false);
                   gameDataPanel.clear();
                }
            }
        });
        file.add(menuItem);
        menuItem = new JMenuItem("test1");
        settings.add(menuItem);
        menuItem = new JMenuItem("test1");
        view.add(menuItem);
        menuItem = new JMenuItem("test1");
        file.add(menuItem);
        menuItem = new JMenuItem("test1");
        settings.add(menuItem);
        menuItem = new JMenuItem("test1");
        view.add(menuItem);
    }

    class MouseAdapter implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
         //   if(clickable)
            for (int next = 0; next < 64; next++) {
                // Search for the position of the square clicked
                if (e.getSource() == square[next]) {
                    
                    // Retrieve the piece on that square
                    squareSelected = chess.getBoard().getSquareAt(next);
                    
                    // Player select a square that has his/her own team piece on the team's turn
                    if(squareSelected.isOccupied() && 
                            squareSelected.getOccupant().getTeam() == chess.getState().getActiveTeam() 
                            && !selected){
                        
                       selectPiece(next);
                    }
                    // Choose where the selected piece will move to
                    else if(selected){
                        
                        // Check if the square selected is possible to move to
                        if(chess.getState().getPossibleMoveIndexes().contains(next)){
                            deselect();
                            // Check if the king is in check after
                            // player finishes a move
                            if(!chess.isKingInCheck(index, next)){
                                undo(next);
                            }
                            
                            // Process the move
                            else{
                                executeMove(next);
                            }
                        }
                        else
                            deselect();
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
    public void selectPiece(int next){
         // Get the piece on the selected square
        chess.selectPiece(next);

        // Save the image of the piece and its location
        selectedPiece =  squareSelected.getOccupant().getImage();
        possibleSquare = squareSelected.getOccupant().getPossibleMoveLocations();

        // Highlight possible moves
        square[next].setBackground(Color.blue);
        for(int i = 0; i < possibleSquare.size(); i++){
            if(possibleSquare.get(i).getNumericalLocation() != -1)
                if(possibleSquare.get(i).isOccupied())
                    square[possibleSquare.get(i).getNumericalLocation()].setBackground(Color.RED);
                else
                    square[possibleSquare.get(i).getNumericalLocation()].setBackground(Color.YELLOW);
        }
        
        //Save selected square number
        index = next;

        // Now to move the piece to where
        selected = true;
    }
    public void undo(int next){
        // Undo move if the king is in check
        // Return the captured piece to the board
        if(chess.isTemporarilyCaptured()){
            if(chess.hasCastedEmpasse()){
                square[chess.getPawnLocation()].setIcon(chess.getCapturedChessPiece().getImage());
            }
            else
                square[next].setIcon(chess.getCapturedChessPiece().getImage());
        }
        else
            // Remove the piece from the board
            square[next].setIcon(null);

        square[index].setIcon(selectedPiece);
        chess.undoMove(next, index);
        System.out.println("Current King is under attack");
    }
    
    public void executeMove(int next){
         // Check if castling fails or passes
        if(chess.hasCastled()){

            // Rook's new location
            square[chess.getCastlingLocation()].setIcon(chess.getBoard().
                    getSquareAt(chess.getCastlingLocation()).getOccupant().getImage());
            square[chess.getRookLocation()].setIcon(null);

            // King's new location
            square[index].setIcon(null);
            square[next].setIcon(selectedPiece);
            chess.setCastled(false);

            // Record the move to the data panel
            ChessPiece king = chess.getBoard().getSquareAt(index).getOccupant();
             int originKingLocation =chess.getBoard().getSquareAt(index).getNumericalLocation();
             int destKingLocation = chess.getBoard().getSquareAt(chess.getRookLocation()).getNumericalLocation();
             ChessPiece rook = chess.getBoard().getSquareAt(chess.getCastlingLocation()).getOccupant();
             int originRookLocation = chess.getBoard().getSquareAt(chess.getRookLocation()).getNumericalLocation();
             int destRookLocation = chess.getBoard().getSquareAt(chess.getCastlingLocation()).getNumericalLocation();
             gameDataPanel.updateMove(convertChartoStringName(king.toString()) + " castles from square " + originKingLocation
                + " move to square: " + destKingLocation + "\n" +
                     convertChartoStringName(rook.toString()) + " castles from square " + originRookLocation
                + " move to square: " + destRookLocation);
        }
        else if(chess.castlingAttempted());
        else {
            // Processing empasse
            if(!chess.getState().getEnPassantPairs().isEmpty()){
                // Remove the piece from the board
                square[next].setIcon(selectedPiece);
                square[chess.getPawnLocation()].setIcon(null);
            }

            square[index].setIcon(null);
            square[next].setIcon(selectedPiece);
                // Execute the move if the move is on the possible move list

            ChessPiece originPiece = chess.getBoard().getSquareAt(next).getOccupant();
            int originLocation = index;
            int destinationLocation = next;

            // A piece is captured
            if(chess.getCapturedPiece() != null){
                if(chess.getCapturedPiece().getTeam() == Team.GREEN){
                    gameDataPanel.updateCapturedPiece(chess.getCapturedPiece().getImage(), 0);
                }
                else{
                    gameDataPanel.updateCapturedPiece(chess.getCapturedPiece().getImage(), 1);
                }

                 gameDataPanel.updateMove(convertChartoStringName(originPiece.toString()) + 
                         " from square " + originLocation +
                         " captures: " + convertChartoStringName(chess.getCapturedPiece().toString())
                    + " at square: " + destinationLocation);
                gameDataPanel.convertWithCap(Integer.toString((int)Math.floor(index / 8)), 
                        Integer.toString(index % 8), 
                        chess.getCapturedPiece().toString(),
                        Integer.toString((int)Math.floor(next / 8)), 
                        Integer.toString(next % 8));
                chess.sendPrisoner();
            }
            else{
                // Record the move to Display messages
                gameDataPanel.updateMove(convertChartoStringName(originPiece.toString()) + 
                        " from square: " + originLocation
                    + " move to square: " + destinationLocation);

                // Record the move into Played Move
                gameDataPanel.convert(Integer.toString((int)Math.floor(index / 8)), 
                        Integer.toString(index % 8), 
                        Integer.toString((int)Math.floor(next / 8)), 
                        Integer.toString(next % 8));
            }

            if(chess.getState().getPawnPromotion()){
                int promo = JOptionPane.showOptionDialog(null, "What will the pawn be promoted to?", 
                        "Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                        new Object[]{"Rook", "Knight", "Bishop", "Queen"}, "Rook");

                Square squareLocation = chess.getBoard().getSquareAt(next);
                if(promo == 0){
                    Rook rook = new Rook();
                    Rook.assignStringName(rook);
                    rook.setTeam(squareLocation.getOccupant().getTeam());
                    chess.addPromotedPiece(rook);
                    rook.setLocation(squareLocation);
                    square[next].setIcon(rook.getImage());
                }
                else if(promo == 1){
                    Knight knight = new Knight();
                    Knight.assignStringName(knight);
                    knight.setTeam(squareLocation.getOccupant().getTeam());
                    chess.addPromotedPiece(knight);
                    knight.setLocation(squareLocation);
                    square[next].setIcon(knight.getImage());
                }
                else if(promo == 2){
                    Bishop bishop = new Bishop();
                    Bishop.assignStringName(bishop);
                    bishop.setTeam(squareLocation.getOccupant().getTeam());
                    chess.addPromotedPiece(bishop);
                    bishop.setLocation(squareLocation);
                    square[next].setIcon(bishop.getImage());
                }
                else if(promo == 3){
                    Queen queen = new Queen();
                    Queen.assignStringName(queen);
                    queen.setTeam(squareLocation.getOccupant().getTeam());
                    chess.addPromotedPiece(queen);
                    queen.setLocation(squareLocation);
                    square[next].setIcon(queen.getImage());
                }

                chess.getState().setPawnPromotion(false);
                chess.getState().toggleActiveTeam();
                chess.updatePossibleMoveLocations();
                chess.updateKingStatus();
                chess.getState().toggleActiveTeam();

            }
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
         /*   String[][] strList = chess.convertToStringArray();
             for(int i = 0; i < strList.length; i++){
                for(int j = 0; j < strList.length; j++){
                    if(strList[i][j] == null)
                        System.out.print(" ");
                    else
                        System.out.print(strList[i][j]);
                }
            System.out.println();
             }*/
        }
    }
    // Return the colors of possible moves to original colors.
    public void deselect() {
        for(int i = 0; i < possibleSquare.size(); i++){
            if(possibleSquare.get(i).getNumericalLocation() != -1){
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
        }

        // Turn blue back to the original color
        if((index<8)||(index>15&&index<24)||(index>31&&index<40)||(index>47&&index<56)){
            if(index%2==0){
                square[index].setBackground(new Color(247,131,0));
            }
            else{
                square[index].setBackground(new Color(3,73,0));	
            }
        }

        if((index>7&&index<16)||(index>23&&index<32)||(index>39&&index<48)||(index>55&&index<=63)){
            if(index%2==0){
                square[index].setBackground(new Color(3,73,0));
            }
            else{
                square[index].setBackground(new Color(247,131,0));
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