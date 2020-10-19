package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ServerRequestProtocol {
    public static void findMatchedPeers(HashMap<String,ArrayList<String>> index, ObjectOutputStream writer,
                                        String file, int npeers) throws IOException {
        System.out.println("Solicitando: "+file);

        List<String> lista = index.get(file); //Cast del Arraylist a Lista

        if(lista !=  null){
            ArrayList<String> matchedPeers = null;
            if (lista.size() > npeers){
                System.out.println("Lo tienen: "+lista.toString());
                matchedPeers = new ArrayList<>(lista.subList(0,npeers)); //SubLista (inicio,fin)
                //System.out.println("Encontrados en sublista:"+matchedPeers);
            }
            else {
                matchedPeers = new ArrayList<>(lista);
            }

            sendMessage(writer, matchedPeers);
        }
        else{
            sendMessage(writer, new ArrayList<String>() {{add("NO EXISTE EL ARCHIVO");}});

        }
        //System.out.println("PeersEncontrados:"+matchedPeers)
    }

    private static void sendMessage(ObjectOutputStream writer, Object msg) throws IOException {

        writer.writeObject(msg); //Se le manda al peer el puerto por el que va a escuchar
        writer.flush();
    }
}
