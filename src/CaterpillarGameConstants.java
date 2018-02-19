/**
 * Interface that lists useful constants for the caterpillar game
 */

public interface CaterpillarGameConstants {
	// Possible directions for the motion of the caterpillar
	public static final int NORTH = 1;

	public static final int EAST = 2;

	public static final int WEST = 3;

	public static final int SOUTH = 4;

	// Distance covered by the caterpillar in one move
	public static final int STEP = 10;

	// Number of body elements added to the caterpillar when it grows
	// (after eating a good cabbage)
	public static final int GROWTH_SPURT = 5;

	// Thickness of the caterpillar
	public static final int CATERPILLAR_WIDTH = 6;

	// Number of good cabbages
	public static final int N_GOOD_CABBAGES = 10;

	// Number of bad cabbages
	public static final int N_BAD_CABBAGES = 10;

	// Distance between cabbage and other cabbages
	public static final int CABBAGES_OFFSET = 50;
	
	// Distance between cabbages and fence
	public static final int CABBAGES_FENCE_OFFSET = 50;
	
	// Radius of a cabbage head
	public static final int CABBAGE_RADIUS = 10;

	// Size of the graphics window
	public static final int WINDOW_HEIGHT = 500;

	public static final int WINDOW_WIDTH = 500;
	
	// X coordinate for the left part of garden
	public static final int GARDEN_X_OFFSET = 120;
	
	// Width of the fence
	public static final int FENCE_WIDTH = 10;

	// Period of the animation (in ms)
	public static final int ANIMATION_PERIOD = 150;
	
	// Period of the multicolored caterpillar (in ms)
	public static final int MULTICOLOR_PERIOD = 10000;
}