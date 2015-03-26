package edu.utdallas.robotchess.gui;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import edu.utdallas.robotchess.manager.RobotState;

public class DiscoveredBotsFrame extends JFrame
{
    private static final long serialVersionUID = 1;

    RobotState robotState;
    JList<String> botJList;
    DefaultListModel<String> listModel = new DefaultListModel<String>();

    public DiscoveredBotsFrame(RobotState robotState)
    {
        this.robotState = robotState;

        setTitle("Discovered Bots List");

        updateBotList();
    }

    private void updateBotList()
    {
        ArrayList<String> botList = robotState.getBotList();
        setSize(300, 100 * botList.size());

        for (String bot : botList)
            listModel.addElement(bot);

        botJList = new JList<String>(listModel);
        add(botJList);
    }

}
