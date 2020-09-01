/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import processing.core.PImage;
import processing.core.PVector;
import transforman.component.ButtonComponent;
import transforman.component.ImageComponent;
import transforman.game_screen.GameScreen;

/**
 * Display the level image and collectibles collected so far.
 * If clicked on, goes to that level.
 */
public class LevelSelectButton extends ButtonComponent {
	ImageComponent imageComponent;
	int levelNum;
	
	public LevelSelectButton(int levelNum, PImage img, int collectibleCount, int collectibleTotal, float x, float y){
		imageComponent = new ImageComponent(img, 0, 0, 88*8, 64*8);
		addChild(imageComponent);
		
		this.levelNum = levelNum;
		
		//levelNum of 0 means level is locked
		if(levelNum > 0) addChild(new CollectibleGUI(collectibleCount, collectibleTotal, 0, 64*8, 88*8, 120));
		
		setTranslation(new PVector(x, y));
	}
	
	public void released(){
		if(levelNum > 0) app.setStage(new GameScreen(levelNum));
	}
}
