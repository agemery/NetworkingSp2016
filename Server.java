public class Server {
	
	public void listenSocket() {
		try {
			server = new ServerSocket(4321);
		} catch (IOException e) {
			System.out.println("Could not listen on port 4321");
			System.exit(-1);
		}
		
		try {
			client = server.accept(); 
		} catch (IOException e) {
			System.out.println("Accept failed: 4321");
			System.exit(-1);
		}
		
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		}
	}
}