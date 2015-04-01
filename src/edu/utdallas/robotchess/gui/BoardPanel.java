package edu.utdallas.robotchess.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import edu.utdallas.robotchess.manager.Manager;
import edu.utdallas.robotchess.game.*;

public class BoardPanel extends JPanel
{
    final int TOTAL_SQUARES = 64;
    
    private Manager manager;
    private SquareButton squares[];
    private Map<String, Icon> imageMap;

    protected BoardPanel(Manager manager)
    {
        this.manager = manager;

        int rows = manager.getBoardRowCount();
        int columns = manager.getBoardColumnCount();

        setLayout(new GridLayout(rows, columns));

        squares = new SquareButton[TOTAL_SQUARES];

        initializeSquares(rows, columns);
    }

    private void initializeSquares(int rows, int columns)
    {
        boolean whiteSquare = true;

        for (int i = 0; i < squares.length; i++) {
            Color squareColor = whiteSquare ? Color.WHITE : Color.BLACK;
            squares[i] = new SquareButton(i, squareColor);
            squares[i].addActionListener(new ButtonListener());

            squares[i].setOpaque(true);

            if (i % 8 < columns && i / 8 < rows)
                add(squares[i]);

            if (i % 8 != 7)
                whiteSquare = !whiteSquare;
        }
    }

    private void updateDisplay()
    {
        ArrayList<ChessPiece> pieces = manager.getActivePieces();
        
        for (int i = 0; i < pieces.size(); i++) {
            ChessPiece piece = pieces.get(i);
            int location = piece.getIntLocation();
            String pieceName = piece.getName();
            Icon icon = imageMap.get(pieceName);
            squares[location].setIcon(icon);
        }
    }
    
    protected void setManager(Manager manager)
    {
        this.manager = manager; 
    }

    class ButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            SquareButton buttonPressed = (SquareButton) e.getSource();
            int buttonIndex = buttonPressed.getIndex();
            manager.handleSquareClick(buttonIndex);
            updateDisplay();
        }
    }
}
