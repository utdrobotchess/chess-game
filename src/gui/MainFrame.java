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

public class MainFrame extends JFrame
{
    final int SQUARE_SIZE = 100;

    UIState uiState;

    JMenuBar menuBar;
    
    JMenu fileMenu;
    JMenu viewMenu;
    JMenu helpMenu;

    JMenuItem newGameMenuItem;
    JMenuItem demoModeMenuItem;
    JMenuItem exitMenuItem;

    MenuItemListener menuListener;
    
    public MainFrame(UIState uiState)
    {
        this.uiState = uiState;
        
        JPanel boardPanel = new BoardPanel(8, 8);
        add(boardPanel);

        setupMenuBar();
        setTitle("Robot Chess");
        setSize(8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected void resizeBoard(int rows, int columns)
    {
        JPanel boardPanel = new BoardPanel(rows, columns);
        add(boardPanel);

        setSize(rows * SQUARE_SIZE, columns * SQUARE_SIZE);
        setLocationRelativeTo(null);
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
                
            }
        }
    }
}
