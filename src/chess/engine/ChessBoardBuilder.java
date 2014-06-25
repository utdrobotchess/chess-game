/*
 * Static class to construct a chessboard from squares that are connected properly
 * to each other.
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.logging.*;

public class ChessBoardBuilder {
    private static final int NUM_INTERIOR_SQUARES = 64;
    private static final int TOTAL_SQUARES = 100;
    private static final int TOTAL_NEIGHBORS = 8;

    private static final Logger logger = ChessLogger.getInstance().logger;
    private static Square[] boardSquares;

    //for each location on the chessboard, denotes its neighboring locations (prior to remapping)
    private final static int[][] mappingReferences = new int[TOTAL_SQUARES][TOTAL_NEIGHBORS];

    //denotes the indexes to which Squares ordered 0-99 should be mapped; inverse
    //of occupantToLocationMapping
    private final static int[] locationToOccupantMapping = new int[TOTAL_SQUARES];

    //denotes the Square that occupies each index; inverse of locationToOccupantMapping
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

    protected static void build(ChessBoard board) {
        boardSquares = board.getAllSquares();
        buildSquares();
        assignMappingReferences();
        applyMappingReferencesToBoard();

        logger.log(Level.FINE, "Chessboard built");
    }

    private static void buildSquares() {
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            if (i < NUM_INTERIOR_SQUARES) {
                boardSquares[i] = InteriorSquare.generateInteriorSquareAt(i);
            } else {
                boardSquares[i] = PerimeterSquare.generatePerimeterSquareAt(i);
            }
        }

        logger.log(Level.FINE, "Squares built for chessboard");
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

    /*
     * From the hard-coded occupant to location mapping array,
     * we compute the inverse mapping
     */
    private static void mapLocationsToOccupants() {
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            locationToOccupantMapping[occupantToLocationMapping[i]] = i;
        }
    }

    /*
     * Until assigned a final mapping reference in the range 0-99,
     * all references should be to -1. All perimeter squares will
     * maintain this -1 reference for directions in which they do
     * not have neighbors
     */
    private static void initializeMappingReferences() {
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            for (int j = 0; j < 8; j++) {
                mappingReferences[i][j] = -1;
            }
        }
    }

    private static void assignNorthMappingReferences() {
        /* skip the first row */
        for (int i = 10; i < TOTAL_SQUARES; i++) {
            mappingReferences[i][0] = i - 10;
        }
    }

    private static void assignNorthEastMappingReferences() {
        /* skip the first row and last column */
        for (int i = 10; i < TOTAL_SQUARES; i++) {
            if (i % 10 != 9) {
                mappingReferences[i][1] = i - 9;
            }
        }
    }

    private static void assignEastMappingReferences() {
        /* skip the last column */
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            if (i % 10 != 9) {
                mappingReferences[i][2] = i + 1;
            }
        }
    }

    private static void assignSouthEastMappingReferences() {
        /* skip the last row and last column */
        for (int i = 0; i < TOTAL_SQUARES - 10; i++) {
            if (i % 10 != 9) {
                mappingReferences[i][3] = i + 11;
            }
        }
    }

    private static void assignSouthMappingReferences() {
        /* skip the last row */
        for (int i = 0; i < TOTAL_SQUARES - 10; i++) {
            mappingReferences[i][4] = i + 10;
        }
    }

    private static void assignSouthWestMappingReferences() {
        /* skip the last row and first column */
        for (int i = 0; i < TOTAL_SQUARES - 10; i++) {
            if (i % 10 != 0) {
                mappingReferences[i][5] = i + 9;
            }
        }
    }

    private static void assignWestMappingReferences() {
        /* skip the first column */
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            if (i % 10 != 0) {
                mappingReferences[i][6] = i - 1;
            }
        }
    }

    private static void assignNorthWestMappingReferences() {
        /* skip the first row and first column */
        for (int i = 10; i < TOTAL_SQUARES; i++) {
            if (i % 10 != 0) {
                mappingReferences[i][7] = i - 11;
            }
        }
    }

    private static void applyMappingReferencesToBoard() {
        Square sq;
        Square newNeighbor;
        int newNeighborIndex;

        for (int i = 0; i < TOTAL_SQUARES; i++) {
            sq = boardSquares[i];

            for (int j = 0; j < TOTAL_NEIGHBORS; j++) {
                newNeighborIndex = mappingReferences[locationToOccupantMapping[i]][j];

                if (newNeighborIndex == -1) {
                    sq.assignNeighborInDirection(NullSquare.generateNullSquare(), j);
                } else {
                    newNeighbor = boardSquares[occupantToLocationMapping[newNeighborIndex]];
                    sq.assignNeighborInDirection(newNeighbor, j);
                }
            }
        }
    }
}
