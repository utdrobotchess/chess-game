package chess.AI;

//##########################################################################################################################//
//-----------------------------------------This is a Main class of the Chess Engine-----------------------------------------//
//This class has the 2D array representation of the chess board. It represents the inital GUI frame and has the function    //
//definition of Alpha-beta pruning, making and undo-ing the moves , flipping the board and sorting(top 6 ranked moves) and  //
//pruning the search space to those 20 moves which has the highest possibility of taking the players pieces. It also has    //
//functionality to increase or decrease the piece points based on user configuration.                                       //
//##########################################################################################################################//

import chess.AI.Evaluation;
import chess.gui.Chess;
import chess.engine.*;
import chess.AI.GenerateMovements;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;
public class ChessGameAI extends JFrame{
    //2D board representation
    public static String[][] BoardRepn;
    ChessGame chessGame;
    static int curWKingPos, curBKingPos,tempWK,tempBK;
    public static int globalDepth=4;
    static int searchSort=0,branchFactor=0,AggressivePlay=0;
    
 
    public void setGameToAI(ChessGame chessGame){
        this.chessGame = chessGame;
        BoardRepn = this.chessGame.convertToStringArray();
       // for(int i=0 ;i<8 ;i++){
       // System.out.println(Arrays.deepToString(BoardRepn[i]));
   // }
        while (!"A".equals(BoardRepn[curWKingPos/8][curWKingPos%8])) {curWKingPos++;}
         while (!"a".equals(BoardRepn[curBKingPos/8][curBKingPos%8])) {curBKingPos++;}
         tempWK=curWKingPos;
         tempBK=curBKingPos;
    }
    public void updateGameStatus(ChessGame chessGame){
        this.chessGame = chessGame;
    }
    public void updateGameBoard(String[][] board){
        BoardRepn = board;
  
    }
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        //generate the possible moves
        String list=posibleMoves();
        //check for the terminating condition
        if (depth==0 || list.length()==0) {return move+(Evaluation.rating(list.length(), depth)*(player*2-1));}
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