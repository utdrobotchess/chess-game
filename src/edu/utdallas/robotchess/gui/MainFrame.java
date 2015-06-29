package edu.utdallas.robotchess.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import edu.utdallas.robotchess.manager.*;
import edu.utdallas.robotchess.robotcommunication.*;
import edu.utdallas.robotchess.game.*;

public class MainFrame extends JFrame
{
    public final static int SQUARE_SIZE = 100;
    private static final long serialVersionUID = 3;

    Manager manager;
    private ChessbotCommunicator comm; //This probably should be a member of the manager class
    DiscoveredBotsFrame discoveredRobotsFrame; //Probably want this to be a panel
    BoardPanel boardPanel;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu optionsMenu;

    JMenuItem newGameMenuItem;
    JMenuItem newChessDemoMenuItem;
    JMenuItem newRCDemoMenuItem;
    JMenuItem exitMenuItem;

    JCheckBoxMenuItem enableRobotsMenuItem;
    JCheckBoxMenuItem robotsDiscoveredMenuItem;
    JCheckBoxMenuItem enableChessAIMenuItem;

    MenuItemListener menuListener;

    public MainFrame()
    {
        boardPanel = new BoardPanel(new NullManager());
        add(boardPanel);

        discoveredRobotsFrame = new DiscoveredBotsFrame();
        discoveredRobotsFrame.setVisible(false);

        setupMenuBar();
        setTitle("Robot Chess");
        setSize(8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setupFileMenu()
    {
        fileMenu = new JMenu("File");

        newGameMenuItem = new JMenuItem("New Game");
        newChessDemoMenuItem = new JMenuItem("New Chess Demo");
        newRCDemoMenuItem = new JMenuItem("New RC Demo");
        exitMenuItem = new JMenuItem("Exit");

        newChessDemoMenuItem.setEnabled(false);
        newRCDemoMenuItem.setEnabled(false);

        fileMenu.add(newGameMenuItem);
        fileMenu.add(newChessDemoMenuItem);
        fileMenu.add(newRCDemoMenuItem);
        fileMenu.add(exitMenuItem);

        newGameMenuItem.addActionListener(menuListener);
        newChessDemoMenuItem.addActionListener(menuListener);
        newRCDemoMenuItem.addActionListener(menuListener);
        exitMenuItem.addActionListener(menuListener);
    }

    private void setupOptionMenu()
    {
        optionsMenu = new JMenu("Options");

        enableRobotsMenuItem = new JCheckBoxMenuItem("Enable Robots");
        enableRobotsMenuItem.setEnabled(true);
        optionsMenu.add(enableRobotsMenuItem);
        enableRobotsMenuItem.addActionListener(menuListener);

        robotsDiscoveredMenuItem = new JCheckBoxMenuItem("View Discovered Robots");
        robotsDiscoveredMenuItem.setEnabled(false);
        optionsMenu.add(robotsDiscoveredMenuItem);
        robotsDiscoveredMenuItem.addActionListener(menuListener);

        enableChessAIMenuItem = new JCheckBoxMenuItem("Enable Chess AI");
        optionsMenu.add(enableChessAIMenuItem);
        enableChessAIMenuItem.addActionListener(menuListener);
    }

    private void setupMenuBar()
    {
        menuBar = new JMenuBar();
        menuListener = new MenuItemListener();

        setupFileMenu();
        setupOptionMenu();

        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);

        setJMenuBar(menuBar);
    }

    class MenuItemListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == newGameMenuItem) {
                boolean robotsEnabled = enableRobotsMenuItem.getState();

                if (robotsEnabled)
                    manager = new RobotChessManager();
                else
                    manager = new ChessManager();

                boardPanel.setManager(manager);
                boardPanel.updateDisplay();
            }

            if (e.getSource() == enableChessAIMenuItem) {
                if (manager instanceof ChessManager) {
                    boolean state = enableChessAIMenuItem.getState();
                    ((ChessManager) manager).setComputerControlsTeam(state, Team.GREEN);
                }
            }

            if (e.getSource() == newChessDemoMenuItem) {
                int[] robotsPresent = determineRobotsPresent();
                int[] initialLocations = generateInitialLocations(robotsPresent);
                manager = new RobotDemoManager(initialLocations);

                int boardRows = determineBoardRows();
                int boardColumns = determineBoardColumns();

                manager.setBoardRowCount(boardRows);
                manager.setBoardColumnCount(boardColumns);

                remove(boardPanel);
                boardPanel = new BoardPanel(manager);
                add(boardPanel);
                boardPanel.updateDisplay();
                setSize(boardColumns * SQUARE_SIZE, boardRows * SQUARE_SIZE);
            }

            if (e.getSource() == newRCDemoMenuItem) {
                ChessbotCommunicator comm = ChessbotCommunicator.create();
                RemoteController rc = new RemoteController(comm);
                JOptionPane.showMessageDialog(null, "Press OK to exit RC mode");
                rc.terminate();
            }

            if (e.getSource() == enableRobotsMenuItem) {
                boolean enable = enableRobotsMenuItem.getState();
                robotsDiscoveredMenuItem.setEnabled(enable);
                newChessDemoMenuItem.setEnabled(enable);
                newRCDemoMenuItem.setEnabled(enable);

                if(!enable)
                    discoveredRobotsFrame.setVisible(false);

                if (enable)
                    comm = ChessbotCommunicator.create();
            }

            if (e.getSource() == robotsDiscoveredMenuItem){
                 boolean enable = robotsDiscoveredMenuItem.getState();
                 discoveredRobotsFrame.setVisible(enable);
            }

            if (e.getSource() == exitMenuItem) {
                System.exit(0);
            }
        }

        private int[] determineRobotsPresent()
        {
            String robotsPresentStr = (String) JOptionPane.showInputDialog(
                "Please enter space-separated list of robots present",
                "e.g. 1 2 4 6");
            String[] robotsPresentStrArr = robotsPresentStr.split(" ");

            int[] robotsPresentIntArr = new int[robotsPresentStrArr.length];
            for (int i = 0; i < robotsPresentIntArr.length; i++)
                robotsPresentIntArr[i] = Integer.parseInt(robotsPresentStrArr[i]);

            return robotsPresentIntArr;
        }

        private int[] generateInitialLocations(int[] robotsPresent)
        {
            int[] locations = new int[32];

            for (int i = 0; i < locations.length; i++)
                locations[i] = -1;

            for (int i = 0; i < robotsPresent.length; i++)
                locations[robotsPresent[i]] = robotsPresent[i];

            return locations;
        }

        private int determineBoardRows()
        {
            Object[] possibleDimensions = {"2", "3", "4", "5", "6", "7", "8"};
            String boardRows = (String) JOptionPane.showInputDialog(
                (Component) null,
                "Please enter the number of board rows",
                "Board Rows",
                JOptionPane.PLAIN_MESSAGE,
                (Icon) null,
                possibleDimensions,
                "8");
            int boardRowCount = Integer.parseInt(boardRows);

            return boardRowCount;
         }

        private int determineBoardColumns()
        {
            Object[] possibleDimensions = {"2", "3", "4", "5", "6", "7", "8"};
            String boardColumns = (String) JOptionPane.showInputDialog(
                (Component) null,
                "Please enter the number of board columns",
                "Board Columns",
                JOptionPane.PLAIN_MESSAGE,
                (Icon) null,
                possibleDimensions,
                "8");
            int boardColumnCount = Integer.parseInt(boardColumns);

            return boardColumnCount;
         }
    }

    public static void main(String[] args)
    {
        MainFrame frame = new MainFrame();
    }
}
