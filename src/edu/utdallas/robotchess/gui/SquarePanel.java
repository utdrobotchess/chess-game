package edu.utdallas.robotchess.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class SquarePanel extends JPanel
{
    private int index;
    private JButton button;

    private final int BORDER_WIDTH = 2;
    private Border selectedBorder;
    private Border moveLocationBorder;

    protected SquarePanel(int index, Color color)
    {
        this.index = index;
        button = new SquareButton(index, color);

        selectedBorder = BorderFactory.createMatteBorder(BORDER_WIDTH,
                                                         BORDER_WIDTH,
                                                         BORDER_WIDTH,
                                                         BORDER_WIDTH,
                                                         new Color(0, 120, 0));

        moveLocationBorder = BorderFactory.createMatteBorder(BORDER_WIDTH,
                                                             BORDER_WIDTH,
                                                             BORDER_WIDTH,
                                                             BORDER_WIDTH,
                                                             new Color(0, 240, 0));

        setLayout(new GridLayout(1, 1));
        add(button);
    }

    protected boolean isOccupied()
    {
        return button.getIcon() != null;
    }

    protected void setSelectedPieceBorder()
    {
        setBorder(selectedBorder);
    }

    protected void setMoveLocationBorder()
    {
        setBorder(moveLocationBorder);
    }

    protected void clearBorder()
    {
        setBorder(null);
    }

    protected void setButtonActionListener(ActionListener listener)
    {
        button.addActionListener(listener);
    }

    protected void setButtonIcon(Icon icon)
    {
        button.setIcon(icon);
    }
}
