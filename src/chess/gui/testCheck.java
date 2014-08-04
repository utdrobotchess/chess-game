package chess.gui;
import chess.engine.*;
import java.util.*;

//For all cases, promote the pawn at square 48 and capture the black pawn at square 13 using the pawn at square 49.
public class testCheck extends GameState{
	squareThirteen thirteen = new squareThirteen();
	ArrayList<Square> squareArrayList = new ArrayList<Square>();
	ArrayList<Integer> movePairs = new ArrayList<Integer>();
	public testCheck(){
		squareArrayList.add(thirteen);
		
		movePairs.add(13);
		movePairs.add(-1);
		movePairs.add(49);
		movePairs.add(13);
		
		this.setActiveTeam(Team.ORANGE);
		this.setCheck(true);
		this.setCheckmate(false);
		this.setDraw(false);
		this.setPawnPromotionIndex(-1);
		this.setPossibleMoveIndexes(squareArrayList);
		this.setMovePairs(movePairs);
	}

}
