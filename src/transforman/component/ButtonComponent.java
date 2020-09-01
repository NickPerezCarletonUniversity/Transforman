/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.component;

import java.awt.Shape;
import java.awt.geom.Path2D;

import processing.core.PVector;
import transforman.input.MouseListener;

/**
 * Component used for standard buttons with a shape and text.
 * Other components can be added.
 * For mouse functionality, must be added to the mouse listeners list using Mouse.add(MouseListener m)
 */
public class ButtonComponent extends ContainerComponent implements MouseListener {
	private TextComponent textComponent;
	private ShapeComponent shapeComponent;
	
	//constructor for button with shape and text
	public ButtonComponent(Shape shape, String text){
		//create shape component
		shapeComponent = new ShapeComponent(shape);
		addChild(shapeComponent);
		
		//create text component
		textComponent = new TextComponent(text);
		addChild(textComponent);
		
		//format text component
		textComponent.setSize((float)shapeComponent.getBounds().getHeight()/(float)shapeComponent.getBounds().getHeight()*65);
		textComponent.setHorizontalAlignment(app.CENTER);
		textComponent.setVerticalAlignment(app.CENTER);
		textComponent.setTranslation(new PVector((float) shapeComponent.getBounds().getWidth()/2, (float) shapeComponent.getBounds().getHeight()/2));
	}
	
	//empty constructor
	public ButtonComponent(){
		this(new Path2D.Float(), "");
	}
	
	//called when the mouse is pressed anywhere
	public void mousePressed(){
		//check if the mouse is on the button
		if(containsMouse()) pressed();
	}
	
	//override this for button press functionality
	public void pressed(){}
	
	//called when the mouse is pressed anywhere
	public void mouseReleased(){
		//check if the mouse is on the button
		if(containsMouse()) released();
	}
	
	//override this for button release functionality
	public void released(){}
	
	
	//getters and setters
	
	public ShapeComponent getShapeComponent(){
		return shapeComponent;
	}
	
	public void setShapeComponent(ShapeComponent s){
		removeChild(shapeComponent);
		shapeComponent = s;
		addChild(0, shapeComponent);
	}
	
	public TextComponent getTextComponent(){
		return textComponent;
	}
	
	public void setTextComponent(TextComponent t){
		removeChild(textComponent);
		textComponent = t;
		addChild(textComponent);
	}
}
