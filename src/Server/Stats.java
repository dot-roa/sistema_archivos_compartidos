package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Stats {

	public static final int PORT = 3400;
	public static final String SERVER = "localhost";
	
	private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Socket statisticsSideSocket;
	
	public Stats() {
		System.out.println("Stats ...");
		
		try {
			statisticsSideSocket = new Socket(SERVER, PORT);
			createStreams();
			
			writer.writeObject("STATS");
			writer.flush();
			
			// Cantidad de peticiones por usuario
			HashMap<String, Integer> obj1 =  (HashMap<String, Integer>) reader.readObject();
			// Cantidad de peticiones por tipo de archivo
			HashMap<String, Integer> obj2 = (HashMap<String, Integer>) reader.readObject();
			// Cantidad de peticiones fallidas
			Object obj3 = (int) reader.readObject();
			
			for (Entry<String, Integer> entry : obj1.entrySet()) {
				System.out.println("Cantidad de veces que un cliente a realizado una solicitud");
			    System.out.println("IP del Cliente: " + entry.getKey() + "Cantidad de peticiones: " + entry.getValue());
			}
			
			for (Entry<String, Integer> entry : obj2.entrySet()) {
				System.out.println("Cantidad de veces que se ha solicitado un tipo de archivo");
			    System.out.println("Tipo de archivo: " + entry.getKey() + "Cantidad de solicitudes: " + entry.getValue());
			}
			
			System.out.println("Cantidad de solicitudes que NO se pudieron atender: " + obj3);
		
		} catch (Exception e) {
			
		}	
	}

	/** Este metodo se encarga de conectar los flujos de entrada y salida de
	/* caracteres con el socket que ha establecido conexion con el servidor.
	 * @throws IOException
	 */
	private void createStreams() throws IOException {
		writer = new ObjectOutputStream(statisticsSideSocket.getOutputStream());
		reader = new ObjectInputStream(statisticsSideSocket.getInputStream());
	}

	public static void main(String[] args) {
		new Stats();
	}
}
