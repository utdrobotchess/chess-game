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
 * Code used to create all panels (except chessBoardPanel, parentPanel, and rightSidePanel) are defined in their own separate class 
 * 
 * If you see a huge gap between lines of code, I'm just trying to separate the different functions to make it easier for you to read
 * 
 * 
 *TODO Remove clickNumbers variables when Ryan finishes his segment of code. 
 */

public class ChessGUI {

    JFrame frame=new JFrame(); //Frame that holds EVERYTHING in this GUI
    JPanel chessBoardPanel = new JPanel();	
    JPanel rightSidePanel = new JPanel();	//contains the gameData and settings Panels. Make only one of these visible at a time. 
    JPanel gridLayoutPanel = new JPanel();
    JPanel parentPanel = new JPanel(); //Holds the gridLayoutPanel, and menuBar

    gameData gameDataPanel = new gameData();    
    gameSettings gameStartPanel = new gameSettings();
    gameOver gameOverPanel = new gameOver();
    
    JMenuBar menuBar = new JMenuBar();;
    JMenu file = new JMenu("Files"), view = new JMenu("View"), settings = new JMenu("Settings"), subMenu;
    JMenuItem menuItem;

    JButton[] chessBoardSquare = new JButton[64];  
    
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
    
    int originSquare;	//which square a chesspiece starts off 
    int destinationSquare;	//which square I want my chesspiece to move to
    int clickNumber=1; //This will be used for the action/listener to determine if I'm dealing with origin or destination
    
    ArrayList<Integer> possibleMoves = new ArrayList<Integer>();//This array is empty right now. This array will be filled with a piece's possible moves
    int moveListSize;	//line 590 for highlightLegalMoves function 
    
    String currentTeam = "white";	//only used to update the text in the Displayed Messages box
    
    int whiteCaptureNumber=0;	//keeps track of how many white pieces captured
    int blackCaptureNumber=0;	//These are going to be used to display the captured pieces in the GUI. 
    
    boolean promotable = false; //Action/Listeners for pawn promotion only activated if promotable == true;
    boolean clickable = false; //If the game is over, or if I'm changing settings, the chessBoardSquares will not be clickable. 
    boolean checkGame = false; //checks for game over, checks, and pawn promotion (used in action/listener) 

    testCheck test1 = new testCheck();
    testCheckMate test2 = new testCheckMate();
    testDraw test3 = new testDraw();
    testPawnPromotion test4 = new testPawnPromotion();
    
    int promotableSquare;	//used for promotion button A/L
    
    
    
    public ChessGUI(){ 
    		///////////////Create the panel that holds the 8x8 chessboard layout////////////////
        	chessBoardPanel.setLayout(new GridLayout(8,8)); 
        	for(int x=0; x<64;x++){
        		chessBoardSquare[x]=new JButton(); 
        		chessBoardPanel.add(chessBoardSquare[x]);  
        	}   
        	/////////////////////Color the squares on the chessboard///////////////////
        	colorSquares();
        	
            ///////////Add the Chess Piece Images to the Buttons /////////////////////
        	addPieceImagesToSquares();
            
            //////////////////////Add the Action Listeners for the Squares///////////////////////
           
        	listenForSquareClick squareClick = new listenForSquareClick();	
            for(int x=0;x<64;x++){
            	chessBoardSquare[x].addActionListener(squareClick);
            }         
          
            ////////////////////////Add Action Listeners when users prompted to Play Again//////////////////////////////////
            listenForYes yes = new listenForYes();
            gameOverPanel.yesButton.addActionListener(yes);
            
            listenForNo no = new listenForNo();
            gameOverPanel.noButton.addActionListener(no);

		    //////////////////////////Add Action Listeners when Change Game Settings Clicked////////////////////////////////
            /*
            listenForChangeSettings changeSettings = new listenForChangeSettings();
            gameDataPanel.settingsButton.addActionListener(changeSettings);
			*/
            
            listenForStartGame startGame = new listenForStartGame();
            gameStartPanel.gameStartButton.addActionListener(startGame);
			

	        ///////////////////////////Add Action/Listeners for Promotion Buttons////////////////////////////////////////////
            listenForQueenPromotion queenPromotion = new listenForQueenPromotion();
            gameDataPanel.queenPromotionButton.addActionListener(queenPromotion);

            listenForRookPromotion rookPromotion = new listenForRookPromotion();
            gameDataPanel.rookPromotionButton.addActionListener(rookPromotion);

            listenForBishopPromotion bishopPromotion = new listenForBishopPromotion();
            gameDataPanel.bishopPromotionButton.addActionListener(bishopPromotion);

            listenForKnightPromotion knightPromotion = new listenForKnightPromotion();
            gameDataPanel.knightPromotionButton.addActionListener(knightPromotion);

            ////////////////////////////////Create the Menu///////////////////////////////////////////////////////////
            file = new JMenu("File");
            view = new JMenu("View");
            settings = new JMenu("Settings");
            addMenuItems();
            
            ////////////////////////////////////////Nest the Panels and finish the framework///////////////////////////
            parentPanel.setLayout(new GridBagLayout());
            gridLayoutPanel.setLayout(new GridLayout(1,2));
            rightSidePanel.add(gameDataPanel);
            rightSidePanel.add(gameStartPanel);
            rightSidePanel.add(gameOverPanel);
            gameStartPanel.setVisible(true);
            gameOverPanel.setVisible(false);	//set to true when game is over. 
            gameDataPanel.setVisible(false);
            
       
           
           //It's necessary to do this in order to prevent resizing issues... Sometimes, when I change the visiblity of certain panels, they resize in a very undesirable way. 
            gridLayoutPanel.add(chessBoardPanel);
            gridLayoutPanel.add(rightSidePanel);
            
            addComponent(parentPanel, menuBar,         0,0,                               GridBagConstraints.REMAINDER,1,                             0,40, GridBagConstraints.HORIZONTAL);
            addComponent(parentPanel, gridLayoutPanel, 0,GridBagConstraints.RELATIVE,     GridBagConstraints.REMAINDER,GridBagConstraints.REMAINDER,  0,180, GridBagConstraints.BOTH);            
            
            parentPanel.setVisible(true);
            frame.add(parentPanel);
            frame.pack();
            frame.setVisible(true);     
    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
    }
    
   






	//////////////////////////////////////////////////Action/Listener definitions followed by all used function definitions////////////////////////////////////////////////////////////        
	private class listenForYes implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		clickNumber=1;
			clickable=false;	//it'll become true when you click the start button from gameSettings
    		addPieceImagesToSquares();
    		System.out.println("Call Ryan's function that makes the generateInitialState() GameState");
    		gameStartPanel.setVisible(true);
    		gameOverPanel.setVisible(false);
    		clearButtons();	//clear the images on the buttons that show which pieces are captured. 
    	}	
    }		

    
    
    
    private class listenForNo implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		System.exit(0);
    	}	
    }


    

    private class listenForQueenPromotion implements ActionListener{
	/* In the action listener, Promote the pawn to the desired piece, 
	 * 		return the click number to 1 because whoever's turn it is currently will have their turn end
	 * 		make promotable false
	 * 		pass the information of which promotion piece clicked on back to RYan's function 
	 */
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		if(promotable==true){
			chessBoardSquare[promotableSquare].setIcon(gameDataPanel.queenPromotionButton.getIcon());
			clickNumber=1;
			clickable=true;
			promotable=false;
			gameDataPanel.queenPromotionButton.setIcon(null);
			gameDataPanel.rookPromotionButton.setIcon(null);
			gameDataPanel.bishopPromotionButton.setIcon(null);
			gameDataPanel.knightPromotionButton.setIcon(null);
			System.out.println("Passing information on which piece promoted back to Ryan's function");
    		}
    	}	
    }




    private class listenForRookPromotion implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		if(promotable==true){
    			chessBoardSquare[promotableSquare].setIcon(gameDataPanel.rookPromotionButton.getIcon());
    			clickNumber=1;
    			clickable=true;
    			promotable=false;
    			gameDataPanel.queenPromotionButton.setIcon(null);
    			gameDataPanel.rookPromotionButton.setIcon(null);
    			gameDataPanel.bishopPromotionButton.setIcon(null);
    			gameDataPanel.knightPromotionButton.setIcon(null);
    			System.out.println("Passing information on which piece promoted back to Ryan's function");
    		}
    	}
    }




    private class listenForBishopPromotion implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		if(promotable==true){
    			chessBoardSquare[promotableSquare].setIcon(gameDataPanel.bishopPromotionButton.getIcon());
    			clickNumber=1;
    			clickable=true;
    			promotable=false;
    			gameDataPanel.queenPromotionButton.setIcon(null);
    			gameDataPanel.rookPromotionButton.setIcon(null);
    			gameDataPanel.bishopPromotionButton.setIcon(null);
    			gameDataPanel.knightPromotionButton.setIcon(null);
    			System.out.println("Passing information on which piece promoted back to Ryan's function");	
    		}
    	}
    }	




    private class listenForKnightPromotion implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		if(promotable==true){
    			chessBoardSquare[promotableSquare].setIcon(gameDataPanel.knightPromotionButton.getIcon());
    			clickNumber=1;
    			clickable=true;
    			promotable=false;
    			gameDataPanel.queenPromotionButton.setIcon(null);
    			gameDataPanel.rookPromotionButton.setIcon(null);
    			gameDataPanel.bishopPromotionButton.setIcon(null);
    			gameDataPanel.knightPromotionButton.setIcon(null);
    			System.out.println("Passing information on which piece promoted back to Ryan's function");
    		}	
    	}
    }



/*
    private class listenForChangeSettings implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		gameDataPanel.setVisible(false);
    		gameSettingsPanel.setVisible(true);
    		clickNumber=clickNumber+3;//prevents users from messing with the pieces while they are changing the settings
    		clickable=false;
    	}
    }
*/



    private class listenForStartGame implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		gameStartPanel.setVisible(false);
    		gameDataPanel.setVisible(true);
    		//clickNumber=clickNumber-3;	//Go back to the clickNumber player was on before he clicked Change Settings 
    		clickable=true;
    	}	
    }

    
    
    //This is what I'll use when Ryan's chess.engine is finished. 
    /**
	private class listenForSquareClick implements ActionListener{
    	@Override
    	public void actionPerformed(Action Event e){
    		if(clickable==true){
    			for(int x=0; x<64; x++){
    				if(e.getSource()==chessBoardSquare[x]){
    					System.out.println("Call handleUserSelection(x) like this: updateGameStateOnGUI(handleUserSelection(x))");
    				}
    			}
    		}
    	}//end of action performed
    }//end of action/listener
    */
    
    
    
    
/*/**********TODO************This is my initial method that uses clickNumbers. Use it for testing purposes only *******************************************************************************************************/
/**
    private class listenForSquareClick implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//////////////////////////////////ORIGIN SQUARE PICK///////////////////////////////////	//
			/**
			 * Stay with the click numbers for now. Create a separate Action/Listener for when Ryan finishes his chess.engine.
			 * We don't need first and second clicks, or anything of that matter. All validation will be done in chess.engine
			 */
/**			if(clickNumber==1){	/*If clicked square is legal, this if statement chooses origin square and highlights squares that are possible destinations. Otherwise, it just displays "choose origin sq correctly." */
/**				for(int x=0;x<64;x++){
					if(e.getSource()==chessBoardSquare[x]){
						boolean originSquareVerified = verifyOriginSquare(x); //true means legal origin picked 
						
						if(originSquareVerified==true){
							originSquare = x;
							gameDataPanel.messageText.append("Clicked on origin square " +originSquare +". Contains: " +chessBoardSquare[originSquare].getIcon() +"\n");
							clickNumber=2;	//Next time I click on a square, it'll be the destination square, and I'll go into the else statement below 

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
					if(e.getSource()==chessBoardSquare[x]){
						//System.out.println("Checking to see if possibleMoves returned properly as an array");
						/*Delete later, just for testing purposes. 
						for(int i=0;i<moveListSize;i++){
							//System.out.println(possibleMoves[i]);
						}
						*/
						//System.out.println("EEEEENNNNDDDD of check!!!!!!!!!!!!!!!!!!!!");
/**						boolean destinationSquareVerified =  verifyDestinationSquare(x, moveListSize);//false means illegal destination

						//System.out.println("Checking to see if possibleMoves length returned properly: " +moveListSize);
						
						//if above function returns true, you passed the checks. You have a legal move.
						if(destinationSquareVerified==true){
							destinationSquare = x;
							gameDataPanel.messageText.append("Clicked on destination square " +destinationSquare +". Previously contained: " +chessBoardSquare[destinationSquare].getIcon() +"\n");
							gameDataPanel.moveListText.append("Moved " +chessBoardSquare[originSquare].getIcon() +" to " +destinationSquare +" and captured " +chessBoardSquare[destinationSquare].getIcon() +"\n");
							gameOverPanel.moveListText.append("Moved " +chessBoardSquare[originSquare].getIcon() +" to " +destinationSquare +" and captured " +chessBoardSquare[destinationSquare].getIcon() +"\n");

							movePiece(originSquare, destinationSquare);
							possibleMoves.clear();	//clear the array so the next selected piece's moves can be stored in it. No you can't do possibleMoves= null (Recall possibleMoves is the address of where the array starts. If you set it = null, you're array basically has a nonexistent address... sort of.  You'll end up getting highlightSquares(null) which will mess up program.
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
 */  
    
    
   
     /*/* This is what we'll use to TEMPORARILY test gamestate objects 
     * @author Tanaeem TODO
     *
     */
    private class listenForSquareClick implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//////////////////////////////////ORIGIN SQUARE PICK///////////////////////////////////	//
			/**
			 * Stay with the click numbers for now. Use the Action/Listener close to line 302 when Ryan finishes his chess.engine.
			 * We don't need first and second clicks, or anything of that matter. All validation will be done in chess.engine
			 */
		
			if(clickNumber==1&&clickable==true){	/*This if statement chooses origin square and highlights squares that are possible destinations. */
    			for(int x=0;x<64;x++){
					if(e.getSource()==chessBoardSquare[x]){
						originSquare = x;
						gameDataPanel.messageText.append("Clicked on origin square " +originSquare +". Contains: " +chessBoardSquare[originSquare].getIcon() +"\n");
			/*TODO*/// 	updateGameStateOnGUI(test1);//this will highlight squares
						updateGameStateOnGUI(test2);//this will highlight squares
					//	updateGameStateOnGUI(test3);//this will highlight squares
					//	updateGameStateOnGUI(test4);//this will highlight squares

						clickNumber=2;	//Next time I click on a square, it'll be the destination square, and I'll go into the else statement below 
						break;	//You've chosen an appropriate origin, let's break and loop and pick an appropriate destination 
					}
				}
			}//end of if statement
		
			///////////////////////////////////DESTINATION SQUARE PICK////////////////////////////////////////////
			else if(clickNumber==2&&checkGame==false){		
				for(int x=0;x<64;x++){
					if(e.getSource()==chessBoardSquare[x]){
						destinationSquare = x;
						gameDataPanel.messageText.append("Clicked on destination square " +destinationSquare +". Previously contained: " +chessBoardSquare[destinationSquare].getIcon() +"\n");
						gameDataPanel.moveListText.append("Moved " +chessBoardSquare[originSquare].getIcon() +" to " +destinationSquare +" and captured " +chessBoardSquare[destinationSquare].getIcon() +"\n");
						gameOverPanel.moveListText.append("Moved " +chessBoardSquare[originSquare].getIcon() +" to " +destinationSquare +" and captured " +chessBoardSquare[destinationSquare].getIcon() +"\n");
							
			/*TODO*///	updateGameStateOnGUI(test1);	//pieces will be moved.
			 			updateGameStateOnGUI(test2);
			 	//		updateGameStateOnGUI(test3);
			 	//		updateGameStateOnGUI(test4);

						//possibleMoves.clear(); You will not need to clear the move list array. I think chess.engine takes care of that 
						checkGame=true;
						break;	//you have a legal destination. You're done. Now we gotta check for checks/checkmate/draw/pawnpromotion 	
					}
				}
			////////////////////////////////////////////////////checkGame//////////////////////////////////////////////////////////////////
			/*TODO*///	updateGameStateOnGUI(test1);
					 	updateGameStateOnGUI(test2);
			//		 	updateGameStateOnGUI(test3);
			//		 	updateGameStateOnGUI(test4);

						System.out.println("Doing a whole bunch of stuff to check if the game is over and what not.");
						checkGame=false;
						clickNumber=1;	//back to the game
			}//End of else if statement 
		}//End of ActionPerformed from square click
    }//End of ActionListener
    
    
    
 //Obsolete methods (will be removed later.)   
/**    
    private boolean verifyOriginSquare(int x){
    	if(chessBoardSquare[x].getIcon()==null){																																									//then this verify function returns false
    		gameDataPanel.messageText.append("Clicked on empty square " +x +" Choose proper origin square" +"\n");	//Break the loop so you can click an appropriate origin								//else it returns true
    		return false;
    	}
		
    		return true;	//If above if statements are cleared, returns true. Replace above statement with handleUserSelection.
    }
    
    
    
    
    private boolean verifyDestinationSquare(int x, int moveListSize){				
    	boolean legalMove = false;

    	//for(int i=0;i<moveListSize;i++){
    	if(possibleMoves.contains(x)){
    		legalMove=true;
    		return true;
    	}
    	
   		else{
   			legalMove=false;
   		}
    	//}
		
    	if(legalMove==false){																	
    		gameDataPanel.messageText.append("Illegal move to square " +x +". Reselect origin and destination" +"\n");
    		possibleMoves.clear(); //clear the array so the next selected piece's moves can be stored in it.
    		clickNumber=1;
    		colorSquares(); //removes the highlighting of legal moves by recoloring the squares orange and green 
    		return false;	//You have an illegal move. Repick your origin and destination 
    	}
    	return false;	
    }
*/
/***************!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!**************************************/
    
    
    
    
    
  
    
    
    
    
    private void movePiece(int origin, int destination){
    	chessBoardSquare[destination].setIcon(chessBoardSquare[origin].getIcon());
    	chessBoardSquare[origin].setIcon(null); 	
    	colorSquares();	//remove highlighting after the moving is complete 
    }
    
    
    
    
    private void movePiece(int originOfCaptured, int destinationOfCaptured, int originOfAttacking, int destinationOfAttacking){
		//if((chessBoardSquare[originOfAttacking].getIcon())!=null){
    		if(chessBoardSquare[originOfCaptured].getIcon()==whitePawn || chessBoardSquare[originOfCaptured].getIcon()==whiteKnight || chessBoardSquare[originOfCaptured].getIcon()==whiteBishop || chessBoardSquare[originOfCaptured].getIcon()==whiteRook || chessBoardSquare[originOfCaptured].getIcon()==whiteQueen || chessBoardSquare[originOfCaptured].getIcon()==whiteKing){ 			
    			Image getImg = ((ImageIcon) chessBoardSquare[originOfCaptured].getIcon()).getImage() ;  
    			Image resizeImg = getImg.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
    			ImageIcon resizedIcon = new ImageIcon( resizeImg );
    			gameDataPanel.whiteCapturedButtons[whiteCaptureNumber].setIcon(resizedIcon);
    			whiteCaptureNumber++;
    		}
		
    		else{
    			Image getImg = ((ImageIcon) chessBoardSquare[originOfCaptured].getIcon()).getImage() ;  
    			Image resizeImg = getImg.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
    			ImageIcon resizedIcon = new ImageIcon( resizeImg );
    			gameDataPanel.blackCapturedButtons[blackCaptureNumber].setIcon(resizedIcon);
    			blackCaptureNumber++;
    			System.out.println(blackCaptureNumber);
    		}
		//}
    	
    	chessBoardSquare[destinationOfAttacking].setIcon(chessBoardSquare[originOfAttacking].getIcon());
    	chessBoardSquare[originOfAttacking].setIcon(null); 	
    	colorSquares();	//remove highlighting of legal moves. 
    }
    
    
    //This is what I'll use when Ryan's chess.engine is finished 
    //TODO 
    private ArrayList<Integer> highlightLegalMoves(ArrayList<Integer> possibleMoves){
    	moveListSize=possibleMoves.size();
    
    	for(int x =0; x<moveListSize; x++){
    		chessBoardSquare[possibleMoves.get(x)].setBackground(new Color(242,240,205));
    	}
    	return possibleMoves;
    }
     
    
    /**
     * //Use this function for testing purposes until Ryan finishes his portion. TODO
     * @param possibleMoves 
     * @return
     */
/**    private ArrayList<Integer> highlightLegalMoves(ArrayList<Integer> possibleMoves){
    	/*
    	 * For testing purposes, let's make this possibleMoves random. 
    	 */
    	/*
    	System.out.println(possibleMoves);
    	System.out.println(possibleMoves[0]);
    	System.out.println(possibleMoves[1]);
    	System.out.println(possibleMoves[3]);
    	*/
/**    	int random;
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

    		possibleMoves.add(random2);
    		//System.out.println("possibleMoves square should match random2: " +possibleMoves[x]);    		
    	}
		//System.out.println("I should print out a zero here: " +possibleMoves[random]);
    	
    	
		//System.out.println("I have " +random +" as my size, so I can move to " +random +" different squares");
    	for(int x =0; x<random; x++){
    		chessBoardSquare[possibleMoves.get(x)].setBackground(new Color(242,240,205));
    	}
    	return possibleMoves;
    }
*/
    
    
    
private void updateGameStateOnGUI(GameState GS){//TODO: 
	if(GS.getActiveTeam()==Team.ORANGE){
		currentTeam="white";
        if(clickNumber==1){//Remove this when Ryan's code is complete 
        	gameDataPanel.messageText.append("White's Turn");
        }
    }  
    else{
       	currentTeam="black";
       	if(clickNumber==1){//Remove this  when Ryan's code is complete 
       		gameDataPanel.messageText.append("Black's Turn");
       	}
    }

	
	if(clickNumber==1){
   		if(GS.getPossibleMoveIndexes().size()!=0){
   			/*
    		 * when this ArrayList is not empty, that will be your indication of squares that need to be highlighted.
    		 *  Don't worry about this being on the first click or second click or anything like that.
    		 *   All validation is done in the chess engine. 
    		 *   If it is a click on a valid origin square, this ArrayList will be filled with the indexes you should highlight.
    		 */
    	    		
    		highlightLegalMoves(GS.getPossibleMoveIndexes()); 
    		//make clickable true after picking a destination square
   		}
	}
	
	
	if(clickNumber==2&&checkGame==false){
   		if(GS.getMovePairs().size()!=0){
    	/*
    	 * when this ArrayList is not empty, that will be your indication to move piece images. 
    	 * It can contain either two or four elements. 
    	 * If it contains two elements, element 0 gives the origin index and element 1 gives the destination index. 
    	 * If it contains four elements, elements 0 and 2 give origin indexes and elements 1 and 3 gives their destination indexes. 
    	 * If there is a captured piece, its move will be the 0-1 indexes while the attacking piece will be the 2-3 indexes. 
    	 * That should help keep your code nice and simple if you just move the captured image and then the attacking image.
    	 * By the way, it would be really cool if you had a place somewhere in the GUI where you could visually show the pieces that have been captured.
    	 */
   			if(GS.getMovePairs().size()==2){
   				movePiece(GS.getMovePairs().get(0),GS.getMovePairs().get(1));
   			}
   			
   			if(GS.getMovePairs().size()==4){
   				//Move the captured piece to one of the designated captured areas.
   					//Get the icon at the origin square of the captured piece, and move it to the designated area 
   				//0 and 2 origin
   				//1 and 3 destination 
   				//0-1 captured piece
   				movePiece(GS.getMovePairs().get(0), GS.getMovePairs().get(1), GS.getMovePairs().get(2), GS.getMovePairs().get(3));
   			}
   		}
   	}//End of first clickNumber==2

	
   	if(clickNumber==2&&checkGame==true){
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
   				gameOverPanel.messageText.append("Game Over. White wins!");
   				/*
   				 * Set gameDataPanel invisible and set gameSettingsPanel visible.
   				 * Set the playAgainPanel visible in the gameSettingsPanel.
   				 * Set Click number to 4 so no one can click squares until yes or no is chosen. 
   				 * When no is clicked, application exists (do this in A/L)
   				 * When yes is clicked, remake initial gameState, redraw images on chessboard, set clickNumber to 1. (do this in A/L)
   				 */
   				gameDataPanel.setVisible(false);
   				gameStartPanel.setVisible(false);
   				gameOverPanel.setVisible(true);
   				clickNumber=4;
   				clickable=false;
   			}
   			else{
   				gameOverPanel.messageText.append("Game Over. Black wins!");
   				gameDataPanel.setVisible(false);
   				gameStartPanel.setVisible(false);
   				gameOverPanel.setVisible(true);
   				clickNumber=4;
   				clickable=false;
   			}
   		}
  
   		if(GS.isDraw()==true){
   			gameOverPanel.messageText.append("Game Over. Draw. You guys should play again and settle the score.");
   			//same deal as checkmate
   			gameDataPanel.setVisible(false);
   			gameStartPanel.setVisible(false);
   			gameOverPanel.setVisible(true);
   			clickNumber=4;	
   			clickable = false;
   		}
   	
   		if(GS.getPawnPromotionIndex()>-1){
   			/*
   			 * When this is non negative, it shows which square has a promotable pawn. 
   			 * The promotableSquare variable will be used by the actionlistner.
   			 * Depending on whose turn it is, it'll show the appropriate colored promotion pieces. 
   			 * In the action listener, Promote the pawn to the desired piece, 
   			 * 		return the click number to 1 because whoever's turn it is currently will have their turn end
   			 * 		make promotable false
   			 *		pass the information of which promotion piece clicked on back to RYan's function 
   			 */
   			promotable = true; 
   			clickNumber=clickNumber+3;
   			clickable=false;
   			promotableSquare=GS.getPawnPromotionIndex(); // For now, set it = square 48. Ryan's function will tell me what square has promotable pawn. 
   			
   			if(currentTeam=="white"){
   				gameDataPanel.messageText.append("White team can promote a pawn. Select the piece below you would like to promote");
   				gameDataPanel.queenPromotionButton.setIcon(whiteQueen);
   				gameDataPanel.rookPromotionButton.setIcon(whiteRook);
   				gameDataPanel.bishopPromotionButton.setIcon(whiteBishop);
   				gameDataPanel.knightPromotionButton.setIcon(whiteKnight);
   			}
   			else{
   				gameDataPanel.messageText.append("Black team can promote a pawn. Select the piece below you would like to promote");	
   				gameDataPanel.queenPromotionButton.setIcon(blackQueen);//TODO
   				gameDataPanel.rookPromotionButton.setIcon(blackRook);
   				gameDataPanel.bishopPromotionButton.setIcon(blackBishop);
   				gameDataPanel.knightPromotionButton.setIcon(blackKnight);
   			}
   		}	
   		else{//If my pawn is not promotable, and if I've passed the above checks for game over then I'm done. 
   			clickNumber=1; //I'm done picking an origin piece, highlighting squares, and moving pieces, now let's go back to picking an origin piece 
   		}
   	}//end of 2nd if click==2	
}
	
	


	private void colorSquares() {
		for(int x=0; x<64;x++){
			if((x<8)||(x>15&&x<24)||(x>31&&x<40)||(x>47&&x<56)){
				if(x%2==0){
					chessBoardSquare[x].setBackground(new Color(247,131,0));	//create a special orange (specify values of r,g,b)
				}
				else{
					chessBoardSquare[x].setBackground(new Color(3,73,0));	//create a special green 
				}
			}

			if((x>7&&x<16)||(x>23&&x<32)||(x>39&&x<48)||(x>55&&x<=63)){
				if(x%2==0){
					chessBoardSquare[x].setBackground(new Color(3,73,0));
				}
				else{
					chessBoardSquare[x].setBackground(new Color(247,131,0));
				}
			}      
		}
	
	}
	
	
	
	
    private void addPieceImagesToSquares(){
        for(int x=0; x<64; x++){		
        	chessBoardSquare[x].setIcon(null);
        }
        
    	for(int x=48; x<56; x++){	
    		chessBoardSquare[x].setIcon(whitePawn);
        }
        
        for(int x=8; x<16; x++){	
        	chessBoardSquare[x].setIcon(blackPawn);
        }
        
        chessBoardSquare[0].setIcon(blackRook);
        chessBoardSquare[7].setIcon(blackRook);
        
        chessBoardSquare[56].setIcon(whiteRook);
        chessBoardSquare[63].setIcon(whiteRook);
        
        chessBoardSquare[1].setIcon(blackKnight);
        chessBoardSquare[6].setIcon(blackKnight);
        
        chessBoardSquare[57].setIcon(whiteKnight);
        chessBoardSquare[62].setIcon(whiteKnight);
        
        chessBoardSquare[2].setIcon(blackBishop);
        chessBoardSquare[5].setIcon(blackBishop);
        
        chessBoardSquare[58].setIcon(whiteBishop);
        chessBoardSquare[61].setIcon(whiteBishop);
        
        chessBoardSquare[4].setIcon(blackKing);
        
        chessBoardSquare[60].setIcon(whiteKing);
        
        chessBoardSquare[3].setIcon(blackQueen);
        
        chessBoardSquare[59].setIcon(whiteQueen);
    }
    
    
    
    
    private void addComponent(JPanel panel, JComponent component, int xPosition, int yPosition, int componentWidth, int componentHeight, int xPixels, int yPixels, int stretch){
		  GridBagConstraints gridConstraints = new GridBagConstraints();
		  gridConstraints.gridx = xPosition;
		  gridConstraints.gridy = yPosition;
		  gridConstraints.gridwidth = componentWidth;
		  gridConstraints.gridheight = componentHeight;
		  //gridConstraints.weightx = 50;	
		  //gridConstraints.weighty = 50;
		  gridConstraints.ipadx = xPixels;
		  gridConstraints.ipady = yPixels;
		  gridConstraints.fill = stretch;
		  gridConstraints.insets = new Insets(10, 5, 0, 5);	//top, left, bottom, right spacing
  
		  panel.add(component, gridConstraints);
	}    
    
    
    
    private void addComponent(JPanel panel, JMenuBar menuBar, int xPosition, int yPosition, int componentWidth, int componentHeight, int xPixels, int yPixels, int stretch){
		  GridBagConstraints gridConstraints = new GridBagConstraints();
		  gridConstraints.gridx = xPosition;
		  gridConstraints.gridy = yPosition;
		  gridConstraints.gridwidth = componentWidth;
		  gridConstraints.gridheight = componentHeight;
		  //gridConstraints.weightx = 50;	
		  //gridConstraints.weighty = 50;
		  gridConstraints.ipadx = xPixels;
		  gridConstraints.ipady = yPixels;
		  gridConstraints.fill = stretch;
		  gridConstraints.insets = new Insets(10, 5, 0, 5);	//top, left, bottom, right spacing

		  panel.add(menuBar, gridConstraints);
	}
    
    
    
    private void addMenuItems(){//TODO Don't worry about this function for now. It's just testing some stuff. 
    	menuBar.add(file);
    	menuBar.add(settings);
    	menuBar.add(view);
    	
    	menuItem = new JMenuItem("test1");
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
    
    
    
    
    private void clearButtons(){
    	whiteCaptureNumber=0;
    	blackCaptureNumber=0;
    	for(int i=0;i<16;i++){
    		gameDataPanel.whiteCapturedButtons[i].setIcon(null);
    	}
    	for(int i=0;i<16;i++){
    		gameDataPanel.blackCapturedButtons[i].setIcon(null);
    	}
    }
    
    
    
    
    public static void main(String[] args) {
    	/*
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               new ChessGUI();
            }
        });
        */
        new ChessGUI(); 
    }
}
