package Server;

import eco.ServerSideProtocol;

import java.io.*;
import java.net.InetSocketAddress;
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

                try {
                    createStreams();
                    String protocol = reader.readObject().toString().split("#")[0];

                    if(protocol.equals("REQUEST")){

                    }
                    else if(protocol.equals("REGISTER")){
                        InetSocketAddress peerIp = (InetSocketAddress)serverSideSocket.getRemoteSocketAddress();
                        ArrayList<String> files = (ArrayList<String>) reader.readObject();
                        ServerRegisterProtocol.register(index, files, writer, peerIp, getCurrPort());
                    }
                    System.out.println("Tamanio del indice luego de un registro"+index.size());

                } catch (IOException | ClassNotFoundException e) {
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

    private String getCurrPort() {
        String p = currPort+"";
        currPort++;
        return p;
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