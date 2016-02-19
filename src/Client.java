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
        	String serverCode;

        	switch(response) {
        	case "-1" : 
        		serverCode = "Invalid operation";
        		break;
        	case "-2" :
        		serverCode = "Number of inputs is fewer than two";
        		break;
        	case "-3" :
        		serverCode = "Number of inputs is more than four";
        		break;
        	case "-4" :
        		serverCode = "One or more inputs contains a non-number";
        		break;
        	case "-5" :
        		serverCode = "Exit.";
        		break;
        	default :
        		serverCode = response;
        	}
        	
        	System.out.println("From " + serverAddress + ": " + serverCode);
        	
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