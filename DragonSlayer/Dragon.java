import java.util.Arrays;
import java.util.Random;

public class Dragon {
	
	private int headXLoc;
	private int headYLoc;
	private int tailXLoc;
	private int tailYLoc;
	private int intersecXLoc;
	private int intersecYLoc;
	private final int LEFT = 3;
	private final int RIGHT = 4;
	private final int UP = 5;
	private final int DOWN = 6;
	public final int TAILS;
	public final int SPEED;
	private int tailDirection;
	private int headDirection;
	private int tailsLeft;
	private boolean isDead;
	private boolean isInIntersection = false;
	private boolean isAtIntersection = false;
	private boolean isExtended = false;
	private int tailsExtended = 0;
	private final double PROB_CHANGE_DIRECTION = 0.0;
	
	public Dragon(int tails, int speed, int x, int y, int d){
		TAILS = tails;
		tailsLeft = TAILS;
		if(speed<0 || tails < 0){
			throw new IllegalArgumentException("Negative value");
		}
		SPEED = speed;
		resetDragon(x,y,d);
	}
	
	public void move(int pix,int[] dirs){
		if(isAtIntersection){
			Random r = new Random();
			int dir = opposite(tailDirection);
			while(dir == opposite(tailDirection) || !Arrays.asList(dirs).contains(dir)){
				dir = r.nextInt(4)+3;
			}
			headDirection = dir;
			intersecXLoc = headXLoc;
			intersecYLoc = headYLoc;
			isInIntersection = true;
			isAtIntersection = false;
		} else if(tailsLeft == 0){
			if(Math.random()<PROB_CHANGE_DIRECTION){
				headDirection = opposite(headDirection);
				tailDirection = opposite(tailDirection);
			}
		}
		switch(headDirection){
			case LEFT: headXLoc -= pix;
			break;
			case RIGHT: headXLoc += pix;
			break;
			case UP: headYLoc += pix;
			break;
			case DOWN: headYLoc -= pix;
			break;
		}
		if(isExtended){
			switch(tailDirection){
				case LEFT: tailXLoc -= pix;
				break;
				case RIGHT: tailXLoc += pix;
				break;
				case UP: tailYLoc += pix;
				break;
				case DOWN: tailYLoc -= pix;
				break;
			}
		} else {
			tailsExtended++;
		}
		if(intersecXLoc == tailXLoc && intersecYLoc == tailXLoc){
			tailDirection = headDirection;
			isInIntersection = false;
		}
		if(tailsExtended == tailsLeft){
			isExtended = true;
		}
	}
	
	public void setIntersection(boolean intersec){
		isAtIntersection = intersec;
	}
	
	public boolean isInIntersection(){
		return isInIntersection;
	}
	
	public int headX(){return headXLoc;} public int headY(){return headYLoc;}
	public int tailX(){return tailXLoc;} public int tailY(){return tailYLoc;}
	public int intY(){return intersecYLoc;} public int intX(){return intersecXLoc;}
	
	public void tailGotChoppedOff(int x, int y, int d){
		if(tailsLeft==0){
			isDead = true;
		} else {
			tailsLeft--;
			resetDragon(x,y,d);
		}
	}
	
	private void resetDragon(int x, int y, int d){
		headXLoc = x;
		headYLoc = y;
		tailXLoc = x;
		tailYLoc = y;
		intersecXLoc = x;
		intersecYLoc = y;
		if(d == UP || d == DOWN || d == LEFT || d == RIGHT){
			headDirection = d;
			tailDirection = d;
		}
		isExtended = false;
		tailsExtended = 0;
	}
	
	public int tailsLeft(){return tailsLeft;}
	
	private int opposite(int dir){
		if(dir==UP){
			return DOWN;
		}
		if(dir==DOWN){
			return UP;
		}
		if(dir==LEFT){
			return RIGHT;
		}
		if(dir==RIGHT){
			return LEFT;
		}
		throw new IllegalArgumentException("No direction given");
	}
}
