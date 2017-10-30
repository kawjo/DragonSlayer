import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Knight {

	private int xLoc;
	private int yLoc;
	private int locInArray;
	private int currentDirection = 0;
	private int nextDirection = 0;
	private int LIVES;
	private int livesLeft = LIVES;
	private boolean isAlive = false;
	private int SPEED = 1;
	private boolean isAtIntersection;
	
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
	
	public void move(int [] dirOptions)
	{
		
		if(isAtIntersection)
		{
			if(currentDirection != nextDirection)
			{
				for(int i = 0; i<dirOptions.length;i++)
				{
					if(dirOptions[i] == nextDirection)
					{
						currentDirection = nextDirection;
						break;
					}
				}
			}
		}
		else
			if(canIMoveToNextSpot(currentDirection))
			{
				switch(currentDirection){
				case LEFT: xLoc -= SPEED;
				break;
				case RIGHT: xLoc += SPEED;
				break;
				case UP: yLoc += SPEED;
				break;
				case DOWN: yLoc -= SPEED;
				break;
			}
	}
	}
	
	private boolean canIMoveToNextSpot(int currentDirection2) 
	{
		return true;
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
