
public class MessagePacket extends Packet{

	protected byte id;
	protected String content;
	
	public MessagePacket(byte sequence, byte id, int checksum, String content) {
		super(sequence, calculateChecksum(content));
		this.id = id;
		this.content = content;
		
	}
	
	public MessagePacket(byte id, String content) {
		super((byte) ((id-1)%2), calculateChecksum(content));
		this.id = id;
		this.content = content;
	
	}
	
	public String toString() {
		return ("" + sequence + " " + id + " " + checksum + " " + content);
	}
	
	public static int calculateChecksum(String s){
		char[] chars = s.toCharArray();
		int sum = 0;
		for(char c : chars) { //calculate checksum
			sum += (int) c; //sum by converting chars to ASCII values
		}
		
		return sum;
	}

}
