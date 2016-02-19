import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;



public class Client {
	
    public static void main(String[] args) throws IOException {
    	Scanner scanner = new Scanner(System.in);
    	
    	System.out.println("Started client.");
        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);
        
        Socket socket = new Socket(serverAddress, port);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        while(true) {
        	String response = input.readLine();
        	System.out.println("From " + serverAddress + ": " + response);
        	
        	//if we get the -5 code, end the program on the client side
        	if(response.equals("-5")) {
        		break;
        	}
        	
        	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        	String clientInput = scanner.nextLine();
        	out.println(clientInput);
        }
        
        System.exit(0);
        socket.close();
    }
}