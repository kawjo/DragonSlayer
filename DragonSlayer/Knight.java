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
	
    
	
	public Knight(int passedLIVES, double passedSPEED, int x, int y)
	{
		LIVES = passedLIVES;
		SPEED = passedSPEED;
		
		setLifeStatus(true);
		setLocation(x,y);
		
		
	}
	
	public void move(int pixToMove,int [] dirOptions){
		if(xLoc%pixToMove!=0||yLoc%pixToMove!=0){
			
			switch(currentDirection){
			case LEFT: xLoc -= pixToMove*SPEED;
			break;
			case RIGHT: xLoc += pixToMove*SPEED;
			break;
			case UP: yLoc -= pixToMove*SPEED;
			break;
			case DOWN: yLoc += pixToMove*SPEED;
			break;
			}
		} else {
			yesMove(pixToMove,dirOptions);
		}
	}
	
	private void yesMove(int pixToMove, int [] dirOptions)
	{
		knightDirs = dirOptions;
		
		System.out.println("Pre-move current Dir: " + currentDirection);
		System.out.println("Pre-move next Dir: " + nextDirection);
		System.out.println("Directions availible: ");
		for(int i =0; i<dirOptions.length; i++)
		{
			System.out.println(dirOptions[i]);
		}
		
		if(isAtIntersection)
		{
			if(currentDirection != nextDirection)
			{
				setMovability(false);
				currentDirection = 0;
				for(int i = 0; i<dirOptions.length;i++)
				{
					if(dirOptions[i] == nextDirection)
					{
						upDateDirection();
						setMovability(true);
						break;
					}
					
				}
			}
		else
		{
			for(int i =0; i<dirOptions.length; i++)
			{
				if(didDirectionChange() && dirOptions[i] == nextDirection)
				{
					upDateDirection();
					break;
				}
				else if(dirOptions[i] == currentDirection)
				{
					upDateDirection();
					setMovability(true);
					break;
				}
				else
				{
					setMovability(false);
					currentDirection = 0;
					break;
				}
			
			}
		}
	}
		if(canIMoveToNextSpot())
			{
				switch(currentDirection){
				case LEFT: xLoc -= pixToMove*SPEED;
				break;
				case RIGHT: xLoc += pixToMove*SPEED;
				break;
				case UP: yLoc -= pixToMove*SPEED;
				break;
				case DOWN: yLoc += pixToMove*SPEED;
				break;
			}
	}
		System.out.println("Post-move current Dir: " + currentDirection);
		System.out.println("Post-move next Dir: " + nextDirection);
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
