package edu.utdallas.robotchess.gui;

import javax.swing.*;
import java.awt.event.*;

import edu.utdallas.robotchess.manager.*;
import edu.utdallas.robotchess.robot.ChessbotCommunicator;
import edu.utdallas.robotchess.robot.RemoteController;

public class MainFrame extends JFrame
{
    public final static int SQUARE_SIZE = 100;
    private static final long serialVersionUID = 3;
    
    Manager manager;

    private ChessbotCommunicator comm;

    JMenuBar menuBar;

    JMenu fileMenu;
    JMenu optionsMenu;

    JMenuItem newGameMenuItem;
    JMenuItem newChessDemoMenuItem;
    JMenuItem newRCDemoMenuItem;
    JMenuItem exitMenuItem;

    JCheckBoxMenuItem enableRobotsMenuItem;
    JCheckBoxMenuItem robotsDiscoveredMenuItem;

    MenuItemListener menuListener;

    DiscoveredBotsFrame discoveredRobotsFrame;

    BoardPanel boardPanel;

    public MainFrame(ChessbotCommunicator comm)
    {
        boardPanel = new BoardPanel(new NullManager());
        add(boardPanel);

        this.comm = comm;

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
        //RemoteController rc = new RemoteController();

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
            }
            
            if (e.getSource() == newChessDemoMenuItem) {
                // display a JOptionPane asking the user for the board dimensions
                manager = new RobotDemoManager();
            }
            
            if (e.getSource() == newRCDemoMenuItem) {
                // create a remote controller
                // pass it the communicator
                // display a JOptionPane allowing the user to end
            }

            if (e.getSource() == enableRobotsMenuItem) {
                boolean enable = enableRobotsMenuItem.getState();
                robotsDiscoveredMenuItem.setEnabled(enable);
                newChessDemoMenuItem.setEnabled(enable);
                newRCDemoMenuItem.setEnabled(enable);

                if(!enable)
                    discoveredRobotsFrame.setVisible(false);
                
                System.out.println("enable the robots here");
            }

            // if(e.getSource() == demoModeMenuItem || 
            //    e.getSource() == rcModeMenuItem || 
            //    e.getSource() == chessModeMenuItem){
            //     boolean enableDemo = demoModeMenuItem.isSelected();
            //     boolean enableRC = rcModeMenuItem.isSelected();
            //     boolean enableChess = chessModeMenuItem.isSelected();
            //     if(enableDemo) {
            //         resizeBoard(4, 8); // TODO allow the user to pick the size of the board
            //         boardPanel.initializeDemo();
            //     }

            //     // if(enableRC)
            //     //     rc.start();
            //     // else
            //     //     rc.terminate();
            // }

            if(e.getSource() == robotsDiscoveredMenuItem){
                 boolean enable = robotsDiscoveredMenuItem.getState();
                 discoveredRobotsFrame.setVisible(enable);
            }

            if (e.getSource() == exitMenuItem) {
                System.exit(0);
            }
        }
    }
    
    public static void main(String[] args)
    {
        MainFrame frame = new MainFrame();
    }
}
