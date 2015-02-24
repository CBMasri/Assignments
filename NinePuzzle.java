/* Program: NinePuzzle.java
   CSC 225 - Fall 2014
   
	Solves the common 3x3 sliding puzzle board.
*/

//import java.util.Queue;
import java.util.*;
import java.io.File;

public class NinePuzzle {

	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;
	// create array to record ancestors for BFS
	private static int[] ancestor = new int[NUM_BOARDS];


	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the 
		value 0),return true if the board is solvable and false otherwise. 
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/
	public static boolean SolveNinePuzzle(int[][] B){
	
		int[][] adjacencyList=BuildNinePuzzleGraph();				// builds the adjacency list for the puzzle
		
		int goal = getGoalBoard();									// gets the index for the solved puzzle state
		int current = getIndexFromBoard(B);							// gets the index of the given puzzle state
		
		boolean solved = runBFS(adjacencyList, current, goal);		// finds the path, if possible
		
		if (solved == true)
		{
			Stack<Integer> stack = new Stack<Integer>();			// initialize stack for printing path
			int b = goal;
			stack.push(goal);					// prints goal board last
			
			while (b != current)
			{
				stack.push( ancestor[b] );		// push ancestors (starting from goal board) onto stack
				b = ancestor[b];				// move up one level
			}
			
			System.out.println("\nThe solution is:\n");
			
			while ( !stack.isEmpty() )			// print the solution move by move
			{
				int p = stack.pop();
				printBoard( getBoardFromIndex(p) );
			}
			return true;
		}
		return false;		// no path was found, board is not solvable
	}
	
	
	public static int[][] BuildNinePuzzleGraph(){
		
		// adjacency list
		int[][] adjacencyList=new int[NUM_BOARDS][4];
		
		// initialize the adjacency list with negative values
		for (int i=0;i<adjacencyList.length;i++)
			for (int j=0;j<adjacencyList[0].length;j++)
				adjacencyList[i][j]=-1;
		
		/* ... Your code here ... */
		
		for (int i = 0; i < NUM_BOARDS; i++)
		{
			int[][] currentBoard = getBoardFromIndex(i);			// get board from index
			int[] emptySpace = findEmptySpace(currentBoard);		// find empty space (0) in the board
			int[][] newBoard;										// create a new board for swapping values
			
			int x = emptySpace[0];				// row coordinate of empty space
			int y = emptySpace[1];				// col coordinate of empty space
			
			if (x > 0)			// move tile [x-1][y] down one row
			{
				newBoard = getBoardFromIndex(i);
				swap(newBoard, x-1, y, x, y);
				adjacencyList[i][0] = getIndexFromBoard( newBoard );
			}
			if (x < 2)		// move tile [x+1][y] up one row
			{
				newBoard = getBoardFromIndex(i);
				swap(newBoard, x+1, y, x, y);
				adjacencyList[i][1] = getIndexFromBoard( newBoard );
			}
			if (y > 0)			// move tile [x][y-1] to the right
			{
				newBoard = getBoardFromIndex(i);
				swap(newBoard, x, y-1, x, y);
				adjacencyList[i][2] = getIndexFromBoard( newBoard );
			}
			if (y < 2)			// move tile [x][y+1] to the left
			{
				newBoard = getBoardFromIndex(i);
				swap(newBoard, x, y+1, x, y);
				adjacencyList[i][3] = getIndexFromBoard( newBoard );
			}
		}
		return adjacencyList;
	}
	
	// Returns the location of the empty space in the given board
	public static int[] findEmptySpace(int[][] currentBoard)
	{
		int[] emptySpace = new int[2];		// stores x,y coordinates of the empty space
		
		for (int i = 0; i < 3; i++)			// finds empty space
		{
			for (int j = 0; j < 3; j++)
			{
				if (currentBoard[i][j] == 0)
				{
					emptySpace[0] = i;
					emptySpace[1] = j;
				}
			}
		}
		return emptySpace;
	}
	
	// Swaps two elements in a 2D array
	public static void swap(int[][] board, int w, int x, int y, int z)
	{
		int temp = board[w][x];
		board[w][x] = board[y][z];
		board[y][z] = temp;
	}
	
	// Finds a minimum length BFS path from u (given board) to v (goal board)
	public static boolean runBFS(int[][] adjacencyList, int u, int v)
	{
		for (int i = 0; i < ancestor.length; i++)			// initialize ancestor array to -1
		{
			ancestor[i] = -1;
		}
		
		Queue<Integer> queue = new LinkedList<Integer>();		// create queue for BFS
		ancestor[u] = u;										// set given board as its own ancestor
		queue.add(u);											// add given board to queue
		int current;
		
		while ( !queue.isEmpty() )			// while the queue is not empty
		{
			current = queue.remove();		// set current to the first thing in the queue
			if (current == v)				// if current is the goal board, we're done
			{
				return true;
			}
			
			for (int i = 0; i < 4; i++)				// check all adjacent matrices
			{
				int w = adjacencyList[current][i];	// get index of adjacent matrix
				
				if (w != -1 && ancestor[w] == -1)
				{
					queue.add(w);			// add adjacent matrix to queue
					ancestor[w] = current;	// record the ancestor
				}
			}
		}
		return false;			// the goal board was not accessible, the board is not solvable
	}
	
	// Returns the index from the solved 9 puzzle board
	public static int getGoalBoard()
	{
		int[][] goal = new int[][] {
			{ 1,2,3 },
			{ 4,5,6 },
			{ 7,8,0 }
		};
		return ( getIndexFromBoard(goal) );
	}
	
	/*  printBoard(B)
		Print the given 9-puzzle board. The SolveNinePuzzle method above will
		use this method when printing the sequence of moves which solves the input
		board. If any other method is used (e.g. printing the board manually), the
		submission may lose marks.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	
	
	/* Board/Index conversion functions
	*/
	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;
			
			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}
		
	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}

	public static void main(String[] args){

		Scanner s;

		if (args.length > 0){
			// If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			// Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		// Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}
}