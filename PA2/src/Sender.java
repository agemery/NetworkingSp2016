import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Sender {
		
    public static void main(String[] args) throws IOException {
    	String networkAddress = args[0];
    	int port = Integer.parseInt(args[1]);
    	String fileName = args[2];
    	
    	System.out.println("Stared Sender, port set to " + port + ".\n"
    			+ "Attempt to read file " + fileName);
    	
        
        Socket socket = new Socket(networkAddress, port);
    	
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	out.println("Sender");
    	
    	List<MessagePacket> packets = readFileAsPackets(fileName); 
        for(MessagePacket packet : packets) {
        	System.out.println(packet.toString());
        }
        
        State state = new WaitACK(packets.get(0));
        int packetCount = 0;
        while(!packets.isEmpty()) {
        	String currentState = state.toString();
        	String action;
        	
        	System.out.println("Attempting to send packet: " + state.packet.toString());
        	out.println(state.packet.toString());//send packet
        	packetCount++;
        	
        	System.out.println("Attempting to read packet.");
        	
        	String answerPacket = input.readLine();  	
        	
        	System.out.println("Read packet: " + answerPacket);
        	Packet ack = Packet.packetFactory(answerPacket); //create packet from response string
        	
        	if(ack.sequence == state.ackNumber) {
        		//if the sequence of the ack matches the sequence the current state expects
        		packets.remove(0); //remove the first packet, it having been sent
        		if(packets.isEmpty()) {
        			action = "no more packets to send";
        			out.println("-1");//exit
        		}
        		else {
        			state = new WaitACK((packets.get(0))); //set wait state to the new first packet
        			action = "send Packet" + state.packet.sequence;
        		}
        	}
        	else {
        		//if incorrect ack number, resend packet on next iteration
        		action = "resent Packet" + state.packet.sequence; 
        	}

        	System.out.println(currentState + ", " + packetCount + ", ACK"+ ack.sequence + ", " + action);
        }
        
        out.close();
        socket.close();
        System.exit(0); 
    }
    
    //given a file name create a list of packets
    private static List<MessagePacket> readFileAsPackets(String fileName) {
    	String file = readFileAsString(fileName);
    	List<MessagePacket> packets = readStringAsPackets(file);

		return packets;
    }
    
    //given a message string, return a list of packets 
    private static List<MessagePacket> readStringAsPackets(String s) {
    	String[] words = s.split(" ");
    	List<MessagePacket> packets = new ArrayList<MessagePacket>();
    	
    	for(int i =0; i< words.length; i++)  { //split string into packets
    		packets.add(new MessagePacket((byte) (i+1), words[i]));
    	}
    	
    	return packets;
    }
    
    //given the file name, read it out as a string
    private static String readFileAsString(String fileName) { 
    	try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
    	    StringBuilder sb = new StringBuilder();
    	    String line = reader.readLine();

    	    while (line != null) {
    	        sb.append(line);
    	        sb.append(System.lineSeparator());
    	        line = reader.readLine();
    	    }
    	    return sb.toString();
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
}