import java.net.Socket;

public abstract class Gateway implements Runnable {
	
	//protected Network network;
	protected Socket socket;
	
	public Gateway(Socket socket) {
		//this.network = network;
		this.socket = socket;
	}
	

}