package Server;

import eco.ServerSideProtocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    public static final int PORT = 3400;
    public static int currPort = 2000;

    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Socket serverSideSocket;
    private ServerSocket listener;

    private HashMap<String, ArrayList<String>> index;

    // Metodo constructor.
    public Server() {
        System.out.println("SERVIDOR INDICE ...");

        index = new HashMap<String, ArrayList<String>>();

        try {

            listener = new ServerSocket(PORT);

            while (true) {
                System.out.println("LISTO PARA ESTABLECER COMUNICACIÓN");

                serverSideSocket = listener.accept();
                serverSideSocket.getInetAddress();

                try {
                    createStreams();
                    // Ejecuci�n del protocolo
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (serverSideSocket != null)
                    serverSideSocket.close();
                if (listener != null)
                    listener.close();
                if (serverSideSocket != null)
                    serverSideSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createStreams() throws IOException {
        writer = new ObjectOutputStream(serverSideSocket.getOutputStream());
        reader = new ObjectInputStream(serverSideSocket.getInputStream());
    }

    /**
     * Metodo principal utilizado para lanzar el programa servidor.
     */
    public static void main(String args[]) {
        new Server();
    }
}