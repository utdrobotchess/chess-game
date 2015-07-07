package edu.utdallas.robotchess.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ChessbotInfoPanel extends JPanel
{
    private static final long serialVersionUID = 1;
    private JTable table;
    private String[] columnNames;

    public ChessbotInfoPanel() {
        super(new GridLayout(1,0));

        columnNames = new String[] {"Chessbot #",
            "XBeeAddress",
            "Last Communication Time",
            "Last Message Sent To",
            "Last Message Recieved From"};

        Object[][] data = {{null, null, null, null, null}};
        updateChessbotInfo(data);
    }

    public void updateChessbotInfo(Object[][] data) {
        //Should check size to ensure that properly update the table

        //Also, there might be a cleaner way of updating the table. Using
        //a method to replace the data, for example, instead of just creating
        //a new one.
        System.out.print("Making new table");
        table = new JTable(data, columnNames);

        table.setShowGrid(false);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        removeAll();
        add(scrollPane);

    }
}
