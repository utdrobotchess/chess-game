/**
 *
 * @author Ryan J. Marcotte
 */

package gui;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;

public class SquareButton extends JButton
{
    int index;
    
    protected SquareButton(int index, Color color)
    {
        this.index = index;
        
        setBackground(color);
        setBorderPainted(false);
    }

    protected int getIndex()
    {
        return index; 
    }
}
