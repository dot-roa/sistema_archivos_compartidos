package Peer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class PeerRegisterProtocol {

	public static String protocol(ObjectOutputStream output, ObjectInputStream input, ArrayList<String> files)
			throws IOException, ClassNotFoundException {
		
		String rutaCarpeta = "C:\\Users\\migue\\Downloads\\torrent pc";
		System.out.print("Ingrese su nombre para ser registrado en el server: ");
		String message = new Scanner(System.in).nextLine();
		output.writeObject(message);
		output.flush();
		Object obj = (String) input.readObject();
		System.out.println("Respuesta del server: " + obj);
		System.out.println("Enviando Archivos ...");
		
		//////////////

		try(DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(rutaCarpeta))) {
			
			for(Path ruta : ds) {
				System.out.println(ruta.getFileName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getStackTrace());
		}
		
		////////////////
		output.writeObject(files);
		output.flush();
		obj = (String) input.readObject();
		System.out.println("Respuesta del server: " + obj);
		return "3400";

	}

}
