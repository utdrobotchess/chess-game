package chess.engine;

import java.util.ArrayList;

/**
 *
 * @author Alexandre
 */
public class GameState {
  //  private static final Logger logger = ChessLogger.getInstance().logger;
    private Team itsActiveTeam;
    private boolean itsCheck;
    private boolean itsCheckmate;
    private boolean itsDraw;
    private boolean itsPawnPromotion;
    private int itsSelectedPieceIndex;
    private ArrayList<Integer> itsPossibleMoveIndexes;
    private ArrayList<Integer> itsMovePairs;
    private ArrayList<Integer> itsEnPassantPairs;

    public GameState() { } //change back to private after testing is over

    protected static GameState generateInitialState() {
        GameState state = new GameState();

        state.setActiveTeam(Team.ORANGE);
        state.setCheck(false);
        state.setCheckmate(false);
        state.setDraw(false);
        state.setSelectedPieceIndex(-1);
        state.setPossibleMoveIndexes(new ArrayList<Square>());
        state.setMovePairs(new ArrayList<Integer>());
        state.setEnPassantPairs(new ArrayList<Integer>());

        //logger.log(Level.FINE, "Game state initialized");

        return state;
    }

    protected void clearPossibleMoveIndexes() {
        itsPossibleMoveIndexes.clear();
    }

    protected void clearMovePairs() {
        itsMovePairs.clear();
    }

    protected void clearEnPassantPairs() {
        itsEnPassantPairs.clear();
    }

    public Team getActiveTeam() {
        return itsActiveTeam;
    }

    public int getSelectedPieceIndex() {
        return itsSelectedPieceIndex;
    }
     public void setPawnPromotion(boolean promotion) {
        itsPawnPromotion = promotion;
    }
    public boolean getPawnPromotion(){
        return itsPawnPromotion;
    }

    public ArrayList<Integer> getPossibleMoveIndexes() {
        return itsPossibleMoveIndexes;
    }

    public ArrayList<Integer> getMovePairs() {
        return itsMovePairs;
    }

    public ArrayList<Integer> getEnPassantPairs() {
        return itsEnPassantPairs;
    }

    public boolean isCheck() {
        return itsCheck;
    }

    public boolean isCheckmate() {
        return itsCheckmate;
    }

    public boolean isDraw() {
        return itsDraw;
    }

    protected void setActiveTeam(Team team) {
        itsActiveTeam = team;
    }

    protected void setSelectedPieceIndex(int index) {
        itsSelectedPieceIndex = index;
    }

    protected void setPossibleMoveIndexes(ArrayList<Square> possibleMoveLocations) {
        itsPossibleMoveIndexes = new ArrayList<>();
        Square sq;

        for (int i = 0; i < possibleMoveLocations.size(); i++) {
            sq = possibleMoveLocations.get(i);
            itsPossibleMoveIndexes.add(sq.getNumericalLocation());
        }
    }

    protected void setMovePairs(ArrayList<Integer> movePairs) {
        itsMovePairs = movePairs;
    }

    protected void setEnPassantPairs(ArrayList<Integer> enPassantPairs) {
        itsEnPassantPairs = enPassantPairs;
    }

    protected void setCheck(boolean check) {
        itsCheck = check;
    }

    protected void setCheckmate(boolean checkmate) {
        itsCheckmate = checkmate;
    }

    protected void setDraw(boolean draw) {
        itsDraw = draw;
    }

    public void toggleActiveTeam() {
        if (itsActiveTeam == Team.GREEN) {
            itsActiveTeam = Team.ORANGE;
        } else {
            itsActiveTeam = Team.GREEN;
        }
    }
}
