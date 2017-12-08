import javax.swing.JLabel;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.geom.Area;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
public class Board {

private Container gameContentPane;
private JLabel[] mazeJLabels;
private Maze maze;
private int[] board;
private Dragon dragon;
private JLabel[] dragonJLabel;
private JLabel dragonIntLabel;
private ImageIcon tailImage;
private ImageIcon headImage;

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
private int timerCount = 0;
private int level = 1;
private boolean isPauseScreenShowing = false;
private boolean invis = false;
private boolean invisDrag = false;
private JLabel pause;

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
    			//String image = "img/StoneBlock"+PIXELS_PER_SPACE+".jpg";
    			URL img = getClass().getResource("img/StoneBlock"+PIXELS_PER_SPACE+".jpg");
        		mazeJLabels[i].setIcon(new ImageIcon(img));
    		} 
    		else {
    			URL img = getClass().getResource("img/BlackSquare.jpg");
    			mazeJLabels[i].setIcon(new ImageIcon(img));
    	}
    	mazeJLabels[i].setBounds(getXPosition(i),getYPosition(i),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    	gameContentPane.add(mazeJLabels[i]);
    	mazeJLabels[i].setVisible(true);
    }
    
    knight = new Knight(1,.5,getXPosition(findKnight()),getYPosition(findKnight()));
    dragon = new Dragon(level,.2,getXPosition(findDragon()),getYPosition(findDragon()),UP); //Change UP to maze.getDragonDirection()
    knightJLabel = new JLabel();
    dragonJLabel = new JLabel[dragon.TAILS+1];
    dragonIntLabel = new JLabel();
    for(int i = 0; i < dragonJLabel.length; i++){
    	dragonJLabel[i] = new JLabel();
    	URL dragonImage;
    	if(i==0){
    		URL head = getClass().getResource("img/DragonHead"+PIXELS_PER_SPACE+6+".jpg");
    		dragonImage = head;
    		headImage = new ImageIcon(dragonImage);
    	} else if (i == dragonJLabel.length - 1){
    		URL head = getClass().getResource("img/DragonTail"+PIXELS_PER_SPACE+dragon.getTailDirection()+".jpg");
    		dragonImage = head;
    		tailImage = new ImageIcon(dragonImage);
    	} else {

    		URL head = getClass().getResource("img/Dragon"+PIXELS_PER_SPACE+".jpg");
    		dragonImage = head;
    		

    		

    	}
    		dragonJLabel[i].setIcon(new ImageIcon(dragonImage));
    		dragonJLabel[i].setBounds(getXPosition(findDragon()),getYPosition(findDragon()),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    		gameContentPane.add(dragonJLabel[i]);
        	dragonJLabel[i].setVisible(false);
    }
    
    
    URL knightImage = getClass().getResource("img/Knight"+PIXELS_PER_SPACE+".jpg");
    knightJLabel.setIcon(new ImageIcon(knightImage));
    knightJLabel.setBounds(knight.getXLocation(),knight.getYLocation(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    gameContentPane.add(knightJLabel);
    if(!invis){
    	knightJLabel.setVisible(true);
    }
    URL img = getClass().getResource("img/DragonInt"+LEFT+UP+".jpg");
    dragonIntLabel.setIcon(new ImageIcon(img));
    dragonIntLabel.setBounds(0,0,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    gameContentPane.add(dragonIntLabel);
    dragonIntLabel.setVisible(false);
    
    draw();
    gameContentPane.add(knightJLabel);
    gameContentPane.setComponentZOrder(knightJLabel,1);
    for(int i = 0; i < dragonJLabel.length; i++){
    	gameContentPane.add(dragonJLabel[i],2);
    }
    gameContentPane.setComponentZOrder(dragonIntLabel,1);
    
    pause = new JLabel();
    pause.setBounds(0,0,1600,900);
    gameContentPane.add(pause);
    gameContentPane.setComponentZOrder(pause, 0);
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


	if(level>=1){
		dragon.move(PIXELS_PER_SPACE, directions(findDragon()),knight.getXLocation(),knight.getYLocation(),true); //boolean isLookNotSmell
	} else {
		dragon.move(PIXELS_PER_SPACE, directions(findDragon()));
	}
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
	URL img = getClass().getResource("img/DragonHead"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG");
	headImage = new ImageIcon(img);
	//System.out.println(headImage.toString());
	dragonJLabel[0].setIcon(headImage);
	dragonJLabel[0].setBounds(dragon.headX(),dragon.headY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
	if(!invisDrag){
		dragonJLabel[0].setVisible(true);
	}
	//dragonJLabel[0].repaint();
	int i = 1;
	if(dragon.isInIntersection()){
		if(dragon.intX()>dragon.headX()){
			for(int x = dragon.headX()+PIXELS_PER_SPACE; x<=dragon.intX(); x+=PIXELS_PER_SPACE){
				dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG"));
				dragonJLabel[i].setBounds(x,dragon.headY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!invisDrag){
					dragonJLabel[i].setVisible(true);
				}
				i++;
			}
		} else if(dragon.intX()<dragon.headX()){
			for(int x = dragon.headX()-PIXELS_PER_SPACE; x>=dragon.intX(); x-=PIXELS_PER_SPACE){
				dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG"));
				dragonJLabel[i].setBounds(x,dragon.headY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!invisDrag){
					dragonJLabel[i].setVisible(true);
				}
				i++;
			}
		} else if(dragon.intY()>dragon.headY()){
			for(int y = dragon.headY()+PIXELS_PER_SPACE; y<=dragon.intY();y+=PIXELS_PER_SPACE){
				dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG"));
				dragonJLabel[i].setBounds(dragon.headX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()&&!invisDrag){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intY()<dragon.headY()){
			for(int y = dragon.headY()-PIXELS_PER_SPACE; y>=dragon.intY();y-=PIXELS_PER_SPACE){
				dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG"));
				dragonJLabel[i].setBounds(dragon.headX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()&&!invisDrag){dragonJLabel[i].setVisible(true);}
				i++;
			}
		}
		
		if(dragon.intX()>dragon.tailX()){
			for(int x = dragon.tailX()+PIXELS_PER_SPACE; x<dragon.intX(); x+=PIXELS_PER_SPACE){
				dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG"));
				dragonJLabel[i].setBounds(x,dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()&&!invisDrag){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intX()<dragon.tailX()){
			for(int x = dragon.tailX()-PIXELS_PER_SPACE; x>dragon.intX(); x-=PIXELS_PER_SPACE){
				dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG"));
				dragonJLabel[i].setBounds(x,dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()&&!invisDrag){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intY()>dragon.tailY()){
			for(int y = dragon.tailY()+PIXELS_PER_SPACE; y<dragon.intY();y+=PIXELS_PER_SPACE){
				dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG"));
				dragonJLabel[i].setBounds(dragon.tailX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()&&!invisDrag){dragonJLabel[i].setVisible(true);}
				i++;
			}
		} else if(dragon.intY()<dragon.tailY()){
			for(int y = dragon.tailY()-PIXELS_PER_SPACE; y>dragon.intY();y-=PIXELS_PER_SPACE){
				dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG"));
				dragonJLabel[i].setBounds(dragon.tailX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!dragonJLabel[i].isVisible()&&!invisDrag){dragonJLabel[i].setVisible(true);}
				i++;
			}
		}
		if(dragon.tailX()==dragon.intX() && dragon.tailY()==dragon.intY()){
			tailImage = new ImageIcon("img/DragonTail"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG");
		} else {
			tailImage = new ImageIcon("img/DragonTail"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG");
		}
		dragonJLabel[dragon.tailsLeft()].setIcon(tailImage);
		dragonJLabel[dragon.tailsLeft()].setBounds(dragon.tailX(),dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
		if(!invisDrag){
			dragonJLabel[dragon.tailsLeft()].setVisible(true);
		}
		
		if(dragon.tailsLeft()>1 && (dragon.tailY()!=dragon.intY() || dragon.tailX()!=dragon.intX())){
			if(dragon.tailY()>dragon.intY() && dragon.headX()>dragon.intX()){
				dragonIntLabel.setIcon(new ImageIcon("img/DragonInt"+RIGHT+DOWN+".jpg"));
				if(!invisDrag){
					dragonIntLabel.setVisible(true);
				}
				dragonIntLabel.setBounds(dragon.intX(),dragon.intY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
			} else if(dragon.tailX()>dragon.intX() && dragon.headY()>dragon.intY()){
				dragonIntLabel.setIcon(new ImageIcon("img/DragonInt"+RIGHT+DOWN+".jpg"));
				if(!invisDrag){
				dragonIntLabel.setVisible(true);
				}
				dragonIntLabel.setBounds(dragon.intX(),dragon.intY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
			}
			if(dragon.tailY()>dragon.intY() && dragon.headX()<dragon.intX()){
				dragonIntLabel.setIcon(new ImageIcon("img/DragonInt"+LEFT+DOWN+".jpg"));
				if(!invisDrag){
				dragonIntLabel.setVisible(true);
				}
				dragonIntLabel.setBounds(dragon.intX(),dragon.intY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
			} else if(dragon.tailX()<dragon.intX() && dragon.headY()>dragon.intY()){
				dragonIntLabel.setIcon(new ImageIcon("img/DragonInt"+LEFT+DOWN+".jpg"));
				if(!invisDrag){
				dragonIntLabel.setVisible(true);
				}
				dragonIntLabel.setBounds(dragon.intX(),dragon.intY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
			}
			if(dragon.tailX()<dragon.intX() && dragon.headY()<dragon.intY()){
				dragonIntLabel.setIcon(new ImageIcon("img/DragonInt"+LEFT+UP+".jpg"));
				if(!invisDrag){
				dragonIntLabel.setVisible(true);
				}
				dragonIntLabel.setBounds(dragon.intX(),dragon.intY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
			} else if(dragon.tailY()<dragon.intY() && dragon.headX()<dragon.intX()){
				dragonIntLabel.setIcon(new ImageIcon("img/DragonInt"+LEFT+UP+".jpg"));
				if(!invisDrag){
				dragonIntLabel.setVisible(true);
				}
				dragonIntLabel.setBounds(dragon.intX(),dragon.intY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
			}
			if(dragon.tailY()<dragon.intY() && dragon.headX()>dragon.intX()){
				dragonIntLabel.setIcon(new ImageIcon("img/DragonInt"+RIGHT+UP+".jpg"));
				if(!invisDrag){
				dragonIntLabel.setVisible(true);
				}
				dragonIntLabel.setBounds(dragon.intX(),dragon.intY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
			} else if(dragon.headY()<dragon.intY() && dragon.tailX()>dragon.intX()){
				dragonIntLabel.setIcon(new ImageIcon("img/DragonInt"+RIGHT+UP+".jpg"));
				if(!invisDrag){
				dragonIntLabel.setVisible(true);
				}
				dragonIntLabel.setBounds(dragon.intX(),dragon.intY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
			}
		}
		
		
	} else {
		if(dragon.tailX()>dragon.headX()){
			for(int x = dragon.headX()+PIXELS_PER_SPACE; x<=dragon.tailX(); x+=PIXELS_PER_SPACE){
				if(x == dragon.tailX()){dragonJLabel[i].setIcon(new ImageIcon("img/DragonTail"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG"));}
				else {dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG"));}
				dragonJLabel[i].setBounds(x,dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!invisDrag){
				dragonJLabel[i].setVisible(true);
				}
				i++;
			}
		} else if(dragon.tailX()<dragon.headX()){
			for(int x = dragon.headX()-PIXELS_PER_SPACE; x>=dragon.tailX(); x-=PIXELS_PER_SPACE){
				if(x == dragon.tailX()){dragonJLabel[i].setIcon(new ImageIcon("img/DragonTail"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG"));}
				else {dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG"));}
				dragonJLabel[i].setBounds(x,dragon.tailY(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!invisDrag){
				dragonJLabel[i].setVisible(true);
				}
				i++;
			}
		} else if(dragon.tailY()>dragon.headY()){
			for(int y = dragon.headY()+PIXELS_PER_SPACE; y<=dragon.tailY();y+=PIXELS_PER_SPACE){
				if(y == dragon.tailY()){dragonJLabel[i].setIcon(new ImageIcon("img/DragonTail"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG"));}
				else {dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG"));}
				dragonJLabel[i].setBounds(dragon.tailX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!invisDrag){
				dragonJLabel[i].setVisible(true);
				}
				i++;
			}
		} else if(dragon.tailY()<dragon.headY()){
			for(int y = dragon.headY()-PIXELS_PER_SPACE; y>=dragon.tailY();y-=PIXELS_PER_SPACE){
				if(y == dragon.tailY()){dragonJLabel[i].setIcon(new ImageIcon("img/DragonTail"+PIXELS_PER_SPACE+dragon.getTailDirection()+".JPG"));}
				else {dragonJLabel[i].setIcon(new ImageIcon("img/Dragon"+PIXELS_PER_SPACE+dragon.getHeadDirection()+".JPG"));}
				dragonJLabel[i].setBounds(dragon.tailX(),y,PIXELS_PER_SPACE,PIXELS_PER_SPACE);
				if(!invisDrag){
				dragonJLabel[i].setVisible(true);
				}
			
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
			//System.out.println("{");
			//System.out.println(ret[i]);
			//System.out.println("}");
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
	
	if(dragon.tailsLeft() == 0)
	{
		Area area1 = new Area(knightJLabel.getBounds());
		Area area2 = new Area(dragonJLabel[0].getBounds());
		if(knight.currentDirection() != dragon.getHeadDirection())
		{
			return area1.intersects(area2.getBounds2D());
		}
		else
			return false;
	}
	else
	{
		return areaA.intersects(areaB.getBounds2D());
	}
		//return areaA.intersects(areaB.getBounds2D());
	
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
				//System.out.println("TAIL CHOPPED");
				return areaC.intersects(areaD.getBounds2D());
			}
		}
	}
		return areaA.intersects(areaB.getBounds2D());
	
}


private void draw(){
	//knightJLabel.setVisible(false);
	dragonIntLabel.setVisible(false);
	//for(int i = 0; i < dragon.tailsLeft()+1; i++){
		//dragonJLabel[i].setVisible(false);
	//}
	drawKnight();
	drawDragon();
	boolean val = new Random().nextInt(150)==0;
	Random rand = new Random();
	int  n = rand.nextInt(5) + 1;
	if(val){roar(n);}
}

public void setVisible(boolean visible){
	for(int i = 0; i < mazeJLabels.length; i++){
		mazeJLabels[i].setVisible(visible);		
	}
	for(int i = 0; i < dragonJLabel.length; i++){
		dragonJLabel[i].setVisible(visible);
	}
	knightJLabel.setVisible(visible);
}

private void drawKnight() 
{
	knightJLabel.setBounds(knight.getXLocation(),knight.getYLocation(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
	ImageIcon up;
	ImageIcon down;
	
	if(knight.currentDirection() != 0)
	{
		up = new ImageIcon("img/FlameKnight"+PIXELS_PER_SPACE+"_"+knight.currentDirection()+".jpg");
		down = new ImageIcon("img/FlameKnight"+PIXELS_PER_SPACE+"_"+knight.currentDirection()+"Swing.jpg");
	}
	else
	{
		up = new ImageIcon("img/FlameKnight"+PIXELS_PER_SPACE+"_"+knight.getLastDir()+".jpg");
		down = new ImageIcon("img/FlameKnight"+PIXELS_PER_SPACE+"_"+knight.getLastDir()+"Swing.jpg");
	}
	
	
	if(timerCount==12)
	{
		if(knight.getSwing() == 1)
		{
			knightJLabel.setIcon(down);
			knight.setSwing(2);
			if(!invis){
			knightJLabel.setVisible(true);
			}
		}
		else if(knight.getSwing() == 2)
		{
			knightJLabel.setIcon(up);
			knight.setSwing(1);
			if(!invis){
			knightJLabel.setVisible(true);
			}
		}
		timerCount = 0;
	}
	else
	{
		if(knight.getSwing() == 1)
		{
			knightJLabel.setIcon(up);
			if(!invis){
			knightJLabel.setVisible(true);
			}
		}
		else if(knight.getSwing() == 2)
		{
			knightJLabel.setIcon(down);
			if(!invis){
			knightJLabel.setVisible(true);
			}
		}
		timerCount++;
	}
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

public void reset(boolean didKnightKillDragon, int level) throws Exception{
	for(int i = 0; i < dragon.tailsLeft()+1; i++){
		gameContentPane.remove(dragonJLabel[i]);
	}
	gameContentPane.remove(knightJLabel);
	
	board = maze.fillMazeArray();
	
	knight.setLocation(getXPosition(findKnight()),getYPosition(findKnight()));
	
	if(didKnightKillDragon){
		dragon.tailGotChoppedOff(getXPosition(findDragon()),getYPosition(findDragon()),UP);
	} else {
		dragon.restore(getXPosition(findDragon()),getYPosition(findDragon()),UP,level);
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
    		gameContentPane.setComponentZOrder(dragonJLabel[i],2);
        	dragonJLabel[i].setVisible(false);
    }
    knightJLabel.setIcon(new ImageIcon("img/Knight"+PIXELS_PER_SPACE+"_"+knight.currentDirection()+".jpg"));
    knightJLabel.setBounds(knight.getXLocation(),knight.getYLocation(),PIXELS_PER_SPACE,PIXELS_PER_SPACE);
    gameContentPane.add(knightJLabel);
    gameContentPane.setComponentZOrder(knightJLabel, 1);
    if(!invis){
    knightJLabel.setVisible(true);
    }
    gameContentPane.setComponentZOrder(dragonIntLabel, 1);
    if(true&&invis){
    	knightJLabel.setVisible(false);
    }
}
public void playSound(String fileName, boolean loop, int dbBoost) {
    try {
        File soundFile = new File("sounds/"+fileName+".au"); 
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);              
        AudioFormat format = audioIn.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
       audioClip.open(audioIn);
       
       if(dbBoost!=0)
       {
      	 	FloatControl gainControl = 
      		    (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
      		gainControl.setValue(+dbBoost);
       }
      
       audioClip.start();
       if(loop) {audioClip.loop(Clip.LOOP_CONTINUOUSLY);}
    } catch (UnsupportedAudioFileException e) {
       //e.printStackTrace();
    } catch (IOException e) {
       //e.printStackTrace();
    } catch (LineUnavailableException e) {
       //e.printStackTrace();
    }
 }
public void roar(int num)
{
	playSound("Snarl_0"+num, false,0);
}

public void show(URL img){
	pause.setIcon(new ImageIcon(img));
	pause.setBounds(0,0,1600,900);
	pause.setVisible(true);
	isPauseScreenShowing = true;
}

public void stopShow(){
	pause.setVisible(false);
	isPauseScreenShowing = false;
}

public boolean isShowing(){
	return isPauseScreenShowing;
}

public void setInvisKnight(){
	knightJLabel.setVisible(false);
	invis = true;
}

public void setInvisDragon(){
	for(int i = 0; i < dragonJLabel.length; i++){
		dragonJLabel[i].setVisible(false);
	}
	invisDrag = true;
}

public void setInvisBoard(){
	for(int i = 0; i < board.length; i++){
		if(board[i] == WALL){
			mazeJLabels[i].setIcon(new ImageIcon("img/BlackSquare.jpg"));
		}
	}
}

public void undoCheats(){
	knightJLabel.setVisible(true);
	invis = false;
	for(int i = 0; i < dragonJLabel.length; i++){
		dragonJLabel[i].setVisible(true);
	}
	invisDrag = false;
	for(int i = 0; i < board.length; i++){
		if(board[i] == WALL){
			mazeJLabels[i].setIcon(new ImageIcon("img/StoneBlock"+PIXELS_PER_SPACE+".jpg"));
		}
	}
}

public void teleport(){
	knight.setLocation(1450,50);
	knight.setCurrentDirection(0);
	knight.setNextDirection(0);
}






}