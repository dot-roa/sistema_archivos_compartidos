package eco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EchoTCPClient {
	
	public static final int PORT = 3400;
	public static final String SERVER = "localhost";

	private Socket clientSideSocket;
	private PrintWriter writer;
	private BufferedReader reader;

	public EchoTCPClient() {
		System.out.println("ECHO TCP CLIENT ...");

		try {
			clientSideSocket = new Socket(SERVER, PORT);
			createStreams();
			ClientSideProtocol.protocol(reader, writer);

		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (reader != null)
					reader.close();
				if (writer != null)
					writer.close();
				if (clientSideSocket != null)
					clientSideSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createStreams() throws IOException {
		writer = new PrintWriter(clientSideSocket.getOutputStream(), true);
		reader = new BufferedReader(new InputStreamReader(clientSideSocket.getInputStream()));
	}

	public static void main(String args[]) {
		new EchoTCPClient();
	}
}