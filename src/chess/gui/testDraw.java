package chess.gui;
import java.util.ArrayList;

import  chess.engine.*;

public class testDraw extends GameState{
	squareThirteen thirteen = new squareThirteen();
	ArrayList<Square> squareArrayList = new ArrayList<Square>();
	ArrayList<Integer> movePairs = new ArrayList<Integer>();
	public testDraw(){
		squareArrayList.add(thirteen);
		
		movePairs.add(13);
		movePairs.add(-1);
		movePairs.add(49);
		movePairs.add(13);
		
		this.setActiveTeam(Team.ORANGE);
		this.setCheck(false);
		this.setCheckmate(false);
		this.setDraw(true);
		this.setPawnPromotionIndex(-1);
		this.setPossibleMoveIndexes(squareArrayList);
		this.setMovePairs(movePairs);
	}

}


