import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
		
    public static void main(String[] args) throws IOException {
    	System.out.println("Stared server.");
    	int port = Integer.parseInt(args[0]);
    	boolean isNotTerminated = true;
    	
        ServerSocket listener = new ServerSocket(port);
        try {
            while (isNotTerminated) {
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
        }
    }
    
    private static int handleCommand(String input) {
    	List<Integer> resultCodes = new ArrayList<Integer>();
    	
    	String[] inputTokens = input.split(" ");
    	
    	//verify valid op code
    	resultCodes = verifyOperationValid(inputTokens, resultCodes);
    	//verify correct input length
    	resultCodes = verifyCorrectInputLength(inputTokens, resultCodes);
    	//verify op parameters are numbers
    	resultCodes = verifyOperationParametersValid(inputTokens, resultCodes);
    	
    	if (resultCodes.size() > 0) { // don't bother doing any math. There is an error; output the code.
        	return resultCodes.get(0);
    	}
    	    	
    	switch(inputTokens[0]) {
    	case ADD_OP :
    		int sum = 0;
    			for (int i = 1; i<inputTokens.length; i++) {
    				sum += Integer.parseInt(inputTokens[i]);
    			}
    			resultCodes.add(sum);
    		break;
    	case MULT_OP :
    		int multiply = 1;
			for (int i = 1; i<inputTokens.length; i++) {
				multiply *= Integer.parseInt(inputTokens[i]);
			}
			resultCodes.add(multiply);
    		break;
    	case SUB_OP :
    		int subtract = Integer.parseInt(inputTokens[1]);
			for (int i = 2; i<inputTokens.length; i++) {
				subtract -= Integer.parseInt(inputTokens[i]);
			}
			resultCodes.add(subtract);
    		break;
    	default :
    		resultCodes.add(-1);
    	}
    	
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
    
    private static List<Integer> verifyOperationValid(String[] inputTokens, List<Integer> resultCodes) {
    	
    	//if not a valid op code, add -1 to resultCodes
    	if(!(inputTokens[0].equals(ADD_OP) || inputTokens[0].equals(SUB_OP) || inputTokens[0].equals(MULT_OP))) {
    		resultCodes.add(-1);
    	}
    	
    	return resultCodes;
    }
}