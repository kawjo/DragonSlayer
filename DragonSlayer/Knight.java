
public class Knight {

	public int xLoc;
	public int yLoc;
	public int locInArray;
	public int currentDirection = 0;
	public int nextDirection = 0;
	public int LIVES;
	public int livesLeft = LIVES;
	public boolean isAlive = false;
	public int SPEED = 1;
	
	public final static int LEFT = 3;
    public final static int RIGHT = 4;
    public final static int UP = 5;
    public final static int DOWN = 6;
	
	
	public Knight(int passedLIVES, int passedSPEED, int arraySpot)
	{
		LIVES = passedLIVES;
		SPEED = passedSPEED;
		
		setLifeStatus(true);
		
	}
	
	
	public void setLocation(int x, int y)
	{
		locInArray = 0;
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
		if(!life)
		{
			isAlive = false;
		}
		else if(life)
		{
			isAlive = true;
		}
	}
	public void upDateDirection()
	{
		currentDirection = nextDirection;
	}
}
