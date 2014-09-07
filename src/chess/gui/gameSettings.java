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
    
	public gameSettings(){
            //addComponent(panel, component, x, y, width, height, pixelsX, pixelsY, stretch)
                    //gridwidth and grid height only specifies how many columns component CAN take up-not number of pixels
            settingsPanel.setLayout(new GridLayout(5,1,0,5));
            settingsPanel.add(playerOrComputerLabel);
            settingsPanel.add(playerOrComputerBox);
            settingsPanel.add(difficultySettingsLabel);
            settingsPanel.add(difficultyBox);
            settingsPanel.add(gameStartButton);
            add(settingsPanel);
		
	}
}
