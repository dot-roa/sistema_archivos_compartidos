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
	private static String peerName;


	public static void protocol(ObjectOutputStream output, ObjectInputStream input, String reqFile, int numPeers, String d_folder, String pName)
			throws IOException, ClassNotFoundException {

		download_folder = d_folder;
		file = reqFile;
		peerName = pName;
		ArrayList<String> matchedPeers = requestMatchedPeers(output, input, numPeers);
		String firstPos = matchedPeers.get(0);
		if(firstPos.equals("NO EXISTE EL ARCHIVO"))
			System.out.println(firstPos);
		else {

			for(String ipServerPeer: matchedPeers){
				System.out.println(String.format("CONECTANDO a [%s]",ipServerPeer ));
				server = ipServerPeer.split(":")[0];
				transferRequestingPort = Integer.parseInt(ipServerPeer.split(":")[1]);
				boolean succes =  requestFile();

				if (succes) break;

			}

		}

		
	}

	private static boolean requestFile()  {

		try {
			clientSideSocket = new Socket(server, transferRequestingPort);

			createStreams(); // Crea los streams con el peer que tiene el archivo
			fileTransferWriter.writeObject(file);

			file = download_folder+File.separator +file;
			toFile = new BufferedOutputStream(new FileOutputStream(file));


			String msg = (String) fileTransferReader.readObject();
			String key = msg.split(":")[0];

			if(key.equals("404")) System.out.println(msg);
			else {
				long size = Long.parseLong(msg.split(":")[1]);
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
				clientSideSocket.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.println(String.format("No se pudo Establecer Conexión con el Peer [%s:%s]",server,transferRequestingPort));
			return false;
		}
		catch(IOException e){
			System.out.println(String.format("Fallo en la Transmisión con el Peer [%s:%s]",server,transferRequestingPort));
			return false;
		}
		System.out.println(String.format("Transferencia Exitosa de %s GG!",file));
		return true;

	}

	private static ArrayList<String> requestMatchedPeers(ObjectOutputStream output, ObjectInputStream input, int numPeers) throws IOException, ClassNotFoundException {
		output.writeObject("REQUEST");
		output.writeObject(file);
		output.writeObject(numPeers);

		System.out.println("Buscando Peers que tengan el archivo...");
		ArrayList<String> matchedPeers = (ArrayList<String>) input.readObject();
		output.writeObject(peerName);
		output.flush();
		return matchedPeers;
	}

	private static void createStreams() throws IOException {
		fileTransferWriter = new ObjectOutputStream(clientSideSocket.getOutputStream());
		fileTransferReader = new ObjectInputStream(clientSideSocket.getInputStream());
		fromNetwork = new BufferedInputStream(clientSideSocket.getInputStream());
	}

}
