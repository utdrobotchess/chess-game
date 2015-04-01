package edu.utdallas.robotchess.manager;

import java.util.ArrayList;

import edu.utdallas.robotchess.game.ChessPiece;

public abstract class Manager
{
    public abstract void handleSquareClick(int index);
    public abstract ArrayList<ChessPiece> getActivePieces();
    public abstract int getBoardRowCount();
    public abstract int getBoardColumnCount();
}
