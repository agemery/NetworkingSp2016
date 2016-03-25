import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
	
	private static boolean isTerminated = false;
	
    public static void main(String[] args) throws IOException {
    	
    	int port = Integer.parseInt(args[0]);
    	System.out.println("Stared Network, port set to " + port + ".");
    	
        ServerSocket listener = new ServerSocket(port);
        NetworkGateway receiver = null, sender = null;
        Thread receiverThread, serverThread;
        
        try {
            while (!isTerminated) {
                Socket socket = listener.accept();

                try {
                    
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String hello = input.readLine();
                    
                    System.out.println("Recieved input: " + hello);
                    
                    if(hello.equals("Receiver")) {
                    	System.out.println("Starting RECEIVER-THREAD");
                    	receiver = new NetworkGateway(socket, "RECEIVER");
                    	if(sender != null) {// link sockets
                    		receiver.linkGateway(sender);
                    	}
                    	receiverThread = new Thread(receiver, "RECEIVER-THREAD");
                    	receiverThread.start();
                    }
                    else if (hello.equals("Sender")) {
                    	System.out.println("Starting SENDER-THREAD");
                    	sender = new NetworkGateway(socket, "SENDER");
                    	if(receiver != null) {// link sockets
                    		sender.linkGateway(receiver);
                    	}
                    	serverThread = new Thread(sender, "SENDER-THREAD");
                    	serverThread.start();
                    }
                    		
                } catch (Exception e) {
                	System.out.println("Connection from " + socket.getInetAddress() + " failed.");
                }
            }
        }
        finally {
            listener.close();
        }
    }
    
    public static void terminate() {
    	isTerminated = true;
    }

}
