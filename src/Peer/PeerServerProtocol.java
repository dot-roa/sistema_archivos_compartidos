package Peer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class PeerServerProtocol {

	public static void protocol(ObjectOutputStream output, 
			ObjectInputStream input, BufferedOutputStream toNetwork) throws IOException, ClassNotFoundException {
		
		String fileName = (String) input.readObject();
		System.out.println("Nombre del archivo solicitado: " + fileName);

		File localFile = new File("Origen"+File.separator+fileName);
		BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));


		byte[] byteArray = new byte[512];
		int in;
		while ((in = fromFile.read(byteArray)) != -1) {
			toNetwork.write(byteArray, 0, in);
		}

		System.out.println("Transferencia finalizada");

		toNetwork.flush();
		fromFile.close();

	}

}
