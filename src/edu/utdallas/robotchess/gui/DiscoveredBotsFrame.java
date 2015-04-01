package edu.utdallas.robotchess.gui;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DiscoveredBotsFrame extends JFrame implements PropertyChangeListener
{
    private static final long serialVersionUID = 1;

    JList<String> botJList;
    DefaultListModel<String> listModel = new DefaultListModel<String>();

    public DiscoveredBotsFrame()
    {
        setTitle("Discovered Bots List");

        updateBotList();
    }

    private void updateBotList()
    {
        ArrayList<String> botList = new ArrayList<>(); //robotState.getBotList();
        setSize(300, 100 * botList.size());

        for (String bot : botList)
            listModel.addElement(bot);

        botJList = new JList<String>(listModel);
        add(botJList);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt)
    {
        if (!SwingUtilities.isEventDispatchThread())
        {
            SwingUtilities.invokeLater(new Runnable() { public void run() { propertyChange(evt); } });
            return;
        }

        // if (evt.getSource() == robotState)
        //     updateBotList();
    }
}
