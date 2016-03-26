import java.io.IOException;

/**
 * Specifies the interface for an object that is triggered by events from
 * the view object in the Nim game.
 * @author Harry Longwell
 *
 */
public interface ViewListener {
	
	/**
	 * Sent when the client starts, to join the game session
	 * @param name - name of the player
	 * @throws IOException
	 */
	public void join(ViewProxy proxy, String name) throws IOException;
	
	/**
	 * Sent when the player clicks a marker.
	 * @param h - number of the heap
	 * @param m - number of markers in the heap
	 * @throws IOException
	 */
	public void take(int h, int m) throws IOException;
	
	/**
	 * Sent when the player clicks the New Game Button
	 * @throws IOException
	 */
	public void newGame() throws IOException;
	
	/**
	 * Sent when the player closes the window
	 * @throws IOException
	 */
	public void quit() throws IOException;
}
