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
 * 
/**
 * @author Tanaeemul Abuhanif, Ryan J. Marcotte, UTD RobotChessTeam
 *
 */
public class gameData extends JPanel{

	  JLabel messageLabel = new JLabel("Displayed Messages: ");	
	  JTextArea messageText= new JTextArea("Massage", 6, 40); //Text area for the messages output by the system. 
	 
	  JLabel moveListLabel = new JLabel("Played Moves:");
	  JTextArea moveListText = new JTextArea("Moves", 6, 40);
	 
	  JPanel whiteCapturedPanel = new JPanel();
	  JLabel whiteCapturedLabel = new JLabel("Captured White Pieces:");
	  //JTextArea whiteCapturedText = new JTextArea("white", 6, 40);
	  JButton whiteCapturedButtons[] = new JButton[16];
	  
	  JPanel blackCapturedPanel = new JPanel();
	  JLabel blackCapturedLabel = new JLabel("Captured Black Pieces:");
	  //JTextArea blackCapturedText = new JTextArea("black", 6, 40);
	  JButton blackCapturedButtons[] = new JButton[16];
	  
	  JPanel promotionPanel= new JPanel();
	  JLabel pawnPromotionLabel = new JLabel("Pawn Promotion: If your pawn is promoted, Click a piece below");
	  JButton queenPromotionButton = new JButton();
	  JButton rookPromotionButton = new JButton();
	  JButton knightPromotionButton = new JButton();
	  JButton bishopPromotionButton = new JButton();
	  
	  JLabel test = new JLabel("test");
	  
	  JButton settingsButton = new JButton("Change Game Settings");
	 
	  //DO NOT do Jpanel gameData = new JPanel(); 
	  //gameDataPanel already inherits JPanel class, so you don't need to do new JPanel. That's why it wasn't showing up when you called it in Main. 
	  
	  public gameData(){
		  
		  
		  this.setLayout(new GridBagLayout()); 
		  //GridBagConstraints GC = new GridBagConstraints();
      
		  //Add the components of the gameDataPanel as needed. 
		  //Use the function addComponent(JPanel,Jcomponent,   xPos,yPos,    gridwidth,gridheight,   ipadx,ipady,    fill) 
		  addComponent(this, messageLabel,       0,0,      1,1,   0,0,   GridBagConstraints.HORIZONTAL);	

		  messageText.setLineWrap(true); // If text doesn't fit on a line, jump to the next
		  messageText.setWrapStyleWord(true);// Makes sure that words stay intact if a line wrap occurs
		  JScrollPane messageTextScroll = new JScrollPane(messageText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Adds scroll bars to the text area -------// Other options: VERTICAL_SCROLLBAR_ALWAYS, VERTICAL_SCROLLBAR_NEVER
		  addComponent(this, messageTextScroll,   0,GridBagConstraints.RELATIVE,   3,3,   0,-30,   GridBagConstraints.BOTH);	//GridBagConstraints.BOTH means the component will be resized to fill horizontal and vertical spaces as frame is resized.
		  
		  
		  addComponent(this, moveListLabel, 0,GridBagConstraints.RELATIVE,   1,1,   0,0,   GridBagConstraints.HORIZONTAL);
	      
		  
		  moveListText.setLineWrap(true); 
		  moveListText.setWrapStyleWord(true);
		  JScrollPane moveListTextScroll = new JScrollPane(moveListText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		  addComponent(this, moveListTextScroll,   0,GridBagConstraints.RELATIVE,   3,3,  0,-30,   GridBagConstraints.BOTH);
		  
		  addComponent(this, whiteCapturedLabel,  0,GridBagConstraints.RELATIVE,   3,1,   0,0,    GridBagConstraints.HORIZONTAL); 	//Specifying 3,1 for gridw and gridh means there will be 3 columns available for components added in relativity to this. IDK what the 1 does
		  whiteCapturedPanel.setLayout(new GridLayout(2,8));
		  for(int x=0;x<16;x++){
			  JLabel addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater = new JLabel();
			  whiteCapturedButtons[x]=new JButton();
			  whiteCapturedPanel.add(whiteCapturedButtons[x]);
			  whiteCapturedButtons[x].setBackground(Color.BLUE);
			  whiteCapturedButtons[x].add(addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater);
		  } 
		  addComponent(this,whiteCapturedPanel,   0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,3,    50,75,    GridBagConstraints.BOTH);
		 

		  addComponent(this, blackCapturedLabel,   0,GridBagConstraints.RELATIVE,   3,1,   0,0,    GridBagConstraints.HORIZONTAL);
		  blackCapturedPanel.setLayout(new GridLayout(2,8));
		  for(int x=0;x<16;x++){
			  JLabel addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater = new JLabel();
			  blackCapturedButtons[x]=new JButton();
			  blackCapturedPanel.add(blackCapturedButtons[x]);
			  blackCapturedButtons[x].setBackground(Color.red);
			  blackCapturedButtons[x].add(addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater);
		  }
		  addComponent(this,blackCapturedPanel,   0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,3,    50,75,    GridBagConstraints.BOTH);
		
		  
		  addComponent(this,pawnPromotionLabel,   0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,    0,0,     GridBagConstraints.NONE);
		  
		  promotionPanel.setLayout(new GridLayout(1,4));
		  preventPromotionButtonResize(queenPromotionButton,rookPromotionButton,knightPromotionButton,bishopPromotionButton);	//When icons are added to these buttons, they auto resize. This funciton prevents that from happening
		  addComponent(promotionPanel, queenPromotionButton,      0,GridBagConstraints.RELATIVE,    							    1,1,                25,50, GridBagConstraints.VERTICAL);
		  addComponent(promotionPanel, rookPromotionButton,       GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE,  	    1,1,                25,50, GridBagConstraints.VERTICAL);
		  addComponent(promotionPanel, knightPromotionButton,     GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE,  	    1,1,                25,50, GridBagConstraints.VERTICAL);
		  addComponent(promotionPanel, bishopPromotionButton,     GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE,  	    1,1,                25,50, GridBagConstraints.VERTICAL);
		  addComponent(this,promotionPanel,                       0,GridBagConstraints.RELATIVE,                     GridBagConstraints.REMAINDER,3,    0,80,     GridBagConstraints.BOTH);
		  
		  addComponent(this,settingsButton,    0,GridBagConstraints.RELATIVE,    GridBagConstraints.REMAINDER,1,    0,15,     GridBagConstraints.BOTH);
		  
		  this.setVisible(true);
	  }
	  
	  
	  
	  
	  //This function makes use of ipadx and ipady for resizing purposes 
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
	  
	  private void preventPromotionButtonResize(JButton promoButton, JButton promoButton2, JButton promoButton3, JButton promoButton4){
		  JLabel addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater = new JLabel();
		  
		  promoButton.add(addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater);
		  promoButton2.add(addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater);
		  promoButton3.add(addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater);
		  promoButton4.add(addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater);

	  }
}