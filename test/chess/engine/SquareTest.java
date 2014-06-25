/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

public class SquareTest {
    private static final Logger logger = ChessLogger.getInstance().logger;

   /*
	* Ensures that the numerical locations of the different types of Squares
	* are properly assigned and can be accessed.
	*/
	@Test
	public void testNumericalLocations() {
        logger.log(Level.WARNING, "Begin testNumericalLocations() - SquareTest");

		Square interSq = InteriorSquare.generateInteriorSquareAt(8);
		Square perimSq = PerimeterSquare.generatePerimeterSquareAt(69);
		Square nullSq = NullSquare.generateNullSquare();

		assertEquals(8, interSq.getNumericalLocation());
		assertEquals(69, perimSq.getNumericalLocation());
		assertEquals(-1, nullSq.getNumericalLocation());

        logger.log(Level.WARNING, "End testNumericalLocations() - SquareTest");
	}

	/*
	 * Ensures that neighbors can be assigned in all directions and then can
	 * be accessed.
	 */
    @Test
    public void testAssignNeighborInDirection() {
        logger.log(Level.WARNING, "Begin testAssignNeighborInDirection() - SquareTest");

        Square interSq1 = InteriorSquare.generateInteriorSquareAt(21);
        Square interSq2 = InteriorSquare.generateInteriorSquareAt(22);
        Square interSq3 = InteriorSquare.generateInteriorSquareAt(38);
        Square interSq4 = InteriorSquare.generateInteriorSquareAt(46);
        Square interSq5 = InteriorSquare.generateInteriorSquareAt(56);
        Square interSq6 = InteriorSquare.generateInteriorSquareAt(49);

        interSq1.assignNeighborInDirection(interSq2, 2);
        interSq2.assignNeighborInDirection(interSq1, 6);
        interSq3.assignNeighborInDirection(interSq4, 4);
        interSq4.assignNeighborInDirection(interSq3, 0);
        interSq5.assignNeighborInDirection(interSq6, 1);
        interSq6.assignNeighborInDirection(interSq5, 5);

        assertEquals(interSq2, interSq1.getNeighborInDirection(2));
        assertEquals(interSq1, interSq2.getNeighborInDirection(6));
        assertEquals(interSq4, interSq3.getNeighborInDirection(4));
        assertEquals(interSq3, interSq4.getNeighborInDirection(0));
        assertEquals(interSq6, interSq5.getNeighborInDirection(1));
        assertEquals(interSq5, interSq6.getNeighborInDirection(5));

        logger.log(Level.WARNING, "End testAssignNeighborInDirection() - SquareTest");
    }

	/*
	 * Ensures that the functionality of assigning and obtaining neighbors
	 * is not limited by the specific type of Square being used.
	 */
    @Test
    public void testMixedAssignNeighborInDirection() {
        logger.log(Level.WARNING, "Begin testMixedAssignNeighborInDirection() - SquareTest");

        Square interSq = InteriorSquare.generateInteriorSquareAt(7);
        Square perimSq = PerimeterSquare.generatePerimeterSquareAt(75);
        Square nullSq = NullSquare.generateNullSquare();

        interSq.assignNeighborInDirection(perimSq, 2);
        perimSq.assignNeighborInDirection(interSq, 6);
        perimSq.assignNeighborInDirection(nullSq, 2);

        assertEquals(perimSq, interSq.getNeighborInDirection(2));
        assertEquals(interSq, perimSq.getNeighborInDirection(6));
        assertEquals(nullSq, perimSq.getNeighborInDirection(2));

        logger.log(Level.WARNING, "End testMixedAssignNeighborInDirection() - SquareTest");
    }

	/*
	 * Ensures that a Square is unoccupied when generated and then shows the
	 * proper occupancy behavior as ChessPieces are spawned and moved.
	 */
    @Test
    public void testOccupancy() {
        logger.log(Level.WARNING, "Begin testOccupancy() - SquareTest");

        Square interSq1 = InteriorSquare.generateInteriorSquareAt(8);
        Square interSq2 = InteriorSquare.generateInteriorSquareAt(9);

        assert(!interSq1.isOccupied());
        assertEquals(Team.NEUTRAL, interSq1.getOccupyingTeam());

        ChessPiece pawn = Pawn.spawnAt(interSq1);

        assert(interSq1.isOccupied());
        assertEquals(Team.GREEN, interSq1.getOccupyingTeam());

		pawn.setLocation(interSq2);

		assert(!interSq1.isOccupied());
		assertEquals(Team.NEUTRAL, interSq1.getOccupyingTeam());

        logger.log(Level.WARNING, "End testOccupancy() - SquareTest");
    }
}
