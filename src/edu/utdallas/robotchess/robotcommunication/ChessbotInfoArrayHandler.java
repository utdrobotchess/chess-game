package edu.utdallas.robotchess.robotcommunication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;

import edu.utdallas.robotchess.robotcommunication.commands.Command;

public class ChessbotInfoArrayHandler
{
    private ArrayList<ChessbotInfo> chessbotArr = new ArrayList<ChessbotInfo>();

    //I don't like having a flag here just for the purpose of a thread. There
    //might be a cleaner way of updating ChessbotInfoFrame
    private boolean updated;

    public void add(XBeeAddress64 addr) {
        if(indexOf(addr) > -1)
            return;
        else
        {
            Date date = new Date();
            ChessbotInfo chessbotInfo = new ChessbotInfo(addr, null, date);
            chessbotArr.add(chessbotInfo);
        }
    }

    public void add(XBeeAddress64 addr, Integer id) {
        if(getChessbotInfoFromId(id) != null)
            return;

        else
        {
            ChessbotInfo chessbotInfo = getChessbotInfoFromAddress(addr);
            if (chessbotInfo == null) {
                Date date = new Date();
                chessbotInfo = new ChessbotInfo(addr, id, date);
                chessbotArr.add(chessbotInfo);
            }

            else
            {
                int chessbotInfoIndex = chessbotArr.indexOf(chessbotInfo);
                chessbotInfo.setLastTimeCommunicated(new Date());
                chessbotInfo.setId(id);
                chessbotArr.set(chessbotInfoIndex, chessbotInfo);
            }

            //I sort upon insertion. May not be the most elegant way of
            //handling this
            Collections.sort(chessbotArr, new ChessbotInfoComparator());
        }
    }

    public int indexOf(XBeeAddress64 addr) {
        for (ChessbotInfo chessbotInfo : chessbotArr) {
            XBeeAddress64 thisAddr = chessbotInfo.getXbeeAddress();
            if(thisAddr.equals(addr))
                return chessbotArr.indexOf(chessbotInfo);
        }

        return -1;
    }

    public int indexOf(Integer id) {
        for (ChessbotInfo chessbotInfo : chessbotArr) {
            Integer thisId = chessbotInfo.getId();
            if(thisId != null && thisId.equals(id))
                return chessbotArr.indexOf(chessbotInfo);
        }

        return -1;
    }

    public void updateMessageSent(XBeeAddress64 addr, Command cmd,
                                    boolean deliveryStatus) {
        ChessbotInfo chessbotInfo = getChessbotInfoFromAddress(addr);
        int chessbotInfoIndex = chessbotArr.indexOf(chessbotInfo);
        chessbotInfo.setLastTimeCommunicated(new Date());
        chessbotInfo.setLastCommandSent(cmd, deliveryStatus);
        chessbotArr.set(chessbotInfoIndex, chessbotInfo);
        updated = true;
    }

    public void updateMessageReceived(XBeeAddress64 addr, ZNetRxResponse msg) {
        ChessbotInfo chessbotInfo = getChessbotInfoFromAddress(addr);
        int chessbotInfoIndex = chessbotArr.indexOf(chessbotInfo);
        chessbotInfo.setLastTimeCommunicated(new Date());
        chessbotInfo.setLastMessageReceived(msg);
        chessbotArr.set(chessbotInfoIndex, chessbotInfo);
        updated = true;
    }

    public ChessbotInfo getChessbotInfoFromAddress(XBeeAddress64 addr) {
        int chessbotInfoIndex = indexOf(addr);
        if (chessbotInfoIndex > -1)
            return chessbotArr.get(chessbotInfoIndex);
        else
            return null;
    }

    public ChessbotInfo getChessbotInfoFromId(Integer id) {
        int chessbotInfoIndex = indexOf(id);
        if (chessbotInfoIndex > -1)
            return chessbotArr.get(chessbotInfoIndex);
        else
            return null;
    }

    public ChessbotInfo getChessbotInfoFromIndex(int index) {
        return chessbotArr.get(index);
    }

    public Integer getIdFromAddress(XBeeAddress64 addr) {
        ChessbotInfo chessbotInfo = getChessbotInfoFromAddress(addr);
        return chessbotInfo.getId();
    }

    public XBeeAddress64 getAddressFromId(Integer id) {
        ChessbotInfo chessbotInfo = getChessbotInfoFromId(id);
        return chessbotInfo.getXbeeAddress();
    }

    public ArrayList<XBeeAddress64> getAddressesWithNullIds() {
        ArrayList<XBeeAddress64> addrArr = new ArrayList<XBeeAddress64>();

        for (ChessbotInfo chessbotInfo : chessbotArr) {
            if(chessbotInfo.getId() == null)
                addrArr.add(chessbotInfo.getXbeeAddress());
        }

        return addrArr;
    }

    public ArrayList<String> getRobotsPresent() {
        ArrayList<String> botIdArr = new ArrayList<String>();

        for (ChessbotInfo ch : chessbotArr) {
            Integer id = ch.getId();
            if (id != null)
                botIdArr.add(id.toString());
        }

        return botIdArr;
    }

    public int size() {
        return chessbotArr.size();
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdatedFlag(boolean updated) {
        this.updated = updated;
    }

    public Object[][] toObjectArray() {
        int numOfChessbots = chessbotArr.size();
        int i = 0;
        Object[][] data = new Object[numOfChessbots][6];

        for (ChessbotInfo chessbotInfo : chessbotArr) {
            data[i] = chessbotInfo.toObjectArray();
            i++;
        }

        return data;
    }

    public String toString() {
        String string = "";

        for (ChessbotInfo chessbotInfo : chessbotArr)
            string += chessbotInfo.toString() + "\n";

        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (o == null || !(o instanceof ChessbotInfoArrayHandler))
            return false;

        ChessbotInfoArrayHandler that = (ChessbotInfoArrayHandler) o;

        if (this.size() != that.size())
            return false;

        for (int i = 0; i < this.size(); i++) {
            if(!this.getChessbotInfoFromIndex(i).equals(that.getChessbotInfoFromIndex(i)))
                return false;
        }

        return true;
    }
}

class ChessbotInfoComparator implements Comparator<ChessbotInfo>
{
    @Override
    public int compare(ChessbotInfo x, ChessbotInfo y)
    {
        Integer xId = x.getId();
        Integer yId = y.getId();

        if (xId == null && yId != null)
            return -1;

        if (xId != null && yId == null)
            return 1;

        if (xId == null && yId == null)
            return 0;

        int xIdint = xId.intValue();
        int yIdint = yId.intValue();

        if (xIdint < yIdint)
            return -1;

        if (xIdint > yIdint)
            return 1;

        return 0;
    }
}
