/**
 *
 * @author Ryan J. Marcotte
 */

package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import manager.UIState;

public class MainFrame extends JFrame
{
    UIState uiState;
    
    public MainFrame(UIState uiState)
    {
        this.uiState = uiState;
        
        JPanel boardPanel = new BoardPanel(8, 8);
        add(boardPanel);
        
        setTitle("Robot Chess");
        setSize(800,800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
