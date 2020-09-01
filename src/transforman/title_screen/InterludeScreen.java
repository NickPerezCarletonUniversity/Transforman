/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import java.io.File;

import processing.core.PImage;
import processing.core.PVector;
import transforman.component.ContainerComponent;
import transforman.component.ImageComponent;
import transforman.component.TextComponent;
import transforman.fonts.Fonts;
import transforman.game_screen.GameScreen;
import transforman.input.Mouse;

/**
 * Scrolling text interlude screens for the story.
 */
public class InterludeScreen extends ContainerComponent {
	private static final PImage PROF_IMAGE = app.loadImage("prof.png");
	private static boolean intro = true;
	
	private static final String[] interludeText = {
			"Henry… Henry, are you awake?\n"
    		+ "The experiment with the alien DNA\n"
    		+ "seems to have worked!\n"
    		+ "Though uhh... you don’t quite look like yourself.\n\n"
    		
			+ "Now, now. There’s no need to panic.\n"
			+ "I’m sure we’ll be able to get you back to normal\n"
			+ "when this is all over, but for now we need your help!\n\n"
			
			+ "You’ve been out for several months now.\n"
			+ "The aliens have taken over, their toxic slime is everywhere,\n"
			+ "and the world is nearly in ruins!\n\n"
			
			+ "You must find and infiltrate the alien mothership.\n"
			+ "It’s the only way we can get them to leave.\n"
			+ "They won’t suspect a thing with your new disguises.",
			
			
			"You see those little yellow shards?\n"
			+ "Those are energy cells.\n\n"
			
			+ "The energy I'll require to make\n"
			+ "you human again is enormous,\n"
			+ "but with enough of those, it just may be possible.\n\n"
			
			+ "Incredible little things aren’t they?\n"
			+ "Oh, but don’t just stand there looking at them...\n"
			+ "Keep moving! You’re on a mission!\n",
			
			
			"The aliens have been through here too.\n"
			+ "Is there any place their repulsive\n"
			+ "slime hasn't touched?\n\n"
			
			+ "At least there are more energy cells\n"
			+ "lying around.\n\n"
			
			+ "There may be hope for you yet.",
			
			
			"What a shame.\n"
			+ "This city used to be my home.\n\n"
					
			+ "My family...\n\n"
			
			+ "My friends...\n\n"
			
			+ "Where are they now?",
			
			
			"The mothership is rumoured to be located\n"
			+ "in this desert up ahead.\n\n"
			
			+ "No, I can't follow you there.\n"
			+ "It's too dangerous for me.\n\n"
			
			+ "I have faith in you Henry,\n"
			+ "That's why I chose you for this mission.\n\n"
			
			+ "Go now... and free us from the wrath\n"
			+ "of those horrible beasts!"};
	
	TextComponent scrollingText;
	
	//title screen buttons
	TitleScreenButton continueBtn;
	
	public InterludeScreen(int levelNumber, int textNumber){
		Mouse.clearListeners();
		app.playMusic(new File("castle_theme.wav"));
		
	    continueBtn = new TitleScreenButton(3000,1800,700,240, "Continue"){
	    	public void released(){
	    		skip(levelNumber);
	    	}
	    };
	    addChild(continueBtn);
	    Mouse.addListener(continueBtn);
	    
	    if(intro){
	    	intro = false;
	    	textNumber--;
	    }
	    scrollingText = new TextComponent(interludeText[textNumber+1], null, Fonts.getFont2(), 120, app.color(255), app.CENTER, app.TOP);
	    
	    scrollingText.setTranslation(new PVector(1500, app.UNSCALED_HEIGHT));
	    scrollingText.setHorizontalAlignment(app.CENTER);
	    addChild(scrollingText);
	    
	    addChild(new ImageComponent(PROF_IMAGE, 3000, 100, 700, 1600));
	}
	
	public void update(){
		if(scrollingText.getTranslation().y > 50){
			scrollingText.translate(new PVector(0,-1.5f));
		}
	    
	    updateChildren();
	}
	
	public void drawTransformed(){
	    app.background(0);
	    drawChildren();
	}
	
	//move on to the level
	public void skip(int levelNumber){
		if(levelNumber == 0) app.setStage(new TitleScreen());
		else app.setStage(new GameScreen(levelNumber));
	}
}
