//******************************************************************************
//
// File:    NimUI.java
// Package: ---
// Unit:    Class NimUI.java
//
// This Java source file is copyright (C) 2015 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
//
// This Java source file is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 3 of the License, or (at your option) any
// later version.
//
// This Java source file is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details.
//
// You may obtain a copy of the GNU General Public License on the World Wide Web
// at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * Class NimUI provides the user interface for the Nim network game.
 *
 * @author  Alan Kaminsky, Harry Longwell
 * @version 07-Oct-2015
 */
public class NimUI implements ModelListener
	{

// Interface for a listener for HeapPanel events.

	private static interface HeapListener
		{
		// Report that markers are to be removed from a heap.
		public void removeObjects
			(int id,          // Heap panel ID
			 int numRemoved); // Number of markers to be removed
		}

// Class for a Swing widget displaying a heap of markers.

	private static class HeapPanel
		extends JPanel
		{
		private static final int W = 50;
		private static final int H = 30;
		private static final Color FC = Color.RED;
		private static final Color OC = Color.BLACK;

		private int id;
		private int maxCount;
		private int count;
		private boolean isEnabled;
		private HeapListener listener;

		// Construct a new heap panel.
		public HeapPanel
			(final int id,       // Heap panel ID
			 final int maxCount) // Maximum number of markers
			{
			this.id = id;
			this.maxCount = maxCount;
			this.count = maxCount;
			this.isEnabled = true;
			Dimension dim = new Dimension (W, maxCount*H);
			setMinimumSize (dim);
			setMaximumSize (dim);
			setPreferredSize (dim);
			addMouseListener (new MouseAdapter()
				{
				public void mouseClicked (MouseEvent e)
					{
					if (isEnabled && listener != null)
						{
						int objClicked = maxCount - 1 - e.getY()/H;
						int numRemoved = count - objClicked;
						if (numRemoved > 0)
								listener.removeObjects (id, numRemoved);
						}
					}
				});
			}

		// Set this heap panel's listener.
		public void setListener
			(HeapListener listener)
			{
			this.listener = listener;
			}

		// Set the number of markers in this heap panel.
		public void setCount
			(int count) // Number of markers
			{
			count = Math.max (0, Math.min (count, maxCount));
			if (this.count != count)
				{
				this.count = count;
				repaint();
				}
			}

		// Enable or disable this heap panel.
		public void setEnabled
			(boolean enabled) // True to enable, false to disable
			{
			if (this.isEnabled != enabled)
				{
				this.isEnabled = enabled;
				repaint();
				}
			}

		// Paint this heap panel.
		protected void paintComponent
			(Graphics g) // Graphics context
			{
			super.paintComponent (g);

			// Clone graphics context.
			Graphics2D g2d = (Graphics2D) g.create();

			// Turn on antialiasing.
			g2d.setRenderingHint
				(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);

			// For drawing markers.
			Ellipse2D.Double ellipse = new Ellipse2D.Double();
			ellipse.width = W - 2;
			ellipse.height = H - 2;
			ellipse.x = 1;

			// If enabled, draw filled markers.
			if (isEnabled)
				{
				g2d.setColor (FC);
				for (int i = 0; i < count; ++ i)
					{
					ellipse.y = (maxCount - 1 - i)*H + 1;
					g2d.fill (ellipse);
					}
				}

			// If disabled, draw outlined markers.
			else
				{
				g2d.setColor (OC);
				for (int i = 0; i < count; ++ i)
					{
					ellipse.y = (maxCount - 1 - i)*H + 1;
					g2d.draw (ellipse);
					}
				}
			}
		}

	/**
	 * class that listens to heap and responds when the player interacts with heap
	 * @author Harry Longwell
	 *
	 */
	private class Listener implements HeapListener{

		/**
		 * Reports that markers have been removed from the heap
		 * @param id - heap id
		 * @param numRemoved - number of markers removed
		 * @throws IOException
		 */
		public void removeObjects(int id, int numRemoved){
			// TODO Auto-generated method stub
			doMouseClick(id, numRemoved);
		}
		
	}
	/**
	 * Sets the listener of this class
	 * @param viewListener
	 */
	public void setViewListener
    (ViewListener viewListener)
    {
		this.viewListener = viewListener;
    }
	
// Hidden data members.

	private static final int NUMHEAPS = 3;
	private static final int NUMOBJECTS = 5;
	private static final int GAP = 10;
	private static final int COL = 10;
	
	private ViewListener viewListener;
	private int id;
	private JFrame frame;
	private String myName, theirName;
	private HeapPanel[] heapPanel;
	private JTextField myNameField;
	private JTextField theirNameField;
	private JTextField whoWonField;
	private JButton newGameButton;
	
	//create HeapListener object
	private Listener listen = new Listener();

// Hidden constructors.

	/**
	 * Construct a new Nim UI.
	 */
	private NimUI
		(String name)
		{
		frame = new JFrame ("Nim -- " + name);
		JPanel panel = new JPanel();
		panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		frame.add (panel);
		panel.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));

		heapPanel = new HeapPanel [NUMHEAPS];
		for (int h = 0; h < NUMHEAPS; ++ h)
			{
			panel.add (heapPanel[h] = new HeapPanel (h, NUMOBJECTS));
			panel.add (Box.createHorizontalStrut (GAP));
			
			//set heaps listener and set them to unabled to start
			heapPanel[h].setListener(listen);
			heapPanel[h].setEnabled(false);
			}
		
		//set heap counts
		heapPanel[0].setCount(3);
		heapPanel[1].setCount(4);

		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout (new BoxLayout (fieldPanel, BoxLayout.Y_AXIS));
		panel.add (fieldPanel);

		myNameField = new JTextField (COL);
		myNameField.setEditable (false);
		myNameField.setHorizontalAlignment (JTextField.CENTER);
		myNameField.setAlignmentX (0.5f);
		fieldPanel.add (myNameField);
		fieldPanel.add (Box.createVerticalStrut (GAP));

		theirNameField = new JTextField (COL);
		theirNameField.setEditable (false);
		theirNameField.setHorizontalAlignment (JTextField.CENTER);
		theirNameField.setAlignmentX (0.5f);
		fieldPanel.add (theirNameField);
		fieldPanel.add (Box.createVerticalStrut (GAP));

		whoWonField = new JTextField (COL);
		whoWonField.setEditable (false);
		whoWonField.setHorizontalAlignment (JTextField.CENTER);
		whoWonField.setAlignmentX (0.5f);
		fieldPanel.add (whoWonField);
		fieldPanel.add (Box.createVerticalStrut (GAP));

		newGameButton = new JButton ("New Game");
		newGameButton.setAlignmentX (0.5f);
		newGameButton.setFocusable (false);
		fieldPanel.add (newGameButton);
		
		//set new game button to be disabled at default
		newGameButton.setEnabled(false);
		
		//set listener of new game button
		newGameButton.addActionListener(new ActionListener(){

			/**
			 * when the button is clicked calls the UI's new button method
			 */
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
					doNewButton();
			}
		});

		frame.pack();
		frame.setVisible (true);
		
		//set listener of the frame
		frame.addWindowListener(new WindowAdapter(){
			
			/**
			 * when the player closes window calls view listener's quit method and exits
			 */
			public void windowClosing(WindowEvent e){
				doQuit();
				System.exit(1);
			}
		});
		
		}
	

// Exported operations.

	/**
	 * An object holding a reference to a Nim UI.
	 */
	private static class UIRef
		{
		public NimUI ui;
		}

	/**
	 * Construct a new Nim UI.
	 */
	public static NimUI create
		(final String name)
		{
		final UIRef ref = new UIRef();
		onSwingThreadDo (new Runnable()
			{
			public void run()
				{
				ref.ui = new NimUI (name);
				}
			});
		return ref.ui;
		}

// Hidden operations.

	/**
	 * Execute the given runnable object on the Swing thread.
	 */
	private static void onSwingThreadDo
		(Runnable task)
		{
		try
			{
			SwingUtilities.invokeAndWait (task);
			}
		catch (Throwable exc)
			{
			exc.printStackTrace (System.err);
			System.exit (1);
			}
		}
	
	/**
	 * Responds to mouse clicks on the heaps, calls the view listeners take method
	 * @param h - heap clicked on
	 * @param m - number of markers to be removed
	 * @throws IOException
	 */
	private synchronized void doMouseClick(int h, int m){
		try {
			viewListener.take(h, m);
		} catch (IOException e) {
			System.err.println("Failed take upon mouse click");
			System.exit(1);
		}
	}
	
	/**
	 * Responds to the player closing the window
	 * @throws IOException
	 */
	private synchronized void doQuit(){
		try{
			viewListener.quit();
		} catch(IOException e){
			System.err.println("Failed to quit game");
			System.exit(1);
		}
	}
	
	/**
	 * Responds to new game button clicks, calls the view listeners new game method
	 * @throws IOException
	 */
	private synchronized void doNewButton(){
		try {
			viewListener.newGame();
		} catch (IOException e) {
			System.err.println("Failed to create new game");
			System.exit(1);
		}
	}

	/**
	 * Sent to one client as the first message when that client joins the game
	 * session. Sets the player's id
	 * @param i - the player's id
	 * @throws IOException
	 */
	public synchronized void id(int i) {
		// TODO Auto-generated method stub
		this.id = i;
	}

	/**
	 * Sent to each client to report one of the player's names.
	 * @param i - id of the player
	 * @param name - name of the player
	 * @throws IOException
	 */
	public synchronized void name(int i, String name) {
		// TODO Auto-generated method stub
		//if id matches, it is current players name
		//else it is the opponents name, enables new game button upon opponent
		//joing the game
		if(i == this.id){
			myName = name;
			onSwingThreadDo(new Runnable(){
				public void run(){
					myNameField.setText(myName + " = 0");
				}
			});
		}
		else{
			theirName = name;
			onSwingThreadDo(new Runnable(){
				public void run(){
					theirNameField.setText(theirName + " = 0");
					newGameButton.setEnabled(true);
				}
			});
		}
	}

	/**
	 * Sent to each client to report one of the player's scores.
	 * @param i - id of player
	 * @param s - score of player
	 * @throws IOException
	 */
	public synchronized void score(int i, int s) {
		// TODO Auto-generated method stub
		//if id matches, it is current player's score
		//else it is the opponents score
		if(i == this.id){
			final int score = s;
			onSwingThreadDo(new Runnable(){
				public void run(){
					myNameField.setText(myName + " = " + score);
				}
			});
		}
		else{
			final int score = s;
			onSwingThreadDo(new Runnable(){
				public void run(){
					theirNameField.setText(theirName + " = " + score);
				}
			});
		}
	}

	/**
	 * sent to each client to report the number of markers in one of the heaps
	 * @param h - which heap it is
	 * @param m - number of markers in the heap
	 * @throws IOException
	 */
	public synchronized void heap(int h, int m) {
		// TODO Auto-generated method stub
		//set each heaps markers
		final int heap_num = h;
		final int count = m;
		onSwingThreadDo(new Runnable(){
			public void run(){
				heapPanel[heap_num].setCount(count);
				whoWonField.setText("");
			}
		});
	}

	/**
	 * Sent to each client to report whose turn it is
	 * @param i - player's id
	 * @throws IOException
	 */
	public synchronized void turn(int i) {
		// TODO Auto-generated method stub
		//if it's current player's turn, enables interact with heaps
		if(i == this.id){
			onSwingThreadDo(new Runnable(){
				public void run(){
					heapPanel[0].setEnabled(true);
					heapPanel[1].setEnabled(true);
					heapPanel[2].setEnabled(true);
				}
			});
		}
		else{
			onSwingThreadDo(new Runnable(){
				public void run(){
					heapPanel[0].setEnabled(false);
					heapPanel[1].setEnabled(false);
					heapPanel[2].setEnabled(false);
				}
			});
		}
	}

	/**
	 * Sent to each client to report whose turn it is
	 * @param i - player's id
	 * @throws IOException
	 */
	public synchronized void win(int i) {
		// TODO Auto-generated method stub
		//sets win message based on who won
		if(i == this.id){
			onSwingThreadDo(new Runnable(){
				public void run(){
					whoWonField.setText(myName + " wins!");
				}
			});
			
		}
		else{
			onSwingThreadDo(new Runnable(){
				public void run(){
					whoWonField.setText(theirName + " wins!");
				}
			});
		}
	}

	/**
	 * Sent when the game session terminates
	 * @throws IOException
	 */
	public synchronized void quit() {
		// TODO Auto-generated method stub
		onSwingThreadDo(new Runnable(){
			public void run(){
				try {
					viewListener.quit();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.dispose();
				System.exit(1);
			}
		});
	}
}
