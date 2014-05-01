package chess.engine;

/**
 * Defines the two teams that a chess piece can be on.
 * @author Ryan J. Marcotte
 */

public enum Team {
	//the orange pieces start at the higher numbered squares, so moving forward
	//for them implies moving in a negative direction; the opposite is true for 
	//the green pieces
	ORANGE(-1), NEUTRAL(0), GREEN(1);

	private final int directionalValue;
	private Team(int value) {
		directionalValue = value;
	}

	public int getDirectionalValue() {
		return directionalValue;
	}
}
