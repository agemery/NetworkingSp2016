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
        ReceiverGateway client;
        SenderGateway server;
        Thread clientThread;
        Thread serverThread;
        
        try {
            while (!isTerminated) {
                Socket socket = listener.accept();

                try {
                    
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String hello = input.readLine();
                    
                    System.out.println("Recieved input: " + hello);
                    
                    if(hello.equals("Receiver")) {
                    	System.out.println("Starting RECEIVER-THREAD");
                    	client = new ReceiverGateway(socket);
                    	clientThread = new Thread(client, "RECEIVER-THREAD");
                    	clientThread.start();
                    }
                    else if (hello.equals("Sender")) {
                    	System.out.println("Starting SENDER-THREAD");
                    	server = new SenderGateway(socket);
                    	serverThread = new Thread(server, "SENDER-THREAD");
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
