import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
/**
 * 
 * @author Prashant Ghimire
 * The purpose of this class is to :
 * -> find the start point of the maze and drive all the way to the goal. Also, it trace the correct path that it moves on.
 * -> different methods, which are described below are used to accomplish this task.
 */
public class Maze {
	private char[][] theMaze;
	private int colStart, rowStart; 
	private int rows, cols;
	private String outputFilename; // this is a way to pass the output filename from the main method to this class
	public Maze(String filename) throws IOException {
		try {
			this.outputFilename = filename;
			Scanner scan = new Scanner(new File(filename));
			StringBuilder sb = new StringBuilder();
			while (scan.hasNext()) {
				sb.append(scan.nextLine());
				this.rows++;
			}
			this.cols = sb.length() / this.rows;
			this.theMaze = new char[this.rows][this.cols];
			int m = 0;
			System.out.println();
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.cols; j++) {
					this.theMaze[i][j] = sb.charAt(m++);
				}
			}
			scan.close();
			findStart();
			solve(this.rowStart, this.colStart);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("ERROR : " + e.getMessage());
		}
	}

	/**
	 * instantiate the index value value of 'S' to this.rowStart, this.colStart
	 */
	private void findStart() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				if (theMaze[i][j] == 'S') {
					this.rowStart = i;
					this.colStart = j;
				}
			}
		}
	}

	/**
	 * 
	 * @param row , current row position of the char
	 * 
	 * @param col, current column index of the char
	 * 
	 * @return true if solved, false other wise.
	 * 
	 * Program Description : 
	 * the program checks if any side positions are ' ' and are "solved"
	 * remember these all are if statements and more than one of them can run and return values being at the same index.
	 * if we had done return solve(row, col + 1) , the program would exit without checking other
	 * possibilities. (infinite recursion/ stack overflow are other possibilities). The problem
	 * is not with the single path, the problem arises when we have MULTIPLE path to go from a point and only one
	 * of those path works.
	 * how this algorithm handles this problem is it will wait to see if those path are really valid at junctions.
	 * it fills the position that row,col were pointing right before with '.' (can be any other characters except ' ', the advantage is it will prevent infinite
	 * recursion. Infinite recursion occurs when function(a,b) and function(c,d) keep calling each other infinitely because the condition is always true)
	 * if one path is not valid , solved = false; will return but the other "trees" will still be exploring the path.
	 * this process continues until we reach 'G'.
	 */
	private boolean solve(int row, int col) {
		char right = this.theMaze[row][col + 1];
		char left = this.theMaze[row][col - 1];
		char up = this.theMaze[row - 1][col];
		char down = this.theMaze[row + 1][col];
		if (right == 'G' || left == 'G' || up == 'G' || down == 'G') {
			this.theMaze[row][col] = '.'; // adding path trace mark to the position we ARE once
											// we're sure we reached the
											// destination
			try {
				File file = new File("SOLUTION"+this.outputFilename); // creating an output file and naming
																			 //according the problem's requirement
				PrintWriter writer = new PrintWriter(file);
				for (int i = 0; i < this.rows; i++) {
					for (int j = 0; j < this.cols; j++) {
						writer.print(this.theMaze[i][j]);
					}
					writer.println();
				}
				writer.close();
			} catch (FileNotFoundException e) {
				System.out.println("ERROR : " + e.getMessage());
			}
			return true; // return true once we reach the destination.
		}
		
		boolean solved = false;
		if (this.theMaze[row][col] != 'S') {
			this.theMaze[row][col] = '.'; // we don't want to mess up by changing value of 'S' when we start our journey.
		}
		if (right == ' ' && !solved) { 			
			solved = solve(row, col + 1);		
		}										
		if (down == ' ' && !solved) {			
			solved = solve(row + 1, col);		
		}										
		if (left == ' ' && !solved) {			
			solved = solve(row, col - 1);		
		}										
		if (up == ' ' && !solved) {
			solved = solve(row - 1, col);
		}
		if (!solved) {
			this.theMaze[row][col] = ' ';	// unsolved path are no good. so lets clear our mark.
		}
		return solved;			// return false if nothing matches.(execute in one branch)
								// I suppose all solved: boolean will return false and the solve() function will finally return false when 
	}							// no path exists. Eventually every path will lead to dead end.
}
