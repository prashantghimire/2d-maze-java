import java.io.IOException;

/**
 * @author Prashant Ghimire
 * Date : 11/25/2013
 * Program Description :
 * This class is the main class. It creates a new Maze object passing the file name as a command line argument. 
 * It calls solve(int row, in col)
 * method of Maze class to solve the instantiated maze.
 */
public class Main {
	public static void main(String[] args) throws IOException{
		try {
			new Maze("maze2.txt"); // args [0] if you want filename from command line
			System.out.println("File has been output!");
		} catch (Exception e) {
			System.out.println("ERROR : "+e.getMessage());
			e.printStackTrace();
		}
	}
}
