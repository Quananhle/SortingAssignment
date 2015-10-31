import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This class is the programs top level container and contains all the swing elements for interacting with the program
 * along with action listeners and methods to handle these interactions 
 * @author Oliver Palmer, stuNumber 12089466
 * 
 *
 */
@SuppressWarnings("serial")
public class mainForm extends JFrame {

	// Variable and Swing components
	int gPoints = 35;
	String[] sortingAlgorithms = {"Bubble Sort", "Selection Sort", "Merge Sort", "Quick Sort"};
	JMenuBar menuBar;
	JMenu fileMenu, helpMenu;
	JMenuItem openMItem, saveTxtMItem, saveCsvMItem, closeMItem, aboutMItem;
	JComboBox<String> sortSelection;
	JButton runButton, stopButton;
	JRadioButton Animate, Analyse;

	// Object creation
	Thread t;
	bubbleSort b;
	selectionSort s;
	mergeSort m;
	quickSort q;
	drawPanel drawP = new drawPanel();

	// ArrayList creation
	ArrayList<arrayStore> bubbleStore;
	ArrayList<arrayStore> selectStore;
	ArrayList<arrayStore> mergeStore;
	ArrayList<arrayStore> quickStore;

	/**
	 * Constructor for mainForm, adds swing elements to the program along with ActionListeners to perform the other methods 
	 * written as Anonymous Inner Classes. Also sets the layout management of the program.
	 */
	public mainForm() {
		super("Sorting Program by Oliver Palmer 12089466");

		//Menu Bar
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");

		// Menu Bar Action Listeners, using Anonymous Inner Classes
		openMItem = new JMenuItem("Open");
		openMItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				open();
			}
		});

		saveTxtMItem = new JMenuItem("Save as .txt");
		saveTxtMItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				save(false);
			}
		});
		
		saveCsvMItem = new JMenuItem("Save as .csv");
		saveCsvMItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				save(true);
			}
		});

		closeMItem = new JMenuItem("Close");
		closeMItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});

		aboutMItem = new JMenuItem("About");
		aboutMItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String s = "This application is used to display and analyse different types of \n"
						+ "sorting algorithm.\n\n"
						+ "To view a Histogram animation of a sorting algorithm please make sure the\n"
						+ "Animate option is checked and select the algorithm you would like to view \n"
						+ "from the drop down selection box. Press the Run button to view the animation.\n\n"
						+ "To view a comparative graph of all the algorithms plotting array size against\n"
						+ "sort time please select the Analyse option and press the Run button";
				JOptionPane.showMessageDialog(null, s, "About", 1);
			}
		});

		// Add menu bar to mainForm
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		fileMenu.add(saveTxtMItem);
		fileMenu.add(saveCsvMItem);
		fileMenu.add(openMItem);
		fileMenu.add(closeMItem);
		helpMenu.add(aboutMItem);
		//Menu Bar End

		sortSelection = new JComboBox<String>(sortingAlgorithms);

		// Button action listeners, using Anonymous Inner Classes
		runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				runPress();
			}
		});

		stopButton = new JButton("Cancel Sort");
		stopButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try { b.stop();} catch (Exception ex) {}
				try { s.stop();} catch (Exception ex) {}
				try { m.stop();} catch (Exception ex) {}
				try { q.stop();} catch (Exception ex) {}
			}
		});

		Animate = new JRadioButton("Animate");
		Animate.setSelected(true);
		Animate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				sortSelection.setEnabled(true);
				stopButton.setEnabled(true);
			}
		});

		Analyse = new JRadioButton("Analyse");
		Analyse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				sortSelection.setEnabled(false);
				stopButton.setEnabled(false);
			}
		});

		// Groups the two JRadioButtons together
		ButtonGroup group = new ButtonGroup();
		group.add(Animate);
		group.add(Analyse);

		// Layout Management and adding buttons to mainForm
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		JPanel Select = new JPanel();
		JPanel SelectB = new JPanel();

		Select.setLayout(new GridBagLayout());
		SelectB.setLayout(new FlowLayout());

		//first Column upper JPanel
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 0;
		gc.gridy = 0;
		add(Select, gc);

		// Select JPanel layer
		Select.add(SelectB, gc);
		SelectB.add(sortSelection, gc);
		SelectB.add(runButton, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		Select.add(Animate, gc);

		gc.gridx = 0;
		gc.gridy = 2;
		Select.add(Analyse, gc);

		gc.gridx = 0;
		gc.gridy = 3;
		Select.add(stopButton, gc);
		// Select JPanel end

		//Second Column upper JPanel
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 0;

		add(drawP, gc);
	}

	/**
	 * This method is used to open a saved .txt or .csv file of results that has been previously saved by the program
	 * it uses a JFileChooser to locate and access the file the user wishes to open. ArrayLists are created and the data
	 * is read into and saved in the ArrayLists using a comma and space as a delimiter. Once all the data is copied to
	 * the ArrayLists they are passed to the drawPanel to be represented in graphical format.
	 */
	private void open() {
		try {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text", "csv");
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(mainForm.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				bubbleStore = new ArrayList<arrayStore>();
				selectStore = new ArrayList<arrayStore>();
				mergeStore = new ArrayList<arrayStore>();
				quickStore = new ArrayList<arrayStore>();

				Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
				sc.useDelimiter(",\\s*");

				for (int i = 0; i < gPoints; i++){
					int aSize = sc.nextInt();
					long sTime = sc.nextLong();
					bubbleStore.add(new arrayStore(aSize, sTime));
				}

				for (int i = 0; i < gPoints; i++){
					int aSize = sc.nextInt();
					long sTime = sc.nextLong();
					selectStore.add(new arrayStore(aSize, sTime));
				}

				for (int i = 0; i < gPoints; i++){
					int aSize = sc.nextInt();
					long sTime = sc.nextLong();
					mergeStore.add(new arrayStore(aSize, sTime));
				}

				for (int i = 0; i < gPoints; i++){
					int aSize = sc.nextInt();
					long sTime = sc.nextLong();
					quickStore.add(new arrayStore(aSize, sTime));
				}

				drawP.drawGraph(bubbleStore, selectStore, mergeStore, quickStore);

				sc.close();
			}

		} catch (IOException ex) {

		}
	}

	/**
	 * This method is used to save the analytical data that has been generated by the program. It first checks to
	 * see if there is data in the ArrayLists to be saved, if not it will prompt the user to generate data. Then
	 * using a JFileChooser to select the filename and directory, the data is written to the file using a comma and
	 * single white space as a delimiter. Contains two options to save as a .csv or .txt depending on which boolean 
	 * value is passed in when the method is called.
	 * @param saveTypeCsv boolean, true if file is to be saved as .csv false if file is to be saved as .txt
	 */
	private void save(boolean saveTypeCsv) {
		try {

			if (bubbleStore != null && selectStore != null && mergeStore != null && quickStore != null) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter saveFilter;
				
				if (saveTypeCsv == true) {
					saveFilter = new FileNameExtensionFilter(".csv", ".csv");
				} else {
					saveFilter = new FileNameExtensionFilter(".txt", ".txt");
				}

				fc.setFileFilter(saveFilter);

				int returnVal = fc.showSaveDialog(mainForm.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					FileWriter fw;

					if (saveTypeCsv == true) {
						fw = new FileWriter(file + ".csv");
					} else {
						fw = new FileWriter(file  + ".txt");
					}

					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw = new PrintWriter(bw);

					for (arrayStore b: bubbleStore){
						pw.print(b.ArraySize + ", " + (int)b.SortTime + ", ");
					}

					for (arrayStore s: selectStore){
						pw.print(s.ArraySize + ", " + (int)s.SortTime + ", ");
					}

					for (arrayStore m: mergeStore){
						pw.print(m.ArraySize + ", " + (int)m.SortTime + ", ");
					}

					for (arrayStore q: quickStore){
						pw.print(q.ArraySize + ", " + (int)q.SortTime + ", ");
					}

					pw.close();
					bw.close();
					fw.close();
				} else {
					JOptionPane.showMessageDialog(null, "Save Canceled", "Error", 2);
				}
			} else {
				String s = "Nothing to save! \nPlease run 'Analyse' to generate results.";
				JOptionPane.showMessageDialog(null, s, "Error", 0);
			}
		} catch (IOException ex) {

		}
	}

	/**
	 * runPress method determines which to methods need to run by checking the option selected in the 
	 * combo box and which radio button is selected when the run button is pressed
	 */
	private void runPress() {

		String selection = (String)sortSelection.getSelectedItem();
		if (selection == "Bubble Sort" && Animate.isSelected()){

			BubbleSort();

		} else if (selection == "Selection Sort" && Animate.isSelected()){

			SelectionSort();

		} else if (selection == "Merge Sort" && Animate.isSelected()){

			MergeSort();

		} else if (selection == "Quick Sort" && Animate.isSelected()){

			QuickSort();

		} else if (Analyse.isSelected()){
			BubbleCompare();
			SelectionCompare();
			MergeCompare();
			QuickCompare();
		}
	}

	/**
	 * This method creates a bubbleSort with 275 values in its array for animation purposes, the drawPanel 
	 * class is passed in but no Array List as the data is not required for analysis. A new thread is then
	 * created and the bubbleSort is run as a thread.
	 */
	private void BubbleSort() {
		b = new bubbleSort(275, drawP, true, null);
		t = new Thread(b);
		t.start();
	}

	/**
	 * This method is used for statistically comparing Array size against sorting time, it creates an array 
	 * of bubbleSort objects, an array of threads to run each sorting algorithm and an ArrayList to hold the
	 * data. A for loop is used to automate the creation of each bubbleSort object with 100 more values in it 
	 * with every reiteration, the bubbleSort objects are passed to the corresponding threads and begin running.
	 * A sleep statement is used to stagger the running of the threads so the ArrayList values are received in
	 * order. The ArrayList is then passed to the drawPanel so the results can be represented.
	 */
	private void BubbleCompare() {

		bubbleStore = new ArrayList<arrayStore>();
		bubbleSort[] bubble = new bubbleSort[gPoints];
		Thread[] t = new Thread[gPoints];
		int j = 100;
		for (int i = 0; i < gPoints; i++){

			bubble[i] = new bubbleSort(j, drawP, false, bubbleStore);
			t[i] = new Thread(bubble[i]);
			t[i].start();
			try { TimeUnit.MILLISECONDS.sleep(20); } catch (Exception e) {}
			j = j + 100;

		}
		drawP.drawBGraph(bubbleStore);
	}

	/**
	 * This method creates a selectionSort with 275 values in its array for animation purposes, the drawPanel 
	 * class is passed in but no Array List as the data is not required for analysis. A new thread is then
	 * created and the selectionSort is run as a thread.
	 */
	private void SelectionSort() {
		s = new selectionSort(275, drawP, true , null);
		t = new Thread(s);
		t.start();
	}

	/**
	 * This method is used for statistically comparing Array size against sorting time, it creates an array 
	 * of selectSort objects, an array of threads to run each sorting algorithm and an ArrayList to hold the
	 * data. A for loop is used to automate the creation of each selectSort object with 100 more values in it 
	 * with every reiteration, the selectSort objects are passed to the corresponding threads and begin running.
	 * A sleep statement is used to stagger the running of the threads so the ArrayList values are received in
	 * order. The ArrayList is then passed to the drawPanel so the results can be represented.
	 */
	private void SelectionCompare() {

		selectStore = new ArrayList<arrayStore>();
		selectionSort[] select = new selectionSort[gPoints];
		Thread[] t = new Thread[gPoints];
		int j = 100;
		for (int i = 0; i < gPoints; i++){

			select[i] = new selectionSort(j, drawP, false, selectStore);
			t[i] = new Thread(select[i]);
			t[i].start();
			try { TimeUnit.MILLISECONDS.sleep(20); } catch (Exception e) {}
			j = j + 100;

		}
		drawP.drawSGraph(selectStore);
	}

	/**
	 * This method creates a mergeSort with 275 values in its array for animation purposes, the drawPanel 
	 * class is passed in but no Array List as the data is not required for analysis. A new thread is then
	 * created and the mergeSort is run as a thread.
	 */
	private void MergeSort() {
		m = new mergeSort(275, drawP, true , null);
		t = new Thread(m);
		t.start();
	}

	/**
	 * This method is used for statistically comparing Array size against sorting time, it creates an array 
	 * of mergeSort objects, an array of threads to run each sorting algorithm and an ArrayList to hold the
	 * data. A for loop is used to automate the creation of each mergeSort object with 100 more values in it 
	 * with every reiteration, the mergeSort objects are passed to the corresponding threads and begin running.
	 * A sleep statement is used to stagger the running of the threads so the ArrayList values are received in
	 * order. The ArrayList is then passed to the drawPanel so the results can be represented.
	 */
	private void MergeCompare() {

		mergeStore = new ArrayList<arrayStore>();
		mergeSort[] merge = new mergeSort[gPoints];
		Thread[] t = new Thread[gPoints];
		int j = 100;
		for (int i = 0; i < gPoints; i++){

			merge[i] = new mergeSort(j, drawP, false, mergeStore);
			t[i] = new Thread(merge[i]);
			t[i].start();
			try { TimeUnit.MILLISECONDS.sleep(20); } catch (Exception e) {}
			j = j + 100;

		}
		drawP.drawMGraph(mergeStore);
	}

	/**
	 * This method creates a quickSort with 275 values in its array for animation purposes, the drawPanel 
	 * class is passed in but no Array List as the data is not required for analysis. A new thread is then
	 * created and the quickSort is run as a thread.
	 */
	private void QuickSort() {
		q = new quickSort(275, drawP, true, null);
		t = new Thread(q);
		t.start();

	}

	/**
	 * This method is used for statistically comparing Array size against sorting time, it creates an array 
	 * of quickSort objects, an array of threads to run each sorting algorithm and an ArrayList to hold the
	 * data. A for loop is used to automate the creation of each quickSort object with 100 additional values in it 
	 * with every reiteration, the quickSort objects are passed to the corresponding threads and begin running.
	 * A sleep statement is used to stagger the running of the threads so the ArrayList values are received in
	 * order. The ArrayList is then passed to the drawPanel so the results can be represented.
	 */
	private void QuickCompare() {

		quickStore = new ArrayList<arrayStore>();
		quickSort[] quick = new quickSort[gPoints];
		Thread[] t = new Thread[gPoints];

		int j = 100;
		for (int i = 0; i < gPoints; i++){

			quick[i] = new quickSort(j, drawP, false, quickStore);
			t[i] = new Thread(quick[i]);
			t[i].start();
			try { TimeUnit.MILLISECONDS.sleep(20); } catch (Exception e) {}
			j = j + 100;

		}
		drawP.drawQGraph(quickStore);
	}
}

