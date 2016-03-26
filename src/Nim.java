import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The class that represent the client main program for the Nim game.
 * @author Harry Longwell
 *
 */
public class Nim {

	/**
	 * Main program
	 * @param args - command line arguments, specify the host,
	 * port, and name of player
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception{
		//checks for the correct number of arguments
		if(args.length != 5){
			System.err.println("Usage: java <serverhost> <serverport> <clienthost> <clientport> <playername>");
			System.exit(1);
		}
		//extract host and port for server
		String serverhost = args[0];
		int serverport = 0;
		serverport = Integer.parseInt(args[1]);
		
		//extract host and port for client
		String clienthost = args[2];
		int clientport = Integer.parseInt(args[3]);
		
		//create new datagram socket
		DatagramSocket mailbox = new DatagramSocket(new InetSocketAddress(clienthost, clientport));
		
		//create modelclone, view, modelproxy, and set all listener
		NimModelClone clone = new NimModelClone();
		NimUI view = NimUI.create(args[4]);
		ModelProxy proxy = new ModelProxy(mailbox, new InetSocketAddress(serverhost, serverport));

		clone.setModelListener(view);
		view.setViewListener(proxy);
		proxy.setModelListener(clone);

		proxy.join(null, args[4]);
	}
}
