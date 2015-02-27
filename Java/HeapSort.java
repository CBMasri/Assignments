/* HeapSort.java
   CSC 225 - Fall 2014
   Assignment 3 - Heap Sort

   To conveniently test the algorithm with a large input, create a
   text file containing space-separated integer values and run the program with
   java HeapSort file.txt
*/

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;

public class HeapSort{
	
	public static int currentSize = 0;		// keeps track of current size of heap
	
	/* HeapSort(A)
		Sort the array A using heap sort.
	*/
	public static void HeapSort (int[] A)
	{
		int[] heap = new int[A.length + 1];		// initializes new heap
		
		// inserts each element into the heap
		for (int i = 0; i < A.length ; i++)
		{
			insert (A[i], heap);
		}
		
		// removes sorted elements from heap and places them back into the array
		for (int i = 0; i < A.length; i++)
		{
			A[i] = removeMin(heap);
		}		

	}

	// inserts new values onto the heap
	public static void insert (int k, int[] heap)
	{
		currentSize++;				// increase size of the heap
		heap[currentSize] = k;		// set the value k to the next available spot in the heap
		bubbleUp(heap);				// bubble k up until its correct placement is found
	}
	
	// deletes values from the min heap, keeping proper heap structure
	public static int removeMin (int[] heap)
	{
		int min = heap[1];				// store current root value to be returned
		heap[1] = heap[currentSize];	// set root to last element in the heap
		currentSize--;					// decrement heap size
		bubbleDown (heap);				// bubble new root down until correct placement is found
		return min;
	}

	// finds correct placement of a node inserted into the heap structure
	public static void bubbleUp (int[] heap)
	{
		// loop from bottom of tree structure to root node, going up level by level
		for (int i = currentSize; i > 1; i = parent(i) )
		{
			// if the child is less than the parent, swap them
			if (heap[i] < heap[parent(i)] )
			{
				swap (i, parent(i), heap);
			}
		}
	}
	
	// finds correct placement of a node that has been placed at the root after a remove operation
	public static void bubbleDown (int[] heap)
	{
		int currentNode = 1;			// root is at index 1
		
		// while the current node has a left child
		while (hasLeft (heap, currentNode))
		{
			int left = leftChild (currentNode);			// store leftchild's index
			int right = rightChild (currentNode);		// store rightchild's index
			int smaller = leftChild (currentNode);
			
			// if the leftchild value is greater than the rightchild value
			if (hasRight(heap, currentNode) && heap[left] > heap[right])
			{
				// the smaller index is stored
				smaller = right;
			}
			
			// if the parent node is greater than the smaller of the two children, swap them
			if (heap[currentNode] > heap[smaller])
			{
				swap (smaller, currentNode, heap);
			}
			else
			{
				break;
			}
			
			// repeat the loop from the new node position
			currentNode = smaller;
		}
	}
	
	// swaps values at indices a and b
	public static void swap (int a, int b, int[] heap)
	{
		int temp = heap[a];
		heap[a] = heap[b];
		heap[b] = temp;
	}
	
	// determines if the value at pos has a right child within the heap
	public static boolean hasRight (int[] heap, int pos)
	{
		return currentSize >= rightChild (pos);
	}
	
	// determines if the value at pos has a left child within the heap
	public static boolean hasLeft (int[] heap, int pos)
	{
		return currentSize >= leftChild (pos);
	}
	
	// determines if the value as pos has a parent within the heap
	public static int parent (int pos)
	{
		return pos / 2;
	}
	
	// returns the index of the left child within the heap
	public static int leftChild (int pos)
	{
		return pos * 2;
	}
	
	// returns the index of the right child within the heap
	public static int rightChild (int pos)
	{
		return pos * 2 + 1;
	}

	/* main()
	   Contains code to test the HeapSort function.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);

		int[] array = new int[inputVector.size()];

		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i);

		System.out.printf("Read %d values.\n",array.length);


		long startTime = System.currentTimeMillis();

		HeapSort(array);

		long endTime = System.currentTimeMillis();

		double totalTimeSeconds = (endTime-startTime)/1000.0;

		// Don't print out the values if there are more than 100 of them
		if (array.length <= 100){
			System.out.println("Sorted values:");
			for (int i = 0; i < array.length; i++)
				System.out.printf("%d ",array[i]);
			System.out.println();
		}

		// Check whether the sort was successful
		boolean isSorted = true;
		for (int i = 0; i < array.length-1; i++)
			if (array[i] > array[i+1])
				isSorted = false;

		System.out.printf("Array %s sorted.\n",isSorted? "is":"is not");
		System.out.printf("Total Time (seconds): %.2f\n",totalTimeSeconds);
	}
}
