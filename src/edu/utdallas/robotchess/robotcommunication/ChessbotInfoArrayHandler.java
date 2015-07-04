package edu.utdallas.robotchess.robotcommunication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;

public class ChessbotInfoArrayHandler
{
    private ArrayList<ChessbotInfo> chessbotArr = new ArrayList<ChessbotInfo>();

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
            if(thisId.equals(id))
                return chessbotArr.indexOf(chessbotInfo);
        }

        return -1;
    }

    public void updateMessageSent(XBeeAddress64 addr, ZNetTxRequest msg,
                                    boolean deliveryStatus) {
        ChessbotInfo chessbotInfo = getChessbotInfoFromAddress(addr);
        int chessbotInfoIndex = chessbotArr.indexOf(chessbotInfo);
        chessbotInfo.setLastTimeCommunicated(new Date());
        chessbotInfo.setLastMessageSent(msg, deliveryStatus);
        chessbotArr.set(chessbotInfoIndex, chessbotInfo);
    }

    public void updateMessageReceived(XBeeAddress64 addr, ZNetRxResponse msg) {
        ChessbotInfo chessbotInfo = getChessbotInfoFromAddress(addr);
        int chessbotInfoIndex = chessbotArr.indexOf(chessbotInfo);
        chessbotInfo.setLastTimeCommunicated(new Date());
        chessbotInfo.setLastMessageReceived(msg);
        chessbotArr.set(chessbotInfoIndex, chessbotInfo);
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



    public boolean allChessbotsConnected() {
        if(chessbotArr.size() == 32) {
            for (ChessbotInfo chessbotInfo : chessbotArr) {
                if (chessbotInfo.getId() == null)
                    return false;
            }
            return true;
        }
        else
            return false;

    }

    public String toString() {
        return ""; //TODO: Implement this
    }
}
class ChessbotInfo
{
    XBeeAddress64 xbeeAddress;
    Integer id;
    Date lastTimeCommunicated;
    ZNetRxResponse lastMessageReceived; //Last message received from Chessbot
    ZNetTxRequest lastMessageSent;      //Last message sent to Chessbot
    boolean lastMessageDeliveryStatus;

    public ChessbotInfo(XBeeAddress64 xbeeAddress, Integer id, Date lastTimeCommunicated)
    {
        this.xbeeAddress = xbeeAddress;
        this.id = id;
        this.lastTimeCommunicated = lastTimeCommunicated;
    }

    public XBeeAddress64 getXbeeAddress() {
        return xbeeAddress;
    }

    public void setXbeeAddress(XBeeAddress64 xbeeAddress) {
        this.xbeeAddress = xbeeAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLastTimeCommunicated() {
        return lastTimeCommunicated;
    }

    public void setLastTimeCommunicated(Date lastTimeCommunicated) {
        this.lastTimeCommunicated = lastTimeCommunicated;
    }

    public ZNetTxRequest getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(ZNetTxRequest lastMessageSent,
                                    boolean deliveryStatus) {
        this.lastMessageSent = lastMessageSent;
        this.lastMessageDeliveryStatus = deliveryStatus;
    }

    public ZNetRxResponse getLastMessageReceived() {
        return lastMessageReceived;
    }

    public void setLastMessageReceived(ZNetRxResponse lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public boolean getLastMessageDeliveryStatus() {
        return lastMessageDeliveryStatus;
    }

    public String formatDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(lastTimeCommunicated);
    }

    public String toString() {
        return ""; //TODO: Implement this
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || !(o instanceof ChessbotInfo))
            return false;

        ChessbotInfo that = (ChessbotInfo) o;

        return this.getXbeeAddress().equals(that.getXbeeAddress()) &&
                this.getId().equals(that.getId()) &&
                this.getLastTimeCommunicated().equals(that.getLastTimeCommunicated());
    }

}