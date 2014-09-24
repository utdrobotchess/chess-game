package chess.AI;

//##########################################################################################################################//

import chess.AI.ChessGameAI;

//------------------------------------This is Move ganeration part of the Chess Engine--------------------------------------//
//This class has the instruction to generate valid piece movements for each chess piece.                                    //
//##########################################################################################################################//
public class GenerateMovements{
    public static String moveGenForPawn(int i) {
        String movements="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=-1; j<=1; j+=2) {//if the Pawn is not in the last but one row and there is a Black Pawn in the diagonally adjacent square then enter if 
            try {
                if (Character.isLowerCase(ChessGameAI.BoardRepn[r-1][c+j].charAt(0)) && i>=16) {
                    oldPiece=ChessGameAI.BoardRepn[r-1][c+j];//back up the diagonally adjacent square pawn
                    ChessGameAI.BoardRepn[r][c]=" ";//assign the existing pawn position with null
                    ChessGameAI.BoardRepn[r-1][c+j]="P";//Make diagonally adjacent square as WP
                    if (ChessGameAI.kingSafe()) {
                        movements=movements+r+c+(r-1)+(c+j)+oldPiece; //add movement path to the list
                    }
                    ChessGameAI.BoardRepn[r][c]="P";//revert the changes
                    ChessGameAI.BoardRepn[r-1][c+j]=oldPiece;
                }
            } catch (Exception e) {}
            try {//if the Pawn is in the last but one row and there is a Black Pawn in the diagonally adjacent square then enter if 
                if (Character.isLowerCase(ChessGameAI.BoardRepn[r-1][c+j].charAt(0)) && i<16) {
                    String[] temp={"Q","R","B","K"};
                    for (int k=0; k<4; k++) {
                        oldPiece=ChessGameAI.BoardRepn[r-1][c+j];
                        ChessGameAI.BoardRepn[r][c]=" ";
                        ChessGameAI.BoardRepn[r-1][c+j]=temp[k];
                        if (ChessGameAI.kingSafe()) {
                            //As the coulmn is already known we have to pass only the column of source and destination and captured piece and new piece and "P" to indicate pawn promotion
                            movements=movements+c+(c+j)+oldPiece+temp[k]+"P";
                        }
                        ChessGameAI.BoardRepn[r][c]="P";
                        ChessGameAI.BoardRepn[r-1][c+j]=oldPiece;
                    }
                }
            } catch (Exception e) {}
        }
        try {//if blank square in the top
            if (" ".equals(ChessGameAI.BoardRepn[r-1][c]) && i>=16) {
                oldPiece=ChessGameAI.BoardRepn[r-1][c];
                ChessGameAI.BoardRepn[r][c]=" ";
                ChessGameAI.BoardRepn[r-1][c]="P";
                if (ChessGameAI.kingSafe()) {
                    movements=movements+r+c+(r-1)+c+oldPiece;
                }
                ChessGameAI.BoardRepn[r][c]="P";
                ChessGameAI.BoardRepn[r-1][c]=oldPiece;
            }
        } catch (Exception e) {}
        try {//if blank square in the topmost two rows and scope for pawn promotion
            if (" ".equals(ChessGameAI.BoardRepn[r-1][c]) && i<16) {
                String[] temp={"Q","R","B","K"};
                for (int k=0; k<4; k++) {
                    oldPiece=ChessGameAI.BoardRepn[r-1][c];
                    ChessGameAI.BoardRepn[r][c]=" ";
                    ChessGameAI.BoardRepn[r-1][c]=temp[k];
                    if (ChessGameAI.kingSafe()) {
                        movements=movements+c+c+oldPiece+temp[k]+"P";
                    }
                    ChessGameAI.BoardRepn[r][c]="P";
                    ChessGameAI.BoardRepn[r-1][c]=oldPiece;
                }
            }
        } catch (Exception e) {}
        try {//jumping 2 positions in the begining
            if (" ".equals(ChessGameAI.BoardRepn[r-1][c]) && " ".equals(ChessGameAI.BoardRepn[r-2][c]) && i>=48) {
                oldPiece=ChessGameAI.BoardRepn[r-2][c];
                ChessGameAI.BoardRepn[r][c]=" ";
                ChessGameAI.BoardRepn[r-2][c]="P";
                if (ChessGameAI.kingSafe()) {
                    movements=movements+r+c+(r-2)+c+oldPiece;
                }
                ChessGameAI.BoardRepn[r][c]="P";
                ChessGameAI.BoardRepn[r-2][c]=oldPiece;
            }
        } catch (Exception e) {}
        return movements;
    }
    public static String moveGenForRook(int i,String piece) {
        //This function generates movements for both Rook and Queen Horizontally and Vertically
        String movement="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j++) {
            for (int k=-1; k<=1; k++) {
                if(j==0 || k==0){//make horizontal movements/ vertical movements
                    try {
                         while(" ".equals(ChessGameAI.BoardRepn[r+temp*j][c+temp*k])){//until the path has blank squares
                            oldPiece=ChessGameAI.BoardRepn[r+temp*j][c+temp*k];
                            ChessGameAI.BoardRepn[r][c]=" ";
                            ChessGameAI.BoardRepn[r+temp*j][c+temp*k]=piece;
                            if (ChessGameAI.kingSafe()) {
                                movement=movement+r+c+(r+temp*j)+(c+temp*k)+oldPiece;//If safe account the movement to list
                            }
                            ChessGameAI.BoardRepn[r][c]=piece;//Revert the changes
                            ChessGameAI.BoardRepn[r+temp*j][c+temp*k]=oldPiece;
                            temp++;
                         }
                        if (Character.isLowerCase(ChessGameAI.BoardRepn[r+temp*j][c+temp*k].charAt(0))) {//if the path is non blank and has a Black Pawn
                            oldPiece=ChessGameAI.BoardRepn[r+temp*j][c+temp*k];//Backup the old piece
                            ChessGameAI.BoardRepn[r][c]=" ";//make  esiting Rook position blank
                            ChessGameAI.BoardRepn[r+temp*j][c+temp*k]=piece;//Step forward
                            if (ChessGameAI.kingSafe()) {//Check kings safety
                                movement=movement+r+c+(r+temp*j)+(c+temp*k)+oldPiece;//If safe accout the movement to list
                            }
                            ChessGameAI.BoardRepn[r][c]=piece;//Revert the changes
                            ChessGameAI.BoardRepn[r+temp*j][c+temp*k]=oldPiece;
                        }
                    } catch (Exception e) {}
                    temp=1;
                }
            }
        }
        return movement;
    }
    public static String moveGenForKnight(int i) {
        //This function generates the "L" shape movements for Knight 
       String movement="", oldPiece;
       int r=i/8, c=i%8;
       for (int j=-2; j<=2; j++) {//if the square is not diagonal or in the same horizontal/Vertical path of the current Knight position
            for (int k=-2; k<=2; k++) {
                if(j!=0 && k!=0 && Math.abs(j)!=Math.abs(k)){ //If the square is empty
                    try {
                        if(" ".equals(ChessGameAI.BoardRepn[r+j][c+k])){
                            oldPiece=ChessGameAI.BoardRepn[r+j][c+k];//Backup
                            ChessGameAI.BoardRepn[r][c]=" ";//Current Kinght pos blank
                            ChessGameAI.BoardRepn[r+j][c+k]="K";//New Knight position
                            if (ChessGameAI.kingSafe()) {
                                movement=movement+r+c+(r+j)+(c+k)+oldPiece;//Add the movement to list
                            }
                            ChessGameAI.BoardRepn[r][c]="K";
                            ChessGameAI.BoardRepn[r+j][c+k]=oldPiece;
                        }
                        if (Character.isLowerCase(ChessGameAI.BoardRepn[r+j][c+k].charAt(0))) {//If the "L" path has a Black Piece
                            oldPiece=ChessGameAI.BoardRepn[r+j][c+k];//Backup
                            ChessGameAI.BoardRepn[r][c]=" ";//Current Knight Pos blank
                            ChessGameAI.BoardRepn[r+j][c+k]="K";//New Knight position
                            if (ChessGameAI.kingSafe()) {
                                movement=movement+r+c+(r+j)+(c+k)+oldPiece;//Add the movement to List
                            }
                            ChessGameAI.BoardRepn[r][c]="K";//Rever the changes
                            ChessGameAI.BoardRepn[r+j][c+k]=oldPiece;
                        }
                    } catch (Exception e) {}
                    
                }
            }
        }
        return movement;
    }
    public static String moveGenForBishop(int i,String piece) {
        //This function generates the movements for Bishop and Queen with diagonal paths
        String movement="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j+=2) {
            for (int k=-1; k<=1; k+=2) {
                    try {
                        while(" ".equals(ChessGameAI.BoardRepn[r+temp*j][c+temp*k]))//If path has blank squares
                        {
                            oldPiece=ChessGameAI.BoardRepn[r+temp*j][c+temp*k];//Backup
                            ChessGameAI.BoardRepn[r][c]=" ";//Make Existing piece square blank
                            ChessGameAI.BoardRepn[r+temp*j][c+temp*k]=piece;//Move the Pieced to new position
                            if (ChessGameAI.kingSafe()) {//Check King safety
                                movement=movement+r+c+(r+temp*j)+(c+temp*k)+oldPiece;//Add the movemen to the list
                            }
                            ChessGameAI.BoardRepn[r][c]=piece;//REver the changes
                            ChessGameAI.BoardRepn[r+temp*j][c+temp*k]=oldPiece;
                            temp++;
                        }
                        if (Character.isLowerCase(ChessGameAI.BoardRepn[r+temp*j][c+temp*k].charAt(0))) {//If the square is non blank and is a Black Piece
                            oldPiece=ChessGameAI.BoardRepn[r+temp*j][c+temp*k];//Repeat the proedure above
                            ChessGameAI.BoardRepn[r][c]=" ";
                            ChessGameAI.BoardRepn[r+temp*j][c+temp*k]=piece;
                            if (ChessGameAI.kingSafe()) {
                                movement=movement+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                            }
                            ChessGameAI.BoardRepn[r][c]=piece;
                            ChessGameAI.BoardRepn[r+temp*j][c+temp*k]=oldPiece;
                        }
                    } catch (Exception e) {}
                    temp=1;
            }
        }
        return movement;
    }
    public static String moveGenForKing(int i) {
        //Generating the moves for King
        String movements="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=0;j<9;j++) {
            if (j!=4) {
                try { //Check if the adjoining square has a black king or a empty spot
                    if (Character.isLowerCase(ChessGameAI.BoardRepn[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(ChessGameAI.BoardRepn[r-1+j/3][c-1+j%3])) {
                        oldPiece=ChessGameAI.BoardRepn[r-1+j/3][c-1+j%3];//backup the existing piece
                        ChessGameAI.BoardRepn[r][c]=" ";//replace current position with blank
                        ChessGameAI.BoardRepn[r-1+j/3][c-1+j%3]="A";//make the new position with king
                        int kingTemp=ChessGameAI.curWKingPos;//back up the kings position
                        ChessGameAI.curWKingPos=i+(j/3)*8+j%3-9;//assign the new kings position 
                        if (ChessGameAI.kingSafe()) {
                            movements=movements+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;//If with the new move the King is safe then add this movement string to the list
                        }
                        ChessGameAI.BoardRepn[r][c]="A";//revert back the changes
                        ChessGameAI.BoardRepn[r-1+j/3][c-1+j%3]=oldPiece;
                        ChessGameAI.curWKingPos=kingTemp;
                    }
                } catch (Exception e) {}
            }
        }
        return movements;//return the list of possible king movements
    }
}