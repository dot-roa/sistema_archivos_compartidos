package Peer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class PeerServerProtocol {

	public static void protocol(ObjectOutputStream output, 
			ObjectInputStream input, BufferedOutputStream toNetwork, String originFolder) throws IOException, ClassNotFoundException {
		
		String fileName = (String) input.readObject();

		File localFile = new File(originFolder+File.separator+fileName);
		if (localFile.exists())
		{
			BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));
			output.writeObject("LENGTH:"+localFile.length());
			output.flush();


			byte[] byteArray = new byte[512];
			int in;
			while ((in = fromFile.read(byteArray)) != -1) {
				toNetwork.write(byteArray, 0, in);
			}

			toNetwork.flush();
			fromFile.close();
		}
		else{
			output.writeObject("404:El archivo ya no esta diponible");
		}


	}

}
