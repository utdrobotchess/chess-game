package AI;

//##########################################################################################################################//
//-----------------------------------------This is GUI part of the Chess Engine---------------------------------------------//
//Class to generate the GUI with chess board and pieces.The class paints the frame black and white and adds the Chess pieces//
//from 16 different chess piece images. It then uses MouseListener to track the piece movements as made by the player.Evalua//
//tes is the mouse movement is valid if valid the makes a movement and then flips the board to call the alpha-beta search to//
//make the computer movement. Then flips the board again for players movement.                                              //
//##########################################################################################################################//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UI extends JPanel implements MouseListener, MouseMotionListener {
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    static int curposX=0, curposY=0, destposX=0,destposY=0;
    static int sq=80;
    static int countOfMoves=0;
    static int king=0,stale=0;
    static String kingStalemove="";
    static int checkCounter=0;
    @Override
    public void paintComponent (Graphics graphics){
        super.paintComponent(graphics);
        this.setBackground(Color.darkGray);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
//black and white grid
        for(int i=0;i<64;i+=2){
            graphics.setColor(Color.WHITE);
            graphics.fillRect((i%8+(i/8)%2)*sq, (i/8)*sq, sq, sq);
            graphics.setColor(Color.BLACK);
            graphics.fillRect(((i+1)%8-((i+1)/8)%2)*sq, ((i+1)/8)*sq, sq, sq);
        }   
//row name
            graphics.drawString("8",650, 40);
            graphics.drawString("7",650, 120);
            graphics.drawString("6",650, 200);
            graphics.drawString("5",650, 280);
            graphics.drawString("4",650, 360);
            graphics.drawString("3",650, 440);
            graphics.drawString("2",650, 520);
            graphics.drawString("1",650, 600);
//column name
            graphics.drawString("A",40, 660);
            graphics.drawString("B",120, 660);
            graphics.drawString("C",200, 660);
            graphics.drawString("D",280, 660);
            graphics.drawString("E",360, 660);
            graphics.drawString("F",440, 660);
            graphics.drawString("G",520, 660);
            graphics.drawString("H",600, 660);
 //setting the chess pawns:
            Image Bking = new ImageIcon("BlackK.png").getImage();
            Image Wking = new ImageIcon("WhiteK.png").getImage();
            Image BBishop = new ImageIcon("BlackB.png").getImage();
            Image WBishop = new ImageIcon("WhiteB.png").getImage();
            Image BKnight = new ImageIcon("BlackN.png").getImage();
            Image WKnight = new ImageIcon("WhiteN.png").getImage();
            Image BPawn = new ImageIcon("BlackP.png").getImage();
            Image WPawn = new ImageIcon("WhiteP.png").getImage();
            Image BQueen = new ImageIcon("BlackQ.png").getImage();
            Image WQueen = new ImageIcon("WhiteQ.png").getImage();
            Image BRook = new ImageIcon("BlackR.png").getImage();
            Image WRook = new ImageIcon("WhiteR.png").getImage();
        for (int i=0;i<64;i++) {
            switch (ChessGameAI.BoardRepn[i/8][i%8]) {
                case "P":graphics.drawImage(WPawn, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "p":graphics.drawImage(BPawn, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "R": graphics.drawImage(WRook, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "r":  graphics.drawImage(BRook, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "K":  graphics.drawImage(WKnight, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "k":  graphics.drawImage(BKnight, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "B":  graphics.drawImage(WBishop, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "b": graphics.drawImage(BBishop, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "Q": graphics.drawImage(WQueen, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "q": graphics.drawImage(BQueen, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "A": graphics.drawImage(Wking, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
                case "a": graphics.drawImage(Bking, (i%8)*sq, (i/8)*sq, (i%8+2)*sq, (i/8+2)*sq, 0, 0, 100, 100, this);
                    break;
            }
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX()<8*sq &&e.getY()<8*sq) {
            //if inside the board
            curposX=e.getX();
            curposY=e.getY();
            repaint();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        String moveTemp;
        if (e.getX()<8*sq &&e.getY()<8*sq) {
            //if the mouse drag destination is inside the board then
            destposX=e.getX();
            destposY=e.getY();
            if (e.getButton()==MouseEvent.BUTTON1) {
                String dragMove;
                if (destposY/sq==0 && curposY/sq==1 && "P".equals(ChessGameAI.BoardRepn[curposY/sq][curposX/sq])) {
                    //if the drag was for pawn then check for promotion criteria
                    dragMove=""+curposX/sq+destposX/sq+ChessGameAI.BoardRepn[destposY/sq][destposX/sq]+"QP";
                } else {
                    //if its for non pawn movements then perform the regular movement
                    dragMove=""+curposY/sq+curposX/sq+destposY/sq+destposX/sq+ChessGameAI.BoardRepn[destposY/sq][destposX/sq];
                }
                String userPosibilities=ChessGameAI.posibleMoves();
                if (userPosibilities.replaceAll(dragMove, "").length()<userPosibilities.length()) {
                    //validate if the player has performed a valid piece movement
                    ChessGameAI.makeMove(dragMove);
                    System.out.println("Your Move: "+moveDecoding(0,dragMove));
                    repaint();//makes the user movement visible on the screen
                    ChessGameAI.flipBoard();
                    //Sets the start time for Computer's movement
                    long startTime=System.currentTimeMillis();
                    moveTemp=ChessGameAI.alphaBeta(ChessGameAI.globalDepth, 1000000, -1000000, "", 0);
                    ChessGameAI.makeMove(moveTemp);
                    long endTime=System.currentTimeMillis();
                    System.out.println("My Move: "+moveDecoding(1,moveTemp.substring(0, 5))+" took: "+(endTime-startTime)+" milliseconds");
                    ChessGameAI.flipBoard();
                    repaint();//makes the computer mmovement visible on the screen
                    countOfMoves++;
                    System.out.println(countOfMoves+" move complete!");
                    if(!ChessGameAI.kingSafe()) king=1;
                    if(king==1){
                    JOptionPane.showMessageDialog(null,"Check!!");
                    checkCounter++;
                    }kingStalemove=GenerateMovements.moveGenForKing(ChessGameAI.curWKingPos);
                    if(kingStalemove.isEmpty()) stale=1;
                    if(stale==1){
                    JOptionPane.showMessageDialog(null,"Stalemate!!Draw Game"); 
                    }
                    if((king==1 && stale==1)){
                    JOptionPane.showMessageDialog(null,"Checkmate! You lose!");
                    }
                    stale=0; king=0;
                }
            }
        }
       
    }
    public String moveDecoding(int player,String moves){
        String moveDecod="",temp="";
        if(player==0){
            switch(moves.charAt(1)){
                case '0': moveDecod+="A";break;
                case '1': moveDecod+="B";break;
                case '2': moveDecod+="C";break;
                case '3': moveDecod+="D";break;
                case '4': moveDecod+="E";break;
                case '5': moveDecod+="F";break;
                case '6': moveDecod+="G";break;
                case '7': moveDecod+="H";break;
            }
            switch(moves.charAt(0)){
                case '0': moveDecod+="8";break;
                case '1': moveDecod+="7";break;
                case '2': moveDecod+="6";break;
                case '3': moveDecod+="5";break;
                case '4': moveDecod+="4";break;
                case '5': moveDecod+="3";break;
                case '6': moveDecod+="2";break;
                case '7': moveDecod+="1";break;
            }
            switch(moves.charAt(3)){
                case '0': moveDecod+="A";break;
                case '1': moveDecod+="B";break;
                case '2': moveDecod+="C";break;
                case '3': moveDecod+="D";break;
                case '4': moveDecod+="E";break;
                case '5': moveDecod+="F";break;
                case '6': moveDecod+="G";break;
                case '7': moveDecod+="H";break;
            }
            switch(moves.charAt(2)){
                case '0': moveDecod+="8";break;
                case '1': moveDecod+="7";break;
                case '2': moveDecod+="6";break;
                case '3': moveDecod+="5";break;
                case '4': moveDecod+="4";break;
                case '5': moveDecod+="3";break;
                case '6': moveDecod+="2";break;
                case '7': moveDecod+="1";break;
            }
//            moveDecod+=moves.charAt(4);
         }
        else
        {
           switch(moves.charAt(1)){
                case '7': moveDecod+="A";break;
                case '6': moveDecod+="B";break;
                case '5': moveDecod+="C";break;
                case '4': moveDecod+="D";break;
                case '3': moveDecod+="E";break;
                case '2': moveDecod+="F";break;
                case '1': moveDecod+="G";break;
                case '0': moveDecod+="H";break;
            }
            switch(moves.charAt(0)){
                case '0': moveDecod+="1";break;
                case '1': moveDecod+="2";break;
                case '2': moveDecod+="3";break;
                case '3': moveDecod+="4";break;
                case '4': moveDecod+="5";break;
                case '5': moveDecod+="6";break;
                case '6': moveDecod+="7";break;
                case '7': moveDecod+="8";break;
            }
            switch(moves.charAt(3)){
                case '7': moveDecod+="A";break;
                case '6': moveDecod+="B";break;
                case '5': moveDecod+="C";break;
                case '4': moveDecod+="D";break;
                case '3': moveDecod+="E";break;
                case '2': moveDecod+="F";break;
                case '1': moveDecod+="G";break;
                case '0': moveDecod+="H";break;
            }
            switch(moves.charAt(2)){
                case '0': moveDecod+="1";break;
                case '1': moveDecod+="2";break;
                case '2': moveDecod+="3";break;
                case '3': moveDecod+="4";break;
                case '4': moveDecod+="5";break;
                case '5': moveDecod+="6";break;
                case '6': moveDecod+="7";break;
                case '7': moveDecod+="8";break;
            }
        }
        return moveDecod;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
   }
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
