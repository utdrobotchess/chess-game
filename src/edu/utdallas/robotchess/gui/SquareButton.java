package edu.utdallas.robotchess.gui;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Dimension;

public class SquareButton extends JButton
{
    int index;
    int occupantID;

    protected SquareButton(int index, Color color)
    {
        this.index = index;
        occupantID = -1;

        setBackground(color);
        setBorderPainted(false);
    }

    protected int getOccupantID()
    {
        return occupantID;
    }

    protected int getIndex()
    {
        return index;
    }

    protected boolean isOccupied()
    {
        return getIcon() != null;
    }

    protected void setOccupantID(int occupantID)
    {
        this.occupantID = occupantID;
    }
}
