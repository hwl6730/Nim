import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 * Provides the network proxy for the view object in the Nim game.
 * The View proxy resides in the server program and communicates
 * with the client program
 * @author Harry Longwell
 *
 */
public class ViewProxy implements ModelListener{
	
	private DatagramSocket mailbox;
	private SocketAddress clientAddress;
	private ViewListener viewListener;
	
	/**
	 * Constructor for ViewProxy
	 * @param mailbox - server's mailbox
	 * @param clientAddress - client's mailbox address
	 */
	public ViewProxy(DatagramSocket mailbox, SocketAddress clientAddress){
		this.mailbox = mailbox;
		this.clientAddress = clientAddress;
	}
	
	/**
	 * set the view listener object for this view proxy
	 * @param viewListener
	 */
	public void setViewListener(ViewListener viewListener){
		this.viewListener = viewListener;
	}

	/**
	 * Sent to one client as the first message when that client joins the game
	 * session.
	 * @param i - the player's id
	 * @throws IOException
	 */
	public void id(int i) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    DataOutputStream out = new DataOutputStream (baos);
	    out.writeByte ('I');
	    out.writeByte (i);
	    out.close();
	    byte[] payload = baos.toByteArray();
	    mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	 * Sent to each client to report one of the player's names.
	 * @param i - id of the player
	 * @param name - name of the player
	 * @throws IOException
	 */
	public void name(int i, String name) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('N');
	      out.writeByte (i);
	      out.writeUTF (name);
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	 * Sent to each client to report one of the player's scores.
	 * @param i - id of player
	 * @param s - score of player
	 * @throws IOException
	 */
	public void score(int i, int s) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('S');
	      out.writeByte (i);
	      out.writeByte (s);
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	 * sent to each client to report the number of markers in one of the heaps
	 * @param h - which heap it is
	 * @param m - number of markers in the heap
	 * @throws IOException
	 */
	public void heap(int h, int m) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('H');
	      out.writeByte (h);
	      out.writeByte (m);
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	 * Sent to each client to report whose turn it is
	 * @param i - player's id
	 * @throws IOException
	 */
	public void turn(int i) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('T');
	      out.writeByte (i);
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	 * Sent to each client to report which player won
	 * @param i - player's id
	 * @throws IOException
	 */
	public void win(int i) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('W');
	      out.writeByte (i);
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	 * Sent when the game session terminates
	 * @throws IOException
	 */
	public void quit() throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('Q');
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send(new DatagramPacket (payload, payload.length, clientAddress));
	}

	/**
	    * Process a received datagram.
	    *
	    * @param  datagram  Datagram.
	    *
	    * @return  True to discard this view proxy, false otherwise.
	    *
	    * @exception  IOException
	    *     Thrown if an I/O error occurred.
	    */
	   public boolean process
	      (DatagramPacket datagram)
	      throws IOException
	      {
	      boolean discard = false;
	      DataInputStream in = new DataInputStream(new ByteArrayInputStream(datagram.getData(), 0, datagram.getLength()));
	      String name;
	      int i, score, h, m;
	      byte b = in.readByte();
	      switch (b)
	         {
	         case 'J':
	            name = in.readUTF();
	            viewListener.join (ViewProxy.this, name);
	            break;
	         case 'N':
	            viewListener.newGame();
	            break;
	         case 'T':
	            h = in.readByte();
	            m = in.readByte();
	            viewListener.take(h, m);
	            break;
	         case 'Q':
	            viewListener.quit();
	            discard = true;
	            break;
	         default:
	            System.err.println ("Bad message");
	            break;
	         }
	      return discard;
	      }
}
