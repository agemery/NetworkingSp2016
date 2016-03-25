import java.net.Socket;

public class ReceiverGateway extends Gateway {
	
	
	public ReceiverGateway(Socket socket) {
		super(socket);
	}

	@Override
	public void run() {
		System.out.println("Receiver connected from " + socket.getInetAddress());
				
	}

}
