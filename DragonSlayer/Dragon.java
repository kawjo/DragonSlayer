import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

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
	public final double SPEED;
	private int tailDirection;
	private int headDirection;
	private int tailsLeft;
	private boolean isDead;
	private boolean isInIntersection = false;
	private boolean isAtIntersection = false;
	private boolean isExtended = false;
	private int tailsExtended = 0;
	private final double PROB_CHANGE_DIRECTION = 0.02;
	
	public Dragon(int tails, double speed, int x, int y, int d){
		TAILS = tails;
		tailsLeft = TAILS;
		if(speed<0 || tails < 0){
			throw new IllegalArgumentException("Negative value");
		}
		SPEED = speed;
		resetDragon(x,y,d);
	}
	
	public void move(int pix,int[] dirs){
		/*System.out.println("\n"+isExtended);
		System.out.println("headDirection: "+headDirection);
		System.out.println("tailDirection: "+tailDirection);
		System.out.println("headX: "+headXLoc);
		System.out.println("headY: "+headYLoc);
		System.out.println("tailX: "+tailXLoc);
		System.out.println("tailY: "+tailYLoc);
		System.out.println("intX: "+intersecXLoc);
		System.out.println("intY: "+intersecYLoc);
		System.out.println("headDirection: "+headDirection);
		System.out.println("tailDirection: "+tailDirection);
		System.out.println("isAtIntersection: "+isAtIntersection);
		System.out.println("isInIntersection: "+isInIntersection);
		System.out.print("dirs: {");
		for(int i = 0; i < dirs.length; i++){
			if(i < dirs.length-1){
				System.out.print(dirs[i]+",");
			} else {
				System.out.print(dirs[i]+"}");
			}
		}*/
		
		if(tailsLeft==0){
			isExtended = true;
		}
		if(isAtIntersection){
			int dir = randChoice(dirs);
			headDirection = dir;
			if(tailsLeft==0){
				tailDirection = dir;
			}
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
			case LEFT: headXLoc -= (int) pix*SPEED;
			break;
			case RIGHT: headXLoc += (int) pix*SPEED;
			break;
			case UP: headYLoc -= (int) pix*SPEED;
			break;
			case DOWN: headYLoc += (int) pix*SPEED;
			break;
		}
		if(isExtended){
			switch(tailDirection){
				case LEFT: tailXLoc -= (int) pix*SPEED;
				break;
				case RIGHT: tailXLoc += (int) pix*SPEED;
				break;
				case UP: tailYLoc -= (int) pix*SPEED;
				break;
				case DOWN: tailYLoc += (int) pix*SPEED;
				break;
			}
		} else if(headXLoc%pix==0&&headYLoc%pix==0){
			tailsExtended++;
		}
		if(intersecXLoc == tailXLoc && intersecYLoc == tailYLoc){
			tailDirection = headDirection;
			isInIntersection = false;
		} else if(tailsLeft==0){
			intersecXLoc = tailXLoc;
			intersecYLoc = tailYLoc;
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
	
	private int randChoice(int[] dirs){
		Random r = new Random();
		int dir = opposite(tailDirection);
		boolean valid = false;
		while(!valid&&tailsLeft>0){
			dir = r.nextInt(4)+3;
			for(int i = 0; i < dirs.length; i++){
				if(dirs[i] == dir && dir != opposite(tailDirection)){
					valid = true;
				}
			}
		}
		while(!valid&&tailsLeft==0){
			dir = r.nextInt(4)+3;
			for(int i = 0; i < dirs.length; i++){
				if(dirs[i]==dir){
					valid = true;
				}
			}
		}
		return dir;
	}
	
	private int smellChoice(int[] dirs,int kx,int ky){
		int dir = opposite(headDirection);
		if(Math.abs(kx-headXLoc) < Math.abs(ky-headYLoc)){
			if(kx-headXLoc>0){
				if(IntStream.of(dirs).anyMatch(x -> x == RIGHT) && dir != RIGHT){
					dir = RIGHT;
				}
			} else {
				
			}
		} else {
			
		}
		return dir;
	}
	
	public void restore(int x, int y, int d){
		tailsLeft = TAILS;
		resetDragon(x,y,d);
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
		} else {
			throw new IllegalArgumentException("No direction given");
		}
		isExtended = false;
		tailsExtended = 0;
	}
	
	public int tailsLeft(){return tailsLeft;}
	
	public boolean areTailsExtended(){return isExtended;}
	
	public int getHeadDirection(){return headDirection;}
	
	public int getTailDirection(){return tailDirection;}
	
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
