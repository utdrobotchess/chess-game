package edu.utdallas.robotchess.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
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
    public final static int CHESSBOT_INFO_PANEL_WIDTH = 300;
    public final static int CHESSBOT_INFO_PANEL_HEIGHT = 300;
    private static final long serialVersionUID = 0;

    Manager manager;
    BoardPanel boardPanel;
    ChessbotInfoPanel chessbotInfoPanel;

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
    JToggleButton showConnectedChessbotButton;
    JButton connectToXbeeButton;
    JButton discoverChessbotsButton;

    MenuItemListener menuListener;

    public MainFrame()
    {
        manager = new NullManager();
        boardPanel = new BoardPanel(manager);
        chessbotInfoPanel = new ChessbotInfoPanel();

        setTitle("Robot Chess");
        setSize(8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMenuBar();
        add(boardPanel, BorderLayout.CENTER);
        add(chessbotInfoPanel, BorderLayout.EAST);

        chessbotInfoPanel.setVisible(false);
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
        showConnectedChessbotButton = new JToggleButton("Show Chessbot Info");
        playWithChessbotsButton = new JRadioButton("Play with Chessbots");
        playWithoutChessbotsButton = new JRadioButton("Play without Chessbots");
        connectToXbeeButton = new JButton("Connect to Xbee");
        enableChessAIMenuItem = new JToggleButton("Enable Chess AI");
        discoverChessbotsButton = new JButton("Discover Chessbots");

        // Figure out how to do keyboard shortcuts
        // gameMenu.setMnemonic(KeyEvent.VK_G);

        chessbotButtonGroup = new ButtonGroup();
        chessbotButtonGroup.add(playWithChessbotsButton);
        chessbotButtonGroup.add(playWithoutChessbotsButton);

        gameMenu.add(newGameMenuItem);
        gameMenu.add(newChessDemoMenuItem);
        chessbotMenu.add(playWithChessbotsButton);
        chessbotMenu.add(playWithoutChessbotsButton);
        chessbotMenu.add(showConnectedChessbotButton);
        optionsMenu.add(enableChessAIMenuItem);
        optionsMenu.add(discoverChessbotsButton);

        gameMenu.setEnabled(false);
        showConnectedChessbotButton.setEnabled(false);
        connectToXbeeButton.setEnabled(false);
        enableChessAIMenuItem.setEnabled(false);
        discoverChessbotsButton.setEnabled(false);

        newGameMenuItem.addActionListener(menuListener);
        newChessDemoMenuItem.addActionListener(menuListener);
        showConnectedChessbotButton.addActionListener(menuListener);
        playWithChessbotsButton.addActionListener(menuListener);
        playWithoutChessbotsButton.addActionListener(menuListener);
        connectToXbeeButton.addActionListener(menuListener);
        enableChessAIMenuItem.addActionListener(menuListener);
        discoverChessbotsButton.addActionListener(menuListener);

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

    private void toggleChessbotInfo(boolean enabled) {
        if (enabled) {
            showConnectedChessbotButton.setText("Hide Chessbot Info");
            setSize(getWidth() + CHESSBOT_INFO_PANEL_WIDTH, getHeight());
        }
        else {
            showConnectedChessbotButton.setText("Show Chessbot Info");
            if (chessbotInfoPanel.isShowing())
                setSize(getWidth() - CHESSBOT_INFO_PANEL_WIDTH, getHeight());
        }

        chessbotInfoPanel.setVisible(enabled);
        showConnectedChessbotButton.setSelected(enabled);
    }

    private void toggleAI(boolean enabled) {
        if (enabled)
            enableChessAIMenuItem.setText("Disable Chess AI");
        else
            enableChessAIMenuItem.setText("Enable Chess AI");

        //Will probably only need to see if instanceof ChessManager or
        //RobotChessManager, as they should both have the
        //setComputerControlsTeam() method

        //Add dialogue later for choosing team for AI as well as
        //choosing difficulty

        if (manager instanceof ChessManager)
            ((ChessManager) manager).setComputerControlsTeam(enabled, Team.GREEN);
        else if (manager instanceof RobotChessManager) {
            //TODO: Implement
        }

        enableChessAIMenuItem.setSelected(enabled);
    }

    private static void createAndShowGUI()
    {
        @SuppressWarnings("unused")
        MainFrame frame = new MainFrame();
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
                discoverChessbotsButton.setEnabled(true);
                switchManager(new NullManager());
            }

            if (e.getSource() == playWithoutChessbotsButton) {
                gameMenu.setEnabled(true);
                connectToXbeeButton.setEnabled(false);
                enableChessAIMenuItem.setEnabled(false);
                showConnectedChessbotButton.setEnabled(false);
                newChessDemoMenuItem.setEnabled(false);
                toggleChessbotInfo(false);
                discoverChessbotsButton.setEnabled(false);
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
                        JOptionPane.showMessageDialog(null, "All Chessbots need to "
                            + "be connected in order to play a chessgame with them."
                            + " To check how many are currently connected, go to "
                            + "Options > Show Chessbot Info",
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
            }

            if (e.getSource() == showConnectedChessbotButton)
                toggleChessbotInfo(showConnectedChessbotButton.isSelected());

            if (e.getSource() == connectToXbeeButton) {
                boolean xbeeConnected = manager.isXbeeConnected();

                if(manager.isXbeeConnected())
                    JOptionPane.showMessageDialog(null, "Xbee is connected");
                else
                {
                    xbeeConnected = manager.connectToXbee();

                    if(xbeeConnected) {
                        chessbotInfoPanel.setChessbotInfoArrayHandler(manager.getChessbotInfo());
                        JOptionPane.showMessageDialog(null, "Successfully connected to Xbee");
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Could not connect to Xbee. " +
                                "Try again after unplugging and plugging in the Xbee. " +
                                "If this does not work, restart the app.",
                                "Cannot find Xbee",
                                JOptionPane.WARNING_MESSAGE);
                }

            }

            if (e.getSource() == discoverChessbotsButton) {

                if (manager.isXbeeConnected()) {
                    if(manager.isDiscoveringChessbots()) {
                        JOptionPane.showMessageDialog(null, "Currently Discovering Chessbots",
                                "Chessbot Discovery Underway",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else
                        manager.discoverChessbots();

                }
                else
                    JOptionPane.showMessageDialog(null, "Could not connect to Xbee. " +
                            "Try again after unplugging and plugging in the Xbee. " +
                            "If this does not work, restart the app.",
                            "Cannot find Xbee",
                            JOptionPane.WARNING_MESSAGE);
            }

            if (e.getSource() == newChessDemoMenuItem) {
                if (manager.isXbeeConnected() == false) {
                    JOptionPane.showMessageDialog(null, "Xbee must be connected in order to " +
                        "choose this game type.",
                        "Xbee not Connected",
                        JOptionPane.WARNING_MESSAGE);

                }
                else {
                    ArrayList<String> robotsPresent = manager.getChessbotInfo().getRobotsPresent();

                    int boardRows = determineBoardRows();
                    int boardColumns = determineBoardColumns();

                    int[] userSelection = offerUserRobotSelection(robotsPresent,
                                                                    boardRows,
                                                                    boardColumns);
                    int[] initialLocations = generateInitialLocations(userSelection);

                    switchManager(new RobotDemoManager(initialLocations));

                    manager.setBoardRowCount(boardRows);
                    manager.setBoardColumnCount(boardColumns);

                    remove(boardPanel);
                    boardPanel = new BoardPanel(manager);
                    add(boardPanel);
                    boardPanel.updateDisplay();
                    setSize(boardColumns * SQUARE_SIZE, boardRows * SQUARE_SIZE);
                }
            }

        }

        private int[] offerUserRobotSelection(ArrayList<String> robotsPresent,
                                                int boardRows,
                                                int boardColumns)
        {
            ArrayList<String> impossibleRobotStartingLocations = new ArrayList<String>(boardRows * boardColumns);

            for (int i = 0; i < 64; i++)
                if (i >= 8 * boardRows || (i % 8) >= boardColumns)
                    impossibleRobotStartingLocations.add(Integer.toString(i));

            for (int i = 0; i < robotsPresent.size(); i++) {
                int tempBotId = Integer.parseInt(robotsPresent.get(i));
                if (tempBotId > 16)
                    robotsPresent.set(i, Integer.toString(tempBotId + 32));
            }

            robotsPresent.removeAll(impossibleRobotStartingLocations);

            for (int i = 0; i < robotsPresent.size(); i++) {
                int tempBotId = Integer.parseInt(robotsPresent.get(i));
                if (tempBotId > 16)
                    robotsPresent.set(i, Integer.toString(tempBotId - 32));
            }

            JList<Object> list = new JList<Object>(robotsPresent.toArray());

            JOptionPane.showMessageDialog(null, list, "Choose Available Chessbots",
                    JOptionPane.PLAIN_MESSAGE);

            int[] selectedIndices = list.getSelectedIndices();
            int[] userSelection = new int[selectedIndices.length];
            int index = 0;

            for (int i : selectedIndices)
                userSelection[index++] = Integer.parseInt(robotsPresent.get(i));

            return userSelection;
        }

        private int[] generateInitialLocations(int[] robotsPresent)
        {
            int[] locations = new int[32];

            for (int i = 0; i < locations.length; i++)
                locations[i] = -1;

            for (int i = 0; i < robotsPresent.length; i++) {
                if (robotsPresent[i] > 15)
                    locations[robotsPresent[i]] = robotsPresent[i] + 32;
                else
                    locations[robotsPresent[i]] = robotsPresent[i];

            }

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
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
