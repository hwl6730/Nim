
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Provides the network proxy for the model object in the Nim game.
 * @author Harry Longwell
 *
 */
public class ModelProxy implements ViewListener{
	// Hidden data members.

	   private DatagramSocket mailbox;
	   private SocketAddress destination;
	   private ModelListener modelListener;

	// Exported constructors.

	   /**
	    * Construct a new model proxy.
	    *
	    * @param  socket  Socket.
	    *
	    * @exception  IOException
	    *     Thrown if an I/O error occurred.
	    */
	   public ModelProxy
	      (DatagramSocket mailbox,
	       SocketAddress destination)
	      throws IOException
	      {
		   this.mailbox = mailbox;
		   this.destination = destination;
	      }

	// Exported operations.

	   /**
	    * Set the model listener object for this model proxy.
	    *
	    * @param  modelListener  Model listener.
	    */
	   public void setModelListener
	      (ModelListener modelListener)
	      {
	      this.modelListener = modelListener;
	      new ReaderThread() .start();
	      }
	   
	/**
	 * Sent when the client starts, to join the game session
	 * @param name - name of the player
	 * @throws IOException
	 */
	public void join(ViewProxy proxy,String name) throws IOException{
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		out.writeByte('J');
		out.writeUTF(name);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, destination));
	}

	/**
	 * Sent when the player clicks a marker.
	 * @param h - number of the heap
	 * @param m - number of markers in the heap
	 * @throws IOException
	 */
	public void take(int h, int m) throws IOException{
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('T');
	      out.writeByte (h);
	      out.writeByte (m);
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send
	         (new DatagramPacket (payload, payload.length, destination));
	}

	/**
	 * Sent when the player clicks the New Game Button
	 * @throws IOException
	 */
	public void newGame() throws IOException{
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('N');
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send
	         (new DatagramPacket (payload, payload.length, destination));
	}

	/**
	 * Sent when the player closes the window
	 * @throws IOException
	 */
	public void quit() throws IOException{
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      DataOutputStream out = new DataOutputStream (baos);
	      out.writeByte ('Q');
	      out.close();
	      byte[] payload = baos.toByteArray();
	      mailbox.send
	         (new DatagramPacket (payload, payload.length, destination));
	}

	/**
 	* Class ReaderThread receives messages from the network, decodes them, and
	* invokes the proper methods to process them.
	*/
	private class ReaderThread extends Thread{
		
		public void run(){
			byte[] payload = new byte[128];
			try{
				for(;;){
					DatagramPacket packet = new DatagramPacket (payload, payload.length);
			        mailbox.receive (packet);
			        DataInputStream in = new DataInputStream(new ByteArrayInputStream(payload, 0, packet.getLength()));
	        		int i, score, h, m;
	        		String name;
	        		byte b = in.readByte();
	        	
	        		switch(b){
	        			case 'I'://handles id message
	        	 			i = in.readByte();
	        	 			modelListener.id(i);
	        	 			break;
	        	 		case 'N'://handles name message
	        	 			i = in.readByte();
	        	 			name = in.readUTF();
	        	 			modelListener.name(i, name);
	        	 			break;
	        	 		case 'S'://handles score messages
	        	 			i = in.readByte();
	        	 			score = in.readByte();
	        	 			modelListener.score(i, score);
	        	 			break;
	        	 		case 'H'://handles heap message
	        	 			h = in.readByte();
	        	 			m = in.readByte();
	        	 			modelListener.heap(h, m);
	        	 			break;
	        	 		case 'T'://handles turn message
	        	 			i = in.readByte();
	        	 			modelListener.turn(i);
	        	 			break;
	        	 		case 'W'://handles win message
	        	 			i = in.readByte();
	        	 			modelListener.win(i);
	        	 			break;
	        	 		case 'Q'://handles quit message
	        	 			modelListener.quit();
	        	 			in.close();
	        	 			break;
	        	 		default:
	        	 			System.err.println("Bad Message");
	        	 			break;
	        	 	}
				}
	        }
	    	catch(IOException e){
	    	  		
	    	}
	    	catch(NoSuchElementException e){
	    	  		
	    	}
	    	finally{
	    		mailbox.close();
	    	}
		}
	}
}
