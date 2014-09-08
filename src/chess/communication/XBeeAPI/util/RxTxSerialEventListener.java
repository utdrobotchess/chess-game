package chess.communication.XBeeAPI.util;

import gnu.io.SerialPortEvent;

public interface RxTxSerialEventListener {
	public void handleSerialEvent(SerialPortEvent event);
}
