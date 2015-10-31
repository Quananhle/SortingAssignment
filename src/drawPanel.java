import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The drawPanel class is a JPanel, this class operates as the drawing window for the application. The animation of the histogram and the
 * graph for the data analysis are both drawn using this panel. Data is passed to storage variables and then draw to the JPanel, when new data
 * is passed to the class the JPanel is redraw to represent the changes in data.
 * @author Oliver Palmer, stuNumber 12089466
 *
 */
@SuppressWarnings("serial")
class drawPanel extends JPanel {

	int Arr[];
	int animateY = 200;
	int analyseY = 300;
	Boolean animate = true;
	ArrayList<arrayStore> selectS, bubbleS, mergeS, quickS;

	/**
	 * Constructor for drawPanel, used to set the JPanel dimension, colour and border style.
	 */
	public drawPanel(){
		setPreferredSize(new Dimension(550, 350));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * draw method is called from the sorting algorithms when the histogram animation is selected, the method sets the local
	 * integer array to the value passed by the sorting algorithm and sets the boolean value animate to true, repaint is called 
	 * and the array is drawn by the paint component method. This method is called every time a value is swapped in the sorting 
	 * algorithm therefore animating the sorting process. 
	 * @param a integer array, the value to set the local Array to
	 */
	public void draw(int[] a){
		Arr = a;
		animate = true;
		repaint();
	}

	/**
	 * drawGraph method is used when the application is required to load and display a saved comparative graph. drawGraph is called 
	 * from the mainForm from the open() method, it passes in the contents of four arrayLists all containing the sort times and array
	 * sizes for their respective sorting algorithms, these arrayLists are used to set the corresponding local arrayLists then the
	 * repaint method is called to redraw the contents of the JPanel 
	 * @param b arrayList of arryStore objects, contains bubbleSort array size and time taken data to be displayed
	 * @param s arrayList of arryStore objects, contains selectSort array size and time taken data to be displayed
	 * @param m arrayList of arryStore objects, contains mergeSort array size and time taken data to be displayed
	 * @param q arrayList of arryStore objects, contains quickSort array size and time taken data to be displayed
	 */
	public void drawGraph(ArrayList<arrayStore> b, ArrayList<arrayStore> s, ArrayList<arrayStore> m, ArrayList<arrayStore> q) {
		animate = false;
		bubbleS = b;
		selectS = s;
		mergeS = m;
		quickS = q;
		repaint();
	}

	/**
	 * drawBGraph method is used when the application is required to display a comparative graph. drawBGraph is called 
	 * from the mainForm from the BubbleCompare() method, it passes in the contents an arrayList containing the array sizes and 
	 * their sort times for the Bubble sorting algorithm, the arrayList is set to the corresponding local arrayList then the 
	 * repaint method is called to redraw the contents of the JPanel 
	 * @param b arrayList of arryStore objects, contains bubbleSort array size and time taken data to be displayed
	 */
	public void drawBGraph(ArrayList<arrayStore> b) {
		bubbleS = b;
		animate = false;
		repaint();
	}

	/**
	 * drawSGraph method is used when the application is required to display a comparative graph. drawSGraph is called 
	 * from the mainForm from the SelectionCompare() method, it passes in the contents an arrayList containing the array sizes and 
	 * their sort times for the Selection sorting algorithm, the arrayList is set to the corresponding local arrayList then the 
	 * repaint method is called to redraw the contents of the JPanel 
	 * @param s arrayList of arryStore objects, contains selectionSort array size and time taken data to be displayed
	 */
	public void drawSGraph(ArrayList<arrayStore> s) {
		selectS = s;
		animate = false;
		repaint();
	}

	/**
	 * drawMGraph method is used when the application is required to display a comparative graph. drawMGraph is called 
	 * from the mainForm from the MergeCompare() method, it passes in the contents an arrayList containing the array sizes and 
	 * their sort times for the Merge sorting algorithm, the arrayList is set to the corresponding local arrayList then the 
	 * repaint method is called to redraw the contents of the JPanel 
	 * @param m arrayList of arryStore objects, contains mergeSort array size and time taken data to be displayed
	 */
	public void drawMGraph(ArrayList<arrayStore> m) {
		mergeS = m;
		animate = false;
		repaint();
	}

	/**
	 * drawQGraph method is used when the application is required to display a comparative graph. drawQGraph is called 
	 * from the mainForm from the QuickCompare() method, it passes in the contents an arrayList containing the array sizes and 
	 * their sort times for the Quick sorting algorithm, the arrayList is set to the corresponding local arrayList then the 
	 * repaint method is called to redraw the contents of the JPanel 
	 * @param q arrayList of arryStore objects, contains quickSort array size and time taken data to be displayed
	 */
	public void drawQGraph(ArrayList<arrayStore> q) {
		quickS = q;
		animate = false;
		repaint();
	}

	/**
	 * paintComponent method, used to draw an output to the JPanel. Checks if a histogram animation or comparative graph is required
	 * by checking the boolean value 'animate' also checks that the required Array or ArrayList is not null to prevent runtime errors
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		// For histogram animation, draws a representation of the Array being sorted
		if (Arr != null && animate == true) {
			g.setColor(Color.darkGray);
			int x1 = 0;

			for (int i = 0; i < Arr.length; i++){
				x1 = x1 + 2;
				g.drawLine(x1, (animateY - Arr[i]), x1, animateY);
			}
		}

		if (animate == false) {
			
			//Graph Grid
			g.setColor(Color.LIGHT_GRAY);
			int y = 275;
			for (int i = 0; i < 10; i++) {
				g.drawLine(50, y, 400, y);
				y = y - 25;
			}
				
			//Vertical axis
			g.setColor(Color.black);
			g.drawLine(50, analyseY, 50, 50);
			//Horizontal axis
			g.drawLine(50, analyseY, 400, analyseY);

			//Axis Labels
			g.drawString("Array Size", 200, 335);
			g.drawString("Time", 5, 175);
			g.drawString("(ms)", 9, 187);

			// X Axis Figures
			g.setColor(Color.GRAY);
			int j = 0;
			int x = 40;
			for (int i = 0; i < 7; i ++) {
				j = j + 500;
				String s = Integer.toString(j);
				x = x + 50;
				g.drawString(s, x, analyseY + 15);
			}
			
			//Y Axis Figures
			j = 0;
			y = analyseY + 5;
			for (int i = 0; i < 6; i ++) {
				String s = Integer.toString(j);
				g.drawString(s, 30, y);
				j = j + 5;
				y = y - 50;	
			}

			//Graph Key
			g.setColor(Color.black);
			g.drawString("Bubble Sort", 450, 50);
			g.drawString("Selection Sort", 450, 100);
			g.drawString("Merge Sort", 450, 150);
			g.drawString("Quick Sort", 450, 200);

			g.setColor(Color.green);
			g.fillRect(430, 40, 10, 10);
			g.setColor(Color.blue);
			g.fillRect(430, 90, 10, 10);
			g.setColor(Color.orange);
			g.fillRect(430, 140, 10, 10);
			g.setColor(Color.red);
			g.fillRect(430, 190, 10, 10);

			// Graph plotting for Bubble Sort algorithm
			if (bubbleS != null){
				g.setColor(Color.GREEN);
				int stime2 = 0;
				int x2 = 0;
				// Cycles through every member of the ArrayList and plots a point in relation to the data provided
				for (arrayStore b: bubbleS) {
					long l1 = b.SortTime / 100;
					int stime = (int) l1;
					int x1 = b.ArraySize / 10;

					g.drawLine(x2 + 50, analyseY - stime2, x1 + 50, analyseY - stime);
					g.drawOval(x1 + 50 - 2, analyseY - 2 - stime, 4, 4);

					stime2 = stime;
					x2 = x1;
				}
			}
			
			// Graph plotting for Selection Sort algorithm
			if (selectS != null) {
				g.setColor(Color.BLUE);
				int stime2 = 0;
				int x2 = 0;
				// Cycles through every member of the ArrayList and plots a point in relation to the data provided
				for (arrayStore s: selectS) {
					long l1 = s.SortTime / 100;
					int stime = (int) l1;
					int x1 = s.ArraySize / 10;

					g.drawLine(x2 + 50, analyseY - stime2, x1 + 50, analyseY - stime);
					g.drawOval(x1 + 50 - 2, analyseY - 2 - stime, 4, 4);

					stime2 = stime;
					x2 = x1;
				}
			}
			
			// Graph plotting for Merge Sort algorithm
			if (mergeS != null){
				g.setColor(Color.ORANGE);
				int stime2 = 0;
				int x2 = 0;
				// Cycles through every member of the ArrayList and plots a point in relation to the data provided
				for (arrayStore m: mergeS) {
					long l1 = m.SortTime / 100;
					int stime = (int) l1;
					int x1 = m.ArraySize / 10;

					g.drawLine(x2 + 50, analyseY - stime2, x1 + 50, analyseY - stime);
					g.drawOval(x1 + 50 - 2, analyseY - 2 - stime, 4, 4);

					stime2 = stime;
					x2 = x1;
				}
			}

			// Graph plotting for Quick Sort algorithm
			if (quickS != null){
				g.setColor(Color.RED);
				int stime2 = 0;
				int x2 = 0;
				// Cycles through every member of the ArrayList and plots a point in relation to the data provided
				for (arrayStore q: quickS) {
					long l1 = q.SortTime / 100;
					int stime = (int) l1;
					int x1 = q.ArraySize / 10;

					g.drawLine(x2 + 50, analyseY - stime2, x1 + 50, analyseY - stime);
					g.drawOval(x1 + 50 - 2, analyseY - 2 - stime, 4, 4);

					stime2 = stime;
					x2 = x1;
				}
			}
		}
	}
	
}