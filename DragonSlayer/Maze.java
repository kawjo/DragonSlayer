package stack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maze 
{
	private String[][] maze;
	//private StackInterface<OrderedPair> myCurrentPath;
	private boolean satisfied = false;
	private int rows;
	private int cols;
	private OrderedPair start;
	//private OrderedPair end;
	private boolean initialized = false;
	
	public Maze()
	{
		String fileName = "Maze3.txt";
		File myFile = new File(fileName);
		try
		{
			// attempt to read the specified file.
			Scanner inputFile = new Scanner(myFile);
			rows = inputFile.nextInt();
			cols = inputFile.nextInt();
			maze = new String[rows][cols];
			for(int i = 0; i < rows; i++)
			{
				String line = inputFile.next();
				maze[i] = line.split("");
			}
			inputFile.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File " + fileName + " not found.");
			return;
		}
		initialized = true;
		
	}
	
	public int getRows()
	{
		return rows;
	}
	
	public int getCols()
	{
		return cols;
	}
	
	public boolean isSatisfied()
	{
		return satisfied;
	}
	
	private void checkInitialization()
	{
		if (!initialized)
			throw new SecurityException("Maze object is not initialized properly.");
	}
	
	public String[][] to2DArray()
	{
		String[][] returnVal = new String[rows][cols];
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
			{
				returnVal[i][j] = maze[i][j];
			}
		return returnVal;
	}
	
	private static class OrderedPair
	{
		private Integer[] pair;
		
		public OrderedPair()
		{
			pair = new Integer[2];
			pair[0] = 0;
			pair[1] = 0;
		}
		
		public OrderedPair(int i, int j)
		{
			setCoords(i,j);
		}
		
		public Integer[] getCoords()
		{
			return pair;
		}
		
		public int getRow()
		{
			return pair[0];
		}
		
		public int getCol()
		{
			return pair[1];
		}
		
		public void setCoords(int i, int j)
		{
			pair = new Integer[2];
			pair[0] = i;
			pair[1] = j;
		}
		
		public boolean equals(OrderedPair p)
		{
			return p.getRow() == this.getRow() && p.getCol() == this.getCol();
		}
	}
}