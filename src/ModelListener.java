import java.io.IOException;

/**
 * Interface specifies the interface for an object that is triggered by
 * events from the model object in the Nim game.
 * @author Harry Longwell
 *
 */
public interface ModelListener {
	
	/**
	 * Sent to one client as the first message when that client joins the game
	 * session.
	 * @param i - the player's id
	 * @throws IOException
	 */
	public void id(int i) throws IOException;
	
	/**
	 * Sent to each client to report one of the player's names.
	 * @param i - id of the player
	 * @param name - name of the player
	 * @throws IOException
	 */
	public void name(int i, String name) throws IOException;
	
	/**
	 * Sent to each client to report one of the player's scores.
	 * @param i - id of player
	 * @param s - score of player
	 * @throws IOException
	 */
	public void score(int i, int s) throws IOException;
	
	/**
	 * sent to each client to report the number of markers in one of the heaps
	 * @param h - which heap it is
	 * @param m - number of markers in the heap
	 * @throws IOException
	 */
	public void heap(int h, int m) throws IOException;
	
	/**
	 * Sent to each client to report whose turn it is
	 * @param i - player's id
	 * @throws IOException
	 */
	public void turn(int i) throws IOException;
	
	/**
	 * Sent to each client to report which player won
	 * @param i - player's id
	 * @throws IOException
	 */
	public void win(int i) throws IOException;
	
	/**
	 * Sent when the game session terminates
	 * @throws IOException
	 */
	public void quit() throws IOException;
}
