package edu.utdallas.robotchess.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import edu.utdallas.robotchess.game.Team;
import edu.utdallas.robotchess.manager.ChessManager;
import edu.utdallas.robotchess.manager.Manager;
import edu.utdallas.robotchess.manager.NullManager;
import edu.utdallas.robotchess.manager.RobotChessManager;
import edu.utdallas.robotchess.manager.RobotDemoManager;

public class MainFrame extends JFrame
{
    public final static int SQUARE_SIZE = 100;
    private static final long serialVersionUID = 0;

    Manager manager;
    BoardPanel boardPanel;
    ChessbotInfoPanel chessbotInfoPanel;
    JFrame chessbotInfoPanelFrame;

    JMenuBar menuBar;

    JMenu gameMenu;
    JMenu chessbotMenu;
    JMenu optionsMenu;

    JMenuItem newGameMenuItem;
    JMenuItem newChessDemoMenuItem;
    JRadioButton playWithChessbotsButton;
    JRadioButton playWithoutChessbotsButton;

    ButtonGroup chessbotButtonGroup;

    JToggleButton enableChessAIMenuItem;
    JButton showConnectedChessbotButton;
    JButton connectToXbeeButton;

    MenuItemListener menuListener;

    public MainFrame()
    {
        boardPanel = new BoardPanel(new NullManager());
        manager = new NullManager();
        chessbotInfoPanel = new ChessbotInfoPanel();
        chessbotInfoPanelFrame = new JFrame();

        chessbotInfoPanel.setOpaque(true);
        chessbotInfoPanelFrame.setContentPane(chessbotInfoPanel);

        chessbotInfoPanelFrame.pack();

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
        showConnectedChessbotButton = new JButton("Show Chessbot Info");
        playWithChessbotsButton = new JRadioButton("Play with Chessbots");
        playWithoutChessbotsButton = new JRadioButton("Play without Chessbots");
        connectToXbeeButton = new JButton("Connect to Xbee");
        enableChessAIMenuItem = new JToggleButton("Enable Chess AI");

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

    private void switchManager(Manager manager) {
        manager.setComm(this.manager.getComm());
        this.manager = manager;
        boardPanel.setManager(this.manager);
        toggleAI(false);
        boardPanel.updateDisplay();
    }

    private void toggleAI(boolean enabled) {
        if (enabled)
            enableChessAIMenuItem.setText("Disable Chess AI");
        else
            enableChessAIMenuItem.setText("Enable Chess AI");

        if (manager instanceof ChessManager)
            ((ChessManager) manager).setComputerControlsTeam(enabled, Team.GREEN);
        else if (manager instanceof RobotChessManager) {
            //TODO: Implement
        }

        enableChessAIMenuItem.setSelected(enabled);
    }

    class MenuItemListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == playWithChessbotsButton) {
                gameMenu.setEnabled(true);
                connectToXbeeButton.setEnabled(true);
                enableChessAIMenuItem.setEnabled(false);
                showConnectedChessbotButton.setEnabled(true);
                newChessDemoMenuItem.setEnabled(true);
                switchManager(new NullManager());
            }

            if (e.getSource() == playWithoutChessbotsButton) {
                gameMenu.setEnabled(true);
                connectToXbeeButton.setEnabled(false);
                enableChessAIMenuItem.setEnabled(false);
                showConnectedChessbotButton.setEnabled(false);
                newChessDemoMenuItem.setEnabled(false);
                chessbotInfoPanelFrame.setVisible(false);
                switchManager(new NullManager());
            }

            if (e.getSource() == newGameMenuItem) {
                if (playWithChessbotsButton.isSelected())
                {
                    if(manager.checkIfAllChessbotsAreConnected()) {
                        switchManager(new RobotChessManager());
                        enableChessAIMenuItem.setEnabled(true);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "All Chessbots need to be connected " +
                            "in order to play a chessgame with them. To check how many are " +
                            "currently connected, go to Options > Show Chessbot Info",
                            "Not enough Chessbots connected",
                            JOptionPane.WARNING_MESSAGE);

                }
                else {
                    switchManager(new ChessManager());
                    enableChessAIMenuItem.setEnabled(true);
                }
            }

            if (e.getSource() == enableChessAIMenuItem) {
                boolean state = enableChessAIMenuItem.isSelected();
                toggleAI(state);

                //Will probably only need to see if instanceof ChessManager or
                //RobotChessManager, as they should both have the
                //setComputerControlsTeam() method

                //Add dialogue later for choosing team for AI as well as
                //choosing difficulty
            }

            //Will change this so that the available robots are queried and
            //shown here.
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

            if (e.getSource() == showConnectedChessbotButton)
                chessbotInfoPanelFrame.setVisible(true);

            if (e.getSource() == connectToXbeeButton) {

                boolean xbeeConnected = manager.isXbeeConnected();

                if(manager.isXbeeConnected())
                    JOptionPane.showMessageDialog(null, "Xbee is connected");
                else
                {
                    xbeeConnected = manager.connectToXbee();

                    if(xbeeConnected)
                        JOptionPane.showMessageDialog(null, "Successfully connected to Xbee");
                    else
                        JOptionPane.showMessageDialog(null, "Could not connect to Xbee. " +
                                "Try again after unplugging and plugging in the Xbee. " +
                                "If this does not work, restart the app.",
                                "Cannot find Xbee",
                                JOptionPane.WARNING_MESSAGE);
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
        @SuppressWarnings("unused")
        MainFrame frame = new MainFrame();
    }
}
