import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SenderGateway extends Gateway {

	public SenderGateway(Socket socket) {
		super(socket);
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
	
	private String passAction(Packet packet, PrintWriter out) {
		Packet ack = new Packet(packet.sequence, 0);
		out.println(ack.toString());
		return "PASS";
	}
	
	private String networkAction(Packet packet, PrintWriter out) {
		return passAction(packet, out);	
	}

}
