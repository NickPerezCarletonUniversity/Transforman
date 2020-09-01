/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.component;

import processing.core.PVector;

/**
 * Only meant to be used by TransforManApplet.
 * Handles scaling the display to different window dimensions.
 */
public class StageManager extends ContainerComponent {
	private GameComponent stage;
	
	public StageManager(){
		super();
	}
	
	public StageManager(float x, float y){
		this();
		setWindowSize(x, y);
	}
	
	//fits stage to window, adding black bars rather than stretching
	public void setWindowSize(float w, float h){
		float sx = w / app.UNSCALED_WIDTH;
		float sy = h / app.UNSCALED_HEIGHT;
		
		if(sx < sy){
			//add bars at top and bottom
			setTranslation( new PVector(0, h/2 - h/2*(sx/sy)) );
			sy = sx;
		} else if(sy < sx){
			//add bars to left and right
			setTranslation( new PVector(w/2 - w/2*(sy/sx), 0) );
			sx = sy;
		} else {
			//no bars, the window is the same dimension as the stage
			setTranslation( new PVector(0, 0) );
		}
		
		setScale( new PVector(sx, sy) );
	}
	
	public GameComponent getStage(){
		return stage;
	}
	
	//can only display one stage at a time
	public void setStage(GameComponent c){
		removeChild(stage);
		
		stage = c;
		
		addChild(stage);
	}
}
