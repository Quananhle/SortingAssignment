
/**
 * The arrayStore class is used to store the Array size and time taken for the array to be sorted, using different instances of the class to store the values
 * and recording them in an ArrayList
 * @author Oliver Palmer, stuNumber 12089466
 *
 */
public class arrayStore {
	
	int ArraySize;
	long SortTime;
	
	/**
	 * Constructor for the arrayStore class
	 * @param a integer, the size of the array sorted
	 * @param t long, the time taken in microseconds to sort an array of size 'a'
	 */
	arrayStore(int a, long t){
		ArraySize = a;
		SortTime = t;
	}
	
	/**
	 * Print method used to print the contents of the ArrayList, used for debugging purposes
	 */
	public void print(){
		System.out.println(ArraySize + " and " + SortTime);
	}

}
