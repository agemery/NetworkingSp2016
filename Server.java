import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static int port = 7826;
	
	
    public static void main(String[] args) throws IOException {
    	System.out.println("Stared server.");
    	
    	boolean isNotTerminated = true;
    	
        ServerSocket listener = new ServerSocket(port);
        try {
            while (isNotTerminated) {
                Socket socket = listener.accept();
                try {
                	boolean isConnected = true;
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("Hello!");
                    
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                    	while(isConnected) {
                    		String answer = input.readLine();
                    		System.out.println(answer);
                    	}
                    		
                } catch (IOException e) {
                	System.out.println("Accept failed.");
                	System.out.println(e);
                }
                finally {
                	socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
}