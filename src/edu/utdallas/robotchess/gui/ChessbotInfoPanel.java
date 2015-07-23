package edu.utdallas.robotchess.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.utdallas.robotchess.robotcommunication.ChessbotInfo;
import edu.utdallas.robotchess.robotcommunication.ChessbotInfoArrayHandler;

public class ChessbotInfoPanel extends JPanel
{
    private final static Logger log = Logger.getLogger(ChessbotInfoPanel.class);
    private static final long serialVersionUID = 1;

    public final static int CHESSBOT_INFO_PANEL_WIDTH = 400;
    public final static int CHESSBOT_INFO_PANEL_HEIGHT = 300;

    private JTable table;
    private ChessbotInfoTableModel chessbotInfoTableModel;

    private ChessbotInfoArrayHandler chessbots;
    private ChessbotInfoThread chessbotInfoThread;

    public ChessbotInfoPanel() {
        super(new GridLayout(1,0));

        PropertyConfigurator.configure("log/log4j.properties");

        chessbotInfoThread = new ChessbotInfoThread();
        chessbots = new ChessbotInfoArrayHandler();

        chessbotInfoTableModel = new ChessbotInfoTableModel();
        table = new JTable(chessbotInfoTableModel);

        chessbotInfoThread.start();

        setOpaque(true);
        setSize(CHESSBOT_INFO_PANEL_WIDTH, CHESSBOT_INFO_PANEL_HEIGHT);

        table.setShowGrid(false);
        table.setPreferredScrollableViewportSize(new Dimension(CHESSBOT_INFO_PANEL_WIDTH,
                    CHESSBOT_INFO_PANEL_HEIGHT));

        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
        add(scrollPane);

        updateDisplay();
    }

    private void updateDisplay()
    {
        chessbotInfoTableModel.setData(chessbots.toObjectArray());

        adjustJTableRowSizes(table);

        for (int i = 0; i < table.getColumnCount(); i++) {
            adjustColumnSizes(table, i, 2);
        }
    }

    private void adjustJTableRowSizes(JTable jTable) {
        for (int row = 0; row < jTable.getRowCount(); row++) {
            int maxHeight = 0;

            for (int column = 0; column < jTable.getColumnCount(); column++) {
                TableCellRenderer cellRenderer = jTable.getCellRenderer(row, column);
                Object valueAt = jTable.getValueAt(row, column);
                Component tableCellRendererComponent = cellRenderer.getTableCellRendererComponent(jTable, valueAt, false, false, row, column);
                int heightPreferable = tableCellRendererComponent.getPreferredSize().height;
                maxHeight = Math.max(heightPreferable, maxHeight);
            }

            jTable.setRowHeight(row, maxHeight);
        }

    }

    private void adjustColumnSizes(JTable table, int column, int margin) {
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(column);
        int width;

        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }

        Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, column);
            comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, column), false, false, r, column);
            int currentWidth = comp.getPreferredSize().width;
            width = Math.max(width, currentWidth);
        }

        width += 2 * margin;

        col.setPreferredWidth(width);
        col.setWidth(width);
    }

    public void setChessbotInfoArrayHandler(ChessbotInfoArrayHandler chessbots) {
        this.chessbots = chessbots;
    }

    class ChessbotInfoTableModel extends AbstractTableModel {

        private String[] columnNames = ChessbotInfo.CHESSBOT_INFO_COLUMNS;
        private Object[][] data = new Object[1][columnNames.length];
        private static final long serialVersionUID = 1;

        public String getColumnName(int col) {
            return columnNames[col].toString();
        }

        public int getRowCount() {
            return data.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

        public void setData(Object[][] data) {
            this.data = data;
        }
    }

    //I should implement Runnable instead of extending Thread, but
    //I don't know how to properly do so at the moment.
    class ChessbotInfoThread extends Thread {

        @Override
        public void run() {

            while(true) {
                if(chessbots.isUpdated()) {
                    updateDisplay();
                    chessbots.setUpdatedFlag(false);
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
