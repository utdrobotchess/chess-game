/**
 *
 * @author Ryan J. Marcotte
 */

package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import manager.UIState;
import manager.RobotState;

public class MainFrame extends JFrame
{
    public final static int SQUARE_SIZE = 100;

    UIState uiState;
    RobotState robotState;

    JMenuBar menuBar;
    
    JMenu fileMenu;
    JMenu viewMenu;
    JMenu helpMenu;

    JMenuItem newGameMenuItem;
    JMenuItem demoModeMenuItem;
    JMenuItem exitMenuItem;

    MenuItemListener menuListener;

    BoardPanel boardPanel;
    
    public MainFrame(UIState uiState, RobotState robotState)
    {
        this.uiState = uiState;
        this.robotState = robotState;
        uiState.setMainFrame(this);
        
        boardPanel = new BoardPanel(uiState, robotState, 8, 8);
        add(boardPanel);

        setupMenuBar();
        setTitle("Robot Chess");
        setSize(8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    protected void resizeBoard(int rows, int columns)
    {
        remove(boardPanel);
        boardPanel = new BoardPanel(uiState, robotState, rows, columns);
        add(boardPanel);
        
        setSize(rows * SQUARE_SIZE, columns * SQUARE_SIZE);
        setLocationRelativeTo(null);

        robotState.setBoardColumns(columns);
        robotState.setBoardRows(rows);
    }

    private void setupMenuBar()
    {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");

        newGameMenuItem = new JMenuItem("New Game");
        demoModeMenuItem = new JMenuItem("Demo Mode");
        exitMenuItem = new JMenuItem("Exit");

        fileMenu.add(newGameMenuItem);
        fileMenu.add(demoModeMenuItem);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        menuListener = new MenuItemListener();

        newGameMenuItem.addActionListener(menuListener);
        demoModeMenuItem.addActionListener(menuListener);
        exitMenuItem.addActionListener(menuListener);
    }

    class MenuItemListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == demoModeMenuItem) {
                //DemoFrame df = new DemoFrame(uiState, robotState);
                resizeBoard(4, 4);
                boardPanel.initializeDemo();
                uiState.setDemoMode(true);
            }

            if (e.getSource() == exitMenuItem) {
                // TODO add an "are you sure option"
                // TODO implement the safe cleanup functionality for the network
                System.exit(0); 
            }
        }
    }
}
