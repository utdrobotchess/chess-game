/**
 *
 * @author Ryan J. Marcotte
 */

package game;

public class ChessBoard
{
    final int NUM_SQUARES = 64;

    Square squares[] = new Square[NUM_SQUARES];
    
    public ChessBoard()
    {
        for (int i = 0; i < NUM_SQUARES; i++)
            squares[i] = new Square(i);

        assignNeighbors();
    }

    private void assignNeighbors()
    {
        for (int i = 0; i < NUM_SQUARES; i++) {

            // north neighbor
            if (i / 8 != 0)
                squares[i].setNeighbor(squares[i-8], 0);
            else
                squares[i].setNeighbor(null, 0);

            // northeast neighbor
            if (i / 8 != 0 && i % 8 != 7)
                squares[i].setNeighbor(squares[i-7], 1);
            else
                squares[i].setNeighbor(null, 1);

            // east neighbor
            if (i % 8 != 7)
                squares[i].setNeighbor(squares[i+1], 2);
            else
                squares[i].setNeighbor(null, 2);

            // southeast neighbor
            if (i / 8 != 7 && i % 8 != 7)
                squares[i].setNeighbor(squares[i+9], 3);
            else
                squares[i].setNeighbor(null, 3);

            // south neighbor
            if (i / 8 != 7)
                squares[i].setNeighbor(squares[i+8], 4);
            else
                squares[i].setNeighbor(null, 4);

            // southwest neighbor
            if (i / 8 != 7 && i % 8 != 0)
                squares[i].setNeighbor(squares[i+7], 5);
            else
                squares[i].setNeighbor(null, 5);

            // west neighbor
            if (i % 8 != 0)
                squares[i].setNeighbor(squares[i-1], 6);
            else
                squares[i].setNeighbor(null, 6);

            // northwest neighbor
            if (i / 8 != 0 && i % 8 != 0)
                squares[i].setNeighbor(squares[i-9], 7);
            else
                squares[i].setNeighbor(null, 7);
        }
    }

    protected Square[] getSquares()
    {
        return squares;
    }

    protected Square getSquareAt(int intLocation)
    {
        return squares[intLocation];
    }
}
