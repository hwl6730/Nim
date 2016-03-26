import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * NimModel provides the server-side model objects in the Nim game.
 * @author Harry Longwell
 *
 */
public class NimModel implements ViewListener{

	private LinkedList<ModelListener> listeners = new LinkedList<ModelListener>();
	private int id1 = 1;
	private int id2 = 2;
	private int heap[] = new int[3];
	private String name1 = null;
	private String name2 = null;
	private int score1 = 0;
	private int score2 = 0;
	private int count = 0;
	private boolean turn = true;
	
	/**
	 * Constructor for model, sets heaps to initial state
	 */
	public NimModel(){
		heap[0] = 3;
		heap[1] = 4;
		heap[2] = 5;
	}
	
	/**
	 * Adds the given model listener to this model
	 * @param modelListener - listener to add
	 * @param name - name of client
	 */
	public synchronized void addModelListener(ModelListener modelListener, String name){
		listeners.add(modelListener);
		//checks if client is the first to join this session
		if(listeners.size() == 1){
			name1 = name;
			count++;
			try {
				listeners.get(0).id(id1);
				listeners.get(0).name(id1, name1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{//if second, sends information needed to start game
			name2 = name;
			count++;
			try {
		        listeners.get(0).heap(0, 3);
		        listeners.get(0).heap(1, 4);
		        listeners.get(0).heap(2, 5);
		        listeners.get(0).turn(id1);
		        listeners.get(0).name(id2, name2);
		        
				listeners.get(1).id(id2);
		        listeners.get(1).heap(0, 3);
		        listeners.get(1).heap(1, 4);
		        listeners.get(1).heap(2, 5);
		        listeners.get(1).name(id1, name1);
		        listeners.get(1).name(id2, name2);
		        listeners.get(1).turn(id1);
		    }catch (IOException exc){
			        // Client failed, stop reporting to it.
			}
		}
		
	}
	
	/**
	 * Sent when the client starts, to join the game session
	 * @param name - name of the player
	 * @throws IOException
	 */
	public synchronized void join(ViewProxy proxy, String name) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Sent when the player clicks a marker.
	 * @param h - number of the heap
	 * @param m - number of markers in the heap
	 * @throws IOException
	 */
	public synchronized void take(int h, int m) throws IOException {
		// TODO Auto-generated method stub

		//set turn to other player and reduce heap amount
		turn = !turn;
		heap[h] -= m;
		
		//report to all clients
		Iterator<ModelListener> iter = listeners.iterator();
	      while (iter.hasNext()){
	         ModelListener listener = iter.next();
	         try{
	            listener.heap(h, heap[h]);
	            if(turn == true){
	            	listener.turn(id1);
	            }
	            else{
	            	listener.turn(id2);
	            }
	         }catch (IOException exc){
	            // Client failed, stop reporting to it.
	            iter.remove();
	         }
	     } 
		
	    //checks if the move has won the game
		if(heap[0] == 0 && heap[1] == 0 && heap[2] == 0){
          	if(turn == false){
          		score1++;
          		listeners.get(0).score(id1, score1);
          		listeners.get(0).win(id1);
          		listeners.get(1).score(id1, score1);
          		listeners.get(1).win(id1);
          	}
          	else{
          		score2++;
          		listeners.get(0).score(id2, score2);
          		listeners.get(0).win(id2);
          		listeners.get(1).score(id2, score2);
          		listeners.get(1).win(id2);
          	}
          }
	}

	/**
	 * Sent when the player clicks the New Game Button
	 * @throws IOException
	 */
	public synchronized void newGame() throws IOException {
		// TODO Auto-generated method stub
		//reset heaps and set turn to first player
		heap[0] = 3;
		heap[1] = 4;
		heap[2] = 5;
		turn = true;
		
		//report to all clients
		Iterator<ModelListener> iter = listeners.iterator();
	      while (iter.hasNext())
	         {
	         ModelListener listener = iter.next();
	         try
	            {
	        	 listener.heap(0, 3);
		         listener.heap(1, 4);
		         listener.heap(2, 5);
		         listener.turn(id1);
	            }
	         catch (IOException exc)
	            {
	            // Client failed, stop reporting to it.
	            iter.remove();
	            }
	         }
	}

	/**
	 * Sent when the player closes the window
	 * @throws IOException
	 */
	public synchronized void quit() throws IOException {
		// TODO Auto-generated method stub
		if(listeners.size() == 1){
			listeners.get(0).quit();
			count = 0;
		}
		else{
			listeners.get(0).quit();
			listeners.get(1).quit();
			count = 0;
		}
	}
	
	/**
	 * returns the player count of the session
	 * @return the player count
	 */
	public synchronized int getCount(){
		return count;
	}

}
