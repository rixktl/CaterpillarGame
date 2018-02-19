import uwcse.graphics.*;

import java.awt.Point;
import java.util.*;
import java.awt.Color;

/**
 * A Caterpillar is the representation and the display of a caterpillar
 */

public class Caterpillar implements CaterpillarGameConstants {
	// The body of a caterpillar is made of Points stored
	// in an ArrayList
	private ArrayList body = new ArrayList();

	// Store the graphical elements of the caterpillar body
	// Useful to erase the body of the caterpillar on the screen
	private ArrayList bodyUnits = new ArrayList();

	// The window the caterpillar belongs to
	private GWindow window;

	// Direction of motion of the caterpillar (NORTH initially)
	private int dir = NORTH;
	
	private final int bodyUnitLength = STEP;
	
	private final int bodyUnitWidth = CATERPILLAR_WIDTH;
	
	// Status of multicolor
	public boolean multiColor = false;
	
	/**
	 * Constructs a caterpillar
	 * 
	 * @param window
	 *            the graphics where to draw the caterpillar.
	 */
	public Caterpillar(GWindow window) {
		// Initialize the graphics window for this Caterpillar
		this.window = window;
		
		// Create the caterpillar (10 points initially)
		// First point
		Point p = new Point();
		p.x = 5;
		p.y = WINDOW_HEIGHT / 2;
		body.add(p);

		// Other points
		for (int i = 0; i < 9; i++) {
			Point q = new Point(p);
			q.translate(STEP, 0);
			body.add(q);
			p = q;
		}

		// Other initializations (if you have more instance fields)
		
		
		// Display the caterpillar (call a private method)
		draw();
	}
	
	/**
	 * Draw the caterpillar
	 */
	private void draw(){
		Point p = (Point)body.get(0);
		
        for(int i=1; i< body.size(); i++)
        {
            Point q = (Point)body.get(i);
            // add a body unit between p and q
            addBodyUnit(p,q,bodyUnits.size());
            p=q;
        }
        
        window.doRepaint();
	}
	
	/**
	 * Moves the caterpillar in the current direction (complete)
	 */
	public void move() {
		move(dir);
	}

	/**
	 * Move the caterpillar in the direction newDir. <br>
	 * If the new direction is illegal, select randomly a legal direction of
	 * motion and move in that direction.<br>
	 * 
	 * @param newDir
	 *            the new direction.
	 */
	public void move(int newDir) {
			
			int oldDir = dir;
			Point head = getHead();
			Point nextHead;
			
			// Check if new direction is illegal(reverse)
			if(newDir == getReverseDirection(dir)){
				// Set new head with old direction
				nextHead = getChangedHead(head, dir);
			}else{
				// Set new head with new direction
				nextHead = getChangedHead(head, newDir);
				// Update new direction
				dir = newDir;
			}
			
			// Check if new direction is crossing fence/wall(illegal)
			if(isCrossEdge(nextHead)){
				int randomDir = (int)(Math.random()*4+1);
				Point randomHead = getChangedHead(head, randomDir);
				
				while(isCrossEdge(randomHead) ||
					  randomDir == getReverseDirection(oldDir) ||
					  randomDir == dir){
					randomDir = (int)(Math.random()*4+1);
					randomHead = getChangedHead(head, randomDir);
				}
				
				// Set new head with random direction
				nextHead = getChangedHead(head, randomDir);
				// Update random direction
				dir = randomDir;
				
			}
			
			// Add new head
			body.add(nextHead);
			// Remove tail
			body.remove(0);
		
	        // Show the new location of the caterpillar
	        moveCaterpillarOnScreen();
	        
	        
	    }
	
	/**
	 * Get the reverse direction of the given one
	 * 
	 * @param newDir
	 * 			the original direction
	 */
	private int getReverseDirection(int newDir){
		switch(newDir)
        {
            case NORTH:
            	return SOUTH;
            case SOUTH:
            	return NORTH;
            case EAST:
            	return WEST;
            case WEST:
            	return EAST;
            default:
            	return 0;
        }
	}
	
	/**
	 * Get the new position of head with new direction
	 * 
	 * @param head
	 * 			old position of head
	 * @param newDir
	 * 			new direction
	 */
	private Point getChangedHead(Point head, int newDir){
		Point copyHead = new Point(head);
		switch(newDir)
        {
            case NORTH:
            	copyHead.y-=STEP;
                break;
            case SOUTH:
            	copyHead.y+=STEP;
                break;
            case EAST:
            	copyHead.x+=STEP;
                break;
            case WEST:
            	copyHead.x-=STEP;
                break;
        }
		
		return copyHead;
	}
	
	
	/**			            
	 * Is the caterpillar about to cross the edge?			
	 * 		
	 * @param head	
	 * 			the next position point of head
	 * 
	 * @return true if the caterpillar is about to cross the edge		
	 */		
	public boolean isCrossEdge(Point head){		
		
		// Get coordinates of head
		int x = (int) head.getX();
		int y = (int) head.getY();
		
		// Check if point is outside garden and within window
		if(x > 0 && x < GARDEN_X_OFFSET &&		
		   y > 0 && y < WINDOW_HEIGHT){		
			return false;		
		}
		
		// Check if head is passing the exit of garden
		if(x >= GARDEN_X_OFFSET && x <= GARDEN_X_OFFSET + FENCE_WIDTH &&		
		   y > WINDOW_HEIGHT / 3 && y < WINDOW_HEIGHT / 3 * 2){		
				return false;
		}
		
		// Check if head is inside garden
		if(x > GARDEN_X_OFFSET + FENCE_WIDTH && x < WINDOW_WIDTH - FENCE_WIDTH &&
		   y > FENCE_WIDTH && y < WINDOW_HEIGHT - FENCE_WIDTH){
			return false;
		}
		
		return true;
				
	}
	

	/**
	 * Are all of the points of the caterpillar outside the garden
	 * 
	 * @return true if the caterpillar is outside the garden and false
	 *         otherwise.
	 */
	public boolean isOutsideGarden() {
		// Initialize x and y coordinates
		int x;
		// Loop through all points construct the caterpillar
		for(int i=0;i<body.size();i++){
			// Get x coordinate of point
			x = ((Point) body.get(i)).x;
			// Check if it is not outside garden
			if(x < 0 || x > GARDEN_X_OFFSET){
				return false;
			}
		}
		
		return true;
	}

	
	/**
	 * Is the caterpillar crawling over itself?
	 * 
	 * @return true if the caterpillar is crawling over itself and false
	 *         otherwise.
	 */
	public boolean isCrawlingOverItself() {
		// Is the head point equal to any other point of the caterpillar?
		// Need to complete this 
		
		// Get coordinates of head
		Point head = getHead();
		int x = (int) head.getX();
		int y = (int) head.getY();

		// Check the caterpillar body
		for(int i = 0; i < body.size() - 1; i++){
			// Get coordinates of body
			int Nx = ((Point) body.get(i)).x;
			int Ny = ((Point) body.get(i)).y;
			// Check if having same coordinates
			if(x == Nx && y == Ny)
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * Return the location of the head of the caterpillar (complete)
	 * 
	 * @return the location of the head of the caterpillar.
	 */
	public Point getHead() {
		return new Point((Point) body.get(body.size() - 1));
	}

	
	/**
	 * Increase the length of the caterpillar (by GROWTH_SPURT elements) Add the
	 * elements at the tail of the caterpillar.
	 */
	private void moveCaterpillarOnScreen(){
		 // Erase the body unit at the tail
        window.remove((Shape)bodyUnits.get(0));
        bodyUnits.remove(0);

        // Add a new body unit at the head
        Point q = (Point)body.get(body.size()-1);
        Point p = (Point)body.get(body.size()-2);
        addBodyUnit(p,q,bodyUnits.size());
        
        // Only change to multicolor when allowed
        if(multiColor)
        	multicoloredBody();
        
        // show it
        window.doRepaint();
	}
	
	/**
	 * Increase the length of the caterpillar (by GROWTH_SPURT elements) Add the
	 * elements at the tail of the caterpillar.
	 */
	public void grow(){
		// Loop by the length of growth
		for(int i=0;i<GROWTH_SPURT;i++){
			// Get the next head
	        Point q = (Point)body.get(body.size()-1);
	        // Get the previous head
	        Point p = (Point)body.get(body.size()-2);
	        // Put to tail
	        addBodyUnit(p,q,0);
	        
	        // Add the previous head of point to tail
	        // Prevent wrong crawling detection
			body.add(0, p);
		}
		
		window.doRepaint();
	}
	
	
	/**
	 * Change body's color to multicolored
	 */
	public void multicoloredBody(){
		// Initialize index, newColor
		int colorIndex = 0;
		Color newColor = Color.black;
		// From head to tail
		for(int i=bodyUnits.size()-1;i>0;i--){
			// Difference color for different parts
			switch(colorIndex){
				case(0):
					newColor = Color.blue;
					break;
				case(1):
					newColor = Color.red;
					break;
				case(2):
					newColor = Color.white;
					break;
				case(3):
					newColor = Color.orange;
					break;
				case(4):
					newColor = Color.yellow;
					// Let the index go back to 0 at next time of loop
					colorIndex = -1;
					break;
			}
			// Change color
			((Rectangle)bodyUnits.get(i)).setColor(newColor);
			// Accumulate for next color
			colorIndex++;
		}
		
	}
	
	
	
	/**
	 * Add rectangle as part of the caterpillar's body
	 */
	private void addBodyUnit(Point p, Point q, int index){
	        // Connect p and q with a rectangle.
	        // To allow for a smooth look of the caterpillar, p and q
	        // are not on the edges of the Rectangle

	        // Upper left corner of the rectangle
	        int x = Math.min(q.x,p.x)-bodyUnitWidth/2;
	        int y = Math.min(q.y,p.y)-bodyUnitWidth/2;

	        // Width and height of the rectangle (vertical or horizontal rectangle?)
	        int width = ((q.y==p.y)?(bodyUnitLength+bodyUnitWidth):bodyUnitWidth);
	        int height = ((q.x==p.x)?(bodyUnitLength+bodyUnitWidth):bodyUnitWidth);

	        // Create the rectangle and place it in the window
	        Rectangle r = new Rectangle(x,y,width,height,Color.red,true);
	        window.add(r);

	        // keep track of that rectangle (we will erase it at some point)
	        bodyUnits.add(index,r);
	}
	
}
