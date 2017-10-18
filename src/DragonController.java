
import javax.swing.JFrame; // for JFrame
import javax.swing.JOptionPane; // messages are displayed using JOptionPane
import javax.swing.ImageIcon; // messages have an icon
import java.awt.*; // for graphics & MouseListener 
import java.awt.event.*; // need for events and MouseListener
import java.util.TimerTask; // use as a timer 


class DragonController extends TimerTask implements MouseListener, KeyListener  {
    
    public static final int MOVE_TIMER = 70; // time in milliseconds on timer
 
    private Container gameContentPane;
    private final int dragonTails = 1;
    private final int knightLives = 1;
    private boolean gameIsReady = false;
    
    private java.util.Timer gameTimer = new java.util.Timer();
    
    public final static int SCREEN_WIDTH = 1600;
    public final static int SCREEN_HEIGHT = 900;
    public static Board gameBoard;
    public static JFrame boardHolder;
    
    public DragonController(String passedInWindowTitle, int gameWindowX, int gameWindowY, int gameWindowWidth, int gameWindowHeight, Board B1){
    		boardHolder = new JFrame(passedInWindowTitle);
    		boardHolder.setSize(gameWindowWidth, gameWindowHeight);
    		boardHolder.setLocation(gameWindowX, gameWindowY);
    		boardHolder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		
        gameContentPane = boardHolder.getContentPane();
        
        gameContentPane.setLayout(null); // not need layout, will use absolute system
        gameContentPane.setBackground(Color.white);
        
        gameBoard = B1;
    		boardHolder.add(gameBoard.getBoard());
        boardHolder.setVisible(true);
        
        //gameKnight = new Knight();// JFrame, pushes required, % change direction
        //gameDragon = new Dragon();// JFrame, pushes required, % change direction           
        //resetGame();
        // start the timer
        gameTimer.schedule(this, 0, MOVE_TIMER);    
 
        // register this class as a mouse event listener for the JFrame
        boardHolder.addMouseListener(this);
    }   
    
    private void resetGame(int dragonTails){
        
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
        if (gameIsReady){
            
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

    public static void main( String args[]){
        DragonController myController = new DragonController("", 50,50, 1600, 900, gameBoard);// window title, int gameWindowX, int gameWindowY, int gameWindowWidth, int gameWindowHeight){
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
    
}