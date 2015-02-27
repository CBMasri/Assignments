// Program: Bejeweled
// This program implements a simplified game of bejeweled that
// is played using the keyboard and printed to the console window

import java.util.*;			// For Scanner, Random

public class Bejeweled {
	
	// Keeps track of players score
	public static int PLAYER_SCORE = 0;
	
	public static void main(String[] args){
		char[][] grid = new char[8][8];			// Creates 8x8 grid
		char[] options = {1,2,3,4,5,6};			// Used different chars (* $ @ + ! &)
		
		intro();
		populateRandom(grid, options);
		removeTriples(grid, options);
		drawGrid(grid);
		play(grid, options);
	}
	
	// Introduces player to the game
	public static void intro() {
		System.out.println(" Welcome to Bejeweled!");
		System.out.println(" How to play: Type a row and column index,");
		System.out.println(" followed by the direction to move it.");
		System.out.println(" >> u (up), r (right), d (down), l (left) <<");
	}
	
	// Fills 2D array randomly from selected characters
	public static void populateRandom(char[][] grid, char[] options) {
		Random rndm = new Random();
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				int random = rndm.nextInt(6);		// 0 to 5
				grid[r][c] = options[random];
			}
		}
	}
	
	// Removes triples that were created in the formation
	// of the grid, or when the grid is updated
	public static void removeTriples (char[][] grid, char[] options) {
		Random rndm = new Random();
		
		// Randomly replaces the middle char if the one before and after are the same
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 6; c++) {
				while (grid[r][c] == grid[r][c + 1] && grid[r][c] == grid[r][c + 2]) {
					int random = rndm.nextInt(6);
					grid[r][c + 1] = options[random];
					// Player gets points for rows created via updating the grid
					PLAYER_SCORE += 10;
				}
			}
		}
		
		// Randomly replaces the middle char if the one above and below are the same
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 8; c++) {
				while (grid[r][c] == grid[r + 1][c] && grid[r][c] == grid[r + 2][c]) {
					int random = rndm.nextInt(6);
					grid[r][c] = options[random];
					PLAYER_SCORE += 10;
				}
			}
		}
	}
	
	// Draws current state of grid to console
	public static void drawGrid(char[][] grid) {
		System.out.println();
		System.out.println("     1    2    3    4    5    6    7    8");
		System.out.println();
		for (int r = 0; r < 8; r++) {
			System.out.print(r + 1);
			for (int c = 0; c < 8; c++) {
				System.out.print("    " + grid[r][c]);
			}
			System.out.println();
			System.out.println();
		}
	}
	
	// Allows user to enter a row, column and direction to swap
	public static void play(char[][] grid, char[] options) {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter <row> <column> <direction to move>, or [q] to quit");
		
		String s = input.nextLine();		// Stores user input as string
		String[] array;						// Creates string array
		int row = 0;			// Initializes int row
		int col = 0;			// Initializes int col
		String cmd = "";		// Initializes string cmd
		
		if (s.equalsIgnoreCase("q")) {		// Exits game
			System.out.println();
			if (PLAYER_SCORE >= 150) {
				System.out.println("HIGH SCORE!!! " + "** " + PLAYER_SCORE + " **");
			}
			System.out.println("Thanks for playing!");
			System.exit(0);
		} else {					// Gives values to row, col and cmd variables
			array = s.split(" ");
			row = Integer.parseInt(array[0]);
			col = Integer.parseInt(array[1]);
			cmd = array[2];
		}
		
		while (!cmd.equalsIgnoreCase("u") && !cmd.equalsIgnoreCase("d") && !cmd.equalsIgnoreCase("l") && !cmd.equalsIgnoreCase("r")) {
			System.out.println("That is not a valid command, please try again:");
			row = input.nextInt();
			col = input.nextInt();
			cmd = input.next();	
		}
		
		// User cannot enter row that is out of array bounds
		while (row > 8 || row < 1) {
			System.out.println("That is not a valid row, please try again:");
			row = input.nextInt();
			col = input.nextInt();
			cmd = input.next();
		}
		
		// User cannot enter column that is out of array bounds
		while (col > 8 || col < 1) {
			System.out.println("That is not a valid column, please try again:");
			row = input.nextInt();
			col = input.nextInt();
			cmd = input.next();
		}
		
		// Prints out what the user entered
		System.out.println("You typed: " + row + ", " + col + ", " + cmd);
		
		// Corrects variables: user enters 1-8, array is 0-7
		row--;
		col--;
		
		// Calls isWithinGrid and assigns it to a boolean
		boolean withinGrid = isWithinGrid(row, col, cmd, grid);
		
		if (withinGrid == true) {			// Move was in bounds, char is swapped
			swap(row, col, cmd, grid);
			makesThree(row, col, cmd, grid, options);
		} else if (withinGrid == false) {		// Move was out of grid bounds, repeats play method
			System.out.println("Invalid move (out of bounds), try again.");
			play(grid, options);
		}
	}
	
	// Checks that command given is within bounds of grid
	// (ex. cannot move chars up that are already in top of grid)
	public static boolean isWithinGrid(int row, int col, String cmd, char[][] grid) {
		if (row == 0 && cmd.equalsIgnoreCase("u")) {
			return false;
		}
		if (row == 7 && cmd.equalsIgnoreCase("d")) {
			return false;
		}
		if (col <= 0 && cmd.equalsIgnoreCase("l")) {
			return false;
		}
		if (col >= 7 && cmd.equalsIgnoreCase("r")) {
			return false;
		}
		return true;		// Command is legal, returns true for continued play
	}
	
	// Uses row, col and cmd to swap indicated character in the specified direction
	public static void swap(int row, int col, String cmd, char[][] grid) {
		if (cmd.equalsIgnoreCase("u")) {			// Swaps up
			char temp = grid[row][col];
			grid[row][col] = grid[row - 1][col];
			grid[row - 1][col] = temp;
		} else if (cmd.equalsIgnoreCase("d")) {		// Swaps down
			char temp = grid[row][col];
			grid[row][col] = grid[row + 1][col];
			grid[row + 1][col] = temp;
		} else if (cmd.equalsIgnoreCase("l")) {		// Swaps left
			char temp = grid[row][col];
			grid[row][col] = grid[row][col - 1];
			grid[row][col - 1] = temp;
		} else if (cmd.equalsIgnoreCase("r")) {		// Swaps right
			char temp = grid[row][col];
			grid[row][col] = grid[row][col + 1];
			grid[row][col + 1] = temp;
		}
	}
	
	// Checks to see if players move has created a row of three
	// If it does, play is continued, if not, swap is reversed
	public static void makesThree(int row, int col, String cmd, char[][] grid, char[] options) {
		boolean legalMove = false;
		
		// Traverses the grid, if horizontal triples are found then legalMove is set true
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 6; c++) {
				if (grid[r][c] == grid[r][c + 1] && grid[r][c] == grid[r][c + 2]) {
					legalMove = true;
				}
			}
		}
		
		// Traverses the grid, if vertical triples are found then legalMove is set true
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 8; c++) {
				if (grid[r][c] == grid[r + 1][c] && grid[r][c] == grid[r + 2][c]) {
					legalMove = true;
				}
			}
		}
		
		// Move was legal and creates a row of three, play continues
		if (legalMove == true) {
			eraseTriples(grid, options);
		// Move was not legal, char is swapped back
		} else if (legalMove == false) {
			if (cmd.equalsIgnoreCase("u")) {
				row--;
				cmd = "d";
				swap(row, col, cmd, grid);
			} else if (cmd.equalsIgnoreCase("d")) {
				row++;
				cmd = "u";
				swap(row, col, cmd, grid);
			} else if (cmd.equalsIgnoreCase("l")) {
				col--;
				cmd = "r";
				swap(row, col, cmd, grid);
			} else if (cmd.equalsIgnoreCase("r")) {
				col++;
				cmd = "l";
				swap(row, col, cmd, grid);
			}
			System.out.println("Invalid move (doesn't create 3), try again");
			play(grid, options);
		}
	}
	
	// This method replaces the triples created by the players move
	// with 0's for easy removal and replacement
	public static void eraseTriples(char[][] grid, char[] options) {
		// Traverses grid, replaces horizontal triples with 0's
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 6; c++) {
				if (grid[r][c] == grid[r][c + 1] && grid[r][c] == grid[r][c + 2]) {
					grid[r][c] = 0;
					grid[r][c + 1] = 0;
					grid[r][c + 2] = 0;
					PLAYER_SCORE += 10;		// +10 to players score
				}
			}
		}
		
		// Traverses grid, replaces vertical triples with 0's
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 8; c++) {
				if (grid[r][c] == grid[r + 1][c] && grid[r][c] == grid[r + 2][c]) {
					grid[r][c] = 0;
					grid[r + 1][c] = 0;
					grid[r + 2][c] = 0;
					PLAYER_SCORE += 10;		// +10 to players score
				}
			}
		}
		
		// Calls updateGrid for continued play
		updateGrid(grid, options);
	}
	
	// Removes triples and creates 3 new "jewels" that fall
	// down from the top to re-fill the grid
	public static boolean updateGrid(char[][] grid, char[] options) {
		Random rndm = new Random();
		
		// Repeatedly swaps triples up the array until
		// they are at the top
		for (int i = 0; i < 10; i++) {
			for (int r = 0; r < 8; r++) {
				for (int c = 0; c < 8; c++) {
					if (grid[r][c] == 0 && r >= 1) {		// If 0 is found and it is not already
						char temp = grid[r][c];				// in the top row, 0 is swapped up
						grid[r][c] = grid[r - 1][c];
						grid[r - 1][c] = temp;
					}
				}
			}
		}
		
		// Replaces the triple at the top with new random chars
		// which simulate the new jewels "falling down"
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 8; c++) {
				if (grid[r][c] == 0) {
					int random = rndm.nextInt(6);
					grid[r][c] = options[random];
				}
			}
		}
		
		removeTriples(grid, options);		// Checks for new triples created
		drawGrid(grid);			// Draws the new grid
		System.out.println("Score: " + PLAYER_SCORE);		// Prints player score
		play(grid, options);	// Repeats play
		
		return true;
	}
}