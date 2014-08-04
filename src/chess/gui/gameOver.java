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

public class gameOver extends JPanel{	
	JLabel playAgainLabel = new JLabel("Play Again?");
	JButton yesButton = new JButton("YES, Let's Play Once More.");
	JButton noButton = new JButton("NO, That's enough...");
	
	JLabel messageLabel = new JLabel("Game Over Message");
	JTextArea messageText = new JTextArea(null,3,40);
	
	JLabel moveListLabel = new JLabel("Played Moves:");
	JTextArea moveListText = new JTextArea(null, 6, 40);
	
	public gameOver(){
		this.setLayout(new GridBagLayout());
		addComponent(this,messageLabel,                  0,0,                   GridBagConstraints.REMAINDER,1,     0,0,    GridBagConstraints.HORIZONTAL);
		
		messageText.setLineWrap(true);
		messageText.setWrapStyleWord(true);
		JScrollPane messageTextScroll = new JScrollPane(messageText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		addComponent(this,messageTextScroll,  0,GridBagConstraints.RELATIVE,    GridBagConstraints.REMAINDER,1,     0,0,    GridBagConstraints.HORIZONTAL);
		
		addComponent(this,moveListLabel,      0,GridBagConstraints.RELATIVE,    GridBagConstraints.REMAINDER,1,     0,0,    GridBagConstraints.HORIZONTAL);
		moveListText.setLineWrap(true); 
		moveListText.setWrapStyleWord(true);
		JScrollPane moveListTextScroll = new JScrollPane(moveListText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		addComponent(this,moveListTextScroll, 0,GridBagConstraints.RELATIVE,    GridBagConstraints.REMAINDER,1,     0,0,   GridBagConstraints.HORIZONTAL);
		
		addComponent(this,playAgainLabel,     0,GridBagConstraints.RELATIVE,                              GridBagConstraints.REMAINDER,1,                                0,0,    GridBagConstraints.HORIZONTAL);
		addComponent(this,yesButton,          0,GridBagConstraints.RELATIVE,                              GridBagConstraints.RELATIVE,GridBagConstraints.REMAINDER,      0,0,    GridBagConstraints.BOTH);
		addComponent(this,noButton,           GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE,    GridBagConstraints.REMAINDER,GridBagConstraints.REMAINDER,     0,0,    GridBagConstraints.BOTH);
	
	}
	
	//For more information on the addComponent function, see my notes in gameData.java
	private void addComponent(JPanel panel, JComponent component, int xPosition, int yPosition, int componentWidth, int componentHeight, int pixelsX, int pixelsY, int stretch){
		GridBagConstraints gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = xPosition;
		gridConstraints.gridy = yPosition;
		gridConstraints.gridwidth = componentWidth;
		gridConstraints.gridheight = componentHeight;
		gridConstraints.ipadx = pixelsX;
		gridConstraints.ipady = pixelsY;	//this adds extra pixels to component to make it taller 
		gridConstraints.fill = stretch;
		gridConstraints.insets = new Insets(5, 5, 0, 5);	//top, left, bottom, right spacing
   
		panel.add(component, gridConstraints);
	}
}

	
