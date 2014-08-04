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
	
	JPanel  settingsPanel = new JPanel();
	
	
    
    JLabel playerOrComputerLabel = new JLabel("Are you challenging a player or computer?");
    String[] playerOrComputer = {"Player","Computer"};
    JComboBox playerOrComputerBox = new JComboBox(playerOrComputer);
    JLabel difficultySettingsLabel = new JLabel("If it's a computer, pick a difficulty before starting");
    String[] difficulties = {"Easy", "Normal", "Hard"};
    JComboBox difficultyBox = new JComboBox(difficulties);
    JButton gameStartButton = new JButton("Let's Start");
    
    //JButton returnButton = new JButton("Return to Game");
    
    
    
    
	public gameSettings(){
		//addComponent(panel, component, x, y, width, height, pixelsX, pixelsY, stretch)
			//gridwidth and grid height only specifies how many columns component CAN take up-not number of pixels
		settingsPanel.setLayout(new GridBagLayout());
		//addComponent(settingsPanel,returnButton,                        0,0,                  GridBagConstraints.REMAINDER,1,  0,15,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,playerOrComputerLabel,               0,0,                  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,playerOrComputerBox,  0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,difficultySettingsLabel,   0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,difficultyBox,        0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		addComponent(settingsPanel,gameStartButton,           0,GridBagConstraints.RELATIVE,  GridBagConstraints.REMAINDER,1,  0,0,  GridBagConstraints.BOTH);
		
		/*
		playAgainPanel.setLayout(new GridBagLayout());
		addComponent(playAgainPanel,playAgainLabel,     0,0,    GridBagConstraints.REMAINDER,1,      0,0,    GridBagConstraints.HORIZONTAL);
		addComponent(playAgainPanel,yesButton,     0,1,    GridBagConstraints.RELATIVE,1,      0,0,    GridBagConstraints.HORIZONTAL);
		addComponent(playAgainPanel,noButton,     1,1,    GridBagConstraints.REMAINDER,1,      0,0,    GridBagConstraints.HORIZONTAL);
		*/
		
		this.setLayout(new GridBagLayout());
		addComponent(this,settingsPanel,     0,0,    GridBagConstraints.REMAINDER,1,      0,0,    GridBagConstraints.BOTH);
		//addComponent(this, playAgainPanel,   0,GridBagConstraints.RELATIVE,    GridBagConstraints.REMAINDER,1, 0,0, GridBagConstraints.HORIZONTAL);

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
