package edu.utdallas.robotchess.robotcommunication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;

import edu.utdallas.robotchess.robotcommunication.commands.Command;

public class ChessbotInfo
{
    XBeeAddress64 xbeeAddress;
    Integer id;
    Integer location;
    String pieceType;
    Date lastTimeCommunicated;
    ZNetRxResponse lastMessageReceived; //Last message received from Chessbot
    Command lastCommandSent;            //Last message sent to Chessbot
    boolean lastMessageDeliveryStatus;

    final int ROOK_IDS[] = {0, 7, 24, 31};
    final int KNIGHT_IDS[] = {1, 6, 25, 30};
    final int BISHOP_IDS[] = {2, 5, 26, 29};
    final int QUEEN_IDS[] = {3, 27};
    final int KING_IDS[] = {4, 28};
    final int PAWN_IDS[] = {8, 9, 10, 11, 12, 13, 14, 15,
                            16, 17, 18, 19, 20, 21, 22, 23};

    public static final String[] CHESSBOT_INFO_COLUMNS = new String[] {
                                                    "ID #",
                                                    "Piece Type",
                                                    "XBee Address",
                                                    "DeliveryStatus",
                                                    "Most Recent Communication",
                                                    "Last Command Sent To",
                                                    "Last Response Received From"};


    public ChessbotInfo(XBeeAddress64 xbeeAddress, Integer id, Date lastTimeCommunicated)
    {
        this.xbeeAddress = xbeeAddress;
        this.lastTimeCommunicated = lastTimeCommunicated;

        setId(id);
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
        setPieceType();
    }

    public void setPieceType() {

        if (id == null)
            return;

        for (int rookId : ROOK_IDS) {
            if (rookId == id) {
                pieceType = "Rook";
                return;
            }
        }

        for (int knightId : KNIGHT_IDS) {
            if (knightId == id) {
                pieceType = "Knight";
                return;
            }
        }

        for (int BishopId : BISHOP_IDS) {

            if (BishopId == id) {
                pieceType = "Bishop";
                return;
            }
        }

        for (int QueenId : QUEEN_IDS) {
            if (QueenId == id) {
                pieceType = "Queen";
                return;
            }
        }

        for (int KingId : KING_IDS) {
            if (KingId == id) {
                pieceType = "King";
                return;
            }
        }

        for (int pawnId : PAWN_IDS)
            if (pawnId == id)
                pieceType = "Pawn";
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getPieceType() {
        return pieceType;
    }

    public Date getLastTimeCommunicated() {
        return lastTimeCommunicated;
    }

    public void setLastTimeCommunicated(Date lastTimeCommunicated) {
        this.lastTimeCommunicated = lastTimeCommunicated;
    }

    public Command getLastCommandSent() {
        return lastCommandSent;
    }

    public void setLastCommandSent(Command lastCommandSent,
                                    boolean deliveryStatus) {
        this.lastCommandSent = lastCommandSent;
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

    public String formatDateToString() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        return dateFormat.format(lastTimeCommunicated);
    }

    //TODO: Should fix this in XbeeAddress64.java and re-jar the lib. This is
    //much less efficient, but temporarily more convenient
    public String simplifyXbeeAddressFormat() {
        String str = xbeeAddress.toString();
        String[] strArr = str.split(",");

        str = new String();
        for (String s : strArr) {
            s = s.substring(2);
            str += s + " ";
        }

        return str.trim();
    }

    public String simplifyMessageLastReceived() {

        String str = new String();
        if (lastMessageReceived != null) {
            str = lastMessageReceived.toString();
            str = str.substring(str.indexOf("data="));
        }

        return str;
    }

    public Object[] toObjectArray() {
        Object[] data = new Object[] {
        id,
        pieceType,
        simplifyXbeeAddressFormat(),
        lastMessageDeliveryStatus,
        formatDateToString(),
        lastCommandSent,
        simplifyMessageLastReceived()
        };

        return data;
    }

    //TODO: Update this
    public String toString() {
        String string = String.format(id + " " + xbeeAddress +
                " " + formatDateToString() + " " + lastCommandSent +
                " " + lastMessageReceived);

        return string;
    }

    //TODO: Update this
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
