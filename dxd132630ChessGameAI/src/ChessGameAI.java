//##########################################################################################################################//
//-----------------------------------------This is a Main class of the Chess Engine-----------------------------------------//
//This class has the 2D array representation of the chess board. It represents the inital GUI frame and has the function    //
//definition of Alpha-beta pruning, making and undo-ing the moves , flipping the board and sorting(top 6 ranked moves) and  //
//pruning the search space to those 20 moves which has the highest possibility of taking the players pieces. It also has    //
//functionality to increase or decrease the piece points based on user configuration.                                       //
//##########################################################################################################################//

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class ChessGameAI extends JFrame{
    //2D board representation
    static String BoardRepn[][]={
         {"r","k","b","q","a","b","k","r"},
         {"p","p","p","p","p","p","p","p"},
         {" "," "," "," "," "," "," "," "},
         {" "," "," "," "," "," "," "," "},
         {" "," "," "," "," "," "," "," "},
         {" "," "," "," "," "," "," "," "},
         {"P","P","P","P","P","P","P","P"},
         {"R","K","B","Q","A","B","K","R"}};
    static int curWKingPos, curBKingPos,tempWK,tempBK;
    static int globalDepth=4;
    static int searchSort=0,branchFactor=0,AggressivePlay=0;
    
    public static void main(String[] args) {
         while (!"A".equals(BoardRepn[curWKingPos/8][curWKingPos%8])) {curWKingPos++;}
         while (!"a".equals(BoardRepn[curBKingPos/8][curBKingPos%8])) {curBKingPos++;}
         tempWK=curWKingPos;
         tempBK=curBKingPos;
         JFrame f=new JFrame();
         f.setTitle("CHESS 5.4");
         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         UI ui=new UI();
         JMenuBar menuBar = new JMenuBar();
         JMenu menu = new JMenu("Game");
         JMenu menu1 = new JMenu("Engine Configuration");
         JMenuItem item1 = new JMenuItem("Re-set");
         JMenuItem item2 = new JMenuItem("     Engine Level::    ");
            JMenuItem rbMenuItem1 = new JMenuItem("Low Level:Depth 3");
            JMenuItem rbMenuItem2 = new JMenuItem("Medium Level:Depth 4");
            JMenuItem rbMenuItem3 = new JMenuItem("High Level:Depth 6");
         JMenu menu1Item1= new JMenu("Alter Weights");
         JMenuItem menu1Item2=new JMenuItem("Sort Search Space");
         JMenuItem menu1Item3=new JMenuItem("Limit Branch Factor");
         JMenuItem menu1Item4=new JMenuItem("Aggressive Play");
            JMenu label1 = new JMenu ("Pawn Weight:");
               JMenuItem rb11 = new JMenuItem("+50");
               JMenuItem rb12 = new JMenuItem("-50");
            JMenu label2 = new JMenu("Knight Weight:");
                JMenuItem rb21 = new JMenuItem("+50");
                JMenuItem rb22 = new JMenuItem("-50");
            JMenu label3 = new JMenu("Bishop Weight:");
                JMenuItem rb31 = new JMenuItem("+50");
                JMenuItem rb32 = new JMenuItem("-50");
            JMenu label4 = new JMenu("Rook Weight:");
                JMenuItem rb41 = new JMenuItem("+50");
                JMenuItem rb42 = new JMenuItem("-50");
            JMenu label5 = new JMenu("QueenWeight:");
                JMenuItem rb51 = new JMenuItem("+50");
                JMenuItem rb52 = new JMenuItem("-50"); 
         menu1Item1.add(label1);
         label1.add(rb11);
         label1.add(rb12);
         menu1Item1.add(label2);
         label2.add(rb21);
         label2.add(rb22);
         menu1Item1.add(label3);
         label3.add(rb31);
         label3.add(rb32);
         menu1Item1.add(label4);
         label4.add(rb41);
         label4.add(rb42);
         menu1Item1.add(label5);
         label5.add(rb51);
         label5.add(rb52);
         menu1.add(menu1Item1);
         menu1.addSeparator();
         menu1.add(menu1Item2);
         menu1.addSeparator();
         menu1.add(menu1Item3);
         menu1.addSeparator();
         menu1.add(menu1Item4);
         menuBar.add(menu);
         menuBar.add(menu1);
         menu.add(item1);
         menu.addSeparator();
         menu.add(item2);
         menu.add(rbMenuItem1);
         menu.add(rbMenuItem2);
         menu.add(rbMenuItem3);
         f.repaint();
         item1.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {Initialize();}});//resetting the board state to original
         f.repaint();
         rbMenuItem1.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {gd3();}});//changing the globalDepth to 4
         rbMenuItem2.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {gd4();}});//Changing the global depth to 7
         rbMenuItem3.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {gd6();}});//Changing the global depth to 10
         rb11.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {increment("P");}});//Incrementing the Pawn points to current value plus 50
         rb12.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {decrement("P");}});//Decrementing the Pawn points to current value plus 50
         rb21.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {increment("K");}});//Incrementing the Knight points to current value plus 50
         rb22.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {decrement("K");}});//Decrementing the Knight points to current value plus 50
         rb31.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {increment("B");}});//Incrementing the Bishop points to current value plus 50
         rb32.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {decrement("B");}});//Decrementing the Bishop points to current value plus 50
         rb41.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {increment("R");}});//Incrementing the Rook points to current value plus 50
         rb42.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {decrement("R");}});//Decrementing the Rook points to current value plus 50
         rb51.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {increment("Q");}});//Incrementing the Queen points to current value plus 50
         rb52.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {decrement("Q");}});//Decrementing the Queen points to current value plus 50
         menu1Item2.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {searchSortPrune();}});//confining the move results to only those moves which take away the oponents pieces
         menu1Item3.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {limitBranchFactor();}});//Iteratively increasing the search depth between range 4 to 10 if the board is in Mid-game
         menu1Item4.addActionListener(new ActionListener() {@Override
         public void actionPerformed(ActionEvent e) {AggressivePlay();}});
         menu.setVisible(true);
         ImageIcon img = new ImageIcon("C:\\Summer2014\\AI\\Project\\Pawn\\BlackN.png");
         f.setIconImage(img.getImage());
         f.setJMenuBar(menuBar);
         f.add(ui);
         f.setSize(700, 730);
         f.setVisible(true);
         f.repaint();
         f.setLocation(300, 45);
         JOptionPane.showMessageDialog(null,"You get to play first.Please make your move!!");
         //Define the GUI frame parameters
    }
    public static void Initialize(){
        //Initializing the board to original board state
         String BoardRepn1[][]={
         {"r","k","b","q","a","b","k","r"},
         {"p","p","p","p","p","p","p","p"},
         {" "," "," "," "," "," "," "," "},
         {" "," "," "," "," "," "," "," "},
         {" "," "," "," "," "," "," "," "},
         {" "," "," "," "," "," "," "," "},
         {"P","P","P","P","P","P","P","P"},
         {"R","K","B","Q","A","B","K","R"}};
         globalDepth=4;
         BoardRepn=BoardRepn1;
         curWKingPos=tempWK;
         curBKingPos=tempBK;
         UI.countOfMoves=0;
         Evaluation.pawnPoints=100;
         Evaluation.rookPoints=500;
         Evaluation.knightPoints=300;
         Evaluation.bishopPoints=300;
         Evaluation.queenPoints=900;
         Evaluation.kingPoints=1000;
         UI.king=0;
         System.out.println("King pos:"+curWKingPos+" "+curBKingPos);
     }
    public static void depthConfig(){//iteratively increasing the search depth in range of 4 to 10 when the board is in mid-game
        Evaluation.depthConfiguration();
    }
    public static void limitBranchFactor(){
        branchFactor=1;//If pruning is selected then set prune to 1
    }
    public static void searchSortPrune(){
        searchSort=1;
    }
    public static void AggressivePlay(){
        AggressivePlay=1;
    }
    public static void increment(String piece){//increment the piece points based on the piece selected
        switch(piece){
            case "P": Evaluation.pawnPoints+=50;
                break;
            case "B": Evaluation.bishopPoints+=50;
                break;
            case "K": Evaluation.knightPoints+=50;
                break;
            case "R": Evaluation.rookPoints+=50;
                break;
            case "Q": Evaluation.queenPoints+=50;
                break;
        }
        JOptionPane.showMessageDialog(null,"Pawn weight: "+Evaluation.pawnPoints+"\n"+
        "Bishop weight: "+Evaluation.bishopPoints+"\n"+
        "Knight weight: "+Evaluation.knightPoints+"\n"+
        "Rook weight: "+Evaluation.rookPoints+"\n"+
        "Queen weight: "+Evaluation.queenPoints+"\n"
                );
    }
    public static void decrement(String piece){//decrement the piece points based on the piece selected
        switch(piece){
            case "P": Evaluation.pawnPoints-=50;
                break;
            case "B": Evaluation.bishopPoints-=50;
                break;
            case "K": Evaluation.knightPoints-=50;
                break;
            case "R": Evaluation.rookPoints-=50;
                break;
            case "Q": Evaluation.queenPoints-=50;
                break;
        }
        JOptionPane.showMessageDialog(null,"Pawn weight: "+Evaluation.pawnPoints+"\n"+
        "Bishop weight: "+Evaluation.bishopPoints+"\n"+
        "Knight weight: "+Evaluation.knightPoints+"\n"+
        "Rook weight: "+Evaluation.rookPoints+"\n"+
        "Queen weight: "+Evaluation.queenPoints+"\n"
                );
    }
    public static void gd3(){
        globalDepth=3;
    }
    public static void gd4(){
        globalDepth=4;
    }
    public static void gd6(){
        globalDepth=6;
    }
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        //generate the possible moves
        String list=posibleMoves();
        //check for the terminating condition
        if (depth==0 || list.length()==0) {return move+(Evaluation.rating(list.length(), depth)*(player*2-1));}
        //if prune is selected then call the function to limit only those moves which are likely to take away the oponents pawns
//        if(prune==1){
//        String list2=posibleMoves();
//        list=pruneSearchSpace(list2);
//        //list=sortMoves(list2);
//        }//sort the moves based on top rank  
//        else if(searchSort==1)
//       list=sortMoves(list);//if no space pruning then only sort the moves based in the best moves
        if(searchSort==1){
            list=sortMoves(list);}
        else if(branchFactor==1){
            searchSort=1;
           list=sortMoves(list);
        }
        else if(AggressivePlay==1){
           String list1=aggressivePlaySearch(list);
            searchSort=1;branchFactor=1;
           list=sortMoves(list1) ;
        }
        player=1-player;//either 1 or 0
        for (int i=0;i<list.length();i+=5) {//for every move
            makeMove(list.substring(i,i+5));//make the move
            flipBoard();//get the oponents move
            String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
            int value=Integer.valueOf(returnString.substring(5));
            flipBoard();//generate the possible moves of player
            undoMove(list.substring(i,i+5));//undo the previous move
            if (player==0) {//if max
                if (value<=beta) {beta=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}//get the bea value
            } else {
                if (value>alpha) {alpha=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}//get the alpha value
            }
            if (alpha>=beta) {//return the move
                if (player==0) {return move+beta;} else {return move+alpha;}
            }
        }
        if (player==0) {return move+beta;} else {return move+alpha;}
    }
    public static void flipBoard() {//invert the complete board to get the point of view from White side
        String temp;
        for (int i=0;i<32;i++) {
            int r=i/8, c=i%8;
            if (Character.isUpperCase(BoardRepn[r][c].charAt(0))) {
                temp=BoardRepn[r][c].toLowerCase();
            } else {
                temp=BoardRepn[r][c].toUpperCase();
            }
            if (Character.isUpperCase(BoardRepn[7-r][7-c].charAt(0))) {
                BoardRepn[r][c]=BoardRepn[7-r][7-c].toLowerCase();
            } else {
                BoardRepn[r][c]=BoardRepn[7-r][7-c].toUpperCase();
            }
            BoardRepn[7-r][7-c]=temp;
        }
        int kingTemp=curWKingPos;
        curWKingPos=63-curBKingPos;
        curBKingPos=63-kingTemp;
    }
    public static void makeMove(String move) {
        if (move.charAt(4)!='P') {//generate the move if it is pawn promotion the take the source column and destination column and replace old piece with null and new position with Queen
            BoardRepn[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=BoardRepn[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            BoardRepn[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
            if ("A".equals(BoardRepn[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
                curWKingPos=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
            }
        } else {
            //if its a pawn promotion then use the source x,y and destination x,y location and make the move and replace old place with blank
            BoardRepn[1][Character.getNumericValue(move.charAt(0))]=" ";
            BoardRepn[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
        }
    }
    public static void undoMove(String move) {
        if (move.charAt(4)!='P') {//if the previous move was not a pawn promotion then undo the move by replacing the new piece with blank and old location with existing piece
            BoardRepn[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=BoardRepn[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            BoardRepn[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
            if ("A".equals(BoardRepn[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
                curWKingPos=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
            }
        } else {
            //if it is a promotion then promoted piece with blank / replaced piece and old location with Pawn
            BoardRepn[1][Character.getNumericValue(move.charAt(0))]="P";
            BoardRepn[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
        }
    }
    public static String posibleMoves() {
        String move="";
        for (int i=0; i<64; i++) {
            switch (BoardRepn[i/8][i%8]) {
                case "P": move+=GenerateMovements.moveGenForPawn(i);//Generate possible moves for pawn
                    break;
                case "R": move+=GenerateMovements.moveGenForRook(i,"R");//Generate possible moves for Rook
                    break;
                case "K": move+=GenerateMovements.moveGenForKnight(i);//Generate possible moves for Knight
                    break;
                case "B": move+=GenerateMovements.moveGenForBishop(i,"B");//Generate possible moves for Bishop
                    break;
                case "Q": move+=GenerateMovements.moveGenForRook(i,"Q")+GenerateMovements.moveGenForBishop(i,"Q");//Generate possible moves like rook and bishop
                    break;
                case "A": move+=GenerateMovements.moveGenForKing(i);//Generate possible moves for knig
                    break;
            }
        }
        return move;//source x,y co-ordinates,destination x,y co-ordinates,piece taken
    }
    public static String sortMoves(String move) {
        int[] score=new int [move.length()/5];
        for (int i=0;i<move.length();i+=5) {//get the ranking of the possible moves
            makeMove(move.substring(i, i+5));
            score[i/5]=-Evaluation.rating(-1, 0);
            undoMove(move.substring(i, i+5));
        }
        String newListA="", newListB=move;
        for (int i=0;i<Math.min(8, move.length()/5);i++) {//get top 6 moves which are likely to have top rating
            int max=-1000000, maxLocation=0;
            for (int j=0;j<move.length()/5;j++) {
                if (score[j]>max) {max=score[j]; maxLocation=j;}
            }
            score[maxLocation]=-1000000;//set the score of the selected move with low negative value
            newListA+=move.substring(maxLocation*5,maxLocation*5+5);//append the best ranked move to newListA
            newListB=newListB.replace(move.substring(maxLocation*5,maxLocation*5+5), "");//remove the best move from old list
        }
        if(searchSort==1 && branchFactor==1){
        String temp=newListA+newListB;
//        System.out.println("Sort with Branching factor:"+temp.substring(0,Math.min(40,temp.length())));
        return temp.substring(0,Math.min(40,temp.length()));
        }
        
        return newListA+newListB;//append best list to old list
    }
    public static String aggressivePlaySearch(String move){
        String newListA="",newListB=move,temp;
//        if(UI.countOfMoves>5){//if the board is in the mid-game
            for(int i=0;i<Math.min(8, move.length()/5);i++){//select top 20 moves which are likely to take oponent pieces
                for (int j=0;j<move.length()/5;j++)
                if(Character.isLowerCase(move.substring(j*5,j*5+5).charAt(4)))
                {
                    newListA+=move.substring(j*5,j*5+5);
                    newListB=newListB.replace(move.substring(j*5,j*5+5), "");
                }
                
            }//return the new move
            temp=newListA+newListB;
//            System.out.println("AggressivePlay Moves:"+temp.substring(0,Math.min(40,temp.length())));
            return temp.substring(0,Math.min(40,temp.length()));

    }
    public static boolean kingSafe() {
        //Is the king getting exposed with current White King position in any diagonal position by the Black Bishop or Queen
        int temp=1;
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    while(" ".equals(BoardRepn[curWKingPos/8+temp*i][curWKingPos%8+temp*j])) {temp++;}
                    if ("b".equals(BoardRepn[curWKingPos/8+temp*i][curWKingPos%8+temp*j]) ||
                            "q".equals(BoardRepn[curWKingPos/8+temp*i][curWKingPos%8+temp*j])) {
                        return false;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        //Is the king getting exposed with current White King position in any Horizontal or Vertical position by the Black Bishop or Queen
        for (int i=-1; i<=1; i+=2) {
            try {
                while(" ".equals(BoardRepn[curWKingPos/8][curWKingPos%8+temp*i])) {temp++;}
                if ("r".equals(BoardRepn[curWKingPos/8][curWKingPos%8+temp*i]) ||
                        "q".equals(BoardRepn[curWKingPos/8][curWKingPos%8+temp*i])) {
                    return false;
                }
            } catch (Exception e) {}
            temp=1;
            try {
                while(" ".equals(BoardRepn[curWKingPos/8+temp*i][curWKingPos%8])) {temp++;}
                if ("r".equals(BoardRepn[curWKingPos/8+temp*i][curWKingPos%8]) ||
                        "q".equals(BoardRepn[curWKingPos/8+temp*i][curWKingPos%8])) {
                    return false;
                }
            } catch (Exception e) {}
            temp=1;
        }
        //Is the king getting exposed with current White King position in any "L" shape position
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    if ("k".equals(BoardRepn[curWKingPos/8+i][curWKingPos%8+j*2])) {
                        return false;
                    }
                } catch (Exception e) {}
                try {
                    if ("k".equals(BoardRepn[curWKingPos/8+i*2][curWKingPos%8+j])) {
                        return false;
                    }
                } catch (Exception e) {}
            }
        }
        //Is the king getting exposed with current White King position in any diagonal right up or left up position
        if (curWKingPos>=16) {
            try {
                if ("p".equals(BoardRepn[curWKingPos/8-1][curWKingPos%8-1])) {
                    return false;
                }
            } catch (Exception e) {}
            try {
                if ("p".equals(BoardRepn[curWKingPos/8-1][curWKingPos%8+1])) {
                    return false;
                }
            } catch (Exception e) {}
            //Is there a Black king in the adjoining squares
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if (i!=0 || j!=0) {
                        try {
                            if ("a".equals(BoardRepn[curWKingPos/8+i][curWKingPos%8+j])) {
                                return false;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        //When none of the above conditions are true return king is safe
        return true;
    }   
}