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
	  JTextArea messageText= new JTextArea("message", 6, 40); //Text area for the messages output by the system. 
	  JLabel whiteCapturedLabel = new JLabel("Captured White Pieces:");
	  JTextArea whiteCapturedText = new JTextArea("white", 6, 40);
	  JLabel blackCapturedLabel = new JLabel("Captured Black Pieces:");
	  JTextArea blackCapturedText = new JTextArea("black", 6, 40);
	  JLabel moveListLabel = new JLabel("Played Moves:");
	  JTextArea moveListText = new JTextArea("Moves", 6, 40);
	  //DO NOT do Jpanel gameData = new JPanel(); 
	  //gameDataPanel already inherits JPanel class, so you don't need to do new JPanel. That's why it wasn't showing up when you called it in Main. 
	  
	  public gameData(){
		  
		  
		  this.setLayout(new GridBagLayout()); 
      
		  //Add the components of the gameDataPanel as needed. 
		  //Use the function addComponent(JPanel,Jcomponent,xPos,yPos,gridwidth,gridheight,anchor,fill) 
		  addComponent(this, messageLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		  messageText.setLineWrap(true); // If text doesn't fit on a line, jump to the next
		  messageText.setWrapStyleWord(true);// Makes sure that words stay intact if a line wrap occurs
		  JScrollPane messageTextScroll = new JScrollPane(messageText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Adds scroll bars to the text area -------// Other options: VERTICAL_SCROLLBAR_ALWAYS, VERTICAL_SCROLLBAR_NEVER
		  addComponent(this, messageTextScroll, 0, 1, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);	//GridBagConstraints.WEST corresponds to anchor, and it means component will be moved to the left side if display size isn't adequate
      																												//GridBagConstraints.HORIZONTAL means the component will be resized to fill horizontal space as frame is resized.
		  addComponent(this, whiteCapturedLabel, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
    
		  whiteCapturedText.setLineWrap(true); 
		  whiteCapturedText.setWrapStyleWord(true);
		  JScrollPane whiteCapturedTextScroll = new JScrollPane(whiteCapturedText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		  addComponent(this, whiteCapturedTextScroll, 0, 5, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
      
		  addComponent(this, blackCapturedLabel, 0, 8, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		  blackCapturedText.setLineWrap(true); 
		  blackCapturedText.setWrapStyleWord(true);
		  JScrollPane blackCapturedTextScroll = new JScrollPane(blackCapturedText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		  addComponent(this, blackCapturedTextScroll, 0, 9, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
      
		  addComponent(this, moveListLabel, 0, 12, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
      
		  moveListText.setLineWrap(true); 
		  moveListText.setWrapStyleWord(true);
		  JScrollPane moveListTextScroll = new JScrollPane(moveListText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		  addComponent(this, moveListTextScroll, 0, 13, 3, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
		  this.setVisible(true);
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
    
		  this.add(component, gridConstraints);
	  }    
}