import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;



public class Client {

	public static int port = 7826;
	
    public static void main(String[] args) throws IOException {
    	Scanner scanner = new Scanner(System.in);
    	
    	System.out.println("Enter server IP address: ");
        String serverIP = scanner.nextLine();
        
        Socket socket = new Socket(serverIP, port);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        while(true) {
        	String response = input.readLine();
        	System.out.println(response);
        	
        	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        	String clientInput = scanner.nextLine();
        	out.println(clientInput);
        	
        	if (clientInput.equals("Bye"))
        		break;
        }
        
        System.exit(0);
    }
}