import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.*;


public class WhiteCabbage extends Cabbage{

	private Oval cabbageHead;
	
	public WhiteCabbage(GWindow window, Point center) {
		super(window, center);
		// Initialize the oval
		this.cabbageHead = new Oval(center.x - CABBAGE_RADIUS, center.y - CABBAGE_RADIUS, 2*CABBAGE_RADIUS, 2*CABBAGE_RADIUS, Color.white, true);
		// Draw it
		draw();
	}

	protected void draw() {
		// Draw to window
		this.cabbageHead.addTo(this.window);
	}


	
	public void isEatenBy(Caterpillar cp) {
		// Remove from window
		this.cabbageHead.removeFromWindow();
		// Let the length grow
		cp.grow();
	}
	

}
