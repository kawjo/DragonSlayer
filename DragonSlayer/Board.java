import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.*;
public class Board {

private Container gameContentPane;
private JLabel[] mazeJLabels;
private Maze maze;
private int[] board;
//private Dragon dragon;
//private Knight knight;
private final int DRAGON = 9;
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
    	} else {
    		String image = "img/StoneBlock"+PIXELS_PER_SPACE+".jpg";
    		mazeJLabels[i].setIcon(new ImageIcon(image));
    	}
    	mazeJLabels[i].setBounds(getXPosition(i),getYPosition(i),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    	gameContentPane.add(mazeJLabels[i]);
    	mazeJLabels[i].setVisible(true);
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

public Container getBoard(){
	return gameContentPane;
}

/*
public Knight knight(){}

public Dragon dragon(){}

public void move(Dragon d){}

public void move(Knight k){}

public boolean isAtIntersection(Dragon d){}

public boolean isAtIntersection(Knight k){}

public boolean didDragonEatKnight(){}

public boolean didKnightKillDragon(){}

private void drawKnight(){}

private void drawDragon(){}

public int whereIsKnight(Dragon d){}

public int whereIsDragon(Dragon d){}
*/

}