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
    protected SquareButton(Color color)
    {
        setBackground(color);
        setBorderPainted(false);
    }
}
