package chess.gui;

import java.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.*;

import chess.engine.*;



/**
 * @author Tanaeemul Abuhanif, Ryan J. Marcotte, UTD RobotChessTeam
 * 
 * To anyone reading/editing this code: Action/Listeners and used functions are defined at the bottom of this code. 
 * 
 * Code used to create gameDataPanel is really long, so it's created in its own separate class called gameData.java
 * 
 * Code used to create gameSettingsPanel is really long, so it's created in its own separate class called gameSettings.java  
 * 		Please take the time to read the "Where I envision this going" message in gameSettings.java 
 * 
 * If you see a huge gap between lines of code, I'm just trying to separate the different functions to make it easier for you to read
 * 
 */

public class ChessGUI {

    JFrame frame=new JFrame(); //Frame that holds EVERYTHING in this GUI
    JPanel chessBoardPanel = new JPanel();	
    gameData gameDataPanel = new gameData();    
    gameSettings gameSettingsPanel = new gameSettings();
    JPanel parentPanel = new JPanel(); //Holds the gameData and chessBoardSquares
    JPanel rightSidePanel = new JPanel();	//contains the gameData and settings Panels. Make only one of these visible at a time. 
    
    JButton[] square;  
    
    ImageIcon whitePawn= new ImageIcon(getClass().getResource("wp.png"), "white pawn");//ImageIcon(String path, String description)  
    ImageIcon blackPawn= new ImageIcon(getClass().getResource("bp.png"), "black pawn");//The description will be used when we do square[x].getImageIcon().
    ImageIcon whiteKing= new ImageIcon(getClass().getResource("wk.png"), "white king");//If no description is available, then the path name, as a String, will be used. 
    ImageIcon blackKing= new ImageIcon(getClass().getResource("bk.png"), "black king");
    ImageIcon whiteQueen= new ImageIcon(getClass().getResource("wq.png"), "white queen");
    ImageIcon blackQueen= new ImageIcon(getClass().getResource("bq.png"), "black queen");
    ImageIcon whiteRook= new ImageIcon(getClass().getResource("wr.png"), "white rook");
    ImageIcon blackRook= new ImageIcon(getClass().getResource("br.png"), "black rook");
    ImageIcon whiteKnight= new ImageIcon(getClass().getResource("wn.png"), "white knight");
    ImageIcon blackKnight= new ImageIcon(getClass().getResource("bn.png"), "black knight");
    ImageIcon whiteBishop= new ImageIcon(getClass().getResource("wb.png"), "white bishop");
    ImageIcon blackBishop= new ImageIcon(getClass().getResource("bb.png"), "black bishop");
    
    
    
    /*int squareNumber=0; Used for testing purposes only in the section where the chessboard square buttons are created*/
    int originSquare;	//which square a chesspiece starts off 
    int destinationSquare;	//which square I want my chesspiece to move to
    int clickNumber=1; //This will be used for the action/listener to determine if I'm dealing with origin or destination
    int[] possibleMoves= new int[64];//This array is empty right now. This array will be filled with a piece's possible moves
    int moveListSize;
    String currentTeam;
    int whiteCaptureNumber=0;	//keeps track of how many white pieces captured
    int blackCaptureNumber=0;
    
    GameState currentState = new GameState();  
    
    
    public ChessGUI(int rows, int columns){ 
    		///////////////Create the panel that holds the 8x8 chessboard layout////////////////
        	chessBoardPanel.setLayout(new GridLayout(rows,columns)); 
   			square=new JButton[rows*columns]; 
        
        	for(int x=0; x<columns*rows;x++){
        		square[x]=new JButton(); 
        		chessBoardPanel.add(square[x]);  
        	}   
        	/////////////////////Color the squares on the chessboard///////////////////
        	colorSquares();
        	
            ///////////Add the Chess Piece Images to the Buttons /////////////////////
        	addPieceImagesToSquares();
            
            //////////////////////Add the Action Listeners for the Squares///////////////////////
            listenForSquareClick squareClick = new listenForSquareClick();	
            for(int x=0;x<64;x++){
            	square[x].addActionListener(squareClick);
            }         
/***********************************************************************************************************************************/           
            ////////////////////////Action Listeners when users prompted to Play Again//////////////////////////////////
listenForYes yes = new listenForYes();
/**gameDataPanel.yesButton.addActionListener(yes);*/
            
listenForNo no = new listenForNo();
/**gameDataPanel.noButton.addActionListener(no);*/

		/////////////////////////////Action Listeners when Change Game Settings Clicked////////////////////////////////
listenForChangeSettings changeSettings = new listenForChangeSettings();
gameDataPanel.settingsButton.addActionListener(changeSettings);

listenForReturnToGame returnToGame = new listenForReturnToGame();
gameSettingsPanel.returnButton.addActionListener(returnToGame);
/***!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/  


            ////////////////////////////////////////Nest the Panels and finish the framework///////////////////////////
            parentPanel.setLayout(new GridLayout(1,2));
            
            rightSidePanel.add(gameDataPanel);
            rightSidePanel.add(gameSettingsPanel);
           
            gameSettingsPanel.setVisible(false);	//initially set to false. It'll be set to true when user clicks on "Change Game Settings"
            //gameDataPanel.setVisible(false);
            
            parentPanel.add(chessBoardPanel);
            parentPanel.add(rightSidePanel);
           
            parentPanel.setVisible(true);
           
            frame.add(parentPanel);
            frame.pack();
            frame.setVisible(true);     
    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	

    }
    
    





//////////////////////////////////////////////////USED FUNCTIONS, ACTion/Listener////////////////////////////////////////////////////////////        
/***********************************************************************************************************************************/    

private class listenForYes implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent arg0) {
		/*
		 * Reset all the images. Then call function that makes the generateInitialState() GameState
		 *addPieceImageToSquare();
		 *
		*/
	}	
}
    
    
    
    
private class listenForNo implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}	
}


    

private class listenForQueenPromotion implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}




private class listenForRookPromotion implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}




private class listenForBishopPromotion implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}




private class listenForKnightPromotion implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}




private class listenForChangeSettings implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		gameDataPanel.setVisible(false);
		gameSettingsPanel.setVisible(true);
		clickNumber=3;//prevents users from tweaking with the pieces while they are changing the settings
	}
	
}




private class listenForReturnToGame implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		gameSettingsPanel.setVisible(false);
		gameDataPanel.setVisible(true);
		clickNumber=2;
	}
}
/***!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/    




/***********************************************************************************************************************************/    
    private class listenForSquareClick implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//////////////////////////////////ORIGIN SQUARE PICK///////////////////////////////////	
			if(clickNumber==1){	/*If clicked square is legal, this if statement chooses origin square and highlights squares that are possible destinations. Otherwise, it just displays "choose origin sq correctly." */
				for(int x=0;x<64;x++){
					if(e.getSource()==square[x]){
						boolean originSquareVerified = verifyOriginSquare(x); //true means legal origin picked 
						
						if(originSquareVerified==true){
							originSquare = x;
							gameDataPanel.messageText.append("Clicked on origin square " +originSquare +". Contains: " +square[originSquare].getIcon() +"\n");
							clickNumber=2;	//Next time I click on a square, it'll be the destination square, and I'll go into the else statement below 



//updateGameStateOnGUI(handleUserSelection(originSquare), x);
 
 
							highlightLegalMoves(possibleMoves);
							break;	//You've chosen an appropriate origin, let's break and loop and pick an appropriate destination 
						}
						break;
					}
				}
			}//end of if statement
			
			///////////////////////////////////DESTINATION SQUARE PICK////////////////////////////////////////////
			else if(clickNumber==2){		
				for(int x=0;x<64;x++){
					if(e.getSource()==square[x]){
						//System.out.println("Checking to see if possibleMoves returned properly as an array");
						/*Delete later, just for testing purposes. 
						for(int i=0;i<moveListSize;i++){
							//System.out.println(possibleMoves[i]);
						}
						*/
						//System.out.println("EEEEENNNNDDDD of check!!!!!!!!!!!!!!!!!!!!");
						boolean destinationSquareVerified =  verifyDestinationSquare(x, moveListSize);//false means illegal destination

						//System.out.println("Checking to see if possibleMoves length returned properly: " +moveListSize);
						
						//if above function returns true, you passed the checks. You have a legal move.
						if(destinationSquareVerified==true){
							
							destinationSquare = x;
							gameDataPanel.messageText.append("Clicked on destination square " +destinationSquare +". Previously contained: " +square[destinationSquare].getIcon() +"\n");


//gameSettingsPanel.setVisible(true);
//gameDataPanel.setVisible(false);

/**							
gameDataPanel.messageText.append("Game Over. White wins!");
gameDataPanel.playAgainLabel.setVisible(true);
gameDataPanel.yesButton.setVisible(true);
gameDataPanel.noButton.setVisible(true);
*/
							

//updateGameStateOnGUI(handleUserSelection(destinationSquare), x);
 
							movePiece(originSquare, destinationSquare);
							possibleMoves=new int[64];	//clear the array so the next selected piece's moves can be stored in it. No you can't do possibleMoves= null (Recall possibleMoves is the address of where the array starts. If you set it = null, you're array basically has a nonexistent address... sort of.  You'll end up getting highlightSquares(null) which will mess up program.
							//System.out.println("Checking to see if my possibleMoves are cleared: " +possibleMoves);
							clickNumber=1;	//reset number of clicks so we can go back to choosing a origin square
						
							colorSquares();//remove highlighting of legal moves by recoloring squares to orange and green 
						
							break;	//you have a legal destination. You're done. 
						}
					}
				}
			}//End of else if statement	
		}//End of ActionPerformed from square click
    }//End of ActionListener
   
    
    
   
private boolean verifyOriginSquare(int x){
	System.out.println("Call handleUserSelection(x)");	//replace originSquareVerified with handleUserSelection later??? Maybe clickNumber should depend on what I get from handleUserSelection?	//If handleUserSelection returns false, 
	if(square[x].getIcon()==null){																																									//then this verify function returns false
		gameDataPanel.messageText.append("Clicked on empty square " +x +" Choose proper origin square" +"\n");	//Break the loop so you can click an appropriate origin								//else it returns true
		return false;
	}
		
    	return true;	//If above if statements are cleared, returns true. Replace above statement with handleUserSelection.
}
    
    
    
    
private boolean verifyDestinationSquare(int x, int moveListSize){				
    boolean legalMove = false;
    System.out.println("Call handleUserSelection(x) for destination square");

    for(int i=0;i<moveListSize;i++){
		if(possibleMoves[i]==x){
			legalMove=true;
			return true;
		}
		else{
			legalMove=false;
		}
	}
		
    if(legalMove==false){																	
		gameDataPanel.messageText.append("Illegal move to square " +x +". Reselect origin and destination" +"\n");
		possibleMoves=new int[64]; //clear the array so the next selected piece's moves can be stored in it.
		clickNumber=1;
		colorSquares(); //removes the highlighting of legal moves by recoloring the squares orange and green 
		return false;	//You have an illegal move. Repick your origin and destination 
	}
    return false;	
}
    
    
/***!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/    
    
    
    private void colorSquares() {
    	for(int x=0; x<64;x++){
    		if((x<8)||(x>15&&x<24)||(x>31&&x<40)||(x>47&&x<56)){
    			if(x%2==0){
    				square[x].setBackground(new Color(247,131,0));	//create a special orange (specify values of r,g,b)
    			}
    			else{
    				square[x].setBackground(new Color(3,73,0));	//create a special green 
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
    
    
    
    
    private void movePiece(int origin, int destination){
    	if(square[destination]!=null){
    		if(square[destinationSquare].getIcon()==whitePawn || square[destinationSquare].getIcon()==whiteKnight || square[destinationSquare].getIcon()==whiteBishop || square[destinationSquare].getIcon()==whiteRook || square[destinationSquare].getIcon()==whiteQueen || square[destinationSquare].getIcon()==whiteKing){ 			
    			Image getImg = ((ImageIcon) square[destinationSquare].getIcon()).getImage() ;  
    			Image resizeImg = getImg.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
    			ImageIcon resizedIcon = new ImageIcon( resizeImg );
    			gameDataPanel.whiteCapturedButtons[whiteCaptureNumber].setIcon(resizedIcon);
    			whiteCaptureNumber++;
    			//System.out.println(whiteCaptureNumber);
    		}
    		
    		//sorry for this super convoluted method... I tried doing an else statement, but for some odd reason, the blackCaptureNumber increments even if I click on a null destination square... Just know that if a blackpiece is being captured, this statement takes care of it.
    		if(square[destinationSquare].getIcon()==blackPawn || square[destinationSquare].getIcon()==blackKnight || square[destinationSquare].getIcon()==blackBishop || square[destinationSquare].getIcon()==blackRook || square[destinationSquare].getIcon()==blackQueen || square[destinationSquare].getIcon()==blackKing){
    			Image getImg = ((ImageIcon) square[destinationSquare].getIcon()).getImage() ;  
    			Image resizeImg = getImg.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
    			ImageIcon resizedIcon = new ImageIcon( resizeImg );
    			gameDataPanel.blackCapturedButtons[blackCaptureNumber].setIcon(resizedIcon);
    			blackCaptureNumber++;
    			System.out.println(blackCaptureNumber);
    		}
    	}
    	
    	square[destination].setIcon(square[origin].getIcon());
    	square[origin].setIcon(null); 	
    }
    
    
    
    
    private int[] highlightLegalMoves(int[] possibleMoves){
    	/*
    	 * For testing purposes, let's make this possibleMoves random. 
    	 */
    	/*
    	System.out.println(possibleMoves);
    	System.out.println(possibleMoves[0]);
    	System.out.println(possibleMoves[1]);
    	System.out.println(possibleMoves[3]);
    	*/
    	int random;
    	Random r = new Random();
    	random = r.nextInt(10);
    	if(random==0){
    		random=1;
    	}
    	moveListSize=random;
    	//System.out.println("Random number is size which is: " +random);
    	
		for(int x=0;x<random;x++){
    		int random2;
    		Random r2 = new Random();
    		random2 = r2.nextInt(64);

    		possibleMoves[x]=random2;
    		//System.out.println("possibleMoves square should match random2: " +possibleMoves[x]);    		
    	}
		//System.out.println("I should print out a zero here: " +possibleMoves[random]);
    	
    	
		//System.out.println("I have " +random +" as my size, so I can move to " +random +" different squares");
    	for(int x =0; x<random; x++){
    		square[possibleMoves[x]].setBackground(new Color(242,240,205));
    	}
    	return possibleMoves;
    }

    
    
/************************************************************************************************************************************/
private void updateGameStateOnGUI(GameState GS, int clickNumber){
    if(clickNumber==1){
    	if(GS.getActiveTeam()==Team.ORANGE){
           	gameDataPanel.messageText.setText("White's Turn");
           	currentTeam="white";
       	}
       	else{
       		gameDataPanel.messageText.setText("Black's Turn");
       		currentTeam="black";
       	}

   	}
    if(GS.isCheck()==true && GS.isCheckmate()==false){
    	if(currentTeam=="white"){
           	gameDataPanel.messageText.append("Black team under check");
    	}
   		else{
           	gameDataPanel.messageText.append("White team under check");
   		}
   	}
    	
    if(GS.isCheckmate()==true){
    	if(currentTeam=="white"){
            gameDataPanel.messageText.append("Game Over. White wins!");
            /*
             * Set gameDataPanel invisible and set gameSettingsPanel visible.
             * Set the playAgainPanel visible in the gameSettingsPanel.
             * Set Click number to 3 so no one can click squares until yes or no is chosen. 
             * 
             * When no is clicked, application exists (do this in A/L)
             * 
             * When yes is clicked, remake initial gameState, redraw images on chessboard, set clickNumber to 1. (do this in A/L)
      		*/
           	clickNumber=3;	//Stops the actionListener for square click? Maybe use a while loop that terminates when either Yes or No is clicked? Wait for further directions/explanations on handleUserSelection...
    	}
    	else{
           	gameDataPanel.messageText.append("Game Over. Black wins!");
           	//Same deal as above. 
           	//gameDataPanel.playAgainLabel.setVisible(true);
           	//gameDataPanel.yesButton.setVisible(true);
           	//gameDataPanel.noButton.setVisible(true);
           	clickNumber=3;
   		}
   	}
    	
   	if(GS.isDraw()==true){
    	gameDataPanel.messageText.append("Game Over. Draw");
       	//same deal as checkmate
       	clickNumber=3;	//???
   	}
    	
    if(GS.getPawnPromotionIndex()>0){
    	gameDataPanel.messageText.append("You can promote a pawn. Select the piece you would like to promote");
    	clickNumber=3;
    	if(currentTeam=="white"){
    		/*
    		miscPanel.queenPromotion.setIcon(whiteQueen);
    		miscPanel.rookPromotion.setIcon(whiteRook);
    		miscPanel.bishopPromotion.setIcon(whiteBishop);
    		miscPanel.knightPromotion.setIcon(whiteKnight);
    		*/
    	}
    	else{
    		/*
    		miscPanel.queenPromotion.setIcon(blackQueen);
    		miscPanel.rookPromotion.setIcon(blackRook);
    		miscPanel.bishopPromotion.setIcon(blackBishop);
    		miscPanel.knightPromotion.setIcon(blackKnight);
    		*/
    	}
    	//ActionListener for each of the buttons. 
		//In the ActionListener, Turn the info of piece clicked into a string, send it back to Ryan's program. Remake Click number to 1
    }
    
    if(GS.getPossibleMoveIndexes().size()!=0){
    	//highlight the squares. Call the highlight function. 
    }
    
    if(GS.getMovePairs().size()!=0){
    	
    }
}
/***!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/  

    
    
    
    private void addPieceImagesToSquares(){
        for(int x=0; x<64; x++){		
        	square[x].setIcon(null);
        }
        
    	for(int x=48; x<56; x++){	
    		square[x].setIcon(whitePawn);
        }
        
        for(int x=8; x<16; x++){	
        	square[x].setIcon(blackPawn);
        }
        
        square[0].setIcon(blackRook);
        square[7].setIcon(blackRook);
        
        square[56].setIcon(whiteRook);
        square[63].setIcon(whiteRook);
        
        square[1].setIcon(blackKnight);
        square[6].setIcon(blackKnight);
        
        square[57].setIcon(whiteKnight);
        square[62].setIcon(whiteKnight);
        
        square[2].setIcon(blackBishop);
        square[5].setIcon(blackBishop);
        
        square[58].setIcon(whiteBishop);
        square[61].setIcon(whiteBishop);
        
        square[4].setIcon(blackKing);
        
        square[60].setIcon(whiteKing);
        
        square[3].setIcon(blackQueen);
        
        square[59].setIcon(whiteQueen);
    }
    
    
    
    
    public static void main(String[] args) {
    	/*
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               new Main(8,8);
            }
        });
        */
        new ChessGUI(8,8); //the 8,8 is for 8x8 button grid 
    }
}
