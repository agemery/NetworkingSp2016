
public abstract class State {
	protected byte ackNumber;
	protected MessagePacket packet;
	
	public abstract String toString();
}
