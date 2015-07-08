package edu.utdallas.robotchess.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.utdallas.robotchess.robotcommunication.ChessbotInfoArrayHandler;

//Ideally, we would add another panel to the right of the Board Panel on
//MainFrame. However, I'm creating a new frame because it seems easier than
//modifying MainFrame any time we want to view discovered bots
public class ChessbotInfoFrame extends JFrame
{
    private static final long serialVersionUID = 4;

    private ChessbotInfoPanel chessbotInfoPanel;

    private ChessbotInfoArrayHandler chessbots;

    private ChessbotInfoThread chessbotInfoThread;

    public ChessbotInfoFrame() {
        chessbotInfoPanel = new ChessbotInfoPanel();
        chessbotInfoThread = new ChessbotInfoThread();
        chessbots = new ChessbotInfoArrayHandler();

        chessbotInfoThread.start();

        chessbotInfoPanel.setOpaque(true);

        setTitle("Previously Connected Chessbots");
        setContentPane(chessbotInfoPanel);
        pack();
    }

    public void setChessbotInfoArrayHandler(ChessbotInfoArrayHandler h) {
        this.chessbots = h;
    }

    //As of now, this thread is updating the table every second when the panel
    //is showing. I haven't implemented
    //ChessbotCommunicator.checkIfChessbotUpdate(), so it always returns true.
    //Also, I should implement Runnable instead of extending Thread, but
    //I don't know how to properly do so at the moment.
    class ChessbotInfoThread extends Thread {

        @Override
        public void run() {

            while(true) {
                if(chessbots != null && chessbots.isUpdated()) {
                    chessbotInfoPanel.updateChessbotInfo();
                    chessbots.setUpdatedFlag(false);
                    System.out.println("Updating"); //temp
                }

                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    class ChessbotInfoPanel extends JPanel
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

            updateChessbotInfo();
        }

        public void updateChessbotInfo() {
            //Should check size to ensure that properly update the table

            //Also, there might be a cleaner way of updating the table. Using
            //a method to replace the data, for example, instead of just creating
            //a new one.
            Object[][] data;

            if (chessbots == null)
                data = new Object[][] {{null, null, null, null, null}};
            else
                data = chessbots.toObjectArray();

            table = new JTable(data, columnNames);

            table.setShowGrid(false);
            table.setPreferredScrollableViewportSize(new Dimension(400, 800));
            table.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(table);
            removeAll();
            add(scrollPane);

        }
    }

}

