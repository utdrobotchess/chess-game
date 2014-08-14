//##########################################################################################################################//
//----------------------------------This is move evaluation of moves of the Chess Engine-----------------------------------//
//This class has the board evaluation functions based on the current piece positions, end game scenario, attacking scenario//
//possible movement option scenario.                                                                                       //
//#########################################################################################################################//

public class Evaluation {
    //default piece points if not user configured
    static int pawnPoints=100,rookPoints=500,knightPoints=300,bishopPoints=300,queenPoints=900,kingPoints=1000;
    static int pawnBoard[][]={//rating based on the prefered position for a pawn
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][]={//rating based on the prefered position for a rook
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][]={//rating based on the prefered position for a Knight
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][]={//rating based on the prefered position for a Bishop
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][]={//rating based on the prefered position for a queen
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][]={//rating based on the prefered position for a king Mid board
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int kingEndBoard[][]={//rating based on the prefered position for a king end board
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
    public static int rating(int list, int depth) {
        int counter=0, material=rateMaterial();
        //based on the scenario if the pawn is towards the promotion region and it is not under any potential attack then pawn gains more points then other major pieces
        pawnPoints=redefinePiecePointsForPromotion(material);
        redefiningEndGamePoints(material);//if the board is on End-game scenario then Rook gains more weight , Queen looses importance
        counter+=ratingAttack();//Rate the potential attacks by checking if attached by major / minor pieces
        counter+=material;//with the possible move does this affect the current material of the board
        counter+=rateMoveablitly(list, depth, material);//evaluate the potentials of moveabiliy
        counter+=ratePositional(material);//Rate the current piece positions
        counter+=rateSeverityofAttack();
        ChessGameAI.flipBoard();//flip the board to get the point of view of opponent
        material=rateMaterial();
        counter-=ratingAttack();//subtract the attacking rank of opponent
        counter-=material;//subtract the material weight of opponent
        counter-=rateMoveablitly(list, depth, material);//subtract the potential moveability of opponent
        counter-=ratePositional(material);//subtract the positional value of opponent
        counter-=rateSeverityofAttack();
        ChessGameAI.flipBoard();//flip back the board to original postion
        return -(counter+depth*50);//based on the search depth return the weightage
    }
    public static void redefiningEndGamePoints(int material){
        int pawnCounter=0,rookCounter=0,knightCounter=0,bishopCounter=0,queen=0;
        for (int i=0;i<64;i++) {
            switch (ChessGameAI.BoardRepn[i/8][i%8]) {
                case "P": pawnCounter++;
                    break;
                case "R": rookCounter++;
                    break;
                case "K": knightCounter++;
                    break;
                case "B": bishopCounter++;
                    break;
                case "Q": queen=1;
                    break;
            }
        }
        if(material <=1750 && pawnCounter<=2){//if the board is towards end-game and there are less then two pawns
            if(bishopCounter==1 && bishopCounter==knightCounter ){
                bishopPoints+=30;//Increase the Bishop weight to be more than Knight
            }
            if(bishopCounter==2 && queen==1 && knightCounter==0){
                queenPoints-=100;//reduce the Queen points and improve the weight of Bishops
            }
            if(rookCounter==1 && pawnCounter==2 && bishopCounter==2){
                bishopPoints+=50;//make bishop more powerful than rook towards end game
            }
                
        }
    }
    public static int redefinePiecePointsForPromotion(int material){
        int temp=0;
        temp=ChessGameAI.curWKingPos;
        if(material>=1100 && material <=2000){//if towards end game and the material count is low and the pawn is has a potential to promote
            for (int i=8;i<24;i++) {//if the pawn is in last two rows 
            if(ChessGameAI.BoardRepn[i/8][i%8].equals("P") && i<16) 
            {ChessGameAI.curWKingPos=i; if (!ChessGameAI.kingSafe()) {ChessGameAI.curWKingPos=temp;return 500; }}//return pawn points as 500 it its in last but one row of board
            
            else if(ChessGameAI.BoardRepn[i/8][i%8].equals("P") && i<16) 
            {ChessGameAI.curWKingPos=i; if (!ChessGameAI.kingSafe()) {ChessGameAI.curWKingPos=temp;return 200; }}//return pawn points as 200 if its in last but two row of board
            
            }
        }
        ChessGameAI.curWKingPos=temp;
        return 100;
    }
    public static int ratingAttack() {//Evaluate the attack to check if  a major or minor piece is under attacked
        int counter=0;
        int tempPositionC=ChessGameAI.curWKingPos;
        for (int i=0;i<64;i++) {
            switch (ChessGameAI.BoardRepn[i/8][i%8]) {
                case "P": {ChessGameAI.curWKingPos=i; if (!ChessGameAI.kingSafe()) {counter-=64;}}//if pawn position is under attack then
                    break;
                case "R": {ChessGameAI.curWKingPos=i; if (!ChessGameAI.kingSafe()) {counter-=500;}}//if Rook is under attack
                    break;
                case "K": {ChessGameAI.curWKingPos=i; if (!ChessGameAI.kingSafe()) {counter-=300;}}//if knoght is under attack
                    break;
                case "B": {ChessGameAI.curWKingPos=i; if (!ChessGameAI.kingSafe()) {counter-=300;}}//if bishop is under attack
                    break;
                case "Q": {ChessGameAI.curWKingPos=i; if (!ChessGameAI.kingSafe()) {counter-=900;}}//if Queen is under attack
                    break;
            }
        }
        ChessGameAI.curWKingPos=tempPositionC;
        if (!ChessGameAI.kingSafe()) {counter-=200;}
        return counter/2;
    }
    public static int rateSeverityofAttack(){
        int counter=0;
        int tempPos=ChessGameAI.curWKingPos;
        for(int i=0;i<64;i++){
            switch(ChessGameAI.BoardRepn[i/8][i%8]){
                case "P":ChessGameAI.curWKingPos=i; counter+=rateAttackByPiece();
                break;
                case "B":ChessGameAI.curWKingPos=i; counter+=rateAttackByPiece();
                break;
                case "K":ChessGameAI.curWKingPos=i; counter+=rateAttackByPiece();
                break;
                case "R":ChessGameAI.curWKingPos=i; counter+=rateAttackByPiece();
                break;
                case "Q":ChessGameAI.curWKingPos=i; counter+=rateAttackByPiece();
                break;
                }
            }
        ChessGameAI.curWKingPos=tempPos;
        return counter/2;
    }
    public static int rateMaterial() {//Evaluate the present board state material weightage
        int counter=0, bishopCounter=0;
        for (int i=0;i<64;i++) {
            switch (ChessGameAI.BoardRepn[i/8][i%8]) {
                case "P": counter+=pawnPoints;
                    break;
                case "R": counter+=rookPoints;
                    break;
                case "K": counter+=knightPoints;
                    break;
                case "B": bishopCounter+=1;
                    break;
                case "Q": counter+=queenPoints;
                    break;
            }
        }
        if (bishopCounter>=2) {
            counter+=bishopPoints*bishopCounter;
        } else {
            if (bishopCounter==1) {counter+=bishopPoints;}
        }
        return counter;
    }
    public static int rateMoveablitly(int listLength, int depth, int material) {
        int counter=0;
        counter+=listLength;//5 pointer per valid move
        if (listLength==0) {//current side is in checkmate or stalemate
            if (!ChessGameAI.kingSafe()) {//if checkmate
                counter+=-200000*depth;
            } else {//if stalemate
                counter+=-150000*depth;
            }
        }
        return 0;
    }
    public static int ratePositional(int material) {
        int counter=0;
        for (int i=0;i<64;i++) {
            switch (ChessGameAI.BoardRepn[i/8][i%8]) {
                case "P": counter+=pawnBoard[i/8][i%8];
                    break;
                case "R": counter+=rookBoard[i/8][i%8];
                    break;
                case "K": counter+=knightBoard[i/8][i%8];
                    break;
                case "B": counter+=bishopBoard[i/8][i%8];
                    break;
                case "Q": counter+=queenBoard[i/8][i%8];
                    break;
                case "A": if (material>=1750) {counter+=kingMidBoard[i/8][i%8]; counter+=GenerateMovements.moveGenForKing(ChessGameAI.curWKingPos).length()*10;} else
                {counter+=kingEndBoard[i/8][i%8]; counter+=GenerateMovements.moveGenForKing(ChessGameAI.curWKingPos).length()*30;}
                    break;
            }
        }
        return counter;
    }
    public static void depthConfiguration(){
        //towards the end game is all the pawns are lost and there are other major pieces and if the globalDepth 
        //is in the range of 4-8 then iteratively increase the search
        int pawnCounter=0,rookCounter=0,knightCounter=0,bishopCounter=0,queen=0;
        for (int i=0;i<64;i++) {
            switch (ChessGameAI.BoardRepn[i/8][i%8]) {
                case "P": pawnCounter++;
                    break;
                case "R": rookCounter++;
                    break;
                case "K": knightCounter++;
                    break;
                case "B": bishopCounter++;
                    break;
                case "Q": queen=1;
                    break;
            }
        }
        if(pawnCounter==0 && (rookCounter<=2 && knightCounter<=2 && bishopCounter<=2 && queen<=1)){
            if(ChessGameAI.globalDepth>4 && ChessGameAI.globalDepth<8)
            ChessGameAI.globalDepth++;
        }
    }
    public static int rateAttackByPiece(){
         //Is the king getting exposed with current White King position in any diagonal position by the Black Bishop or Queen
        int temp=1;
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    while(" ".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+temp*i][ChessGameAI.curWKingPos%8+temp*j])) {temp++;}
                    if ("b".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+temp*i][ChessGameAI.curWKingPos%8+temp*j]) ||
                            "q".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+temp*i][ChessGameAI.curWKingPos%8+temp*j])) {
                        return 500;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        //Is the king getting exposed with current White King position in any Horizontal or Vertical position by the Black Bishop or Queen
        for (int i=-1; i<=1; i+=2) {
            try {
                while(" ".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8][ChessGameAI.curWKingPos%8+temp*i])) {temp++;}
                if ("r".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8][ChessGameAI.curWKingPos%8+temp*i]) ||
                        "q".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8][ChessGameAI.curWKingPos%8+temp*i])) {
                    return 330;
                }
            } catch (Exception e) {}
            temp=1;
            try {
                while(" ".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+temp*i][ChessGameAI.curWKingPos%8])) {temp++;}
                if ("r".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+temp*i][ChessGameAI.curWKingPos%8]) ||
                        "q".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+temp*i][ChessGameAI.curWKingPos%8])) {
                    return 330;
                }
            } catch (Exception e) {}
            temp=1;
        }
        //Is the king getting exposed with current White King position in any "L" shape position
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    if ("k".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+i][ChessGameAI.curWKingPos%8+j*2])) {
                        return 300;
                    }
                } catch (Exception e) {}
                try {
                    if ("k".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+i*2][ChessGameAI.curWKingPos%8+j])) {
                        return 300;
                    }
                } catch (Exception e) {}
            }
        }
        //Is the king getting exposed with current White King position in any diagonal right up or left up position
        if (ChessGameAI.curWKingPos>=16) {
            try {
                if ("p".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8-1][ChessGameAI.curWKingPos%8-1])) {
                    return 100;
                }
            } catch (Exception e) {}
            try {
                if ("p".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8-1][ChessGameAI.curWKingPos%8+1])) {
                    return 100;
                }
            } catch (Exception e) {}
            //Is there a Black king in the adjoining squares
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if (i!=0 || j!=0) {
                        try {
                            if ("a".equals(ChessGameAI.BoardRepn[ChessGameAI.curWKingPos/8+i][ChessGameAI.curWKingPos%8+j])) {
                                return 200;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return 0;
     
    }
}
