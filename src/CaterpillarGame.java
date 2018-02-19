import uwcse.graphics.*;

import java.util.*;
import java.awt.Color;

import javax.swing.JOptionPane;

import java.awt.Point;


/**
 * A CaterpillarGame displays a garden that contains good and bad cabbages and a
 * constantly moving caterpillar. The player directs the moves of the
 * caterpillar. Every time the caterpillar eats a cabbage, the caterpillar
 * grows. The player wins when all of the good cabbages are eaten and the
 * caterpillar has left the garden. The player loses if the caterpillar eats a
 * bad cabbage or crawls over itself.
 */

public class CaterpillarGame extends GWindowEventAdapter implements
		CaterpillarGameConstants
// The class inherits from GWindowEventAdapter so that it can handle key events
// (in the method keyPressed), and timer events.
// All of the code to make this class able to handle key events and perform
// some animation is already written.
{
	// Game window
	private GWindow window;

	// The caterpillar
	private Caterpillar cp;

	// Direction of motion given by the player
	private int dirFromKeyboard;

	// Do we have a keyboard event
	private boolean isKeyboardEventNew = false;

	// The list of all the cabbages
	private ArrayList cabbages;

	// is the current game over?
	private boolean gameOver;
	
	// Message after game over
	private String messageGameOver;

	// Timer for activating multicolor caterpillar
	private int multiColorTimer = 0;
	
	/**
	 * Constructs a CaterpillarGame
	 */
	public CaterpillarGame() {
		// Create the graphics window
		window = new GWindow("Caterpillar game", WINDOW_WIDTH, WINDOW_HEIGHT);

		// Any key or timer event while the window is active is sent to this
		// CaterpillarGame
		window.addEventHandler(this);

		// Set up the game (fence, cabbages, caterpillar)
		initializeGame();

		// Display the game rules	
		String gameRules = "Eat all of the non red cabbage heads.\n"
							  + "and exit the garden.\n"
							  + "Don't eat the red cabbage heads.\n"
							  + "Don't craw over yourself.\n"
							  + "To move left, press 'j'.\n"
							  + "To move right, press 'l'. \n"
							  + "To move up, press 'u'. \n"
							  + "To move down, press 'k'. \n" ;
		JOptionPane.showMessageDialog(null,gameRules, " Caterpillar game ", 
				JOptionPane.INFORMATION_MESSAGE);
		this.initializeGame();

		// I added the rules of the games here 
		// Also check out the endTheGame()

		// ...
	}

	/**
	 * Initializes the game (draw the garden, garden fence, cabbages,
	 * caterpillar)
	 */
	private void initializeGame() {
		// Clear the window
		window.erase();

		// New game
		gameOver = false;

		// No keyboard event yet
		isKeyboardEventNew = false;

		// Background (the garden)
		window.add(new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT,
				Color.green, true));

		// Create the fence around the garden
		Rectangle rect1 = new Rectangle(GARDEN_X_OFFSET, 0, WINDOW_WIDTH, FENCE_WIDTH, Color.black,true );
		Rectangle rect2 = new Rectangle(WINDOW_WIDTH-FENCE_WIDTH, 0, FENCE_WIDTH, 
				WINDOW_HEIGHT, Color.black, true);
		Rectangle rect3 = new Rectangle(GARDEN_X_OFFSET, WINDOW_WIDTH-FENCE_WIDTH, 
				WINDOW_WIDTH, FENCE_WIDTH, Color.black,true);
		Rectangle rect4 = new Rectangle(GARDEN_X_OFFSET, WINDOW_HEIGHT / 3 * 2, FENCE_WIDTH, 
				WINDOW_HEIGHT, Color.black,true);
		Rectangle rect5 = new Rectangle(GARDEN_X_OFFSET, 0, FENCE_WIDTH, 
				WINDOW_HEIGHT / 3, Color.black, true);


		window.add(rect1);
		window.add(rect2);
		window.add(rect3);
		window.add(rect4);
		window.add(rect5);
		
		
		// Cabbages
		// ...
		cabbages = new ArrayList(N_GOOD_CABBAGES + N_BAD_CABBAGES);

		// Initialize the elements of the ArrayList = cabbages
		// (they should not overlap and be in the garden) ....
		
		// Good cabbages
		for(int i=0;i<N_GOOD_CABBAGES;i++){
			// 40% for multicolored cabbages
			if(Math.random()*10 < 4){
				// Multicolored cabbages
				cabbages.add( new MulticoloredCabbage(window, randomPointInsideGarden()) );
			}else{
				// White cabbages
				cabbages.add( new WhiteCabbage(window, randomPointInsideGarden()) );
			}
		}
		
		// Bad cabbages
		for(int i=0;i<N_BAD_CABBAGES;i++){
			cabbages.add( new RedCabbage(window, randomPointInsideGarden()) );
		}
		
		// Create the caterpillar
		cp = new Caterpillar(window);

		// start timer events (to do the animation)
		this.window.startTimerEvents(ANIMATION_PERIOD);
	}


/**
	 * Get random coordinates for cabbages without overlapping and crossing fence
	 */
	private Point randomPointInsideGarden(){
		// Range for random coordinates
		int Xmax =  WINDOW_WIDTH - CABBAGES_FENCE_OFFSET;
		int Xmin = GARDEN_X_OFFSET + CABBAGES_FENCE_OFFSET;
		int Ymax = WINDOW_HEIGHT - CABBAGES_FENCE_OFFSET;
		int Ymin = CABBAGES_FENCE_OFFSET;
		// Randomize coordinates
		int x = (int) (Math.random()*(Xmax-Xmin)) + Xmin;
		int y = (int) (Math.random()*(Ymax-Ymin)) + Ymin;
		
		boolean isOverlap = false;
		
		do{
			isOverlap = false;
			// Loop through all cabbages
			for(int i=0;i<cabbages.size();i++){
				// Get location of cabbages
				int Xcabbage = (int) ((Cabbage) cabbages.get(i)).getLocation().getX();
				int Ycabbage = (int) ((Cabbage) cabbages.get(i)).getLocation().getY();
				// Check if distance between cabbage and coordinates is too small
				if( Math.abs(Xcabbage-x) < CABBAGES_OFFSET && Math.abs(Ycabbage-y) < CABBAGES_OFFSET){
					isOverlap = true;
					// Generate random coordinates again
					x = (int) (Math.random()*(Xmax-Xmin)) + Xmin;
					y = (int) (Math.random()*(Ymax-Ymin)) + Ymin;
				}
			}
		// Only run if there is/are cabbage(s) overlapping
		}while(isOverlap);
		
		return new Point(x, y);
	}





	/**
	 * Moves the caterpillar within the graphics window every ANIMATION_PERIOD
	 * milliseconds.
	 * 
	 * @param e
	 *            the timer event
	 */
	public void timerExpired(GWindowEvent e) {
		
		// Check multicolor status
		if(cp.multiColor){
			//Accumulate with period
			multiColorTimer += ANIMATION_PERIOD;
		}
		
		// Check timer status
		if(multiColorTimer >= MULTICOLOR_PERIOD){
			// Disable multicolor
			cp.multiColor = false;
			// Reset timer
			multiColorTimer = 0;
		}
		
		// Did we get a new direction from the user?
		// Use isKeyboardEventNew to take the event into account
		// only once
		if (isKeyboardEventNew) {
			isKeyboardEventNew = false;
			cp.move(dirFromKeyboard);
		} else
			cp.move();

		// Is the caterpillar eating a cabbage? Is it crawling over itself?
		// Is the game over? etc...
		// (do all of these checks in a private method)...
		
		// Is the game over?
		// if (???) {
		// endTheGame();
		// }
		
		updateGameStatus();
		
		if(gameOver){
			endTheGame();
		}
		

	}
	
	/**
	 * Update game status
	 */
	private void updateGameStatus(){
		
		// Eat the cabbage(when possible)
		eatCabbage();
		
		// Check status of caterpillar
		if(cp.isCrawlingOverItself()){
			messageGameOver = "Don't crawl over yourself!";
			gameOver = true;
		}else if(isWon()){
			messageGameOver = "Congratulations, you win!";
			gameOver = true;
		}
		
	}
	
	
	/**
	 * Return true if winning the game or false otherwise
	 */
	private boolean isWon(){
		// Go through all cabbages
		for(int i=0;i<cabbages.size();i++){
			// Check if any good cabbage(s) is/are not eaten
			if(cabbages.get(i).getClass().getName() == "WhiteCabbage" ||
			   cabbages.get(i).getClass().getName() == "MulticoloredCabbage"){
				return false;
			}
		}
		
		// Win if eaten all good cabbages and stay outside garden
		return (cp.isOutsideGarden());
	}

	/**
	 * Do effects when caterpillar ate cabbage
	 */
	private void eatCabbage(){
		// Loop through all cabbages
		for(int i=0;i<cabbages.size();i++){
			
			// Check if the head of caterpillar within the cabbage
			if( ((Cabbage) cabbages.get(i)).isPointInCabbage(cp.getHead()) ){
				// Check the type of cabbage
				switch(cabbages.get(i).getClass().getName()){
				
					case("WhiteCabbage"):
						// Remove cabbage from window and perform effect on caterpillar
						((Cabbage)cabbages.get(i)).isEatenBy(cp);
						// Remove from list
						cabbages.remove(i);
						break;
						
					case("MulticoloredCabbage"):
						// Remove cabbage from window and perform effect on caterpillar
						((Cabbage)cabbages.get(i)).isEatenBy(cp);
						// Remove from list
						cabbages.remove(i);
						// Reset multicolor timer
						multiColorTimer = 0;
						break;
						
					case("RedCabbage"):
						// End game because of eating poison cabbage
						messageGameOver = "Don't eat the poisonous cabbages!";
						endTheGame();
				}
			}
			
		}
	}
	
	
	
	/**
	 * Moves the caterpillar according to the selection of the user i: NORTH, j:
	 * WEST, k: EAST, m: SOUTH
	 * 
	 * @param e
	 *            the keyboard event
	 */
	public void keyPressed(GWindowEvent e) {
		switch (Character.toLowerCase(e.getKey())) {
		case 'i':
			dirFromKeyboard = NORTH;
			break;
		case 'j':
			dirFromKeyboard = WEST;
			break;
		case 'l':
			dirFromKeyboard = EAST;
			break;
		case 'k':
			dirFromKeyboard = SOUTH;
			break;
		default:
			return;
		}

		// new keyboard event
		isKeyboardEventNew = true;
	}

	/**
	 * The game is over. Starts a new game or ends the application
	 */
	private void endTheGame() {
		// Stop all animations
		this.window.stopTimerEvents();
		// messageGameOver is an instance String that
		// describes the outcome of the game that just ended
		// (e.g. congratulations! you win)

		// Check if player want next game
		if(!anotherGame(messageGameOver)){
			// Exit program
			System.exit(0);
		}
		
		// Start a new one
		new CaterpillarGame();
					
	}

	/**
	 * Does the player want to play again?
	 */
	private boolean anotherGame(String s) {
		int choice = JOptionPane.showConfirmDialog(null, s
				+ "\nDo you want to play again?", "Game over",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new CaterpillarGame();
	}
}

