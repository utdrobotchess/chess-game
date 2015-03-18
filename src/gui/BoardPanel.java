/**
 *
 * @author Ryan J. Marcotte
 */

package gui;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import manager.UIState;
import manager.RobotState;
import robot.Motion;
import robot.SmartCenterCommand;

public class BoardPanel extends JPanel
{
    final int TOTAL_SQUARES = 64;

    UIState uiState;
    RobotState robotState;

    SquareButton squares[];

    int rows;
    int columns;

    protected BoardPanel(UIState uiState, RobotState robotState, int rows, int columns)
    {
        this.uiState = uiState;
        this.robotState = robotState;
        this.rows = rows;
        this.columns = columns;

        setLayout(new GridLayout(rows, columns));

        squares = new SquareButton[TOTAL_SQUARES];

        initializeSquares();
    }

    protected void initializeSquares()
    {
        boolean whiteSquare = true;

        for (int i = 0; i < squares.length; i++) {
            Color squareColor = whiteSquare ? Color.WHITE : Color.BLACK;
            squares[i] = new SquareButton(i, squareColor);
            squares[i].addActionListener(new ButtonListener());

            if (i % 8 < columns && i / 8 < rows)
                add(squares[i]);

            if (i % 8 != 7)
                whiteSquare = !whiteSquare;
        }
    }

    protected void initializeDemo()
    {
        int locations[] = {0}; // XXX This is what we change depending on which robots are here

        for (int i = 0; i < locations.length; i++) {
            squares[locations[i]].setIcon(uiState.getPieceImage("green-pawn"));
            uiState.setPieceLocation(locations[i], locations[i]);
        }
    }

    class ButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            SquareButton buttonPressed = (SquareButton) e.getSource();

            if (uiState.isDemoMode()) {
                int selectedIndex = uiState.getSelectedIndex();

                if (buttonPressed.isOccupied()) {
                    if (buttonPressed.getIndex() == selectedIndex) {
                        int robotID = uiState.getPieceIDFromLocation(selectedIndex);
                        robotState.addNewCommand(new SmartCenterCommand(robotID));
                        uiState.setSelectedIndex(-1);
                    } else {
                        uiState.setSelectedIndex(buttonPressed.getIndex());
                    }
                } else if (selectedIndex != -1) {
                    squares[selectedIndex].setIcon(null);
                    uiState.setSelectedIndex(-1);
                    buttonPressed.setIcon(uiState.getPieceImage("green-pawn"));

                    int current[] = uiState.getAllPieceLocations();
                    uiState.setPieceLocation(uiState.getPieceIDFromLocation(selectedIndex),
                                             buttonPressed.getIndex());
                    int desired[] = uiState.getAllPieceLocations();

                    robotState.addNewMotion(new Motion(current, desired));

                }
            }
        }
    }
}
