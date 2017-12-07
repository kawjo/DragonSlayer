
import javax.swing.JFrame; // for JFrame
import javax.swing.JOptionPane; // messages are displayed using JOptionPane
import javax.swing.JPanel;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.sound.AudioFloatInputStream;

import sun.applet.Main;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon; // messages have an icon
import java.awt.*; // for graphics & MouseListener 
import java.awt.event.*; // need for events and MouseListener
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask; // use as a timer 
import javax.swing.JLabel; //use for display text


class DragonController implements MouseListener, KeyListener  {
    
    private static final int INITIAL_MOVE_TIMER = 40; // time in milliseconds on timer
    
    private String cheatString = "";
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
 
    private Container gameContentPane;
    private boolean gameIsReady = false;
    private boolean gamePause = false;
    private boolean isStart = false;
    
    private java.util.Timer gameTimer = new java.util.Timer();
    
    public final static int SCREEN_WIDTH = 1600;
    public final static int SCREEN_HEIGHT = 900;
    public final static int LEFT = 3;
    public final static int RIGHT = 4;
    public final static int UP = 5;
    public final static int DOWN = 6;
    
    
    public static Board gameBoard;
    public static JFrame boardHolder;
    
    private int level = 1;
    
    public DragonController(String passedInWindowTitle, int gameWindowX, int gameWindowY, int gameWindowWidth, int gameWindowHeight, Board B1) throws Exception{
    	boardHolder = new JFrame(passedInWindowTitle);
    	boardHolder.setSize(gameWindowWidth, gameWindowHeight);
    	boardHolder.setLocation(gameWindowX, gameWindowY);
    	boardHolder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	boardHolder.addMouseListener(this);
        boardHolder.addKeyListener(this);
            
        gameContentPane = boardHolder.getContentPane();
        
        gameBoard = new Board(new Maze(), gameContentPane);
  
        boardHolder.pack();
        boardHolder.setResizable(false);
        
        boardHolder.setVisible(true);
        
        //gameIsReady = true;
        //resetGame();
        // start the timer
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                  runTimer(); // this is my old run() function in this class, just renamed
          }
      };
      gameTimer.schedule(timerTask, 0, INITIAL_MOVE_TIMER);
 
        gameBoard.moveAll();
        // register this class as a mouse event listener for the JFrame

        gameContentPane.addMouseListener(this);
        gameContentPane.addKeyListener(this);
        
        boardHolder.addMouseListener(this);
        boardHolder.addKeyListener(this);
        
        //gameIsReady = true;
        //startPanel = new JPanel();
        //startString = new JLabel("PRESS ANY KEY TO START");
        //startString.setVisible(true);
        //startPanel.add(startString);
        //boardHolder.add(startPanel);
        //startPanel.setVisible(true);

        gameBoard.playSound("Battle_02",true,0);

        //boardHolder.setComponentZOrder(startPanel,0);
        //startString.setFont(new Font(null,Font.PLAIN,100));
        gameBoard.show("img/DragonSlayer.JPG");
        isStart = true;
    }   
    
    //this run() function overrides run() in java.util.TimerTask
    // this is "run" every time the timer expires (yes, they could have picked a better name)
    public void runTimer() {
        if (gameIsReady)
        {
            try {
            	//gameBoard.print();
				gameBoard.moveAll();
				//boardHolder.repaint();
				if(gameBoard.dragon().areTailsExtended()&&gameBoard.didDragonEatKnight()){
					level = 1;
					gameIsReady=false;
					TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            runTimer();
                       }
                  };
                  gameTimer.cancel();
                  gameTimer = new java.util.Timer();
                  gameTimer.schedule(timerTask, 0,INITIAL_MOVE_TIMER);

					//startString.setText("<html>YOU LOST!<br>SUCKER >P<br><br>press any key to play again</html>");
					//startPanel.setVisible(true);
					

					gameBoard.playSound("DragonKill_single",false,6);
					Random r = new Random();
					int i = r.nextInt(5)+1;
					gameBoard.show("img/DragonEaten"+i+".jpg");
					gameBoard.undoCheats();
					gameBoard.reset(false,level);
					System.out.println("YOU LOST");
					System.out.println("SUCKER");
					gamePause = true;
					Thread.sleep(3000);
					gamePause = false;
					
				} else if(gameBoard.dragon().areTailsExtended()&&gameBoard.didKnightKillDragon()){
					gameIsReady=false;
					System.out.println("Tails extended:"+gameBoard.dragon().areTailsExtended());
					System.out.println("Tails left:"+gameBoard.dragon().tailsLeft());
					if(gameBoard.dragon().tailsLeft()==0){
						if(level<5){
							//startString.setText("<html>YOU BEAT LEVEL "+level+"!<br>press any key to play again</html>");
							//startPanel.setVisible(true);
							level++;
							gameBoard.show("img/DragonLevel"+level+".JPG");
							gameBoard.playSound("finalwin",false,4);
							TimerTask timerTask = new TimerTask() {
		                        @Override
		                        public void run() {
		                            runTimer();
		                        }
		                    };
		                    gameTimer.cancel();
		                    gameTimer = new java.util.Timer();
		                    gameTimer.schedule(timerTask, 0,INITIAL_MOVE_TIMER - (level-1)*6);
		                    gameBoard.reset(false,level);
						    gamePause = true;
							Thread.sleep(2000);
							gamePause = false;
						} else {
							//startString.setText("<html>YOU BEAT THE ENTIRE GAME!<br>press any key to play again</html>");
							//gameBoard.setVisible(false);
							//startPanel.setVisible(true);
							gameBoard.show("img/DragonWin.JPG");
							gameBoard.playSound("finalwin",false,4);
							level = 1;
							gameBoard.reset(false,level);
							TimerTask timerTask = new TimerTask() {
		                        @Override
		                        public void run() {
		                            runTimer();
		                        }
		                    };
		                    gameTimer.cancel();
		                    gameTimer = new java.util.Timer();
		                    gameTimer.schedule(timerTask, 0,INITIAL_MOVE_TIMER - (level-1)*6);
							gamePause = true;
							Thread.sleep(10000);
							gamePause = false;
						}
						
					} else {
						//startString.setText("<html>YOU CHOPPED SOME TAIL OFF!<br>press any key to resume</html>");

						gameBoard.playSound("tailChopped",false,0);

						//startPanel.setVisible(true);
						gameBoard.show("img/DragonTailChopped.JPG");
						gameBoard.reset(true,level);
					}
					System.out.println("YOU WON!");
					gamePause = true;
					Thread.sleep(500);
					gamePause = false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				System.out.println(e.getLocalizedMessage());
				System.out.println(e.toString());
			}
        
        }
    }

    public void mousePressed(MouseEvent event){
        
    }
    
    public void mouseEntered(MouseEvent event) {    
        ;
    }
    public void mouseExited(MouseEvent event) {
        ;
    }
    public void mouseClicked( MouseEvent event) {
        ;
    }
    public void mouseReleased( MouseEvent event) {
        ;
    }

    public static void main( String args[]) throws Exception{
        DragonController myController = new DragonController("", 50,50, 1600, 900, gameBoard);// window title, int gameWindowX, int gameWindowY, int gameWindowWidth, int gameWindowHeight){
    }

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar()=='q'){
			cheatString = "";
		} else if(alphabet.indexOf(e.getKeyChar())>0 && !cheatString.endsWith(String.valueOf(e.getKeyChar()))){
			cheatString += e.getKeyChar();
			if(cheatString.length()>20){
				cheatString = "";
			} else {
				boolean valid = false;
				boolean invis = false;
				int n = 0;
				if(cheatString.equals("levelone")){
					n = 1; valid = true;
				}
				if(cheatString.equals("leveltwo")){
					n = 2; valid = true;
				}
				if(cheatString.equals("levelthre")){
					n = 3; valid = true;
				}
				if(cheatString.equals("levelfour")){
					n = 4; valid = true;
				}
				if(cheatString.equals("levelfive")){
					n = 5; valid = true;
				}
				if(cheatString.equals("invisible")){
					n = 1; invis = true;
				}
				if(cheatString.equals("enemy")){
					n = 2; invis = true;
				}
				if(cheatString.equals("nolight")){
					n = 3; invis = true;
				}
				if(valid){
					cheatString = "";
					level = n;
					gameIsReady = false;
					gameBoard.show("img/DragonLevel"+level+".JPG");
					gameBoard.playSound("finalwin",false,4);
					TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            runTimer();
                        }
                    };
                    gameTimer.cancel();
                    gameTimer = new java.util.Timer();
                    gameTimer.schedule(timerTask, 0,INITIAL_MOVE_TIMER - (level-1)*6);
                    try{
                    gameBoard.reset(false,level);
				    gamePause = true;
					Thread.sleep(2000);
					gamePause = false;
                    }catch(Exception ex){System.out.println(ex.toString());}
				}
				if(invis){
					cheatString = "";
					switch(n){
					case 1: gameBoard.setInvisKnight();
					System.out.println("KNIGHT IS INVISIBLE");
					break;
					case 2: gameBoard.setInvisDragon();
					System.out.println("DRAGON IS INVISIBLE");
					break;
					case 3: gameBoard.setInvisBoard();
					System.out.println("BOARD IS INVISIBLE");
					break;
					}
				}
			}
		}
		System.out.println(cheatString);
	}

	public void keyPressed(KeyEvent e) {
		if(!gameIsReady&&!gamePause&&gameBoard.isShowing()){
			if(isStart){
				gameBoard.stopShow(); gameBoard.show("img/Instructions.jpg");
			} else {
				gameBoard.stopShow();gameIsReady=true;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			gameBoard.knight().setNextDirection(LEFT);
		}
	    else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
	    {
	    		gameBoard.knight().setNextDirection(RIGHT);
	    }
	    else if (e.getKeyCode() == KeyEvent.VK_UP)
	    {
	    		gameBoard.knight().setNextDirection(UP);
	    }
	    else if (e.getKeyCode() == KeyEvent.VK_DOWN)
	    {
	    		gameBoard.knight().setNextDirection(DOWN);
	    }

	}

	public void keyReleased(KeyEvent e) {
		if(isStart){
			isStart = false;
		}
		
	}
	

    
}