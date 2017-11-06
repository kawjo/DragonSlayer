import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Knight {

	private int xLoc;
	private int yLoc;
	private int locInArray;
	private int currentDirection = 4;
	private int nextDirection = 4;
	private int LIVES;
	private int livesLeft = LIVES;
	private boolean isAlive = false;
	private double SPEED = 1;
	private boolean isAtIntersection;
	private int[] knightDirs;
	private boolean canMove;
	
	public final static int LEFT = 3;
    public final static int RIGHT = 4;
    public final static int UP = 5;
    public final static int DOWN = 6;
	
    
	
	public Knight(int passedLIVES, int passedSPEED, int x, int y)
	{
		LIVES = passedLIVES;
		SPEED = passedSPEED;
		
		setLifeStatus(true);
		setLocation(x,y);
		
		
	}
	
	public void move(int pixToMove, int [] dirOptions)
	{
		knightDirs = dirOptions;
		
		if(isAtIntersection)
		{
			if(currentDirection != nextDirection)
			{
				for(int i = 0; i<dirOptions.length;i++)
				{
					if(dirOptions[i] == nextDirection)
					{
						currentDirection = nextDirection;
						setMovability(true);
						break;
					}
				}
			}
		else
		{
			for(int i =0;i<dirOptions.length;i++)
			{
				if(didDirectionChange() && dirOptions[i] == nextDirection)
				{
					upDateDirection();
				}
				else if(dirOptions[i] == currentDirection)
				{
					setMovability(true);
					break;
				}
				else
				{
					setMovability(false);
					currentDirection = 0;
				}
			
			}
		}
	}
		if(canIMoveToNextSpot())
			{
				switch(currentDirection){
				case LEFT: xLoc -= pixToMove;
				break;
				case RIGHT: xLoc += pixToMove;
				break;
				case UP: yLoc -= pixToMove;
				break;
				case DOWN: yLoc += pixToMove;
				break;
			}
	}
	}
	
	private boolean didDirectionChange()
	{
		if(currentDirection != nextDirection)
		{
			return true;
		}
		else
			return false;
	}
	private boolean canIMoveToNextSpot() 
	{
		return canMove;
	}
	private void setMovability(boolean movement)
	{
		canMove = movement;
	}
	public void setLocation(int x, int y)
	{
		xLoc = x;
		yLoc = y;
	}
	public int currentDirection()
	{
		return currentDirection;
	}
	public void setNextDirection(int n)
	{
		nextDirection = n;
	}
	public void setCurrentDirection(int n)
	{
		currentDirection = n;
	}
	public int getXLocation()
	{
		return xLoc;
	}
	public int getYLocation()
	{
		return yLoc;
	}
	public int getLives()
	{
		return livesLeft;
	}
	public boolean isAlive()
	{
		return isAlive;
	}
	public void setLifeStatus(boolean life)
	{
		isAlive = life;
	}
	public void setIntersection(boolean intersec){
		isAtIntersection = intersec;
	}
	public void upDateDirection()
	{
		currentDirection = nextDirection;
	}
}
