package HTTPS_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SimpleClient {

	public static void main(String[] args) {
		try {
//			get("https://www.bundestag.de/presse");
//			System.out.println(urlExists("https://www.bundestag.de/presse"));
			System.out.println(getContent("https://www.bundestag.de/presse"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void get(String s) throws IOException {
		BufferedReader reader = connectToURL(s);
		
		String result = reader.readLine();
		
		while(result != null) {
			System.out.println(result);
			result = reader.readLine();
		}
	}
	
	public static boolean urlExists(String s) throws IOException {
		BufferedReader reader = connectToURL(s);
		
		String result = reader.readLine();
		return (result.contains("200"));
	}

	public static String getContent(String s) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader reader = connectToURL(s);

		String line = reader.readLine();

		while(line != null) {
			result.append(line + "\n");
			line = reader.readLine();
		}

		return result.toString();
	}

	private static BufferedReader connectToURL(String s) throws IOException {
		URL url = new URL(s);
		int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();

		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket socket = (SSLSocket) factory.createSocket(url.getHost(), port);

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		writer.write("GET " + url.getPath() + " HTTP/1.0");
		writer.newLine();
		writer.write("Host: " + url.getHost());
		writer.newLine();
		writer.newLine();
		writer.flush();

		return reader;
	}

}
