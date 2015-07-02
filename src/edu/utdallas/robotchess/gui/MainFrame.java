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
    BoardPanel boardPanel;

    JMenuBar menuBar;

    JMenu gameMenu;
    JMenu chessbotMenu;
    JMenu optionsMenu;

    JMenuItem newGameMenuItem;
    JMenuItem newChessDemoMenuItem;
    JRadioButton playWithChessbotsButton;
    JRadioButton playWithoutChessbotsButton;

    ButtonGroup chessbotButtonGroup;

    JButton showConnectedChessbotButton;
    JButton connectToXbeeButton;
    JCheckBoxMenuItem enableChessAIMenuItem;

    MenuItemListener menuListener;

    public MainFrame()
    {
        boardPanel = new BoardPanel(new NullManager());
        comm = ChessbotCommunicator.create();

        setTitle("Robot Chess");
        setSize(8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMenuBar();
        add(boardPanel);
        setVisible(true);
    }

    private void setupMenuBar()
    {
        menuBar = new JMenuBar();
        menuListener = new MenuItemListener();
        gameMenu = new JMenu("Play Game");
        chessbotMenu = new JMenu("Chessbots");
        optionsMenu = new JMenu("Options");
        newGameMenuItem = new JMenuItem("New Chessgame");
        newChessDemoMenuItem = new JMenuItem("New Chessbot Demo");
        showConnectedChessbotButton = new JButton("Show Connected Chessbots");
        playWithChessbotsButton = new JRadioButton("Play with Chessbots");
        playWithoutChessbotsButton = new JRadioButton("Play without Chessbots");
        connectToXbeeButton = new JButton("Connect to Xbee");
        enableChessAIMenuItem = new JCheckBoxMenuItem("Enable Chess AI");

        chessbotButtonGroup = new ButtonGroup();
        chessbotButtonGroup.add(playWithChessbotsButton);
        chessbotButtonGroup.add(playWithoutChessbotsButton);

        gameMenu.add(newGameMenuItem);
        gameMenu.add(newChessDemoMenuItem);
        chessbotMenu.add(playWithChessbotsButton);
        chessbotMenu.add(playWithoutChessbotsButton);
        chessbotMenu.add(showConnectedChessbotButton);
        optionsMenu.add(enableChessAIMenuItem);

        gameMenu.setEnabled(false);
        showConnectedChessbotButton.setEnabled(false);
        connectToXbeeButton.setEnabled(false);
        enableChessAIMenuItem.setEnabled(false);

        newGameMenuItem.addActionListener(menuListener);
        newChessDemoMenuItem.addActionListener(menuListener);
        showConnectedChessbotButton.addActionListener(menuListener);
        playWithChessbotsButton.addActionListener(menuListener);
        playWithoutChessbotsButton.addActionListener(menuListener);
        connectToXbeeButton.addActionListener(menuListener);
        enableChessAIMenuItem.addActionListener(menuListener);

        menuBar.add(gameMenu);
        menuBar.add(chessbotMenu);
        menuBar.add(optionsMenu);
        menuBar.add(connectToXbeeButton);

        setJMenuBar(menuBar);
    }

    class MenuItemListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == playWithChessbotsButton) {
                gameMenu.setEnabled(true);
                connectToXbeeButton.setEnabled(true);
                enableChessAIMenuItem.setEnabled(true);
                showConnectedChessbotButton.setEnabled(true);
                newChessDemoMenuItem.setEnabled(true);
            }

            if (e.getSource() == playWithoutChessbotsButton) {
                gameMenu.setEnabled(true);
                connectToXbeeButton.setEnabled(false);
                enableChessAIMenuItem.setEnabled(true);
                showConnectedChessbotButton.setEnabled(false);
                newChessDemoMenuItem.setEnabled(false);

                if (comm.isConnected())
                    comm.endCommnication();
            }

            if (e.getSource() == newGameMenuItem) {
                if (playWithChessbotsButton.isSelected())
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

            if (e.getSource() == connectToXbeeButton) {

                if(comm.isConnected())
                    JOptionPane.showMessageDialog(null, "XBee is connected");
                else
                {
                    comm.SearchForXbeeOnComports();

                    if(comm.isConnected())
                        JOptionPane.showMessageDialog(null, "Successfully connected to Xbee");
                    else
                        JOptionPane.showMessageDialog(null, "Could not connect to Xbee. " +
                                "Try again after unplugging and plugging in the Xbee. " +
                                "If this does not work, restart the app.");
                }

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
