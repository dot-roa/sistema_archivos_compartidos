package Peer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PeerServerProtocol {

	public static void protocol(ObjectOutputStream output, 
			ObjectInputStream input, BufferedOutputStream toNetwork) throws IOException, ClassNotFoundException {

		Object obj = (String) input.readObject();
		System.out.println("Nombre del archivo solicitado: " + obj);

		//poner la ruta donde esten guardados los archivos
		File localFile = new File("Origen" +File.separator + obj);
		BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));
		
		long size = localFile.length();

		System.out.println(size);

		output.writeObject("Size:" + size);
		byte[] byteArray = new byte[512];
		int in;
		while ((in = fromFile.read(byteArray)) != -1) {
			toNetwork.write(byteArray, 0, in);
		}
		toNetwork.flush();
		fromFile.close();

	}

}
