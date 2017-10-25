import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.*;
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


public Board(Maze m,Container gameContentPane){
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
    		if(board[i] == 1){
    			mazeJLabels[i].setIcon(new ImageIcon("img/BlackSquare.jpg"));
    		} 
    		else if(board[i] == 2) {
    		String image = "img/StoneBlock"+PIXELS_PER_SPACE+".jpg";
    		mazeJLabels[i].setIcon(new ImageIcon(image));
    	}
    	mazeJLabels[i].setBounds(getXPosition(i),getYPosition(i),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    	gameContentPane.add(mazeJLabels[i]);
    	mazeJLabels[i].setVisible(true);
    }
    knight = new Knight();
    dragon = new Dragon(1,1,getXPosition(findDragon()),getYPosition(findDragon()),maze.getDragonDirection());
    knightJLabel = new JLabel();
    for(int i = 0; i < dragon.TAILS+1; i++){
    	dragonJLabel[i] = new JLabel();
    	String dragonImage = "";
    	if(i==0){
    		dragonImage = "img/DragonHead"+PIXELS_PER_SPACE+".jpg";
    	} else {
    		dragonImage = "img/Dragon"+PIXELS_PER_SPACE+".jpg";
    	}
    		dragonJLabel[i].setIcon(new ImageIcon(dragonImage));
    }
    
    String knightImage = "img/Knight"+PIXELS_PER_SPACE+".jpg";
    knightJLabel.setIcon(new ImageIcon(knightImage));
}


private int getXPosition(int co){
	int squaresPerWidth = DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE;
	return (co%squaresPerWidth)*PIXELS_PER_SPACE;
}

private int getYPosition(int co){
	int squaresPerHeight = DragonController.SCREEN_HEIGHT/PIXELS_PER_SPACE;
	return (co/squaresPerHeight)*PIXELS_PER_SPACE;
}

private int getCo(int x, int y){
	int squaresPerWidth = DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE;
	return (y/PIXELS_PER_SPACE)*squaresPerWidth + x%PIXELS_PER_SPACE;
}

public Container getBoard(){
	return gameContentPane;
}

private int oneDown(int index){
	if(index<0||index>=board.length){
		throw new IndexOutOfBoundsException();
	}
	if(board[index]==WALL){
		throw new IllegalArgumentException("Illegal Coordinates");
	}
	return board[index+DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE];
}

private int oneUp(int index){
	if(index<0||index>=board.length){
		throw new IndexOutOfBoundsException();
	}
	if(board[index]==WALL){
		throw new IllegalArgumentException("Illegal Coordinates");
	}
	return board[index-DragonController.SCREEN_WIDTH/PIXELS_PER_SPACE];
}

private int findDragon() throws Exception{
	for(int i = 0; i < board.length; i++){
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
		throw new IllegalArgumentException();
	}
	if(isAtIntersection(dragon)){
		dragon.setIntersection(true);
	} else {
		dragon.setIntersection(false);
	}
	dragon.move(PIXELS_PER_SPACE, directions(findDragon()));
	
}

private void move(Knight k){
	if(k != knight){
		throw new IllegalArgumentException();
	}
	if(isAtIntersection(knight)){
		knight.setIntersection(true);
	} else {
		knight.setIntersection(false);
	}
	knight.move(directions(findKnight()));
	board[findKnight()]=CORRIDOR;
	board[getCo(knight.getXLocation(),knight.getYLocation())]=KNIGHT;
}

public void moveAll() throws Exception{
	move(dragon);
	move(knight);
	draw();
}

private int[] directions(int index){
	if(board[index]==WALL){
		throw new IllegalArgumentException("Illegal Coordinates");
	}
	boolean right = board[index+1]==CORRIDOR;
	boolean left = board[index-1]==CORRIDOR;
	boolean up = board[oneUp(index)]==CORRIDOR;
	boolean down = board[oneDown(index)]==CORRIDOR;
	ArrayList<Integer> a = new ArrayList<Integer>();
	if(right){a.add(RIGHT);}
	if(left){a.add(LEFT);}
	if(up){a.add(UP);}
	if(down){a.add(DOWN);}
	int[] ret = new int[a.size()];
	for(int i = 0; i < a.size(); i++){
		ret[i] = (int) a.get(i);
	}
	return ret;
}

public boolean isAtIntersection(Dragon d) throws Exception{
	if(d != dragon){
		throw new IllegalArgumentException();
	}
	if(board[oneUp(findDragon())]==CORRIDOR){
		return true;
	}
	if(board[oneDown(findDragon())]==CORRIDOR){
		return true;
	}
	return false;
}

public boolean isAtIntersection(Knight k) throws Exception{
	if(k != knight){
		throw new IllegalArgumentException();
	}
	if(board[oneUp(findKnight())]==CORRIDOR){
		return true;
	}
	if(board[oneDown(findKnight())]==CORRIDOR){
		return true;
	}
	return false;
}

public boolean didDragonEatKnight(){}

public boolean didKnightKillDragon(){}

private void draw(){
	knightJLabel.setVisible(false);
	knightJLabel.setBounds(knight.getXLocation(),knight.getYLocation(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
	knightJLabel.setVisible(true);
}


}