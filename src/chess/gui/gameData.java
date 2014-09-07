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
    String moveList = "";
    JPanel p = new JPanel(new GridBagLayout());
    JLabel messageLabel = new JLabel("Displayed Messages: ");	
    JTextArea messageText= new JTextArea(null, 6, 40); //Text area for the messages output by the system. 
	 
    JLabel moveListLabel = new JLabel("Played Moves:");
    JTextArea moveListText = new JTextArea(null, 6, 40);
    String record = "";
    int numberOfWhiteCaptured = 0;
    JPanel whiteCapturedPanel = new JPanel(new GridLayout(2,8));
    JLabel whiteCapturedLabel = new JLabel("Captured White Pieces:");
    JButton whiteCapturedButtons[] = new JButton[16];
	  
    int numberOfBlackCaptured = 0;
    JPanel blackCapturedPanel = new JPanel(new GridLayout(2,8));
    JLabel blackCapturedLabel = new JLabel("Captured Black Pieces:");
    JButton blackCapturedButtons[] = new JButton[16];
	  
    JPanel promotionPanel= new JPanel(new GridLayout(1,4));
    JLabel pawnPromotionLabel = new JLabel("Pawn Promotion: If your pawn is promoted, Click a piece below");
    JButton queenPromotionButton = new JButton();
    JButton rookPromotionButton = new JButton();
    JButton knightPromotionButton = new JButton();
    JButton bishopPromotionButton = new JButton();
	  
    JLabel test = new JLabel("test");
	  
    JButton settingsButton = new JButton("Change Game Settings");
	  
    public gameData(){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        p.add(messageLabel, c);
        c.gridy = 10;          
             
        messageText.setLineWrap(true); // If text doesn't fit on a line, jump to the next
        messageText.setWrapStyleWord(true);
        JScrollPane messageTextScroll = new JScrollPane(messageText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        p.add(messageTextScroll, c);
        c.gridy = 20;
        p.add(moveListLabel, c);
        c.gridy = 30;
        moveListText.setLineWrap(true); 
        moveListText.setWrapStyleWord(true);
        JScrollPane moveListTextScroll = new JScrollPane(moveListText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        p.add(moveListTextScroll, c);
        c.gridy = 40;
        for(int x=0;x<16;x++){
            JLabel addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater = new JLabel();
            whiteCapturedButtons[x]=new JButton();
            whiteCapturedPanel.add(whiteCapturedButtons[x]);
            whiteCapturedButtons[x].setBackground(Color.BLUE);
            whiteCapturedButtons[x].add(addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater);
        } 
        p.add(whiteCapturedLabel, c);
        c.gridy = 50;
        c.ipadx = 50;
        c.ipady = 75;
        p.add(whiteCapturedPanel, c);
        c.gridy = 60;
        c.ipadx = 0;
        c.ipady = 0;
        p.add(blackCapturedLabel, c);
        c.gridy = 70;
        c.ipadx = 50;
        c.ipady = 75;
        for(int x=0;x<16;x++){
            JLabel addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater = new JLabel();
            blackCapturedButtons[x]=new JButton();
            blackCapturedPanel.add(blackCapturedButtons[x]);
	    blackCapturedButtons[x].setBackground(Color.red);
	    blackCapturedButtons[x].add(addThisPlaceHolderJLabelToPreventJButtonFromResizingWhenImagesAreAddedToItLater);
        }
        p.add(blackCapturedPanel, c);
             
       /* c.gridy = 80;
        c.ipadx = 0;
        c.ipady = 0;
        p.add(pawnPromotionLabel, c);
        promotionPanel.add(queenPromotionButton);
        promotionPanel.add(rookPromotionButton);
        promotionPanel.add(knightPromotionButton);
        promotionPanel.add(bishopPromotionButton);
        c.ipadx = 50;
        c.ipady = 30;
        c.gridy = 90;
        p.add(promotionPanel, c);*/
        add(p);
    }
    public void updateMove(String data){
        moveList += data + "\n";
        messageText.setText(moveList);
    }
    public void convert(String Orow, String Ocolumn, String Drow, String Dcolumn){
        record += Orow + Ocolumn + Drow + Dcolumn + "\n";
        moveListText.setText(record);
    }
    public void convertWithCap(String Orow, String Ocolumn, String piece, String Drow, String Dcolumn){
        record += Orow + Ocolumn + Drow + Dcolumn + piece+ "\n";
        moveListText.setText(record);
    }
    public void updateCapturedPiece(ImageIcon image, int team){
        if(team == 0){
            whiteCapturedButtons[numberOfWhiteCaptured++].setIcon(image);
        }
        else
            blackCapturedButtons[numberOfBlackCaptured++].setIcon(image);
    }
    public void clear(){
        for(int i = 0; i < whiteCapturedButtons.length; i++){
            whiteCapturedButtons[i].setIcon(null);
            blackCapturedButtons[i].setIcon(null);
        }
        messageText.setText(null);
        moveListText.setText(null);
    }
}