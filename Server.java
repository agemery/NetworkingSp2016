import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

	public static int port = 7826;
	
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(port);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                    out.println("Hello!");
                } finally {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
}