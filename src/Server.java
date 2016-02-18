import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                    System.out.println("Client connected.");
                    out.println("Hello!");
                    
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                    	while(isConnected) {
                    		String answer = input.readLine();

                    		if(answer.equals("bye")) {
                    			isConnected = false;
                    			out.println("-5");
                    			System.out.println("Client disconnected.");
                    		} else if (answer.equals("terminate")) {
                    			isConnected = false;
                    			isNotTerminated = false;
                    			out.println("-5");
                    			System.out.println("exit");
                    		}
                    		else {
                    			out.println(handleCommand(answer));
                    		}
                    	}
                    		
                } catch (Exception e) {
                	System.out.println("Connection failed.");
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
    
    private static int handleCommand(String input) {
    	List<Integer> resultCodes = new ArrayList<Integer>();
    	
    	System.out.println("Client input: " + input);
    	String[] inputTokens = input.split(" ");
    	
    	//verify correct input length
    	resultCodes = verifyCorrectInputLength(inputTokens, resultCodes);
    	//verify op parameters are numbers
    	resultCodes = verifyOperationParametersValid(inputTokens, resultCodes);
    	
    	if (resultCodes.size() > 0) { // don't bother doing any math. There is an error; output the code.
    		Collections.reverse(resultCodes);
        	return resultCodes.get(0);
    	}
    	    	
    	switch(inputTokens[0]) {
    	case "add" :
    		int sum = 0;
    			for (int i = 1; i<inputTokens.length; i++) {
    				sum += Integer.parseInt(inputTokens[i]);
    			}
    			resultCodes.add(sum);
    		break;
    	case "multiply" :
    		int multiply = 1;
			for (int i = 1; i<inputTokens.length; i++) {
				multiply *= Integer.parseInt(inputTokens[i]);
			}
			resultCodes.add(multiply);
    		break;
    	case "subtract" :
    		int subtract = Integer.parseInt(inputTokens[1]);
			for (int i = 2; i<inputTokens.length; i++) {
				subtract -= Integer.parseInt(inputTokens[i]);
			}
			resultCodes.add(subtract);
    		break;
    	default :
    		resultCodes.add(-1);
    	}
    	
    	Collections.reverse(resultCodes);
    	return resultCodes.get(0);
    }
    
    //checks to make sure input length is within expected bounds. The operation (add/multiply/subtract) or whatever token is at index 0 is counted towards the input length
    //if proper # of inputs, this method does not change the resultCodes list
    private static List<Integer> verifyCorrectInputLength(String[] inputTokens, List<Integer> resultCodes) {
    	if(inputTokens.length > 5) {
    		resultCodes.add(-3);
    	}
    	else if(inputTokens.length < 3) {
    		resultCodes.add(-2);
    	}

    	return resultCodes;
    }
    
    //checks to make sure that the op parameters are all numbers
    //if all parameters are numbers, do not change resultCodes
    private static List<Integer> verifyOperationParametersValid(String[] inputTokens, List<Integer> resultCodes) {
    	
    	for(int i = 1; i<inputTokens.length; i++) {
    		try {
    			//if the parameter is not a number of some kind, an exception will be raised
    			Double parameter = Double.parseDouble(inputTokens[i]);
    		}
    		catch (NumberFormatException e) {
    			resultCodes.add(-4);
    		}
    	}

    	return resultCodes;
    }
    
    /*private List<String> validateCommand(String command) {
    	
    }*/
}