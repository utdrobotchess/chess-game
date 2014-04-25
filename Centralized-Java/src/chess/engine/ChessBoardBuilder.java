/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chess.engine;

/**
 *
 * @author Ryan J. Marcotte
 */
public class ChessBoardBuilder {
    private static Square[] boardSquares;
    private static int[] remappingIndexes = {
        64, 65, 66, 67, 68, 69, 70, 71, 72, 73,
        74,  0,  1,  2,  3,  4,  5,  6,  7, 75,
        76,  8,  9, 10, 11, 12, 13, 14, 15, 77,
        78, 16, 17, 18, 19, 20, 21, 22, 23, 79,
        80, 24, 25, 26, 27, 28, 29, 30, 31, 81,
        82, 32, 33, 34, 35, 36, 37, 38, 39, 83,
        84, 40, 41, 42, 43, 44, 45, 46, 47, 85,
        96, 48, 49, 50, 51, 52, 53, 54, 55, 87,
        88, 56, 57, 58, 59, 60, 61, 62, 63, 89,
        90, 91, 92, 93, 94, 95, 96, 97, 98, 99};
    
    protected static void build(ChessBoard board) {
        boardSquares = board.getAllSquares();
        buildSquares();
        assignNeighbors();
        remap();
    }
    
    private static void buildSquares() {
        for(int i = 0; i < 100; i++) {
            if(i < 64) {
                boardSquares[i] = InteriorSquare.generateInteriorSquareAt(i);
            } else {
                boardSquares[i] = PerimeterSquare.generatePerimeterSquareAt(i);
            }
            
            assignNullNeighbors(boardSquares[i]);
        }
    }
    
    private static void assignNullNeighbors(Square sq) {
        for(int i = 0; i < 8; i++) {
            sq.assignNeighborInDirection(NullSquare.generateNullSquare(), i);
        }
    }
    
    private static void assignNeighbors() {

    }
    
    private static void remap() {
        
    }
}
