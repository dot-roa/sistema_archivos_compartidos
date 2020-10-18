package Peer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class PeerClientProtocol {

	public static int transferRequestingPort;
	public static String server ;
	private static Socket clientSideSocket;
	private static ObjectOutputStream fileTransferWriter;
	private static ObjectInputStream fileTransferReader;
	// flujo de entrada para leer bytes de la red
	private static BufferedInputStream fromNetwork;
	// flujo de salida para escribir en un archivo
	private static BufferedOutputStream toFile;

	private static String download_folder;
	private static String file;


	public static String protocol(ObjectOutputStream output, ObjectInputStream input, String d_folder)
			throws IOException, ClassNotFoundException {

		download_folder = d_folder;
		ArrayList<String> matchedPeers = requestMatchedPeers(output, input);
		String firstPos = matchedPeers.get(0);
		if(firstPos.equals("NO EXISTE EL ARCHIVO"))
			System.out.println(firstPos);
		else {

			for(String ipServerPeer: matchedPeers){
				System.out.println(String.format("CONECTANDO a [%s]",ipServerPeer ));
				server = ipServerPeer.split(":")[0];
				server = server.split("/")[1];
				transferRequestingPort = Integer.parseInt(ipServerPeer.split(":")[1]);
				requestFile();
			}

		}

		return "3400";
		
	}

	private static void requestFile()  {

		try {
			clientSideSocket = new Socket(server, transferRequestingPort);

			file = download_folder+File.separator +file;


			createStreams(); // Crea los streams con el peer que tiene el archivo

			String sizeString = (String) fileTransferReader.readObject();
			long size = Long.parseLong(sizeString.split(":")[1]);
			// se recibe el archivo en bloques de 512 bytes
			byte[] receivedData = new byte[512];
			int in;
			long remainder = size;
			while ((in = fromNetwork.read(receivedData)) != -1) {
				toFile.write(receivedData, 0, in);
				remainder -= in;
				if (remainder == 0)
					break;
			}
			toFile.close();
			fileTransferReader.close();
			fileTransferWriter.close();
			fromNetwork.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("No se pudo Establecer Conexión con el Peer");
		}


	}

	private static ArrayList<String> requestMatchedPeers(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
		System.out.print("Ingrese el nombre del archivo que necesita: ");
		file = new Scanner(System.in).nextLine();
		output.writeObject(file);
		output.flush();
		System.out.print("Buscando Peers que tengan el archivo...");
		ArrayList<String> matchedPeers = (ArrayList<String>) input.readObject();

		return matchedPeers;
	}

	private static void createStreams() throws IOException {
		fileTransferWriter = new ObjectOutputStream(clientSideSocket.getOutputStream());
		fileTransferReader = new ObjectInputStream(clientSideSocket.getInputStream());
		fromNetwork = new BufferedInputStream(clientSideSocket.getInputStream());
		toFile = new BufferedOutputStream(new FileOutputStream(file));
	}

}
