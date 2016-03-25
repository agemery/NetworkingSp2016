import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Receiver {
	
    public static void main(String[] args) throws IOException {
    	
    	int port = Integer.parseInt(args[1]);
        String networkAddress = args[0];
        
        System.out.println("Stared Receiver, attempt connection to " + networkAddress + ":" + port);
       
        Socket socket = new Socket(networkAddress, port);
    	
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	out.println("Receiver");
    	
    	List<MessagePacket> packets = new ArrayList<MessagePacket>();
        
        boolean isRunning = true;
        State state = new WaitMessage(new Packet((byte) 0, 0)); //wait for message seq #0
        while(isRunning) {

        	System.out.println("Attempting to read packet.");
        	String answerPacket = input.readLine(); 
        	MessagePacket msg = (MessagePacket) Packet.packetFactory(answerPacket);
        	System.out.println("Read packet: " + msg.toString());
        	
        	
        	if(msg.sequence == state.ackNumber && msg.checksum == MessagePacket.calculateChecksum(msg.content)) {
        		//if the sequence of the msg matches the sequence the current state expects
        		packets.add(msg); //add this packet, it being correct
        		out.println(state.packet); //send ack
        		Packet nextPacket = new Packet((byte) ((state.ackNumber+1)%2), 0);
        		state = new WaitMessage(nextPacket);
        		/*
        		if(packets.isEmpty()) {
        			action = "no more packets to send";
        			out.println("-1");//exit
        		}
        		else {
        			state = new WaitACK((packets.get(0))); //set wait state to the new first packet
        			action = "send Packet" + state.packet.sequence;
        		}*/
        	}
        	else {
        		//if incorrect ack number, resend ack on next iteration
        		//action = "resend Packet" + state.packet.sequence; 
        	}
        	
        	System.out.println("Attempting to send packet: " + state.packet.toString());
        	out.println(state.packet.toString());//send packet
        	
        	
        }
        
        System.exit(0);
        socket.close();
    }
}
