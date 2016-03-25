public class WaitMessage extends State {

	public WaitMessage(Packet packet) {
		this.ackNumber = packet.sequence;
		this.packet = packet;
		this.stateString = "Waiting ACK" + this.ackNumber;
	}
	
	@Override
	public String toString() {
		return stateString;
	}
}
