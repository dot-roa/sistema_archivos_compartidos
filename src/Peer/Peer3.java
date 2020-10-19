package Peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Peer3 {

	public static final int PORT = 3400;
	public static final String SERVER = "localhost";

	public static final String SHARE_FOLDER = "Origen/peer3";
	public static final String DOWNLOAD_FOLDER = "Destino/peer3";

	private int transferListeningPort;
	private int transferRequestingPort;

	private Socket peerSideSocket;
	private ServerSocket listener;

	//Los del listener/Hilo
	private ObjectOutputStream writer;
	private ObjectInputStream reader;


	private PeerThread peerThread;
	//private PeerThread peerThread;


	// Metodo constructor.
	public Peer3() throws ClassNotFoundException {
		System.out.println("PEER CONNECTION ...");

		try {
			peerSideSocket = new Socket(SERVER, PORT);
			
			createStreams();

			transferListeningPort = PeerRegisterProtocol.protocol(writer, reader, SHARE_FOLDER);

			peerSideSocket.close();


			peerThread = new PeerThread(transferListeningPort, SHARE_FOLDER);
			peerThread.start();
			peerSideSocket.close();

			while(true){

				System.out.print("Ingrese el nombre del archivo que necesita: ");
				String reqFile = new Scanner(System.in).nextLine();

				peerSideSocket = new Socket(SERVER, PORT);
				createStreams();

				PeerClientProtocol.protocol(writer, reader, reqFile, DOWNLOAD_FOLDER);

			}


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
				reader.close();
				writer.close();
//				peerSideSocket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	 public synchronized void stopThread() {
		 //peerThread.setStop(true);
	 }

	
	/** Este metodo se encarga de conectar los flujos de entrada y salida de
	/* caracteres con el socket que ha establecido conexion con el servidor.
	 * @throws IOException
	 */
	private void createStreams() throws IOException {
		// Creacion del flujo de salida de datos al cual se le conecta el flujo
		// salida del socket. Este flujo de salida de datos se utiliza para
		// enviar un flujo de caracteres al servidor.
		writer = new ObjectOutputStream(peerSideSocket.getOutputStream());

		// Creacion del buffer de lectura al cual se le conecta un lector de un
		// flujo de entrada que a su vez se conecta con el flujo de entrada del
		// socket. Este flujo de entrada se utiliza para leer un flujo
		// de caractere que viene del servidor.
		reader = new ObjectInputStream(peerSideSocket.getInputStream());
	}
		
		
	/**
	 * Metodo principal de la aplicacion.
	 * @throws ClassNotFoundException 
	 */
	public static void main(String args[]) throws ClassNotFoundException {
		new Peer3();
	}
	
}