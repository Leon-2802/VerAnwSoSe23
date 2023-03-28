package Services_01_TimeService;

import java.net.ServerSocket;
import java.net.Socket;

public class TimeServiceMultithreaded {
	
	public static void main(String[] args) throws Exception {
		try (ServerSocket serverSocket = new ServerSocket(7885)) {
			while (true) {
				Socket clientSocket = serverSocket.accept();	
				new SocketThread(clientSocket).start();
			}
		}
	}

}
