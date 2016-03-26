import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * NimServer is the server main program for the Nim game.
 * @author Harry Longwell
 *
 */
public class NimServer {

		/**
	    * Main program.
	    */
	   public static void main
	      (String[] args)
	      throws Exception
	      {
	      if (args.length != 2) usage();
	      String host = args[0];
	      int port = Integer.parseInt (args[1]);

	      DatagramSocket mailbox = new DatagramSocket(new InetSocketAddress (host, port));

	      MailboxManager manager = new MailboxManager (mailbox);

	      for (;;)
	         {
	         manager.receiveMessage();
	         }
	      }

	   /**
	    * Print a usage message and exit.
	    */
	   private static void usage()
	      {
	      System.err.println ("Usage: java NimServer <host> <port>");
	      System.exit (1);
	      }
}
