
import javax.swing.JFrame; // for JFrame
import javax.swing.JOptionPane; // messages are displayed using JOptionPane
import javax.swing.ImageIcon; // messages have an icon
import java.awt.*; // for graphics & MouseListener 
import java.awt.event.*; // need for events and MouseListener
import java.util.TimerTask; // use as a timer 


class DragonController extends TimerTask implements MouseListener, KeyListener  {
    
    public static final int MOVE_TIMER = 700; // time in milliseconds on timer
 
    private Container gameContentPane;
    private final int dragonTails = 1;
    private final int knightLives = 1;
    private boolean gameIsReady = false;
    
    private java.util.Timer gameTimer = new java.util.Timer();
    
    public final static int SCREEN_WIDTH = 1600;
    public final static int SCREEN_HEIGHT = 900;
    public final static int LEFT = 3;
    public final static int RIGHT = 4;
    public final static int UP = 5;
    public final static int DOWN = 6;
    
    public static Board gameBoard;
    public static JFrame boardHolder;
    
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
        
        gameIsReady = true;
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
        
        gameIsReady = true;

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
				boardHolder.repaint();
				if(gameBoard.didDragonEatKnight()){
					gameIsReady=false;
					System.out.println("YOU LOST");
					System.out.println("SUCKER");
				} else if(gameBoard.didKnightKillDragon()){
					gameIsReady=false;
					System.out.println("YOU WON!");
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
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			gameBoard.knight().setCurrentDirection(LEFT);
		}
	    else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
	    {
	    		gameBoard.knight().setCurrentDirection(LEFT);
	    }
	    else if (e.getKeyCode() == KeyEvent.VK_UP)
	    {
	    		gameBoard.knight().setCurrentDirection(UP);
	    }
	    else if (e.getKeyCode() == KeyEvent.VK_DOWN)
	    {
	    		gameBoard.knight().setCurrentDirection(DOWN);
	    }
		
		/*switch(e.getKeyCode()){
		case KeyEvent.VK_KP_LEFT: gameBoard.knight().setNextDirection(LEFT);
		break;
		case KeyEvent.VK_KP_RIGHT: gameBoard.knight().setNextDirection(LEFT);
>>>>>>> 0dbcce2bbc145798613928c5e63374162d414765
		break;
		case KeyEvent.VK_KP_UP: gameBoard.knight().setNextDirection(UP);
		break;
		case KeyEvent.VK_KP_DOWN: gameBoard.knight().setNextDirection(DOWN);
		break;
		}*/
	}

	public void keyReleased(KeyEvent e) {
		;
		
	}
    
}