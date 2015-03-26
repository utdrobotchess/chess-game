/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.utdallas.robotchess.manager.UIState;
import edu.utdallas.robotchess.manager.RobotState;

public class DemoFrame extends JFrame
{
    private static final long serialVersionUID = 2;
    final int FRAME_WIDTH = 300;
    final int FRAME_HEIGHT = 200;

    UIState uiState;
    RobotState robotState;

    BoardSizeComboBox rowComboBox;
    BoardSizeComboBox columnComboBox;

    protected DemoFrame(UIState uiState, RobotState robotState)
    {
        this.uiState = uiState;
        this.robotState = robotState;

        setLayout(new BorderLayout());

        uiState.setDemoModeSetup(true);

        JPanel boardSizePanel = new JPanel();
        JPanel boardSizeComboBoxPanel = new JPanel();
        boardSizePanel.setLayout(new GridLayout(3, 1));
        boardSizeComboBoxPanel.setLayout(new GridLayout(2, 2));

        JLabel boardSizeLabel = new JLabel("Please select the size of the board",
                                           SwingConstants.CENTER);
        JLabel rowLabel = new JLabel("Rows", SwingConstants.CENTER);
        JLabel columnLabel = new JLabel("Columns", SwingConstants.CENTER);
        rowComboBox = new BoardSizeComboBox();
        columnComboBox = new BoardSizeComboBox();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new OKButtonListener());
        boardSizeComboBoxPanel.add(rowLabel);
        boardSizeComboBoxPanel.add(columnLabel);
        boardSizeComboBoxPanel.add(rowComboBox);
        boardSizeComboBoxPanel.add(columnComboBox);
        boardSizePanel.add(boardSizeLabel);
        boardSizePanel.add(boardSizeComboBoxPanel);
        boardSizePanel.add(okButton);

        add(boardSizePanel);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class BoardSizeComboBox extends JComboBox
    {
        private static final long serialVersionUID = 4;
        final String boardSizes[] = {"1", "2", "3", "4", "5", "6", "7", "8"};

        protected BoardSizeComboBox()
        {
            for (int i = 0; i < boardSizes.length; i++)
                addItem(boardSizes[i]);
        }
    }

    class OKButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int rows = Integer.parseInt((String) rowComboBox.getSelectedItem());
            int columns = Integer.parseInt((String) columnComboBox.getSelectedItem());
            uiState.getMainFrame().resizeBoard(rows, columns);
            setVisible(false);
        }
    }
}
