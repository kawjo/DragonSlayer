
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maze 
{
	private int[] maze;
	private boolean satisfied = false;
	private int nums;
	private boolean initialized = false;
	public int PIXELS_PER_SPACE = 0;
	private int col;
	
	public Maze()
	{
		String fileName = "Maze3.txt";
		File myFile = new File(fileName);
		try
		{
			// attempt to read the specified file.
			Scanner inputFile = new Scanner(myFile);
			nums = inputFile.nextInt();
			col = inputFile.nextInt();
			maze = new int[nums];
			for(int i = 0; i < nums; i++)
			{
				maze[i] = inputFile.nextInt();
			}
			inputFile.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File " + fileName + " not found.");
			return;
		}
		PIXELS_PER_SPACE = DragonController.SCREEN_WIDTH/col;
		initialized = true;
		
	}
	
	
	public boolean isSatisfied()
	{
		return satisfied;
	}
	
	public void checkInitialization()
	{
		if (!initialized)
			throw new SecurityException("Maze object is not initialized properly.");
	}
	
	public int[] fillMazeArray()
	{
		int[] returnVal = new int[nums];
		for(int i = 0; i < nums; i++){
				returnVal[i] = maze[i];
		}
		return returnVal;
	}
	
}