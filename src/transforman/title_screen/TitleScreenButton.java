/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import java.awt.geom.Rectangle2D;

import processing.core.PVector;
import transforman.component.ButtonComponent;

/**
 * Basic button used for most purposes
 */
public class TitleScreenButton extends ButtonComponent {
	private String text;
	public TitleScreenButton(float x, float y, float w, float h, String text){
		super(new Rectangle2D.Float(0, 0, w, h), text);
		setTranslation(new PVector(x, y));
		
		this.text = text;
	}
	
	public TitleScreenButton(float x, float y, float w, float h, String text, int size){
		super(new Rectangle2D.Float(0, 0, w, h), text);
		setTranslation(new PVector(x, y));
		app.textSize(size);
		this.text = text;
	}
	
	public void update(){
	    if(getShapeComponent().containsMouse()){
	    	getShapeComponent().setFill(app.color(50));
	    	getShapeComponent().setStroke(app.color(240));
	    	getTextComponent().setFill(app.color(240));
	    	
	    } else {
	    	getShapeComponent().setFill(app.color(240));
	    	getShapeComponent().setStroke(app.color(50));
	    	getTextComponent().setFill(app.color(50));
	    }
	    
	    updateChildren();
	}
}
