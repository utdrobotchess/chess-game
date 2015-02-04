/**
 *
 * @author Ryan J. Marcotte
 */

package game;

public enum Team
{
   /*
    * the white pieces start at the higher numbered squares, so moving forward
	* for them implies moving in a negative direction; the opposite is true for
	* the black pieces
    */
	WHITE(-1), BLACK(1);

	private final int directionalValue;
    
	private Team(int value)
    {
		directionalValue = value;
	}

	public int getDirectionalValue()
    {
		return directionalValue;
	}
}
