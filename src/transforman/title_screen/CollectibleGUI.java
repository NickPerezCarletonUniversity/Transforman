/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import java.awt.geom.Rectangle2D;

import processing.core.PVector;
import transforman.component.ContainerComponent;
import transforman.component.ShapeComponent;
import transforman.component.TextComponent;

/**
 * Displays the number of collectibles found for a level
 */
public class CollectibleGUI extends ContainerComponent {
	private static final float WIDTH = 880, HEIGHT = 200;
	
	public CollectibleGUI(int collectibleCount, int collectibleTotal, float x, float y, float w, float h){
		ShapeComponent shapeComponent = new ShapeComponent(new Rectangle2D.Float(-HEIGHT*0.8f/app.sqrt(2)/2, -HEIGHT*0.8f/app.sqrt(2)/2, HEIGHT*0.8f/app.sqrt(2), HEIGHT*0.8f/app.sqrt(2)));
		shapeComponent.setTranslation(new PVector(HEIGHT/2 + 60, HEIGHT/2 - 10));
		shapeComponent.setRotation(app.PI/4);
		shapeComponent.setStrokeWeight(0);
		shapeComponent.setFill(app.color(255, 255, 0));
		addChild(shapeComponent);
		
		TextComponent textComponent = new TextComponent(collectibleCount+" / "+collectibleTotal);
		textComponent.setTranslation(new PVector(HEIGHT*1.5f, 0));
		textComponent.setFill(app.color(255));
		textComponent.setSize(HEIGHT);
		addChild(textComponent);
		
		setTranslation(new PVector(x, y));
		setScale(new PVector(w/WIDTH, h/HEIGHT));
	}
}
