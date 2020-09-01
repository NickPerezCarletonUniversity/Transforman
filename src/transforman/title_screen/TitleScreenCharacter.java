/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import processing.core.PImage;
import processing.core.PVector;
import transforman.component.ImageComponent;
import transforman.component.ShapeComponent;
import transforman.core.TransforManApplet;

/**
 * Component used for all the shapes seen on the title screen
 */
public class TitleScreenCharacter extends ShapeComponent {
	private static TransforManApplet app = TransforManApplet.getInstance();
	
	//character index flags
	public static final int NUM_CHARACTERS = 4;
	public static final int SQUARE = 0, CIRCLE = 1, STAR = 2, TRIANGLE = 3;
	public static final PImage[] IMAGES = {app.loadImage("square.png"), app.loadImage("sticky.png"), app.loadImage("spiky.png"), app.loadImage("switchy.png")};
	private ImageComponent imageComponent;
	
	int type;
	  
	public TitleScreenCharacter(float x, float y, float r, int charIndex, int type){
		super();
		
		this.type = type;
		
		setTranslation(new PVector(x, y));
		setScale(new PVector(r, r));
		
		//setup based on character index
		
		if(type != TitleScreen.FALLING){
			imageComponent = new ImageComponent(IMAGES[charIndex], -1, -1, 2, 2);
		}
		
		setCharacter(charIndex);
	}
	
	public void setCharacter(int charIndex){
		if(type == TitleScreen.FALLING){
			//choose shape for falling background shapes
			
			int fillAlpha = 136;
			int strokeAlpha = 0;
			
			Shape s = null;
			switch(charIndex){
			case SQUARE:
				s = new Rectangle2D.Float(-1, -1, 2, 2);
				
				setFill(app.color(255, 130, 0, fillAlpha));
				break;
			    
			case CIRCLE:
				s = new Ellipse2D.Float(-1, -1, 2, 2);
				
				setFill(app.color(0, 190, 0, fillAlpha));
				break;
				
			case STAR:
				s = new Path2D.Float();
				((Path2D.Float) s).moveTo(1, -1);
				
				int starPoints = 4;
				for(int i=0; i<starPoints*2; i++){
					float dist = app.sqrt(2);
					if(i%2 == 0) dist *= 0.4f;
					
					float px = dist*app.cos(app.TWO_PI * i / (starPoints*2));
					float py = dist*app.sin(app.TWO_PI * i / (starPoints*2));
					
					((Path2D.Float) s).lineTo(px, py);
				}
				((Path2D.Float) s).closePath();
				
				setFill(app.color(100, 100, 255, fillAlpha));
				break;
				
			case TRIANGLE:
				s = new Path2D.Float();
				((Path2D.Float) s).moveTo(-1, 1);
				((Path2D.Float) s).lineTo(0, -1);
				((Path2D.Float) s).lineTo(1, 1);
				((Path2D.Float) s).closePath();
				
				setFill(app.color(255, 100, 255, fillAlpha));
				break;
			}
			
			setStroke(app.color(255, strokeAlpha));
			setStrokeWeight(0.2f);
			setShape(s);
		}
		
		//choose image and set fill colour for central and orbiting characters
		else {
			imageComponent.setImage(IMAGES[charIndex]);
			
			switch(charIndex){
			case SQUARE:
				setFill(app.color(255, 130, 0));
				break;
			    
			case CIRCLE:
				setFill(app.color(0, 190, 0));
				break;
				
			case STAR:
				setFill(app.color(100, 100, 255));
				break;
				
			case TRIANGLE:
				setFill(app.color(255, 100, 255));
				break;
			}
		}
	}
	
	public void drawTransformed(){
		if(type == TitleScreen.FALLING){
			super.drawTransformed();
		} else {
			imageComponent.draw();
		}
	}
}
