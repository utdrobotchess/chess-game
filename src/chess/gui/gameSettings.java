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
 * @author Tanaeemul Abuhanif, Ryan J. Marcotte, UTD RobotChessTeam
 *
 *This panel holds pictures for pawn promotion and displays the end game message. 
 */


public class gameSettings extends JPanel{
	JPanel playAgainPanel = new JPanel();
	JPanel  settingsPanel = new JPanel();
	
	JLabel playAgainLabel = new JLabel("Play Again?");
	JButton yesButton = new JButton("Heck Yea! It's not over yet!");
	JButton noButton = new JButton("NO WAY! You're just too powerful...");
    
    JLabel playerOrComputerLabel = new JLabel("Art thou challenging a player or computer?");
    String[] playerOrComputer = {"Player","Computer"};
    JComboBox playerOrComputerBox = new JComboBox(playerOrComputer);
    JLabel difficultySettingsLabel = new JLabel("If it's a computer, pick a difficulty before starting");
    String[] difficulties = {"Walk In The Park", "I'm A Regular Here", "Nine Thousand and One"};
    JComboBox difficultyBox = new JComboBox(difficulties);
    JButton gameStartButton = new JButton("LET's BOOGIE!");
    
    JButton returnButton = new JButton();
    
    
    
    
	public gameSettings(){
		//addComponent(panel, component, x, y, width, height, pixelsX, pixelsY, stretch)
			//gridwidth and grid height only specifies how many columns component CAN take up-not number of pixels
		settingsPanel.setLayout(new GridBagLayout());
		returnButton.setText("Return to Game");
		addComponent(settingsPanel,returnButton,                        0,0,                  GridBagConstraints.REMAINDER,1,  0,15,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,playerOrComputerLabel,               0,0,                  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,playerOrComputerBox,  0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,difficultySettingsLabel,   0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,difficultyBox,        0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,gameStartButton,           0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		
		playAgainPanel.setLayout(new GridBagLayout());
		addComponent(playAgainPanel,playAgainLabel,     0,0,    GridBagConstraints.REMAINDER,1,      0,0,    GridBagConstraints.HORIZONTAL);
		addComponent(playAgainPanel,yesButton,     0,1,    GridBagConstraints.RELATIVE,1,      0,0,    GridBagConstraints.HORIZONTAL);
		addComponent(playAgainPanel,noButton,     1,1,    GridBagConstraints.REMAINDER,1,      0,0,    GridBagConstraints.HORIZONTAL);

		this.setLayout(new GridBagLayout());
		addComponent(this,settingsPanel,     0,0,    GridBagConstraints.REMAINDER,1,      0,0,    GridBagConstraints.BOTH);
		addComponent(this, playAgainPanel,   0,GridBagConstraints.RELATIVE,    GridBagConstraints.REMAINDER,1, 0,0, GridBagConstraints.HORIZONTAL);

		this.setVisible(true);
	}

	/**
	 * To any readers/editors: This is where I envision this going: 
	 * If an AI is developed, The settingsPanel, where we determine AI difficulty, will be used. Otherwise, we'll just leave it blank.
	 * 		If an AI program is developed: After the settings are chosen, the game begins. Afterwards, all the components in the settingsPanel have setVisibility set to false
	
	 * 
	 * Also, terribly sorry about this function down below that has so many arguments... I felt it was necessary to specify all these variables to adequately use GridBagConstraints.
	 * 	What this function basically does: It adds a component(button,label,etc) to a Panel at a specific place at the panel.  
	 * 		Pros: Reusability. I only needed to specify these variables down here in this function so I could reuse for all the components I add to each Panel.
	 * 		Cons: I'm learning this as a go along, so this may not have been the best approach. Some of the components I add to the panels don't even use all these variables.
	 * 			If a component being added to Panel doesn't need a certain variable, I basically pass the default value as the function argument. 
	 * 
	 */
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
