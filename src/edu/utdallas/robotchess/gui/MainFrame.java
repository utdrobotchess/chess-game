/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.gui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenu;
import javax.swing.ButtonGroup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.utdallas.robotchess.manager.UIState;
import edu.utdallas.robotchess.manager.RobotState;
import edu.utdallas.robotchess.manager.ApplicationState;
import edu.utdallas.robotchess.robot.RemoteController;

public class MainFrame extends JFrame
{
    public final static int SQUARE_SIZE = 100;
    private static final long serialVersionUID = 3;

    UIState uiState;
    RobotState robotState;
    ApplicationState applicationState;

    JMenuBar menuBar;

    JMenu fileMenu;
    JMenu gameModeMenu;
    JMenu optionsMenu;

    JMenuItem newGameMenuItem;
    JMenuItem exitMenuItem;

    JCheckBoxMenuItem enableRobotsMenuItem;
    JCheckBoxMenuItem robotsDiscoveredMenuItem;

    JRadioButtonMenuItem chessModeMenuItem;
    JRadioButtonMenuItem demoModeMenuItem;
    JRadioButtonMenuItem rcModeMenuItem;

    MenuItemListener menuListener;

    DiscoveredBotsFrame discoveredRobotsFrame;

    BoardPanel boardPanel;

    public MainFrame(ApplicationState applicationState,
                     UIState uiState, RobotState robotState)
    {
        this.applicationState = applicationState;
        this.uiState = uiState;
        this.robotState = robotState;
        uiState.setMainFrame(this);

        boardPanel = new BoardPanel(uiState, robotState, 8, 8);
        add(boardPanel);

        discoveredRobotsFrame = new DiscoveredBotsFrame(robotState);
        discoveredRobotsFrame.setVisible(false);

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

    private void setupFileMenu()
    {
        fileMenu = new JMenu("File");

        newGameMenuItem = new JMenuItem("New Game");
        exitMenuItem = new JMenuItem("Exit");

        fileMenu.add(newGameMenuItem);
        fileMenu.add(exitMenuItem);

        newGameMenuItem.addActionListener(menuListener);
        exitMenuItem.addActionListener(menuListener);
    }

    private void setupGameModeMenu()
    {
        gameModeMenu = new JMenu("Game Mode");

        chessModeMenuItem = new JRadioButtonMenuItem("Chess Mode");
        demoModeMenuItem = new JRadioButtonMenuItem("Demo Mode");
        rcModeMenuItem = new JRadioButtonMenuItem("RC Mode");

        ButtonGroup bg = new ButtonGroup();
        bg.add(chessModeMenuItem);
        bg.add(demoModeMenuItem);
        bg.add(rcModeMenuItem);

        chessModeMenuItem.setEnabled(false);
        demoModeMenuItem.setEnabled(false);
        rcModeMenuItem.setEnabled(false);

        gameModeMenu.add(chessModeMenuItem);
        gameModeMenu.add(demoModeMenuItem);
        gameModeMenu.add(rcModeMenuItem);

        enableRobotsMenuItem.addActionListener(menuListener);
        demoModeMenuItem.addActionListener(menuListener);
        rcModeMenuItem.addActionListener(menuListener);
    }

    private void setupOptionMenu()
    {
        optionsMenu = new JMenu("Options");

        robotsDiscoveredMenuItem = new JCheckBoxMenuItem("Discovered Robots");
        robotsDiscoveredMenuItem.setEnabled(false);

        optionsMenu.add(robotsDiscoveredMenuItem);

        robotsDiscoveredMenuItem.addActionListener(menuListener);
    }

    private void setupMenuBar()
    {
        menuBar = new JMenuBar();
        menuListener = new MenuItemListener();

        enableRobotsMenuItem = new JCheckBoxMenuItem("Enable Robots");

        setupFileMenu();
        setupGameModeMenu();
        setupOptionMenu();

        menuBar.add(fileMenu);
        menuBar.add(gameModeMenu);
        menuBar.add(optionsMenu);
        menuBar.add(enableRobotsMenuItem);

        setJMenuBar(menuBar);
    }

    class MenuItemListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == enableRobotsMenuItem) {
                boolean enable = enableRobotsMenuItem.getState();

                applicationState.setRobotsActive(enable);

                chessModeMenuItem.setEnabled(enable);
                demoModeMenuItem.setEnabled(enable);
                rcModeMenuItem.setEnabled(enable);
                robotsDiscoveredMenuItem.setEnabled(enable);
            }

            if (e.getSource() == demoModeMenuItem) {
                resizeBoard(4, 8); // TODO allow the user to pick the size of the board
                boardPanel.initializeDemo();
                uiState.setDemoMode(true);
            }

            if (e.getSource() == rcModeMenuItem) {
                RemoteController rc = new RemoteController(robotState);
                rc.start();
            }

            if(e.getSource() == robotsDiscoveredMenuItem){
                boolean enable = robotsDiscoveredMenuItem.getState();
                discoveredRobotsFrame.setVisible(enable);
            }

            if (e.getSource() == exitMenuItem) {
                // TODO add an "are you sure option"
                // TODO implement the safe cleanup functionality for the network
                System.exit(0);
            }
        }
    }
}
