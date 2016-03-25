
public class Packet {
	protected byte sequence;
	protected int checksum;
	
	public Packet(byte seq, int checksum) {
		this.sequence = seq;
		this.checksum = checksum;
	}
	
	public String toString() {
		return "" + sequence + " " + checksum;
	}
	
	public static Packet packetFactory(String packet) {
		//System.out.println("Packet factory: " + packet);
		String[] words = packet.split(" ");
		
		
		if(words.length == 2) { //ACK packet
			return new Packet(Byte.parseByte(words[0]), Integer.parseInt(words[1]));
		} else if (words.length == 4) { //Message packet
			return new MessagePacket(Byte.parseByte(words[0]), Byte.parseByte(words[1]),
					Integer.parseInt(words[2]), words[3]);
		}
		else {
			return null;
		}
	}
}
