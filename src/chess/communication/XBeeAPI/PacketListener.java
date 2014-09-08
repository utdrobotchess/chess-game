package chess.communication.XBeeAPI;

/**
 * Add an implementation of this interface to xbee.addPacketListener to get notifications
 * of new packets
 * <p/>
 * @author andrew
 */
public interface PacketListener {
	public void processResponse(XBeeResponse response);
//	public void handleError(XBeeResponse error);
//	public void connectionClosed();
}
