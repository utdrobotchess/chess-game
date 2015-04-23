package edu.utdallas.robotchess.gui;

import javax.swing.*;
import java.awt.*;

public class SquareButton extends JButton
{
    private int index;

    public SquareButton(int index, Color color)
    {
        this.index = index;
        setBackground(color);
        setBorderPainted(false);
        setOpaque(true);
    }

    protected int getIndex()
    {
        return index;
    }
}
