import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class NetworkGateway implements Runnable {
	protected Socket incomingSocket;
	protected NetworkGateway pairedGateway = null;
	protected String threadName;
	protected Random r;

	public NetworkGateway(Socket socket, String name) {
		this.incomingSocket = socket;
		this.threadName = name;
		r = new Random();
	}
	
	public void linkGateway(NetworkGateway pairedGateway) {
		this.pairedGateway = pairedGateway;
		pairedGateway.pairedGateway = this;	
	}

	@Override
	public void run() {
		System.out.println("Connection from " + incomingSocket.getInetAddress() + "created " + threadName + " thread.");
		boolean isRunning = true;
		BufferedReader input;
		//BufferedReader outputForward;
		PrintWriter out;
		//PrintWriter inputForward;
		
		
		while(isRunning) {
			try {
				input = new BufferedReader(new InputStreamReader(incomingSocket.getInputStream()));
				out = new PrintWriter(incomingSocket.getOutputStream(), true);
				
				while(this.pairedGateway == null) //sleep until both gateways set up
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				String answer = input.readLine();
				
				if(answer.equals("-1")) {
					System.out.println("Received terminate command! Exiting.");
					//forward to receiver
					out.println("-1");
					isRunning = false; // terminate
					//incomingSocket.close();
					//Network.terminate();
					System.exit(0);
					
				}
				else {
					Packet packet = Packet.packetFactory(answer);
					String log;
					if(packet instanceof MessagePacket) {
						MessagePacket mp = (MessagePacket) packet;
						log = "Packet" + mp.sequence + ", " + mp.id + ", " + this.pairedGateway.networkAction(mp, out);
					}
					else {
						log = "ACK" + packet.sequence + ", " + this.pairedGateway.networkAction(packet, out);
					}
					System.out.println("Received:" + log);
				}
				
				/*outputForward = new BufferedReader(new InputStreamReader(outgoingSocket.getInputStream()));
				String forward = outputForward.readLine();
				inputForward = new PrintWriter(incomingSocket.getOutputStream(), true);
				inputForward.println(forward);*/
				
				
			} catch (IOException e) {
				System.out.println("Exception in " + threadName );
				e.printStackTrace();
			}
		}
		
		try {
			incomingSocket.close();
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
