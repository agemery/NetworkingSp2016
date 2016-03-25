import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class SenderGateway extends Gateway {
	Random r;

	public SenderGateway(Socket socket) {
		super(socket);
		r = new Random();
	}

	@Override
	public void run() {
		System.out.println("Sender connected from " + socket.getInetAddress());
		boolean isRunning = true;
		BufferedReader input;
		PrintWriter out;
		
		
		while(isRunning) {
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true); 
				
				String answer = input.readLine();

				if(answer.equals("-1")) {
					System.out.println("Received terminate command! Exiting.");
					isRunning = false; // terminate
					socket.close();
					Network.terminate();
					System.exit(0);
					//also forward to receiver
				}
				else {
					Packet packet = Packet.packetFactory(answer);
					String log;
					if(packet instanceof MessagePacket) {
						MessagePacket mp = (MessagePacket) packet;
						log = "Packet" + mp.sequence + ", " + mp.id + ", " + networkAction(mp, out);
					}
					else {
						log = "ACK" + packet.sequence + ", " + networkAction(packet, out);
					}
					System.out.println("Received:" + log);
				}
			} catch (IOException e) {
				System.out.println("Exception in SENDER-THREAD!");
				e.printStackTrace();
			}
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String networkAction(Packet packet, PrintWriter out) {
		double random = r.nextDouble();
		if(random < 0.5) {
			return passAction(packet, out);	//PASS
		} else if(random >= 0.5 && random < 0.75) {
			return corruptAction(packet, out); //CORRUPT
		} else {
			return dropAction(packet, out); //DROP
		}	
	}

	private String corruptAction(Packet packet, PrintWriter out) {
		packet.checksum += 1; //increase checksum by 1
		out.println(packet.toString());
		return "CORRUPT";
	}

	private String dropAction(Packet packet, PrintWriter out) {
		Packet ack = new Packet((byte) 2, 0); //DROP ACK
		out.println(ack.toString());
		return "DROP";
	}
	
	private String passAction(Packet packet, PrintWriter out) {
		Packet ack = new Packet(packet.sequence, 0);
		out.println(ack.toString());
		return "PASS";
	}
}
