package eco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class EchoTCPServer {
	public static final int PORT = 3400;

	private ServerSocket listener;
	private Socket serverSideSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private HashMap<String, Integer> clients;

	public EchoTCPServer() {
		System.out.println("ECHO TCP SERVER ...");

		clients = new HashMap<String, Integer>();
		
		try {
			listener = new ServerSocket(PORT);

			while (true) {
				System.out.println("The ECHO TCP SERVER is waiting for a client....");
				serverSideSocket = listener.accept();

				try {
					createStreams();

					ServerSideProtocol.protocol(reader, writer, clients);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (serverSideSocket != null)
					serverSideSocket.close();
				if (listener != null)
					listener.close();
				if (serverSideSocket != null)
					serverSideSocket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createStreams() throws IOException {
		writer = new PrintWriter(serverSideSocket.getOutputStream(), true);
		reader = new BufferedReader(new InputStreamReader(serverSideSocket.getInputStream()));
	}

	public static void main(String args[]) {
		new EchoTCPServer();
	}
}