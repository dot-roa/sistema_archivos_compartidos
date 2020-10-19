package Server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    public static final int PORT = 3400;
    public static int currPort = 2001;

    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Socket serverSideSocket;
    private ServerSocket listener;

    private HashMap<String, ArrayList<String>> index;
    private HashMap<String, Integer> solicitudesPorCliente;
    private HashMap<String, Integer> solicitudesPorTipoArchivo;
    private Integer solicitudesNoResueltas;

    // Metodo constructor.
    public Server() {
        System.out.println("SERVIDOR INDICE ...");
        index = new HashMap<String, ArrayList<String>>();
        solicitudesPorCliente = new HashMap<String, Integer>();
        solicitudesPorTipoArchivo = new HashMap<String, Integer>();
        solicitudesNoResueltas = 0;
        try {

            listener = new ServerSocket(PORT);

            while (true) {
                System.out.println("LISTO PARA ESTABLECER COMUNICACIÓN");
                serverSideSocket = listener.accept();
                InetSocketAddress remote_ip = (InetSocketAddress)serverSideSocket.getRemoteSocketAddress();
                String peerIp = (""+remote_ip).split(":")[0];
                peerIp = peerIp.split("/")[1];
                System.out.println("Desde: "+peerIp);
                try {
                    createStreams();
                    String protocol = reader.readObject().toString();

                    if(protocol.equals("REQUEST")){ // PROTOCOLO DE INDICE
                        System.out.println("Solicitud de indice desde:"+peerIp);
                        String file = reader.readObject().toString();

                        int numberPeers = (int) reader.readObject();
                        System.out.println("El numero de peer a buscar se fijo a: " + numberPeers);
                        ServerRequestProtocol.findMatchedPeers(index, writer, file, numberPeers, solicitudesNoResueltas);
                        addSolicitudPorCliente(peerIp);
                        addSolicitudPorTipoArchivo(file);
                    }
                    else if(protocol.equals("REGISTER")){                                       //PROTOCOLO DE REGISTRO
                        addSolicitudPorCliente(peerIp);
                        writer.writeObject("REGISTRANDO ARCHIVOS");
                        ArrayList<String> files = (ArrayList<String>) reader.readObject();
                        ServerRegisterProtocol.register(index, files, writer, peerIp, getCurrPort());
                        System.out.println("FIN DE REGISTRO");
                        System.out.println("Quedan:"+index.toString());
                    }
                    else if(protocol.equals("STATS")){
                        writer.writeObject(solicitudesPorCliente);
                        writer.writeObject(solicitudesPorTipoArchivo);
                        writer.writeObject(solicitudesNoResueltas);
                    }

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

    private void addSolicitudPorTipoArchivo(String file) {
        System.out.println("FILEEEE "+file);
        String ext = file.split("\\.")[1];

        if(solicitudesPorTipoArchivo.get(ext) == null){
            solicitudesPorTipoArchivo.put(ext,1);
        }
        else
        {
            int solicitudes = solicitudesPorTipoArchivo.get(ext) + 1;
            solicitudesPorTipoArchivo.replace(ext,solicitudes);
        }
    }

    private void addSolicitudPorCliente(String peerIp) {
        if(solicitudesPorCliente.get(peerIp) == null){
            solicitudesPorCliente.put(peerIp,1);
        }
        else
        {
            int solicitudes = solicitudesPorCliente.get(peerIp) + 1;
            solicitudesPorCliente.replace(peerIp,solicitudes);
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