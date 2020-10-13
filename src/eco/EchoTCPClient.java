package eco;

/*
 * EchoTCPClient
 * 
 * El objetivo de este programa es leer del teclado un String y enviarlo al
 * servidor. El servidor hace eco de este mensaje y lo envia de nuevo al cliente.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EchoTCPClient {
	// Constantes para definir la direccion y el numero de puerto del servidor.
	public static final int PORT = 3400;
	public static final String SERVER = "localhost";

	private Socket clientSideSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	

	// Metodo constructor.
	public EchoTCPClient() {
		System.out.println("ECHO TCP CLIENT ...");

		try {
			// Creacion del socket en el lado cliente. Debe especificar la
			// direccion IP o nombre de host donde esta el servidor y
			// el numero de puerto en el cual esta escuchando.
			clientSideSocket = new Socket(SERVER, PORT);

			createStreams();

			ClientSideProtocol.protocol(reader, writer);
			
			// Lectura del mensaje que el usuario desea enviar al servidor.
//			System.out.print("Ingrese un mensaje: ");
//			String message = new Scanner(System.in).nextLine();
//
//			// Envio del mensaje al servidor.
//			writer.println(message);
//
//			// Lectura del mensaje que el servidor le envia al cliente.
//			String receivedMessage = reader.readLine();
//
//			// Impresion del mensaje recibido en la consola.
//			System.out.println("FROM SERVER: " + receivedMessage);
		}
		// Puede lanzar una excepcion de host desconocido.
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// Puede lanzar una excepcion de entrada y salida.
		catch (IOException e) {
			e.printStackTrace();
		}
		// Finalmente se cierran los flujos y el socket.
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

	// Este metodo se encarga de conectar los flujos de entrada y salida de
	// caracteres con el socket que ha establecido conexion con el servidor.
	private void createStreams() throws IOException {
		// Creacion del flujo de salida de datos al cual se le conecta el flujo
		// salida del socket. Este flujo de salida de datos se utiliza para
		// enviar un flujo de caracteres al servidor.
		writer = new PrintWriter(clientSideSocket.getOutputStream(), true);

		// Creacion del buffer de lectura al cual se le conecta un lector de un
		// flujo de entrada que a su vez se conecta con el flujo de entrada del
		// socket. Este flujo de entrada se utiliza para leer un flujo
		// de caractere que viene del servidor.
		reader = new BufferedReader(new InputStreamReader(clientSideSocket.getInputStream()));
	}

	/**
	 * Metodo principal de la aplicacion.
	 */
	public static void main(String args[]) {
		new EchoTCPClient();
	}
}