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

	public static int protocol(ObjectOutputStream output, ObjectInputStream input, String rutaCarpeta)
			throws IOException, ClassNotFoundException {
		

		// Se envia el nombre del peer para ser registrado en el Server
		output.writeObject("REGISTER");
		output.flush();
		
		// Se espera un tipo de respuesta como, envie sus archivos 
		Object obj = (String) input.readObject();
		System.out.println("Enviando Archivos ...");
		System.out.println("Respuesta del server: " + obj);
		////////////// Se recorre el directorio para obtener los nombres de los archivos que contiene

		ArrayList<String> files = new ArrayList<String>();
		try(DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(rutaCarpeta))) {
			
			for(Path ruta : ds) {
				files.add(ruta.getFileName().toString());
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		// Se envia un ArrayList<String> con los nombre de los archivos contenidos en el directorio 
		output.writeObject(files);
		output.flush();
		
		// Se espera el retorno del puerto asignado por el Server
		int puerto = Integer.parseInt(""+input.readObject());
		System.out.println("Puerto Asignado: " + puerto);
		// Se retorna en String el puerto asignado a este peer 
		return puerto;

	}

}
