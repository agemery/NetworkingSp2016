
public abstract class State {
	protected byte ackNumber;
	protected Packet packet;
	protected String stateString;
	
	public abstract String toString();
}
