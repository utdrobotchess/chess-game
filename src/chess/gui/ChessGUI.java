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



/**
 * @author Tanaeemul Abuhanif, Ryan J. Marcotte, UTD RobotChessTeam
 * 
 * To anyone reading/editing this code: Action/Listeners and used functions are defined at the bottom of this code. 
 * Code used to create gameDataPanel is really long, so it's created in its own separate class called gameData.  
 */

public class ChessGUI {

    JFrame frame=new JFrame(); //Frame that holds EVERYTHING in this GUI
    JPanel chessBoardPanel = new JPanel();	
    gameData gameDataPanel = new gameData();    
    JPanel parentPanel = new JPanel(); //Holds the gameData and chessBoardSquares
    JButton[] square; 
    
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
   
    
    ImageIcon whitePawn= new ImageIcon(getClass().getResource("wp.png"), "white pawn");//ImageIcon(String path, String description)  
    ImageIcon blackPawn= new ImageIcon(getClass().getResource("bp.png"), "black pawn");//The description will be used when we do square[x].getImageIcon().
    ImageIcon whiteKing= new ImageIcon(getClass().getResource("wk.png"), "white king");//If no description is available, then the path name, as a String, will be used. 
    ImageIcon blackKing= new ImageIcon(getClass().getResource("bk.png"), "black king");
    ImageIcon whiteQueen= new ImageIcon(getClass().getResource("wq.png"), "white queen");
    ImageIcon blackQueen= new ImageIcon(getClass().getResource("bq.png"), "black queen");
    ImageIcon whiteRook= new ImageIcon(getClass().getResource("wr.png"), "white rook");
    ImageIcon blackRook= new ImageIcon(getClass().getResource("br.png"), "black rook");
    ImageIcon whiteKnight= new ImageIcon(getClass().getResource("wk.png"), "white knight");
    ImageIcon blackKnight= new ImageIcon(getClass().getResource("bk.png"), "black knight");
    ImageIcon whiteBishop= new ImageIcon(getClass().getResource("wb.png"), "white bishop");
    ImageIcon blackBishop= new ImageIcon(getClass().getResource("bb.png"), "black bishop");
        
    /*int squareNumber=0; Used for testing purposes only in the section where the chessboard square buttons are created*/
    int originSquare;	//which square a chesspiece starts off 
    int destinationSquare;	//which square I want my chesspiece to move to
    int clickNumber=1; //This will be used for the action/listener to determine if I'm dealing with origin or destination
    int[] possibleMoves= new int[64];//This array is empty right now. This array will be filled with a piece's possible moves
    int moveListSize;
    public ChessGUI(int rows, int columns){ 
    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
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
            
            //////////////////////Add the Action Listeners for the Squares///////////////////////
            listenForSquareClick squareClick = new listenForSquareClick();	
            for(int x=0;x<64;x++){
            	square[x].addActionListener(squareClick);
            }
            
            
            ////////////////////////////////////////Nest the Panels and finish the framework///////////////////////////
            parentPanel.setLayout(new GridLayout(1,2));
            parentPanel.add(chessBoardPanel);
            
            parentPanel.add(gameDataPanel);
            parentPanel.setVisible(true);
            frame.add(parentPanel);
            frame.pack();
            frame.setVisible(true);     
    }
    
    





	//////////////////////////////////////////////////USED FUNCTIONS, ACTion/Listener////////////////////////////////////////////////////////////    
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
    	square[destination].setIcon(square[origin].getIcon());
    	square[origin].setIcon(null); 	
    }
    
    
    
    
    private int[] highlightLegalMoves(int[] possibleMoves){////////////////////////////////////////////////////////////////
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
    
    
    
    
    private class listenForSquareClick implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//////////////////////////////////ORIGIN SQUARE PICK///////////////////////////////////	
			if(clickNumber==1){	/*If clicked square is legal (Find out what determines legal,) this if statement chooses origin square and highlights squares that are possible destinations. Otherwise, it just displays "choose origin sq correctly." */
				for(int x=0;x<64;x++){
					if(e.getSource()==square[x]){
						boolean originSquareVerified = verifyOriginSquare(x); //true means legal origin picked 
					
						//If above function returns true, you have a legal origin. 
						if(originSquareVerified==true){
							gameDataPanel.messageText.setText("Clicked on origin square " +x +". Contains: " +square[x].getIcon() +"\n");
							originSquare = x;
						
							highlightLegalMoves(possibleMoves);
							clickNumber=2;	//Next time I click on a square, it'll be the destination square, and I'll go into the else statement below 
							break;	//You've chosen an appropriate origin, let's break and loop and pick an appropriate destination 
						}
					}
				}
			}//end of if statement
			
			///////////////////////////////////DESTINATION SQUARE PICK////////////////////////////////////////////
			else{		
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
							gameDataPanel.messageText.append("Clicked on destination square " +x +". Previously contained: " +square[x].getIcon() +"\n");
							movePiece(originSquare, destinationSquare);
							possibleMoves=new int[64];	//clear the array so the next selected piece's moves can be stored in it. No you can't do possibleMoves= null (Recall possibleMoves is the address of where the array starts. If you set it = null, you're array basically has a nonexistent address... sort of.  You'll end up getting highlightSquares(null) which will mess up program.
							//System.out.println("Checking to see if my possibleMoves are cleared: " +possibleMoves);
							clickNumber=1;	//reset number of clicks so we can go back to choosing a origin square
						
							colorSquares();//remove highlighting of legal moves by recoloring squares to orange and green 
						
							//Insert function that makes it next player's turn
							break;	//you have a legal destination. You're done. 
						}
					}
				}
			}//End of else statement
			
		}//End of ActionPerformed from square click
    }//End of ActionListener
   
    
    
   
    private boolean verifyOriginSquare(int x){
    	if(square[x].getIcon()==null){
			gameDataPanel.messageText.setText("Clicked on empty square " +x +" Choose proper origin square");	//Break the loop so you can click an appropriate origin
			return false;
		}
		
		/*if(the clicked origin square contains enemy piece){
		 *  "Square " +x +" contains ... choose proper origin square"
		 *	originSquareVerified=false
		 *	return originSquareVerified;
		 *	}
		*/
		
		/*if(clicked piece has no legal moves){
		 * "No legal moves. Choose proper origin square"
		 * originSquareVerified = false;
		 * return originSquareVerified;
		 * }
		 */
    		return true;	//If above if statements are cleared, returns true
    }
    
    
    
    
    private boolean verifyDestinationSquare(int x, int moveListSize){
    	boolean legalMove = false;
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
			gameDataPanel.messageText.append("Illegal move to square " +x +". Reselect origin and destination");
			possibleMoves=new int[64]; //clear the array so the next selected piece's moves can be stored in it.
			clickNumber=1;
			colorSquares(); //removes the highlighting of legal moves by recoloring the squares orange and green 
			return false;	//You have an illegal move. Repick your origin and destination 
		}
    	return false;	
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
