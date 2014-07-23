
/*
 *
 * @author Ryan J. Marcotte
 */

package chess.engine;

import java.util.*;
import java.util.logging.*;

import org.junit.*;
import static org.junit.Assert.*;

import chess.engine.Team;

public class ChessGameTest {
    private static final Logger logger = ChessLogger.getInstance().logger;
    private ChessGame game;
    private GameState state;

    public ChessGameTest() {

    }

    @Before
    public void initialize() {
        game = ChessGame.setupGame();
        state = GameState.generateInitialState();
    }

    @Test
    public void testBadUserSelection() {
        logger.log(Level.WARNING, "Begin testBadUserSelection() - ChessGameTest");

        int[] selectionLocations = {45, 53, 33,  0, 58,
                                    51, 35, 35,  3, 11,
                                    10, 20, 14, 30,  6,
                                    48, 58, 23, 58, 30};
        Team[] activeTeams = {Team.ORANGE, Team.ORANGE, Team.ORANGE, Team.ORANGE, Team.ORANGE,
                              Team.ORANGE, Team.GREEN, Team.GREEN, Team.GREEN, Team.GREEN,
                              Team.GREEN, Team.GREEN, Team.GREEN, Team.ORANGE, Team.ORANGE,
                              Team.ORANGE, Team.ORANGE, Team.ORANGE, Team.ORANGE, Team.GREEN};
        int[] selectedPieceIndexes = {-1, 53, -1, -1, 58,
                                      51, -1, -1,  3, 11,
                                      10, -1, 14, -1, -1,
                                      48, 58, -1, 58, -1};
        int[][] possibleMoveIndexArrays = {{ }, {37, 45}, { }, { },
                                           { }, {35, 43}, { }, { },
                                           { }, {19, 27}, {18, 26},
                                           { }, {22, 30}, { }, { },
                                           {32, 40}, {30, 37, 44, 51},
                                           { }, {30, 37, 44, 51}, { }};
        int[][] movePairArrays = {{ }, { }, { }, { }, { }, { },
                                  {51, 35}, { }, { }, { }, { },
                                  { }, { }, {14, 30}, { }, { },
                                  { }, { }, { }, {30, -1, 58, 30}};

        for (int i = 0; i < selectionLocations.length; i++) {
            state = game.updateStateFromUserSelection(selectionLocations[i]);

            assertEquals("Selection index: " + i + "\nActive teams ", activeTeams[i],
                         state.getActiveTeam());
            assertEquals("Selection index: " + i + "\nSelected piece index ", selectedPieceIndexes[i],
                         state.getSelectedPieceIndex());

            ArrayList<Integer> actualPossibleMoveIndexes = state.getPossibleMoveIndexes();

            assertEquals("Selection index: " + i + "\nSize of possible move indexes",
                         possibleMoveIndexArrays[i].length, actualPossibleMoveIndexes.size());

            for (int j = 0; j < possibleMoveIndexArrays[i].length; j++) {
                assertEquals("Selection index: " + i + "\nPossible move indexes ",
                             possibleMoveIndexArrays[i][j], (int) actualPossibleMoveIndexes.get(j));
            }

            ArrayList<Integer> actualMovePairs = state.getMovePairs();

            assertEquals("Selection index: " + i + "\nSize of move pairs ", movePairArrays[i].length,
                         actualMovePairs.size());

            for (int j = 0; j < movePairArrays[i].length; j++) {
                assertEquals("Selection index: " + i + "\nMove pairs ", movePairArrays[i][j],
                             (int) actualMovePairs.get(j));
            }
        }

        logger.log(Level.WARNING, "End testBadUserSelection() - ChessGameTest");
    }

    @Test
    public void testIdentifyCheck() {
        logger.log(Level.WARNING, "Begin testIdentifyCheck() - ChessGameTest");

        int[] userInputs = {52, 36, 12, 28, 53, 45,  3, 39,
                            54, 46, 39, 46, 55, 46,  6, 21,
                            51, 35, 28, 35, 59, 35, 21, 38,
                            35, 28,  4,  3, 28,  4,  3,  4};
        boolean[] checkState = {false, false, false, false, false,
                                false, false, true, true, false,
                                false, true, true, false, false,
                                false, false, false, false, false,
                                false, false, false, false, false,
                                true, true, false, false, true,
                                true, false};

        for (int i = 0; i < userInputs.length; i++) {
            state = game.updateStateFromUserSelection(userInputs[i]);
            assertEquals("User input: " + i + "\nCheck state ", checkState[i], state.isCheck());
        }

        logger.log(Level.WARNING, "End testIdentifyCheck() - ChessGameTest");
    }
}
