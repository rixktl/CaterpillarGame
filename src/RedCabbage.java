import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;


public class RedCabbage extends Cabbage {

	private Oval cabbageHead;
	
	public RedCabbage(GWindow window, Point center) {
		super(window, center);
		// Initialize the oval
		this.cabbageHead = new Oval(center.x - CABBAGE_RADIUS, center.y - CABBAGE_RADIUS, 2*CABBAGE_RADIUS, 2*CABBAGE_RADIUS, Color.red, true);
		// Draw it
		draw();
	}

	protected void draw() {
		// Draw to window
		this.cabbageHead.addTo(this.window);
	}

	
	public void isEatenBy(Caterpillar cp) {
		// No visible effect on caterpillar
	}
	
}
