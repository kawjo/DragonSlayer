import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
public class Board {

private Container gameContentPane;
private JLabel[] mazeJLabels;
private Maze maze;
private int[] board;
private Dragon dragon;
private JLabel[] dragonJLabel;
private JLabel knightJLabel;
private Knight knight;
private final int DRAGON = 9;
private final int DRAGON_HEAD = 8;
private final int KNIGHT = 7;
private final int PIXELS_PER_SPACE;
private final int WALL = 2;
private final int CORRIDOR = 1;
private final int LEFT = 3;
private final int RIGHT = 4;
private final int UP = 5;
private final int DOWN = 6;


public Board(Maze m,Container gameContentPane) throws Exception{
	maze = m;
	PIXELS_PER_SPACE = maze.PIXELS_PER_SPACE;
	this.gameContentPane = gameContentPane;
	gameContentPane.setPreferredSize(new Dimension(DragonController.SCREEN_WIDTH-26, DragonController.SCREEN_HEIGHT-26));
	gameContentPane.setLayout(null);
    gameContentPane.setBackground(Color.GREEN);
    board = maze.fillMazeArray();
    mazeJLabels = new JLabel[board.length];
    for(int i = 0; i < mazeJLabels.length; i++){
    		mazeJLabels[i] = new JLabel();
    		if(board[i] == 2){
    			String image = "img/StoneBlock"+PIXELS_PER_SPACE+".jpg";
        		mazeJLabels[i].setIcon(new ImageIcon(image));
    		} 
    		else {
    			mazeJLabels[i].setIcon(new ImageIcon("img/BlackSquare.jpg"));
    	}
    	mazeJLabels[i].setBounds(getXPosition(i),getYPosition(i),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    	gameContentPane.add(mazeJLabels[i]);
    	mazeJLabels[i].setVisible(true);
    }
    
    knight = new Knight(1,.5,getXPosition(findKnight()),getYPosition(findKnight()));
    dragon = new Dragon(2,.2,getXPosition(findDragon()),getYPosition(findDragon()),UP); //Change UP to maze.getDragonDirection()
    knightJLabel = new JLabel();
    dragonJLabel = new JLabel[dragon.TAILS+1];
    
    for(int i = 0; i < dragonJLabel.length; i++){
    	dragonJLabel[i] = new JLabel();
    	String dragonImage = "";
    	if(i==0){
    		dragonImage = "img/DragonHead"+PIXELS_PER_SPACE+".jpg";
    	} else if (i == dragonJLabel.length - 1){
    		dragonImage = "img/DragonTail"+PIXELS_PER_SPACE+".jpg";
    	} else {
    		dragonImage = "img/Dragon"+PIXELS_PER_SPACE+".jpg";
    	}
    		dragonJLabel[i].setIcon(new ImageIcon(dragonImage));
    		
    		dragonJLabel[i].setBounds(getXPosition(findDragon()),getYPosition(findDragon()),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    		gameContentPane.add(dragonJLabel[i]);
        	dragonJLabel[i].setVisible(false);
    }
    
    
    String knightImage = "img/Knight"+PIXELS_PER_SPACE+".jpg";
    knightJLabel.setIcon(new ImageIcon(knightImage));
    knightJLabel.setBounds(knight.getXLocation(),knight.getYLocation(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    gameContentPane.add(knightJLabel);
    knightJLabel.setVisible(true);
    
    draw();
    gameContentPane.add(knightJLabel);
    gameContentPane.setComponentZOrder(knightJLabel,0);
    for(int i = 0; i < dragonJLabel.length; i++){
    	gameContentPane.add(dragonJLabel[i],0);
    }
}


private int getXPosition(int co){
	int squaresPerWidth = DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE;
	return (co%squaresPerWidth)*PIXELS_PER_SPACE;
}

private int getYPosition(int co){
	int squaresPerWidth = DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE;
	return (co/squaresPerWidth)*PIXELS_PER_SPACE;
}

public int getCo(int x, int y){
	int squaresPerWidth = DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE;

	//return ((y/PIXELS_PER_SPACE)*squaresPerWidth + x/PIXELS_PER_SPACE);

	int val = (y/PIXELS_PER_SPACE)*squaresPerWidth + x/PIXELS_PER_SPACE;
	return val;

}

public Container getBoard(){
	return gameContentPane;
}

private int oneDown(int index){
	if(index<0||index>=board.length){
		throw new IndexOutOfBoundsException("Wrong number");
	}
	if(board[index]==WALL){
		throw new IllegalArgumentException("Illegal Coordinates");
	}
	return board[index+DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE];
}

private int oneUp(int index){
	if(index<0||index>=board.length){
		throw new IndexOutOfBoundsException("Wrong number");
	}
	if(board[index]==WALL){
		throw new IllegalArgumentException("Illegal Coordinates");
	}
	return board[index-DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE];
}

private int findDragon() throws Exception{
	for(int i = 0; i < board.length; i++){
		//System.out.println(board[i]);
		if(board[i]==DRAGON_HEAD){
			return i;
		}
	}
	throw new Exception("Dragon not found");
}

private int findKnight() throws Exception{
	for(int i = 0; i < board.length; i++){
		if(board[i]==KNIGHT){
			return i;
		}
	}
	throw new Exception("Knight not found");
}

public Knight knight(){return knight;}

public Dragon dragon(){return dragon;}

private void move(Dragon d) throws Exception{
	if(d != dragon){
		throw new IllegalArgumentException("Not dragon");
	}
	if(isAtIntersection(dragon)&&dragon.headX()%PIXELS_PER_SPACE==0&&dragon.headY()%PIXELS_PER_SPACE==0){
		 		dragon.setIntersection(true);
		 	} else {
		 		dragon.setIntersection(false);
		 	}
	
	dragon.move(PIXELS_PER_SPACE, directions(findDragon()));
	if(dragon.headX()%PIXELS_PER_SPACE==0&&dragon.headY()%PIXELS_PER_SPACE==0){
		upDateDragonLocation();
	}
}

private void upDateDragonLocation(){
	for(int i = 0; i < board.length; i++){
		if(board[i]==DRAGON || board[i] == DRAGON_HEAD){
			board[i] = CORRIDOR;
		}
	}
	for(int i = 0; i < dragon.tailsLeft()+1; i++){
		board[getCo(dragonJLabel[i].getLocation().x,dragonJLabel[i].getLocation().y)]=CORRIDOR;
	}
	board[getCo(dragon.headX(),dragon.headY())]=DRAGON_HEAD;
	if(dragon.isInIntersection()){
		if(dragon.intX()>dragon.headX()){
			for(int x = dragon.intX(); x>dragon.headX(); x-=PIXELS_PER_SPACE){
				board[getCo(x,dragon.headY())]=DRAGON;
			}
		} else if(dragon.intX()<dragon.headX()){
			for(int x = dragon.intX(); x<dragon.headX(); x+=PIXELS_PER_SPACE){
				board[getCo(x,dragon.headY())]=DRAGON;
			}
		} else if(dragon.intY()>dragon.headY()){
			for(int y = dragon.intY(); y>dragon.headY();y-=PIXELS_PER_SPACE){
				board[getCo(dragon.headX(),y)]=DRAGON;
			}
		} else if(dragon.intY()<dragon.headY()){
			for(int y = dragon.intY(); y<dragon.headY();y+=PIXELS_PER_SPACE){
				board[getCo(dragon.headX(),y)]=DRAGON;
			}
		}
		
		if(dragon.intX()>dragon.tailX()){
			for(int x = dragon.intX(); x>=dragon.tailX(); x-=PIXELS_PER_SPACE){
				board[getCo(x,dragon.tailY())]=DRAGON;
			}
		} else if(dragon.intX()<dragon.tailX()){
			for(int x = dragon.intX(); x<=dragon.tailX(); x+=PIXELS_PER_SPACE){
				board[getCo(x,dragon.tailY())]=DRAGON;
			}
		} else if(dragon.intY()>dragon.tailY()){
			for(int y = dragon.intY(); y>=dragon.tailY();y-=PIXELS_PER_SPACE){
				board[getCo(dragon.tailX(),y)]=DRAGON;
			}
		} else if(dragon.intY()<dragon.tailY()){
			for(int y = dragon.intY(); y<=dragon.tailY();y+=PIXELS_PER_SPACE){
				board[getCo(dragon.tailX(),y)]=DRAGON;
			}
		}
		
	} else {
		if(dragon.tailX()>dragon.headX()){
			for(int x = dragon.tailX(); x>dragon.headX(); x-=PIXELS_PER_SPACE){
				board[getCo(x,dragon.headY())]=DRAGON;
			}
		} else if(dragon.tailX()<dragon.headX()){
			for(int x = dragon.tailX(); x<dragon.headX(); x+=PIXELS_PER_SPACE){
				board[getCo(x,dragon.headY())]=DRAGON;
			}
		} else if(dragon.tailY()>dragon.headY()){
			for(int y = dragon.tailY(); y>dragon.headY();y-=PIXELS_PER_SPACE){
				board[getCo(dragon.headX(),y)]=DRAGON;
			}
		} else if(dragon.tailY()<dragon.headY()){
			for(int y = dragon.tailY(); y<dragon.headY();y+=PIXELS_PER_SPACE){
				board[getCo(dragon.headX(),y)]=DRAGON;
			}
		}
	}
}

private void drawDragon(){
	dragonJLabel[0].setBounds(dragon.headX(),dragon.headY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
	dragonJLabel[0].setVisible(true);
	int i = 1;
	if(dragon.isInIntersection()){
		if(dragon.intX()>dragon.headX()){
			for(int x = dragon.intX(); x>dragon.headX(); x-=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(x,dragon.headY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intX()<dragon.headX()){
			for(int x = dragon.intX(); x<dragon.headX(); x+=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(x,dragon.headY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intY()>dragon.headY()){
			for(int y = dragon.intY(); y>dragon.headY();y-=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(dragon.headX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intY()<dragon.headY()){
			for(int y = dragon.intY(); y<dragon.headY();y+=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(dragon.headX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		}
		
		if(dragon.intX()>dragon.tailX()){
			for(int x = dragon.intX()-PIXELS_PER_SPACE; x>=dragon.tailX(); x-=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(x,dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intX()<dragon.tailX()){
			for(int x = dragon.intX()+PIXELS_PER_SPACE; x<=dragon.tailX(); x+=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(x,dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intY()>dragon.tailY()){
			for(int y = dragon.intY()-PIXELS_PER_SPACE; y>=dragon.tailY();y-=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(dragon.tailX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intY()<dragon.tailY()){
			for(int y = dragon.intY()+PIXELS_PER_SPACE; y<=dragon.tailY();y+=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(dragon.tailX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		}
		
	} else {
		if(dragon.tailX()>dragon.headX()){
			for(int x = dragon.headX()+PIXELS_PER_SPACE; x<=dragon.tailX(); x+=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(x,dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.tailX()<dragon.headX()){
			for(int x = dragon.headX()-PIXELS_PER_SPACE; x>=dragon.tailX(); x-=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(x,dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.tailY()>dragon.headY()){
			for(int y = dragon.headY()+PIXELS_PER_SPACE; y<=dragon.tailY();y+=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(dragon.tailX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.tailY()<dragon.headY()){
			for(int y = dragon.headY()-PIXELS_PER_SPACE; y>=dragon.tailY();y-=PIXELS_PER_SPACE){
				dragonJLabel[i].setBounds(dragon.tailX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()){dragonJLabel[i].setVisible(true);}
				i++;
			}
		}
	}
}


private void move(Knight k) throws Exception{
	if(k != knight){
		throw new IllegalArgumentException("not knight");
	}
	if(isAtIntersection(knight)){
		knight.setIntersection(true);
	} else {
		knight.setIntersection(false);
	}
	knight.move(PIXELS_PER_SPACE,directions(findKnight()));
	if(knight.getXLocation()%PIXELS_PER_SPACE==0&&knight.getYLocation()%PIXELS_PER_SPACE==0){
		board[findKnight()]=CORRIDOR;
		board[getCo(knight.getXLocation(),knight.getYLocation())]=KNIGHT;
	}
}



public void moveAll() throws Exception{
	move(dragon);
	move(knight);
	draw();
}

private int[] directions(int index) throws Exception{
	if(board[index]==WALL){
		throw new IllegalArgumentException("Illegal Coordinates");
	}
	boolean right = board[index+1]!=WALL;
	boolean left = board[index-1]!=WALL;
	boolean up = oneUp(index)!=WALL;
	boolean down = oneDown(index)!=WALL;
	ArrayList<Integer> a = new ArrayList<Integer>();
	if(right){a.add(RIGHT);}
	if(left){a.add(LEFT);}
	if(up){a.add(UP);}
	if(down){a.add(DOWN);}
	int[] ret = new int[a.size()];
	for(int i = 0; i < a.size(); i++){
		ret[i] = (int) a.get(i);
		if(index==findDragon()){
			System.out.println("{");
			System.out.println(ret[i]);
			System.out.println("}");
		}
	}
	return ret;
}

public boolean isAtIntersection(Dragon d) throws Exception{
	if(d != dragon){
		throw new IllegalArgumentException("not dragon");
	}
	if(oneUp(findDragon())!=WALL && oneDown(findDragon())==WALL){
		return true;
	}
	if(oneUp(findDragon())==WALL && oneDown(findDragon())!=WALL){
		return true;
	}
	if(board[findDragon()-1]==WALL && board[findDragon()+1]!=WALL){
		return true;
	}
	if(board[findDragon()+1]==WALL && board[findDragon()-1]!=WALL){
		return true;
	}
	if(oneDown(findDragon())!=WALL && oneUp(findDragon())!=WALL && board[findDragon()-1]!=WALL && board[findDragon()+1]!=WALL){
		return true;
	}
	return false;
}

public boolean isAtIntersection(Knight k) throws Exception{
	if(k != knight){
		throw new IllegalArgumentException("not knight");
	}
	if(oneUp(findKnight())!=WALL && oneDown(findKnight())==WALL){
		return true;
	}
	if(oneUp(findKnight())==WALL && oneDown(findKnight())!=WALL){
		return true;
	}
	if(board[findKnight()-1]==WALL && board[findKnight()+1]!=WALL){
		return true;
	}
	if(board[findKnight()+1]==WALL && board[findKnight()-1]!=WALL){
		return true;
	}
	if(oneDown(findKnight())!=WALL && oneUp(findKnight())!=WALL && board[findKnight()-1]!=WALL && board[findKnight()+1]!=WALL){
		return true;
	}
	return false;
}

public boolean didDragonEatKnight() throws Exception{
	
	Area areaA = new Area(knightJLabel.getBounds());
    Area areaB = new Area(dragonJLabel[0].getBounds());

    return areaA.intersects(areaB.getBounds2D());
	
}

public boolean didKnightKillDragon(){
	
	Area areaA = new Area(knightJLabel.getBounds());
	Area areaB = new Area(dragonJLabel[dragonJLabel.length -1].getBounds());
	
	if(dragon.tailsLeft() == 0)
	{
		Area area1 = new Area(knightJLabel.getBounds());
		Area area2 = new Area(dragonJLabel[0].getBounds());
		if(knight.currentDirection() == dragon.getHeadDirection())
		{
			/*knight.currentDirection()-dragon.getHeadDirection() != 1 
			||knight.currentDirection()-dragon.getHeadDirection() != -1*/
			return area1.intersects(area2.getBounds2D());
		}
		else
			return false;
	}
	else
	{
		for(int i=0;i<dragonJLabel.length;i++)
		{
			Area areaC = new Area(knightJLabel.getBounds());
			Area areaD = new Area(dragonJLabel[i].getBounds());
			if(areaC.intersects(areaD.getBounds2D()))
			{
				return areaC.intersects(areaD.getBounds2D());
			}
		}
	}
		return areaA.intersects(areaB.getBounds2D());
	
}


private void draw(){
	knightJLabel.setVisible(false);
	knightJLabel.setBounds(knight.getXLocation(),knight.getYLocation(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
	knightJLabel.setVisible(true);
	for(int i = 0; i < dragon.tailsLeft()+1; i++){
		dragonJLabel[i].setVisible(false);
	}
	drawDragon();
}

public void print(){
	int count = 0;
	System.out.println();
	for(int i: board){
		System.out.print(i+" ");
		if(count==DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE){
			count = 0;
			System.out.println();
		}
		count++;
	}
}

public void reset(boolean didKnightKillDragon) throws Exception{
	for(int i = 0; i < dragon.tailsLeft()+1; i++){
		gameContentPane.remove(dragonJLabel[i]);
	}
	gameContentPane.remove(knightJLabel);
	
	this.print();
	board = maze.fillMazeArray();
	this.print();
	knight.setLocation(getXPosition(findKnight()),getYPosition(findKnight()));
	
	if(didKnightKillDragon){
		dragon.tailGotChoppedOff(getXPosition(findDragon()),getYPosition(findDragon()),UP);
	} else {
		dragon.restore(getXPosition(findDragon()),getYPosition(findDragon()),UP);
	}
	
	knightJLabel = new JLabel();
    dragonJLabel = new JLabel[dragon.tailsLeft()+1];
    
    for(int i = 0; i < dragonJLabel.length; i++){
    	dragonJLabel[i] = new JLabel();
    	String dragonImage = "";
    	if(i==0){
    		dragonImage = "img/DragonHead"+PIXELS_PER_SPACE+".jpg";
    	} else if (i == dragonJLabel.length - 1){
    		dragonImage = "img/DragonTail"+PIXELS_PER_SPACE+".jpg";
    	} else {
    		dragonImage = "img/Dragon"+PIXELS_PER_SPACE+".jpg";
    	}
    		dragonJLabel[i].setIcon(new ImageIcon(dragonImage));
    		dragonJLabel[i].setBounds(getXPosition(findDragon()),getYPosition(findDragon()),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    		gameContentPane.add(dragonJLabel[i]);
    		gameContentPane.setComponentZOrder(dragonJLabel[i],0);
        	dragonJLabel[i].setVisible(false);
    }
    knightJLabel.setIcon(new ImageIcon("img/Knight"+PIXELS_PER_SPACE+".jpg"));
    knightJLabel.setBounds(knight.getXLocation(),knight.getYLocation(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    gameContentPane.add(knightJLabel);
    gameContentPane.setComponentZOrder(knightJLabel, 0);
    knightJLabel.setVisible(true);
}


}