import java.util.ArrayList;
import java.util.Random;

/**
 * The mergeSort class is is used to implement a Merge Sorting process on an Array of random integers that are 
 * automatically generated by the algorithm, the class can be used to either animate the bubble sort process or
 * record the time taken to fully sort a single array. Runs as a single thread.
 * @author Oliver Palmer, stuNumber 12089466
 *
 */
public class mergeSort implements Runnable {


	static Random r = new Random();
	static final int largestValue = 100;
	drawPanel draw;
	private int Array[];
	private int tempArray[];
	boolean running = false, animate = true;
	ArrayList<arrayStore> list;

	/**
	 * Constructor for mergeSort
	 * @param i int, determine the size of the array to be sorted
	 * @param d drawPanel, the JPanel that the animation is draw on
	 * @param animate boolean, to determine whether the class is being accessed for animation or analysis
	 * @param l ArrayList, used to store the sort time and array size when performing analysis
	 */
	mergeSort (int i, drawPanel d, boolean animate, ArrayList<arrayStore> l){
		Array = new int[i];
		draw = d;
		this.list = l;
		this.animate = animate;
	}

	/**
	 * Sort method, used to begin the sorting progress, records the time in nano seconds then divides by 1000 to convert
	 * the value to microseconds. If the class is initialised to analyse the data then this method records the timetaken and
	 * array size in the array list provided by the constructor
	 */
	public void sort(){

		running = true;
		fillArray(Array);
		tempArray = new int[Array.length];

		long startTime = System.nanoTime();
		MergeSort(0, Array.length -1);
		long endTime = System.nanoTime();

		long timeTaken = (endTime - startTime) / 1000;

		if (animate == false) {
			this.list.add(new arrayStore(Array.length, timeTaken));
		}
		running = false;	
	}

	/**
	 * MergeSort method, finds the middle value of the array and recursively divides the Array in half until a single value is 
	 * reached using the array indexes as size boundaries, calls merge method once the dividing is complete to reassemble the 
	 * divided array in ascending order.
	 * @param leftIndex the left index of the portion of the array to be sorted
	 * @param rightIndex the right index of the portion of the array to be sorted
	 */
	private void MergeSort(int leftIndex, int rightIndex) {

		if (leftIndex < rightIndex) {
			int middle = leftIndex + (rightIndex - leftIndex) / 2;
			if (running == true) {
				//Splits the left side of the array
				MergeSort(leftIndex, middle);
			}
			if (running == true) {
				//Splits the right side of the array
				MergeSort(middle + 1, rightIndex);
			}
			if (running == true) {
				//Merges both sides together
				merge(leftIndex, middle, rightIndex);
			}
		}
	}

	/**
	 * merge method, copies the portion of the array to be merged to a temporary array then reassembles the left and right sections 
	 * in order by comparing the values of each then writing them to the array
	 * @param leftIndex, the left index of the f the portion of the array to be merged
	 * @param middle, the middle index of the portion of the array to be merged
	 * @param rightIndex, the right index of the portion of the array to be merged
	 */
	private void merge(int leftIndex, int middle, int rightIndex) {

		for (int i = leftIndex; i <= rightIndex; i++) {
			tempArray[i] = Array[i];
		}
		int i = leftIndex;
		int j = middle + 1;
		int k = leftIndex;
		while (i <= middle && j <= rightIndex) {
			if (tempArray[i] <= tempArray[j]) {
				Array[k] = tempArray[i];

				// if required to animate, sends current state of array to drawPanel
				if (animate == true) {
					draw.draw(Array);
					// sleep statement required to make the animation run at a visible speed
					try { Thread.sleep(5);} catch (Exception ex) {}
				}
				i++;
			} else {
				Array[k] = tempArray[j];
				
				// if required to animate, sends current state of array to drawPanel
				if (animate == true) {
					draw.draw(Array);
					// sleep statement required to make the animation run at a visible speed
					try { Thread.sleep(5);} catch (Exception ex) {}
				}
				j++;
			}
			k++;
		}
		while (i <= middle) {
			Array[k] = tempArray[i];
			
			// if required to animate, sends current state of array to drawPanel
			if (animate == true) {
				draw.draw(Array);
				// sleep statement required to make the animation run at a visible speed
				try { Thread.sleep(5);} catch (Exception ex) {}
			}
			k++;
			i++;
		}
	}

	/**
	 * Stop method, changes the boolean value running to false so the code will halt when the value is next checked
	 */
	public void stop(){
		running = false;
	}

	/**
	 * fillArray method fills the classes array with random numbers ranging from 0 to 100
	 * @param a the array to fill
	 */
	private void fillArray (int[] a){
		for (int i = 0; i < a.length; i++){
			a[i] = r.nextInt(largestValue);
		}
	}

	/**
	 * run method, used for running the class as a thread
	 */
	@Override
	public void run() {
		sort();
	}

}