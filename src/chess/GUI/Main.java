import javax.swing.JComponent;
import javax.swing.JFrame; 
import javax.swing.JButton; 
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout; 
import java.awt.Toolkit;
import java.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.*;

public class Main {

    JFrame frame=new JFrame(); //Frame that holds EVERYTHING in this GUI
    JPanel chessBoardSquares = new JPanel();	//panel that holds chessboard
    JPanel gameData = new JPanel();	//Panel that holds game gameData like captured pieces, list of moves, etc. 
    JPanel parentPanel = new JPanel(); //Holds the gameData and chessBoardSquares
    
    JButton[] board; //names the squares on the chessboard grid
    JLabel message = new JLabel("Displayed Messages: ");;	
    JTextArea messageText= new JTextArea("message", 6, 40); //Text area for the messages output by the system. 
    JLabel whiteCaptured = new JLabel("Captured White Pieces:");
    JTextArea whiteCapturedText = new JTextArea("white", 6, 40);
    JLabel blackCaptured = new JLabel("Captured Black Pieces:");
    JTextArea blackCapturedText = new JTextArea("black", 6, 40);
    JLabel moveList = new JLabel("Played Moves:");
    JTextArea moveListText = new JTextArea("Moves", 6, 40);
    
    
    

    public Main(int rows, int columns){ 
    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//create the main frame that holds everything
      
    		
    		//Create the panel that holds the 8x8 chessboard layout
            chessBoardSquares.setLayout(new GridLayout(rows,columns)); 
    		board=new JButton[rows*columns]; //algorithm to add the 8x8 chessboard as buttons
            int square =0;
            for(int y=0; y<columns*rows;y++){
                            String squareString = String.valueOf(square);
                            board[y]=new JButton(squareString); //makes newbutton. Can be used for repainting purposes  
                            chessBoardSquares.add(board[y]); //adds button to grid
                            square++;    
            }
        		
          
            //Create the panel that holds the gameData
            gameData.setLayout(new GridBagLayout()); 
        
            //Add the components of the gameData panel as needed. 
            //Use the function addComponent(JPanel,Jcomponent,xPos,yPos,gridwidth,gridheight,anchor,fill) 
            //The function is defined below for your reference
            addComponent(gameData, message, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
 
            messageText.setLineWrap(true); // If text doesn't fit on a line, jump to the next
            messageText.setWrapStyleWord(true);// Makes sure that words stay intact if a line wrap occurs
            JScrollPane messageTextScroll = new JScrollPane(messageText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Adds scroll bars to the text area -------// Other options: VERTICAL_SCROLLBAR_ALWAYS, VERTICAL_SCROLLBAR_NEVER
            addComponent(gameData, messageTextScroll, 0, 1, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);	//GridBagConstraints.WEST corresponds to anchor, and it means component will be moved to the left side if display size isn't adequate
            																												//GridBagConstraints.HORIZONTAL means the component will be resized to fill horizontal space as frame is resized.
            addComponent(gameData, whiteCaptured, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
          
            whiteCapturedText.setLineWrap(true); // If text doesn't fit on a line, jump to the next
            whiteCapturedText.setWrapStyleWord(true);// Makes sure that words stay intact if a line wrap occurs
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
            
            parentPanel.setLayout(new GridLayout(1,2));
            parentPanel.add(chessBoardSquares);
            parentPanel.add(gameData);
            
            frame.add(parentPanel);
            frame.pack();
            frame.setVisible(true);
       
        
             
            
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
           gridConstraints.insets = new Insets(0, 5, 5, 0);
           
           gameData.add(component, gridConstraints);
    }
    
    
    public static void main(String[] args) {
            new Main(8,8);//makes new ButtonGrid with 2 parameters
    }
}
