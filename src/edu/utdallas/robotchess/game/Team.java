package edu.utdallas.robotchess.game;

public enum Team
{
   /*
    * the white pieces start at the higher numbered squares, so moving forward
	* for them implies moving in a negative direction; the opposite is true for
	* the black pieces
    */
	ORANGE(-1), GREEN(1);

	private final int directionalValue;

	private Team(int value)
    {
		directionalValue = value;
	}

	public int getDirectionalValue()
    {
		return directionalValue;
	}

    @Override
    public String toString()
    {
        if (directionalValue == -1)
            return "orange";
        else
            return "green";
    }
}
