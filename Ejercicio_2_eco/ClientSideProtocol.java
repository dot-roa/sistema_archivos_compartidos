package Ejercicio_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ClientSideProtocol {
    public static void protocol(BufferedReader input, PrintWriter output) throws IOException {
        System.out.print("Ingrese su nombre: ");
        String message = new Scanner(System.in).nextLine();
        output.println("LOGIN " + message);
        String receivedMessage = input.readLine();
        System.out.println("FROM SERVER: " + receivedMessage);
        
        System.out.println("Escriba INFORME o INFORME DETALLADO para observar los clientes registrados");
        String message2 = new Scanner(System.in).nextLine();
        output.println(message2);
        String receivedMessage2 = input.readLine();
        System.out.println("FROM SERVER: " + receivedMessage2);
    }
}
