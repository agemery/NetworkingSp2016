
public class WaitACK extends State {

	protected State nextState;
	private String stateString;
	
	public WaitACK(MessagePacket packet) {
		this.ackNumber = packet.sequence;
		this.packet = packet;
		this.stateString = "Waiting ACK" + this.ackNumber;
	}
	
	public void setPacket(MessagePacket packet) {
		this.packet = packet;
	}
	
	public String toString() {
		return stateString;
	}

}
