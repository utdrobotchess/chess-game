import java.*;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame; 
import javax.swing.JButton; 
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.*;

/*
 * http://stackoverflow.com/questions/19697996/adding-the-same-components-to-multiple-panels
 * http://docs.oracle.com/javase/7/docs/api/javax/swing/JButton.html
 * http://docs.oracle.com/javase/7/docs/api/javax/swing/JLabel.html
 * http://stackoverflow.com/questions/6578205/swing-jlabel-text-change-on-the-running-application
 */

public class Main {

    JFrame frame=new JFrame(); //Frame that holds EVERYTHING in this GUI
    JPanel chessBoardSquares = new JPanel();	//panel that holds chessboard
    JPanel gameData = new JPanel();	//Panel that holds game gameData like captured pieces, list of moves, etc. 
    JPanel parentPanel = new JPanel(); //Holds the gameData and chessBoardSquares
    
    JButton[] square; //names the squares on the chessboard grid
   
    JLabel message = new JLabel("Displayed Messages: ");;	
    JTextArea messageText= new JTextArea("message", 6, 40); //Text area for the messages output by the system. 
    JLabel whiteCaptured = new JLabel("Captured White Pieces:");
    JTextArea whiteCapturedText = new JTextArea("white", 6, 40);
    JLabel blackCaptured = new JLabel("Captured Black Pieces:");
    JTextArea blackCapturedText = new JTextArea("black", 6, 40);
    JLabel moveList = new JLabel("Played Moves:");
    JTextArea moveListText = new JTextArea("Moves", 6, 40);
    
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
    public Main(int rows, int columns){ 
    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
///////////////////////////////Create the panel that holds the 8x8 chessboard layout///////////////////////////////////////////////////////////////////
        	chessBoardSquares.setLayout(new GridLayout(rows,columns)); 
   			square=new JButton[rows*columns]; 
        
        	for(int x=0; x<columns*rows;x++){
        		//String squareString = String.valueOf(squareNumber);
        		square[x]=new JButton(/*squareString*/); 
        		chessBoardSquares.add(square[x]); 
        		//squareNumber++;  
                
        		/////////////////////Color the squares on the chessboard///////////////////
        		
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
            
            ///////////Add the Chess Piece Images to the Buttons /////////////////////
            for(int x=48; x<56; x++){	
            	/*
            	square[48].add(test);
                square[49].add(test);
                					//Does not work. Only the last mentioned item (square[53] in this example) will hold the test JLabel
               	.........
               	square[53].add(test);
                */
            	//^Forget above method. Just use this function
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
            listenForSquareClick squareClick = new listenForSquareClick();	//This Action Listener function is defined near the bottom of code
            for(int x=0;x<64;x++){
            	square[x].addActionListener(squareClick);
            }
            
            
////////////////////////////////////Create the panel that holds the gameData////////////////////////////////////////////////////////////
            gameData.setLayout(new GridBagLayout()); 
        
            //Add the components of the gameData panel as needed. 
            //Use the function addComponent(JPanel,Jcomponent,xPos,yPos,gridwidth,gridheight,anchor,fill) 
            //The function is defined near the bottom of the program for your reference
            addComponent(gameData, message, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
 
            messageText.setLineWrap(true); // If text doesn't fit on a line, jump to the next
            messageText.setWrapStyleWord(true);// Makes sure that words stay intact if a line wrap occurs
            JScrollPane messageTextScroll = new JScrollPane(messageText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Adds scroll bars to the text area -------// Other options: VERTICAL_SCROLLBAR_ALWAYS, VERTICAL_SCROLLBAR_NEVER
            addComponent(gameData, messageTextScroll, 0, 1, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);	//GridBagConstraints.WEST corresponds to anchor, and it means component will be moved to the left side if display size isn't adequate
            																												//GridBagConstraints.HORIZONTAL means the component will be resized to fill horizontal space as frame is resized.
            addComponent(gameData, whiteCaptured, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
          
            whiteCapturedText.setLineWrap(true); 
            whiteCapturedText.setWrapStyleWord(true);
            JScrollPane whiteCapturedTextScroll = new JScrollPane(whiteCapturedText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            addComponent(gameData, whiteCapturedTextScroll, 0, 5, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
            
            addComponent(gameData, blackCaptured, 0, 8, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

            blackCapturedText.setLineWrap(true); 
            blackCapturedText.setWrapStyleWord(true);
            JScrollPane blackCapturedTextScroll = new JScrollPane(blackCapturedText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            addComponent(gameData, blackCapturedTextScroll, 0, 9, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
            
            addComponent(gameData, moveList, 0, 12, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            
            moveListText.setLineWrap(true); 
            moveListText.setWrapStyleWord(true);
            JScrollPane moveListTextScroll = new JScrollPane(moveListText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            addComponent(gameData, moveListTextScroll, 0, 13, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
            
            
////////////////////////////////////////////////////Nest the Panels and finish the framework////////////////////////////////////////////
            parentPanel.setLayout(new GridLayout(1,2));
            parentPanel.add(chessBoardSquares);
            parentPanel.add(gameData);
            
            frame.add(parentPanel);
            frame.pack();
            frame.setVisible(true);     
    }
    
    





	//////////////////////////////////////////////////USED FUNCTIONS, ACTion/Listener////////////////////////////////////////////////////////////    
    private void reColorSquares() {
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
    
    private void addComponent(JPanel panel, JComponent component, int xPosition, int yPosition, int componentWidth, int componentHeight, int place, int stretch){
    	   GridBagConstraints gridConstraints = new GridBagConstraints();
		
    	   gridConstraints.gridx = xPosition;
           gridConstraints.gridy = yPosition;
           gridConstraints.gridwidth = componentWidth;
           gridConstraints.gridheight = componentHeight;
           gridConstraints.weightx = 50;	
           gridConstraints.weighty = 50;
           gridConstraints.anchor = place;
           gridConstraints.fill = stretch;
           gridConstraints.insets = new Insets(0, 5, 0, 0);
           
           gameData.add(component, gridConstraints);
    }
    
    private void movePiece(int origin, int destination){
    	square[destination].setIcon(square[origin].getIcon());
    	square[origin].setIcon(null);
    	
    }
    
    private int[] highlightSquares(int[] possibleMoves){////////////////////////////////////////////////////////////////
    	/*
    	 * For testing purposes, let's make this possibleMoves random. 
    	 */
    	//System.out.println(possibleMoves);
    	//System.out.println(possibleMoves[0]);
    	//System.out.println(possibleMoves[1]);
    	//System.out.println(possibleMoves[3]);
    	int random;
    	Random r = new Random();
    	random = r.nextInt(10);
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
    	//What do I want to happen when a button is clicked on? DONE
    	//Display square number DONE
    	//Display piece (if there's a piece)DONE
    		//If there's a piece, highlight squares and stuff DONE
    	//Move piece from square a to square b DONE
		@Override
		public void actionPerformed(ActionEvent e) {
//////////////////////////////////////////////////////////////ORIGIN SQUARE PICK///////////////////////////////////////////////////////	
			if(clickNumber==1){	/*If clicked square is legal (Find out what determines legal,) this if statement chooses origin square and highlights squares that are possible destinations. Otherwise, it just displays "choose origin sq correctly." */
				for(int x=0;x<64;x++){
					if(e.getSource()==square[x]){
						if(square[x].getIcon()==null){
							messageText.setText("Clicked on square " +x +" Choose proper origin square");
							
							break;	//Break the loop so you can choose an appropriate origin
						}
						
						/*if(the clicked origin square contains enemy piece){
						 *  "Square " +x +" contains ... choose proper origin square"
						 *	break;
						 *	}
						*/
						
						/*if(clicked piece has no legal moves){
						 * "No legal moves. Choose proper origin square"
						 * break;
						 * }
						 */
						
						//If you passed the above if statements, you have a legal origin. Let's highlight the legal destination moves.
						messageText.setText("Clicked on origin square " +x +". Contains: " +square[x].getIcon() +"\n");
						originSquare = x;
						
						//highlight legal moves: Call the highlightMoves function located above the action listener function 
						//For testing purposes, fill the possibleMoves array with random numbers. When Ryan gives you specifications, set a piece's possible moves = possibleMoves array
                  highlightSquares(possibleMoves);
						clickNumber=2;	//Next time I click on a square, it'll be the destination square, and I'll go into the else statement below 
						break;	//You've chosen an appropriate origin, let's break and loop and pick an appropriate destination 
					}
				}
			}
			
///////////////////////////////////////////////DESTINATION SQUARE PICK//////////////////////////////////////////////////////////////////////////
			else{
				for(int x=0;x<64;x++){/*Make sure the destination square is legal before continuing with this else statement*/
					if(e.getSource()==square[x]){
						
                  //System.out.println("Checking to see if possibleMoves returned properly as an array");
                  for(int i=0;i<moveListSize;i++){
	                  //System.out.println(possibleMoves[i]);
                  }
                  //System.out.println("EEEEENNNNDDDD of check!!!!!!!!!!!!!!!!!!!!");
                  boolean legalMove = false;
                  destinationSquare = x;

                  //System.out.println("Checking to see if possibleMoves length returned properly: " +moveListSize);
                  for(int i=0;i<moveListSize;i++){
	                  if(possibleMoves[i]==destinationSquare){
		                  legalMove=true;
		                  break;
	                  }
                  }
						
                  if(legalMove==false){			/*if(illegal move basically if destinationSquare !=one of the integers from the return highlightMoves array...){
						 								* "Illegal Move. Reselect origin and destination squares"
						 								* clickNumber=1;
						 								* break;
						 								* }
						 								*/																			
	                  messageText.append("Illegal move on square " +x +". Reselect origin and destination");
	                  possibleMoves=new int[64];  //clear the array so the next selected piece's moves can be stored in it.
	                  clickNumber=1;
	                  reColorSquares(); //removes the highlighting of legal moves
	                  break;	//You have an illegal move. Break out of the main for loop and repick your origin and destination 
                  }
						
					
					
						//You passed the above checks. You have a legal move.
						messageText.append("Clicked on destination square " +x +". Previously contained: " +square[x].getIcon() +"\n");
						
						//move the chess piece. Call Move function located above the action listener function 
						movePiece(originSquare, destinationSquare);
						
                  possibleMoves=new int[64];	//clear the array so the next selected piece's moves can be stored in it. No you can't do possibleMoves= null (Recall possibleMoves is the address of where the array starts. If you set it = null, you're array basically has a nonexistent address... sort of.  You'll end up getting highlightSquares(null) which will mess up program.
                  //System.out.println("Checking to see if my possibleMoves are cleared: " +possibleMoves);
					
						clickNumber=1;	//reset number of clicks so we can go back to choosing a origin square
						
						reColorSquares();//remove highlighting of legal moves
						
						//Insert function that makes it next player's turn
						break;	//you have a legal destination. You're done. 
					}
				}
			}
			
			
		}//End of ActionPerformed from square click
    	
    }//End of ActionListener
    
    public static void main(String[] args) {
    	/*
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               new Main(8,8);
            }
        });
        */
        new Main(8,8); //the 8,8 is for 8x8 button grid 
    }
}
