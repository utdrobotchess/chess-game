package chess.communication.XBeeAPI;

public interface ResponseQueueFilter {
	public boolean accept(XBeeResponse response);
}
