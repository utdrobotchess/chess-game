/**
 *
 * @author Ryan J. Marcotte
 */

package edu.utdallas.robotchess.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenu;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import java.util.ArrayList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.utdallas.robotchess.manager.UIState;
import edu.utdallas.robotchess.manager.RobotState;
import edu.utdallas.robotchess.manager.ApplicationState;
import edu.utdallas.robotchess.robot.RemoteController;

public class MainFrame extends JFrame
{
    public final static int SQUARE_SIZE = 100;

    UIState uiState;
    RobotState robotState;
    ApplicationState applicationState;

    JMenuBar menuBar;

    JMenu fileMenu;
    JMenu optionsMenu;

    JMenuItem newGameMenuItem;
    JMenuItem exitMenuItem;

    JCheckBoxMenuItem enableRobotsMenuItem;
    JRadioButtonMenuItem gameModeMenuItem;
    JRadioButtonMenuItem demoModeMenuItem;
    JRadioButtonMenuItem rcModeMenuItem;
    JRadioButtonMenuItem robotsDiscoveredMenuItem;

    MenuItemListener menuListener;

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
        optionsMenu = new JMenu("Options");

        newGameMenuItem = new JMenuItem("New Game");
        exitMenuItem = new JMenuItem("Exit");

        enableRobotsMenuItem = new JCheckBoxMenuItem("Enable Robots");
        gameModeMenuItem = new JRadioButtonMenuItem("Game Mode");
        demoModeMenuItem = new JRadioButtonMenuItem("Demo Mode");
        rcModeMenuItem = new JRadioButtonMenuItem("RC Mode");
        robotsDiscoveredMenuItem = new JRadioButtonMenuItem("Discovered Robots");

        ButtonGroup bg = new ButtonGroup();
        bg.add(gameModeMenuItem);
        bg.add(demoModeMenuItem);
        bg.add(rcModeMenuItem);
        bg.add(robotsDiscoveredMenuItem);

        gameModeMenuItem.setEnabled(false);
        demoModeMenuItem.setEnabled(false);
        rcModeMenuItem.setEnabled(false);
        robotsDiscoveredMenuItem.setEnabled(false);

        fileMenu.add(newGameMenuItem);
        fileMenu.add(exitMenuItem);

        optionsMenu.add(enableRobotsMenuItem);
        optionsMenu.add(gameModeMenuItem);
        optionsMenu.add(demoModeMenuItem);
        optionsMenu.add(rcModeMenuItem);
        optionsMenu.add(robotsDiscoveredMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);

        setJMenuBar(menuBar);

        menuListener = new MenuItemListener();

        newGameMenuItem.addActionListener(menuListener);
        exitMenuItem.addActionListener(menuListener);

        enableRobotsMenuItem.addActionListener(menuListener);
        demoModeMenuItem.addActionListener(menuListener);
        rcModeMenuItem.addActionListener(menuListener);
        robotsDiscoveredMenuItem.addActionListener(menuListener);
    }

    class MenuItemListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == enableRobotsMenuItem) {
                boolean enable = enableRobotsMenuItem.getState();

                applicationState.setRobotsActive(enable);

                gameModeMenuItem.setEnabled(enable);
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
                DefaultListModel<String> listModel = new DefaultListModel<String>();
                ArrayList<String> botList = robotState.getBotList();

                for (String bot : botList) {
                    listModel.addElement(bot);
                }

                JFrame frame = new JFrame();
                frame.setSize(300, 100);

                JList<String> botListJList = new JList<String>(listModel);
                frame.add(botListJList);
                frame.setVisible(true);
            }

            if (e.getSource() == exitMenuItem) {
                // TODO add an "are you sure option"
                // TODO implement the safe cleanup functionality for the network
                System.exit(0);
            }
        }
    }
}
