import java.net.Socket;

public class SenderGateway extends Gateway {

	public SenderGateway(Socket socket) {
		super(socket);
	}

	@Override
	public void run() {
		System.out.println("Sender connected from " + socket.getInetAddress());
	}

}
