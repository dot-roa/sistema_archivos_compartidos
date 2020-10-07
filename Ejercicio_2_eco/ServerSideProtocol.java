package Ejercicio_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class ServerSideProtocol {
    public static void protocol(BufferedReader input, PrintWriter output,  HashMap<String, Integer> clients) throws IOException {
    	
    	String answer = "";
        System.out.println("Connection incoming ...");
        String clientMessage = input.readLine();
        System.out.println("From cliente message: " + clientMessage);
        
        answer = verificarLogin(clientMessage, clients);
		System.out.println("Sent to client " + answer );
		output.println(answer);
        
		clientMessage = input.readLine();
		System.out.println("From cliente message: " + clientMessage);
		
        if (clientMessage.equals("INFORME")) {
			
			answer = clients.keySet().toString(); 
			System.out.println("Sent to client " + answer );
			output.println(answer);
			
		} else if(clientMessage.equals("INFORME DETALLADO")) {
			answer = clients.entrySet().toString(); 
			System.out.println("Sent to client " + answer );
			output.println(answer);
		} else {
			System.out.println("error en la palabra");
		}
        

    }

	public static String verificarLogin(String clientMessage, HashMap<String, Integer> clients) {
		
		String answer = "";
		
		String msg = clientMessage.split(" ")[0]; 
		String user = clientMessage.split(" ")[1];
		
		if (clients.containsKey(user)) {
			clients.put(user, clients.get(user) + 1 );
			answer = " ACCESO, Conexion numero " + clients.get(user) + " del usuario " + user ;
		} else {
			clients.put(user, 1);
			answer = " BIENVENIDO, primera conexion al servidor de " + user;
		}
		System.out.println(clients);
		
		return answer;
	}
}
