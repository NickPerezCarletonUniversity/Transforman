/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.component;

import java.awt.geom.Rectangle2D;

import processing.core.PImage;
import processing.core.PVector;

/**
 * Component useful for scaling and rotating images.
 */
public class ImageComponent extends GameComponent {
	private PImage image;
	
	public ImageComponent(PImage img){
		image = img;
	}
	
	public ImageComponent(PImage img, float x, float y){
		image = img;
		setTranslation(new PVector(x, y));
	}
	
	public ImageComponent(PImage img, float x, float y, float w, float h){
		image = img;
		setTranslation(new PVector(x, y));
		//stretch to fit the specified width and height
		setScale(new PVector(w/img.width, h/img.height));
	}
	
	public void drawTransformed(){
		app.image(image, 0, 0);
	}
	
	public Rectangle2D.Float getBounds(){
		return new Rectangle2D.Float(0, 0, image.width, image.height);
	}
	
	public boolean contains(PVector p){
		return getBounds().contains(p.x, p.y);
	}

	//getters
	
	public PImage getImage(){
		return image;
	}
	
	//setters
	
	public void setImage(PImage img){
		image = img;
	}
}
