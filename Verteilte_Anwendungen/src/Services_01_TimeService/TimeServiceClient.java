package Services_01_TimeService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TimeServiceClient {
	
	public static String dateFromServer(String ip) {
		Socket socket;
		try {
			socket = new Socket(ip, 7885);
			return getInfoFromServer(socket, "date");
		} catch (IOException e) {
			return e.getMessage();
		}
	}
	
	public static String timeFromServer(String ip) {
		Socket socket;
		try {
			socket = new Socket(ip, 7885);
			return getInfoFromServer(socket, "time");
		} catch (IOException e) {
			return e.getMessage();
		}
	}
	
	private static String getInfoFromServer(Socket socket, String infoType) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			reader.readLine();
			
			writer.write(infoType);
			writer.flush();
			writer.newLine();
			writer.flush();
			
			String result = reader.readLine();
			socket.close();
			return result;
			
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}

}
