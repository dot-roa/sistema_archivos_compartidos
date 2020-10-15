package Peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PeerClientProtocol {

	public static String protocol(ObjectOutputStream output, ObjectInputStream input, ArrayList<String> files)
			throws IOException, ClassNotFoundException {
		
		System.out.print("Ingrese su nombre del archivo solicitado: ");
		String message = new Scanner(System.in).nextLine();
		output.writeObject(message);
		output.flush();
		Object obj = (String) input.readObject();
		System.out.println("Respuesta del server: " + obj);
		System.out.println("Enviando Archivos ...");
		output.writeObject(files);
		output.flush();
		obj = (String) input.readObject();
		System.out.println("Respuesta del server: " + obj);
		return "3400";
		
	}
	
}
