package chess.gui;
import java.util.ArrayList;

import  chess.engine.*;

public class testPawnPromotion extends GameState{
	squareThirteen thirteen = new squareThirteen();
	ArrayList<Square> squareArrayList = new ArrayList<Square>();
	ArrayList<Integer> movePairs = new ArrayList<Integer>();
	public testPawnPromotion(){
		squareArrayList.add(thirteen);
		
		movePairs.add(13);
		movePairs.add(-1);
		movePairs.add(49);
		movePairs.add(13);
		
		this.setActiveTeam(Team.ORANGE);
		this.setCheck(false);
		this.setCheckmate(false);
		this.setDraw(false);
		this.setPawnPromotionIndex(48);
		this.setPossibleMoveIndexes(squareArrayList);
		this.setMovePairs(movePairs);
	}

}

