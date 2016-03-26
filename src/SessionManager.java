import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * SessionManager maintains the sessions' model objects in the Nim game server
 * @author Harry Longwell
 *
 */
public class SessionManager implements ViewListener{

	private NimModel prev = null;
	
	/**
	 * constructor for session manager
	 */
	public SessionManager(){
		
	}
	
	/**
	 * Sent when the client starts, to join the game session
	 * @param name - name of the player
	 * @throws IOException
	 */
	public synchronized void join(ViewProxy proxy, String name) throws IOException {
		// TODO Auto-generated method stub
		
		if(prev == null || prev.getCount() != 1){
			prev = new NimModel();
			prev.addModelListener(proxy, name);
			proxy.setViewListener(prev);
		}
		else{
			prev.addModelListener(proxy, name);
			proxy.setViewListener(prev);
			prev = null;
		}
	}

	/**
	 * Sent when the player clicks a marker.
	 * @param h - number of the heap
	 * @param m - number of markers in the heap
	 * @throws IOException
	 */
	public void take(int h, int m) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Sent when the player clicks the New Game Button
	 * @throws IOException
	 */
	public void newGame() throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Sent when the player closes the window
	 * @throws IOException
	 */
	public void quit() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
