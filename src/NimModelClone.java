import java.awt.Color;
import java.io.IOException;

/**
 * NimModelClone provides a client-side clone of the server-side model object
 * in the network Nim game
 * @author Harry Longwell
 *
 */
public class NimModelClone implements ModelListener{

	//the model listener of the clone
	private ModelListener modelListener;
	
	/**
	 * Constructor method
	 */
	public NimModelClone(){
		
	}
	
	/**
	 * sets the model listener for this model clone
	 * @param modelListener - model listener
	 */
	public void setModelListener(ModelListener modelListener){
		this.modelListener = modelListener;
	}
	
	/**
	 * calls id method of the listener of the clone
	 * @param i - id of player
	 * @throws IOException
	 */
	public void id(int i) throws IOException {
		// TODO Auto-generated method stub
		this.modelListener.id(i);
	}

	/**
	 * calls the name method of the listener of the clone
	 * @param i - id of player
	 * @param name - name of player
	 * @throws IOException
	 */
	public void name(int i, String name) throws IOException {
		// TODO Auto-generated method stub
		this.modelListener.name(i, name);
	}

	/**
	 * calls the score method of the listener of the clone
	 * @param i - id of player
	 * @param s - score of player
	 * @throws IOException
	 */
	public void score(int i, int s) throws IOException {
		// TODO Auto-generated method stub
		this.modelListener.score(i, s);
	}

	/**
	 * calls the heap method of the listener of the clone
	 * @param i - number of heap
	 * @param m - number of markers in heap
	 * @throws IOException
	 */
	public void heap(int h, int m) throws IOException {
		// TODO Auto-generated method stub
		this.modelListener.heap(h, m);
	}

	/**
	 * calls the turn method of the listener of the clone
	 * @param i - id of player
	 * @throws IOException
	 */
	public void turn(int i) throws IOException {
		// TODO Auto-generated method stub
		this.modelListener.turn(i);
	}

	/**
	 * calls the win method of the listener of the clone
	 * @param i - id of player
	 * @throws IOException
	 */
	public void win(int i) throws IOException {
		// TODO Auto-generated method stub
		this.modelListener.win(i);
	}

	/**
	 * calls the quit method of the listener of the clone
	 */
	public void quit() throws IOException {
		// TODO Auto-generated method stub
		this.modelListener.quit();
	}

}
