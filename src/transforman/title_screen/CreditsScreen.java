/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import processing.core.PVector;
//import transforman.component.ButtonComponent;
import transforman.component.ContainerComponent;
import transforman.component.TextComponent;
import transforman.input.Mouse;

public class CreditsScreen extends ContainerComponent {
	public CreditsScreen(){
		Mouse.clearListeners();
		
		//large print text for our names
		
		String text1 = "Nathan Marshall\nMitchell Blanchard\nNick Perez";
		
		TextComponent credits1 = new TextComponent(text1);
		credits1.setFill(app.color(255));
		credits1.setSize(150);
		credits1.setHorizontalAlignment(app.CENTER);
		credits1.setVerticalAlignment(app.CENTER);
		credits1.setTranslation(new PVector(1940,500));
		addChild(credits1);
		
		//smaller print for resources used
		
		String text2 = "Background art by \"MindChamber\"\n"
				+ "opengameart.org/content/red-baron-backgrounds\n\n"
				+ "Music obtained from freemusicarchive.org";
		
		TextComponent credits2 = new TextComponent(text2);
		credits2.setFill(app.color(255));
		credits2.setSize(100);
		credits2.setHorizontalAlignment(app.CENTER);
		credits2.setVerticalAlignment(app.CENTER);
		credits2.setTranslation(new PVector(1940,1500));
		addChild(credits2);
		
		//button for main menu
		
		TitleScreenButton mainMenuButton = new TitleScreenButton(2800,1800,800,240, "MAIN MENU"){
	    	public void released(){
	    		app.setStage(new TitleScreen());
	    	}
	    };
	    Mouse.addListener(mainMenuButton);
	    addChild(mainMenuButton);
	}
	
	public void drawTransformed(){
	    app.background(0);
	    drawChildren();
	}
}
