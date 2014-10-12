package chess.gui;

import chess.engine.*;
import chess.AI.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private ChessGame chess;
    private String moveTemp;
    private String strUpdate = "";
    private String capturedPiece = "";
    private Square squareSelected;
    private ChessGameAI gameAI = new ChessGameAI();
    private ArrayList<ChessPiece> itsChessPieces = new ArrayList<>();
    private ArrayList<Square> possibleSquare = new ArrayList<>();
    private JFrame f = new JFrame();
    private static JButton[] square = new JButton[64];
    private int source = 0;
    private int[] flipBoard = new int[64];
    private ImageIcon selectedPiece = new ImageIcon();
    private boolean selected;
    private boolean clickable;
    private boolean tryagain = true;
    private boolean againstPlayer;
    private JLabel whoseTurn = new JLabel("Choose Settings", JLabel.CENTER);
    private JPanel rightSidePanel = new JPanel(new BorderLayout());
    private gameSettings gameStartPanel = new gameSettings();
    private gameData gameDataPanel = new gameData();
    private JMenu file = new JMenu("Files"), view = new JMenu("View"), settings = new JMenu("Settings");
    private JMenuBar menuBar = new JMenuBar();
    private JMenuItem globalDepth1 = new JMenuItem("1");
    private JMenuItem globalDepth3 = new JMenuItem("3");
    private JMenuItem globalDepth4 = new JMenuItem("4");
    public static void main(String[] args) {
       new Chess();
    }

    public Chess() {
        JPanel chessBoard = new JPanel(new GridLayout(8, 8));
        JPanel parentPanel = new JPanel(new BorderLayout());
        JPanel chessPanel = new JPanel(new BorderLayout());
        JPanel gridLayoutPanel = new JPanel(new BorderLayout());
        for (int i = 0; i < flipBoard.length; i++)
            flipBoard[i] = 63 - i;
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
                if(x%2==0)
                    square[x].setBackground(new Color(247,131,0));
                else
                    square[x].setBackground(new Color(3,73,0));
            }
            if((x>7&&x<16)||(x>23&&x<32)||(x>39&&x<48)||(x>55&&x<=63)){
                if(x%2==0)
                    square[x].setBackground(new Color(3,73,0));
                else
                    square[x].setBackground(new Color(247,131,0));
            }
            // Add the colored square to the board
            chessBoard.add(square[x]);
        }
        // Add the pieces to the square
        chess = ChessGame.setupGame();
        itsChessPieces = chess.setUpPieces();
        // Set up the chess pieces
        for (int j = 0; j < itsChessPieces.size(); j++) 
            square[itsChessPieces.get(j).getNumericalLocation()].setIcon(itsChessPieces.get(j).getImage());

        // Register the action listener.
        for (int i = 0; i < 64; i++) 
            square[i].addActionListener(new ButtonListener());
        gameAI.setGameToAI(chess);
        // Start building a GUI
        parentPanel.add(menuBar, BorderLayout.NORTH);
        parentPanel.add(gridLayoutPanel);
        chessPanel.add(chessBoard, BorderLayout.CENTER);
        JPanel rowLabel = new JPanel(new GridLayout(8, 0));
        JPanel columnLabel = new JPanel(new GridLayout(0, 8));
        for(int i = 8; i >= 1; i--)
            rowLabel.add(new JLabel(String.valueOf(i)));
        
        chessPanel.add(rowLabel, BorderLayout.WEST);
        for(int i = 0; i < 8; i++){
            JLabel label = new JLabel(String.valueOf((char)(i + 'A')), JLabel.CENTER);
            columnLabel.add(label);
        }
        chessPanel.add(whoseTurn, BorderLayout.NORTH);
        chessPanel.add(columnLabel, BorderLayout.SOUTH);
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
                if(JOptionPane.showConfirmDialog(null, "Restart the game?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION) == 0){
                    // Rebuild the chess game
                    chess = ChessGame.setupGame();
                    
                    // Gather the chess pieces
                    itsChessPieces = chess.setUpPieces();
                    
                    // Clear the board
                    for(int i = 0; i < 64; i++)
                        square[i].setIcon(null);
                    
                    // Set up chess pieces
                    for (int j = 0; j < itsChessPieces.size(); j++) 
                        square[itsChessPieces.get(j).getNumericalLocation()].setIcon(itsChessPieces.get(j).getImage());
                   
                    whoseTurn.setText("Choose Settings");
                    gameDataPanel.setVisible(false);
                    gameDataPanel.clear();
                    gameStartPanel.setVisible(true);
                    rightSidePanel.add(gameStartPanel);
                    f.pack();
                    gameStartPanel.reset();
                    clickable = false;
                    deselect();
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
            if(e.getSource() == globalDepth1)
                ChessGameAI.globalDepth = 1;
            else if(e.getSource() == globalDepth3)
                ChessGameAI.globalDepth = 3;
            else if(e.getSource() == globalDepth4)
                ChessGameAI.globalDepth = 4;
        }
    }
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
                                if(againstPlayer);
                                    deselect();
                                
                                // Check if the king is in check after a move is made
                                if(!chess.isKingInCheck(source, dest)){
                                    System.out.println("check");
                                    chess.undoMove(dest, source);
                                }

                                // If the move is valid, process the move
                                else{
                                    executeMove(dest);
                                    // Playing against an AI
                                    if(!againstPlayer && clickable)
                                        AITurn();
                                    toggle();                  
                                }
                            }
                             deselect();
                            selected = false;
                        }
                        break;
                    }
                }
            }
        }
    }
    public void AITurn(){
         // Playing against an AI
        toggle();
        while(tryagain){
            gameAI.updateGameBoard(chess.convertToStringArray());
            ChessGameAI.flipBoard();
            long startTime=System.currentTimeMillis();
            moveTemp=ChessGameAI.alphaBeta(ChessGameAI.globalDepth, 1000000, -1000000, "", 0);
            ChessGameAI.makeMove(moveTemp);
            long endTime=System.currentTimeMillis();
         //   System.out.println("My Move: "+UI.moveDecoding(1,moveTemp.substring(0, 5))+" took: "+(endTime-startTime)+" milliseconds");

            //ChessGameAI.flipBoard();

         /*   for(int i=0 ;i<8 ;i++){
                System.out.println(Arrays.deepToString(ChessGameAI.BoardRepn[i]));
            }*/

            executeAIMove(moveTemp.substring(0, 5));
        }
        gameAI.updateGameStatus(chess);
        tryagain = true;
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
        int dest = flipBoard[sourceX + sourceY];
        System.out.println(move);
        // AI selects the chess piece to move
        chess.selectPieceAtLocation(dest);
        
        // Save the image of the piece and its location
        selectedPiece =  chess.getBoard().getSquareAt(dest).getOccupant().getImage();        
        selected = true;
     //   if(Character.isLetter(move.charAt(2)) || move.charAt(2) == ' ');
        int destX = (move.charAt(2) - 48) * 8;
        int destY = (move.charAt(3) - 48);
        // Check if AI's move is valid

        if(!chess.isKingInCheck(source, flipBoard[destX + destY])){
            System.out.println("Invalid move");
            chess.undoMove(dest, source);
            tryagain = true;
        }
        
        // Execute if the move is valid
        else{
            source = dest;
            executeMove(flipBoard[destX + destY]);
            tryagain = false;
        }
        //gameAI.updateGameBoard(chess.convertToStringArray());
    }
    public void selectPiece(int dest){
         // Get the piece on the selected square
        chess.selectPieceAtLocation(dest);

        // Save the image of the piece and its location
        selectedPiece =  squareSelected.getOccupant().getImage();
            possibleSquare = squareSelected.getOccupant().getPossibleMoveLocations();
        source = dest;
        // Highlight the selected piece
        square[source].setBackground(Color.blue);
        
        // Retrieve the list of all possible moves of the selected Piece
        for(int i = 0; i < possibleSquare.size(); i++){
            if(possibleSquare.get(i).isOccupied())
                square[possibleSquare.get(i).getNumericalLocation()].setBackground(Color.RED);
            else
                square[possibleSquare.get(i).getNumericalLocation()].setBackground(Color.YELLOW);
        }

        // Now to move the piece to where
        selected = true;
    }
    
    public void executeMove(int dest){
        square[source].setIcon(null);
        square[dest].setIcon(selectedPiece);

        ChessPiece originPiece = chess.getBoard().getSquareAt(dest).getOccupant();

        // Remove the captured pawn if enpassant is invoked
        if(chess.hasCastedEnpassant())
            square[chess.getPawnLocation()].setIcon(null);
        // A piece is captured
        if(chess.isCaptured()){
            // Update to the data panel
            if(chess.getCapturedPiece().getTeam() == Team.GREEN)
                gameDataPanel.updateCapturedPiece(chess.getCapturedPiece().getImage(), 0);
            else
                gameDataPanel.updateCapturedPiece(chess.getCapturedPiece().getImage(), 1);

            strUpdate = convertChartoStringName(originPiece.toString()) + 
                     " from square " + source +
                     " captures " + convertChartoStringName(chess.getCapturedPiece().toString())
                + " at square " + dest;
            capturedPiece = chess.getCapturedPiece().toString();
            chess.setCapture(false);
        }
        else if(chess.hasCastled()){

            // Rook's new location
            square[chess.getCastlingLocation()].setIcon(chess.getBoard().
                    getSquareAt(chess.getCastlingLocation()).getOccupant().getImage());
            square[chess.getRookLocation()].setIcon(null);

            chess.setCastled(false);

            // Record the move to the data panel
             ChessPiece rook = chess.getBoard().getSquareAt(chess.getCastlingLocation()).getOccupant();
             strUpdate = convertChartoStringName(originPiece.toString()) + " castles from square " + source
                + " move to square: " + dest + "\n" +
                     convertChartoStringName(rook.toString()) + " castles from square " + chess.getRookLocation()
                + " move to square: " + chess.getCastlingLocation();
        }
        else{
            strUpdate = convertChartoStringName(originPiece.toString()) + 
                    " from square: " + source
                + " move to square " + dest;
            capturedPiece = "";
        }
        // Record the move to Display messages
        gameDataPanel.updateMove(strUpdate);

        // Record the move into Played Move
        gameDataPanel.convert(Integer.toString(source / 8), 
                Integer.toString(source % 8), 
                capturedPiece,
                Integer.toString(dest / 8), 
                Integer.toString(dest % 8));
            
        // Pawn gets promoted
        if(chess.getState().getPawnPromotion()){
            Square squareLocation = chess.getBoard().getSquareAt(dest);
            ChessPiece oldPawn = squareLocation.getOccupant();
            if(!againstPlayer){
                promotePawnTo(new Queen(), dest, "Q", "Queen.png");
            }
            else{
                while(true){
                    int promo = JOptionPane.showOptionDialog(null, "What will the pawn be promoted to?", 
                            "Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                            new Object[]{"Rook", "Knight", "Bishop", "Queen"}, "Queen");
                    if(promo == 0){
                        promotePawnTo(new Rook(), dest, "R", "Rook.png");
                        break;
                    }
                    else if(promo == 1){
                        promotePawnTo(new Knight(), dest, "K", "Knight.png");
                        break;
                    }
                    else if(promo == 2){
                        promotePawnTo(new Bishop(), dest, "B", "Bishop.png");
                        break;
                    }
                    else if(promo == 3){
                        promotePawnTo(new Queen(), dest, "Q", "Queen.png");
                        break;
                    }
                }
            }
            chess.updatePawnPromotion(oldPawn, dest);
        }
        
        chess.updateStatus(dest);
            
        // End if there is a stalemate or a checkmate
        if(chess.getState().isCheckmate()){
            Team winner = chess.getBoard().getSquareAt(dest).getOccupant().getTeam();
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
    public void promotePawnTo(ChessPiece piece, int dest, String name, String image){
        Square squareLocation = chess.getBoard().getSquareAt(dest);
        piece.setTeam(squareLocation.getOccupant().getTeam());
        ChessPiece.assignStringName(piece, name, image);
        piece.setLocation(squareLocation);
        square[dest].setIcon(piece.getImage());
    }
    // Return the colors of possible moves to original colors.
    public void deselect() {
        // Deselect possible moves
        for(int i = 0; i < possibleSquare.size(); i++){
            int x = possibleSquare.get(i).getNumericalLocation();
            if((x<8)||(x>15&&x<24)||(x>31&&x<40)||(x>47&&x<56)){
                if(x%2==0)
                    square[x].setBackground(new Color(247,131,0));
                else
                    square[x].setBackground(new Color(3,73,0));	 
            }
            if((x>7&&x<16)||(x>23&&x<32)||(x>39&&x<48)||(x>55&&x<=63)){
                if(x%2==0)
                    square[x].setBackground(new Color(3,73,0));
                else
                    square[x].setBackground(new Color(247,131,0));
            }
        }

        // Turn from blue back to the original color
        if((source<8)||(source>15&&source<24)||(source>31&&source<40)||(source>47&&source<56)){
            if(source%2==0)
                square[source].setBackground(new Color(247,131,0));
            else
                square[source].setBackground(new Color(3,73,0));	
        }
        if((source>7&&source<16)||(source>23&&source<32)||(source>39&&source<48)||(source>55&&source<=63)){
            if(source%2==0)
                square[source].setBackground(new Color(3,73,0));
            else
                square[source].setBackground(new Color(247,131,0));    
        }
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