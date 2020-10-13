package eco;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Peer2 {
	
	public static final int PORT = 3400;
	public static final String SERVER = "localhost";
	
	public static final String SHARE_FOLDER = "compartida/peer1"; 
	public static final String DOWNLOAD_FOLDER = "descargado/peer1";
	
	private int transferListeningPort;
	private int transferRequestingPort;
	
	private Socket peerSideSocket;
	private ServerSocket listener;
	
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	
	private PeerThread peerThread;
	
	
	// Metodo constructor.
	public Peer2() {
		System.out.println("PEER CONNECTION ...");		
		peerThread = new PeerThread(3400);
		peerThread.start();
//			
	}
	
	 public synchronized void stopThread() {
		 peerThread.setStop(true);
	 }

	
	// Este metodo se encarga de conectar los flujos de entrada y salida de
	// caracteres con el socket que ha establecido conexion con el servidor.
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
	 */
	public static void main(String args[]) {
		new Peer2();
	}
	
}