import java.io.BufferedReader;
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
    	System.out.println("Stared Sender, port set to " + port + ".");
    	
        
        Socket socket = new Socket(networkAddress, port);
       // BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    	//String clientInput = scanner.nextLine();
    	out.println("Sender");
        
        
        System.exit(0);
        socket.close();
    	
    	/*boolean isTerminated = false;
    	
        ServerSocket listener = new ServerSocket(port);
        try {
            while (!isTerminated) {
                Socket socket = listener.accept();
                try {
                	boolean isConnected = true;
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    System.out.println("Client connected from " + socket.getInetAddress());
                    out.println("Hello!");
                    
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                    	while(isConnected) {
                    		String answer = input.readLine();

                    		if(answer.equals("bye")) {
                    			isConnected = false;
                    			out.println("-5");
                    			System.out.println("Client " + socket.getInetAddress() + " disconnected; return -5");
                    		} else if (answer.equals("terminate")) {
                    			isConnected = false;
                    			isNotTerminated = false;
                    			out.println("-5");
                    			System.out.println("exit");
                    		}
                    		else {
                    			int response = handleCommand(answer);
                    			System.out.println("Client " + socket.getInetAddress() + " input: " + answer + "; return " + response );
                    			out.println(response);
                    		}
                    	}
                    		
                } catch (Exception e) {
                	System.out.println("Connection from " + socket.getInetAddress() + " failed.");
                }
                finally {
                	socket.close();
                }
            }
        }
        finally {
            listener.close();
        }*/
    }
}