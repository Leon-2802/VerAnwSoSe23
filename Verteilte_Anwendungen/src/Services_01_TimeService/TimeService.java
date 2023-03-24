package Services_01_TimeService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeService {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(7885);
		while (true) {
			
			try {
				Socket clientSocket = serverSocket.accept();
				
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
				BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				writer.write("connected!");
				writer.newLine();
				writer.flush();
				
				
				while (true) {
					String input = reader.readLine();
					
					switch (input) {
					case "date":
						writer.write(Clock.date());
						writer.newLine();
						writer.flush();
						break;
					case "time":
						writer.write(Clock.time());
						writer.newLine();
						writer.flush();
						break;
					default:
						clientSocket.close();
						break;
					}
				}
			} catch (Exception e) {
			}
		}
	}

}
