package edu.utdallas.robotchess.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.utdallas.robotchess.robotcommunication.ChessbotInfoArrayHandler;

public class ChessbotInfoPanel extends JPanel
{
    private final static Logger log = Logger.getLogger(ChessbotInfoPanel.class);
    private static final long serialVersionUID = 1;
    private JTable table;
    private String[] columnNames;

    private ChessbotInfoArrayHandler chessbots;
    private ChessbotInfoThread chessbotInfoThread;


    public ChessbotInfoPanel() {
        super(new GridLayout(1,0));

        PropertyConfigurator.configure("log/log4j.properties");

        chessbotInfoThread = new ChessbotInfoThread();
        chessbots = new ChessbotInfoArrayHandler();

        chessbotInfoThread.start();

        columnNames = new String[] {"ID #",
            "XBee Address",
            "Last DeliveryStatus",
            "Last Communication Time",
            "Last Message Sent To",
            "Last Message Recieved From"};

        setOpaque(true);
        setSize(MainFrame.CHESSBOT_INFO_PANEL_WIDTH, MainFrame.CHESSBOT_INFO_PANEL_HEIGHT);

        updateChessbotInfo();
    }

    public void updateChessbotInfo() {
        //TODO: Should check size to ensure that properly update the table

        //TODO: There might be a cleaner way of updating the table. Using
        //a method to replace the data, for example, instead of just creating
        //a new one.
        Object[][] data;

        if (chessbots == null)
            data = new Object[][] {{null, null, null, null, null, null}};
        else
            data = chessbots.toObjectArray();

        table = new JTable(data, columnNames);

        TableColumn column = null;

        column = table.getColumnModel().getColumn(0);
        column.setMaxWidth(41);
        column.setMinWidth(40);
        column = table.getColumnModel().getColumn(1);
        column.setMaxWidth(301);
        column.setMinWidth(300);
        column = table.getColumnModel().getColumn(2);
        column.setMaxWidth(121);
        column.setMinWidth(120);
        column = table.getColumnModel().getColumn(3);
        column.setMaxWidth(171);
        column.setMinWidth(170);
        column = table.getColumnModel().getColumn(4);
        column.setMaxWidth(1201);
        column.setMinWidth(1200);
        column = table.getColumnModel().getColumn(5);
        column.setMaxWidth(1201);
        column.setMinWidth(1200);

        table.setShowGrid(false);
        table.setPreferredScrollableViewportSize(new Dimension(MainFrame.CHESSBOT_INFO_PANEL_WIDTH,
                    MainFrame.CHESSBOT_INFO_PANEL_HEIGHT));

        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);

        removeAll();
        add(scrollPane);
    }

    public void setChessbotInfoArrayHandler(ChessbotInfoArrayHandler h) {
        this.chessbots = h;
    }

    //I should implement Runnable instead of extending Thread, but
    //I don't know how to properly do so at the moment.
    class ChessbotInfoThread extends Thread {

        @Override
        public void run() {

            while(true) {
                if(chessbots != null && chessbots.isUpdated()) {
                    updateChessbotInfo();
                    chessbots.setUpdatedFlag(false);
                    log.debug("Updating Frame");
                }

                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    log.debug("Thread Interrupted", e);
                }
            }
        }
    }


}

