/**
 *
 * @author Ryan J. Marcotte
 */

package gui;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardPanel extends JPanel
{
    JButton squares[];
    
    protected BoardPanel(int rows, int columns)
    {
        setLayout(new GridLayout(rows, columns));
        
        squares = new SquareButton[rows * columns];

        boolean whiteSquare = true;

        for (int i = 0; i < squares.length; i++) {
            Color squareColor = whiteSquare ? Color.WHITE : Color.BLACK;
            squares[i] = new SquareButton(squareColor);
            squares[i].addActionListener(new ButtonListener());
            add(squares[i]);

            if (i % 8 != 7)
                whiteSquare = !whiteSquare;
        }
    }

    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("button clicked");
        }
    }
}
