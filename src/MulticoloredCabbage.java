import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.*;


public class MulticoloredCabbage extends Cabbage{

	private Oval core;
	private Oval middle;
	private Oval edge;
	
	public MulticoloredCabbage(GWindow window, Point center) {
		super(window, center);
		
		int radius = CABBAGE_RADIUS;
		int offset = 2;
		
		// Initialize three ovals
		this.edge = new Oval(center.x - radius, center.y - radius, 2*radius, 2*radius, Color.yellow, true);
		radius -= offset;
		
		this.middle = new Oval(center.x - radius, center.y - radius, 2*radius, 2*radius, Color.orange, true);
		radius -= offset;
		
		this.core = new Oval(center.x - radius, center.y - radius, 2*radius, 2*radius, Color.blue, true);
		
		// Draw it
		draw();
	}

	protected void draw() {
		// Draw all to window
		// from biggest to smallest
		this.edge.addTo(this.window);
		this.middle.addTo(this.window);
		this.core.addTo(this.window);
	}

	
	public void isEatenBy(Caterpillar cp) {
		// Remove all from window
		this.edge.removeFromWindow();
		this.middle.removeFromWindow();
		this.core.removeFromWindow();
		// Let the length grow
		cp.grow();
		// Allow multicolor body
		cp.multiColor = true;
	}

}
