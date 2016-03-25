import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Receiver {
	
    public static void main(String[] args) throws IOException {
    	//Scanner scanner = new Scanner(System.in);
    	
    	int port = Integer.parseInt(args[1]);
    	System.out.println("Started receiver on port " + port + ".");
        String networkAddress = args[0];
        
        Socket socket = new Socket(networkAddress, port);
       // BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    	//String clientInput = scanner.nextLine();
    	out.println("Receiver");
        
        
        System.exit(0);
        socket.close();
    }
}
