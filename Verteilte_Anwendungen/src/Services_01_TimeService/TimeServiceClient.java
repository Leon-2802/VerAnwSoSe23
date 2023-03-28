package Services_01_TimeService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TimeServiceClient {
	
	public static String dateFromServer(String ip) {
		try {
			Socket socket = new Socket(ip, 7885);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			reader.readLine();
			
			writer.write("date");
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
	
	public static String timeFromServer(String ip) {
		try {
			Socket socket = new Socket(ip, 7885);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			reader.readLine();
			
			writer.write("time");
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
