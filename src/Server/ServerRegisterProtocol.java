package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerRegisterProtocol {
    public static void register(HashMap <String,ArrayList<String>> index, ArrayList<String> files,
                                ObjectOutputStream writer, InetSocketAddress peerIp, String currPort) throws IOException {
        for(String file: files){
            if (index.get(file) == null){ //No existe el archivo
                ArrayList<String> ips = new ArrayList<String>();
                ips.add(String.format("%s:%s",peerIp,currPort));
                index.put(file,ips);
            }
            else{ //El archivo ya existe
                ArrayList<String> ips = index.get(file);
                ips.add(String.format("%s:%s",peerIp,currPort));
                index.replace(file,ips);
            }
        }
       sendMessage(writer, currPort);
    }

    private static void sendMessage(ObjectOutputStream writer, String currPort) throws IOException {
        writer.writeObject(currPort); //Se le manda al peer el puerto por el que va a escuchar
        writer.flush();
    }


}
