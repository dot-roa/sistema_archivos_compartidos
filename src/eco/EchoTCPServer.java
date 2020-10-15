package eco;

/*
 * EchoTCPServer
 * 
 * El objetivo de este programa es leer de la red un String enviada por un cliente y
 * hacer eco de ese mensaje envi�ndolo de nuevo al cliente.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

//Clase que especifica el servidor principal de eco.
public class EchoTCPServer {
	// Constante arbitraria para definir el numero de puerto en el que escucha
	// el servidor.
	public static final int PORT = 3400;

	private ServerSocket listener;
	private Socket serverSideSocket;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private HashMap<String, Integer> clients;

	// Metodo constructor.
	public EchoTCPServer() {
		System.out.println("ECHO TCP SERVER ...");

		clients = new HashMap<String, Integer>();
		
		try {
			// Creacion del socket de servidor. Debe especificar el numero de
			// puerto en el cual esta escuchando.
			listener = new ServerSocket(PORT);

			// Ciclo infinito que permite al servidor atender varios clientes,
			// uno a la vez. El servidor va a prestar un mismo servicio a cada
			// cliente.
			while (true) {
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

	/**
	 * Metodo principal utilizado para lanzar el programa servidor.
	 */
	public static void main(String args[]) {
		new EchoTCPServer();
	}
}