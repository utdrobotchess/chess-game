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
    private static int[][] mappingReferences = new int[100][8];
    private static int[] locationToOccupantMapping = new int[100];
    private final static int[] occupantToLocationMapping = {
        64, 65, 66, 67, 68, 69, 70, 71, 72, 73,
        74,  0,  1,  2,  3,  4,  5,  6,  7, 75, 
        76,  8,  9, 10, 11, 12, 13, 14, 15, 77,
        78, 16, 17, 18, 19, 20, 21, 22, 23, 79,
        80, 24, 25, 26, 27, 28, 29, 30, 31, 81,
        82, 32, 33, 34, 35, 36, 37, 38, 39, 83,
        84, 40, 41, 42, 43, 44, 45, 46, 47, 85,
        86, 48, 49, 50, 51, 52, 53, 54, 55, 87,
        88, 56, 57, 58, 59, 60, 61, 62, 63, 89,
        90, 91, 92, 93, 94, 95, 96, 97, 98, 99
    };
    
    public static void main(String[] args) {
        ChessBoard board = ChessBoard.generateChessBoard();
    }
    
    protected static void build(ChessBoard board) {
        boardSquares = board.getAllSquares();
        buildSquares();
        assignMappingReferences();
        remap();
    }
    
    private static void buildSquares() {
        for(int i = 0; i < 100; i++) {
            if(i < 64) {
                boardSquares[i] = InteriorSquare.generateInteriorSquareAt(i);
            } else {
                boardSquares[i] = PerimeterSquare.generatePerimeterSquareAt(i);
            }
        }
    }
    
    private static void assignMappingReferences() {
        mapLocationsToOccupants();
        initializeMappingReferences();
        assignNorthMappingReferences();
        assignNorthEastMappingReferences();
        assignEastMappingReferences();
        assignSouthEastMappingReferences();
        assignSouthMappingReferences();
        assignSouthWestMappingReferences();
        assignWestMappingReferences();
        assignNorthWestMappingReferences();
    }
    
    private static void mapLocationsToOccupants() {
        for(int i = 0; i < 100; i++) {
            locationToOccupantMapping[occupantToLocationMapping[i]] = i;
        }
    }
    
    private static void initializeMappingReferences() {
        for(int i = 0; i < 100; i++) {
            for(int j = 0; j < 8; j++) {
                mappingReferences[i][j] = -1;
            }
        }
    }
    
    private static void assignNorthMappingReferences() {
        for(int i = 10; i < 100; i++) {
            mappingReferences[i][0] = i - 10;
        }
    }
    
    private static void assignNorthEastMappingReferences() {
        for(int i = 10; i < 100; i++) {
            if(i % 10 != 9)
                mappingReferences[i][1] = i - 9;
        }
    }
    
    private static void assignEastMappingReferences() {
        for(int i = 0; i < 100; i++) {
            if(i % 10 != 9)
                mappingReferences[i][2] = i + 1;
        }
    }
    
    private static void assignSouthEastMappingReferences() {
        for(int i = 0; i < 90; i++) {
            if(i % 10 != 9)
                mappingReferences[i][3] = i + 11;
        }
    }
    
    private static void assignSouthMappingReferences() {
        for(int i = 0; i < 90; i++) {
            mappingReferences[i][4] = i + 10;
        }
    }
    
    private static void assignSouthWestMappingReferences() {
        for(int i = 0; i < 90; i++) {
            if(i % 10 != 0)
                mappingReferences[i][5] = i + 9;
        }
    }
    
    private static void assignWestMappingReferences() {
        for(int i = 0; i < 100; i++) {
            if(i % 10 != 0)
                mappingReferences[i][6] = i - 1;
        }
    }
    
    private static void assignNorthWestMappingReferences() {
        for(int i = 10; i < 100; i++) {
            if(i % 10 != 0)
                mappingReferences[i][7] = i - 11;
        }
    }
    
    private static void remap() {        
        for(int i = 0; i < 100; i++) {
            Square sq = boardSquares[i];
            
            for(int j = 0; j < 8; j++) {
                int newNeighborIndex = mappingReferences[locationToOccupantMapping[i]][j];
                
                if(newNeighborIndex == -1) {
                    sq.assignNeighborInDirection(NullSquare.generateNullSquare(), j);
                } else {
                    Square newNeighbor = boardSquares[occupantToLocationMapping[newNeighborIndex]];
                    sq.assignNeighborInDirection(newNeighbor, j);
                }
            }
        }
    }
}
