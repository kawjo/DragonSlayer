
import javax.swing.JFrame; // for JFrame
import javax.swing.JOptionPane; // messages are displayed using JOptionPane
import javax.swing.JPanel;

import sun.applet.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon; // messages have an icon
import java.awt.*; // for graphics & MouseListener 
import java.awt.event.*; // need for events and MouseListener
import java.util.TimerTask; // use as a timer 
import javax.swing.JLabel; //use for display text


class DragonController extends TimerTask implements MouseListener, KeyListener  {
    
    public static final int MOVE_TIMER = 30; // time in milliseconds on timer
 
    private Container gameContentPane;
    private boolean gameIsReady = false;
    private boolean gamePause = false;
    
    private java.util.Timer gameTimer = new java.util.Timer();
    
    public final static int SCREEN_WIDTH = 1600;
    public final static int SCREEN_HEIGHT = 900;
    public final static int LEFT = 3;
    public final static int RIGHT = 4;
    public final static int UP = 5;
    public final static int DOWN = 6;
    
    
    public static Board gameBoard;
    public static JFrame boardHolder;
    private JLabel startString;
    private JPanel startPanel;
    
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
        run();
        //resetGame();
        // start the timer
        gameTimer.schedule(this, 0, MOVE_TIMER);    
 
        gameBoard.moveAll();
        // register this class as a mouse event listener for the JFrame

        gameContentPane.addMouseListener(this);
        gameContentPane.addKeyListener(this);
        
        boardHolder.addMouseListener(this);
        boardHolder.addKeyListener(this);
        
        //gameIsReady = true;
        startPanel = new JPanel();
        startString = new JLabel("PRESS ANY KEY TO START");
        startString.setVisible(true);
        startPanel.add(startString);
        boardHolder.add(startPanel);
        startPanel.setVisible(true);
        playSound("Battle.wav");
        boardHolder.setComponentZOrder(startPanel,0);
        startString.setFont(new Font(null,Font.PLAIN,100));
    }   
    
    private void resetGame()
    {
 
    }
    
    private void dragonGotHit() {
    	
    }
    	
    private void knightGotHit() {
    	
    }
    
    private void didSomeoneDie() {
    	
    }
    	
    private boolean didIWin(){
         return false;
    }
    
    //this run() function overrides run() in java.util.TimerTask
    // this is "run" everytime the timer expires (yes, they could have picked a better name)
    public void run() {
        if (gameIsReady)
        {
    		
            try {
            	//gameBoard.print();
				gameBoard.moveAll();
				//boardHolder.repaint();
				if(gameBoard.dragon().areTailsExtended()&&gameBoard.didDragonEatKnight()){
					level = 1;
					gameIsReady=false;
					startString.setText("<html>YOU LOST!<br>SUCKER >P<br><br>press any key to play again</html>");
					gameBoard.setVisible(false);
					startPanel.setVisible(true);
					gameBoard.reset(false,level);
					System.out.println("YOU LOST");
					System.out.println("SUCKER");
					
					gamePause = true;
					Thread.sleep(250);
					gamePause = false;
					
				} else if(gameBoard.dragon().areTailsExtended()&&gameBoard.didKnightKillDragon()){
					gameIsReady=false;
					System.out.println("Tails extended:"+gameBoard.dragon().areTailsExtended());
					System.out.println("Tails left:"+gameBoard.dragon().tailsLeft());
					if(gameBoard.dragon().tailsLeft()==0){
						if(level<6){
							startString.setText("<html>YOU BEAT LEVEL "+level+"!<br>press any key to play again</html>");
							gameBoard.setVisible(false);
							startPanel.setVisible(true);
							level++;
						} else {
							startString.setText("<html>YOU BEAT THE ENTIRE GAME!<br>press any key to play again</html>");
							gameBoard.setVisible(false);
							startPanel.setVisible(true);
							level = 1;
						}
						gameBoard.reset(false,level);
						gamePause = true;
						Thread.sleep(1000);
						gamePause = false;
					} else {
						startString.setText("<html>YOU CHOPPED SOME TAIL OFF!<br>press any key to resume</html>");
						gameBoard.setVisible(false);
						startPanel.setVisible(true);
						gameBoard.reset(true,level);
					}
					System.out.println("YOU WON!");
					gamePause = true;
					Thread.sleep(250);
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
		
		;
		
	}

	public void keyPressed(KeyEvent e) {
		if(!gameIsReady&&!gamePause&&startPanel.isVisible()){gameBoard.setVisible(true);startPanel.setVisible(false);gameIsReady=true;}
		
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
		;
		
	}
	public static synchronized void playSound(String url) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
		          Main.class.getResourceAsStream("sounds/" + url));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}
    
}