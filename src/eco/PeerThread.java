package eco;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class PeerThread extends Thread {
	
	private boolean stop = false;
	
	public int port;

	private ServerSocket listener;
	private Socket serverSideSocket;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private HashMap<String, Integer> clients;
	
	public PeerThread(int port) {
		this.port = port;
	}
	
	
	
	public void run() {
		System.out.println("ECHO TCP SERVER in thread[port " + port + "]...");

		clients = new HashMap<String, Integer>();
		
		try {
			// Creacion del socket de servidor. Debe especificar el numero de
			// puerto en el cual esta escuchando.
			listener = new ServerSocket(port);

			// Ciclo infinito que permite al servidor atender varios clientes,
			// uno a la vez. El servidor va a prestar un mismo servicio a cada
			// cliente.
			while(!stop) {
				System.out.println("The ECHO TCP SERVER is waiting for a client....");
				// El servidor queda bloqueado esperando una conexion de un
				// cliente.
				// Cuando el servidor recibe el contacto de un cliente, crea un
				// nuevo socket, dedicado para atender ese cliente en
				// particular.
				serverSideSocket = listener.accept();

				try {
					createStreams();
					
					System.out.println("Connection incoming ...");

					// Lectura del mensaje que el cliente le envia al servidor.
					String clientMessage = (String)reader.readObject();

					System.out.println("From client: " + clientMessage);

					// Procesamiento de la informacion que el cliente ha enviado
					// al servidor.
					String answer = "OK";

					System.out.println("Sent to client: " + answer);

					// Envio del mensaje al cliente.
					writer.writeObject(answer);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// Puede lanzar una excepcion de entrada y salida.
		catch (IOException e) {
			e.printStackTrace();
		}
		// Al terminar se cierran los sockets.
		finally {
			try {
				if (serverSideSocket != null) serverSideSocket.close();
				if (listener != null) listener.close();
				if (serverSideSocket != null) serverSideSocket.close();
			}
			// Puede lanzar una excepcion de entrada y salida.
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createStreams() throws IOException {
		// Creacion del flujo de salida de datos al cual se le conecta el flujo
		// salida del socket. Este flujo de salida de datos se utiliza para
		// enviar un flujo de caracteres al servidor.
		writer = new ObjectOutputStream(serverSideSocket.getOutputStream());
	
		// Creacion del buffer de lectura al cual se le conecta un lector de un
		// flujo de entrada que a su vez se conecta con el flujo de entrada del
		// socket. Este flujo de entrada se utiliza para leer un flujo
		// de caractere que viene del servidor.
		reader = new ObjectInputStream(serverSideSocket.getInputStream());
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}

}
