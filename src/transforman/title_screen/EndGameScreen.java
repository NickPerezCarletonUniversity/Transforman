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
import transforman.input.Mouse;

public class EndGameScreen extends ContainerComponent {
	private static final PImage PROF_IMAGE = app.loadImage("prof.png");

    //MenuCharacter type flags
	public static final int ORBITING = 0, FALLING = 1, CENTRAL = 2;
	
	//drawn shapes
	ContainerComponent backgroundShapes;
	TitleScreenCharacter centralShape;
	
	//title text
	TextComponent title, titleShadow;
	
	//title screen buttons
	TitleScreenButton continueButton;
	
	//orbit and background variables
	int t;
	int backgroundTimer;
	PVector orbitCenter;
	
	public EndGameScreen(){
		app.playMusic(new File("victory.wav"));
		Mouse.clearListeners();
		
		t = 0;
	    backgroundTimer = 0;
	    orbitCenter = new PVector(1920, 1550);
	    
	    //main text
	    
	    title = new TextComponent("Congratulations!\nYou've restored order to the world!\nNow try to go back and\ncollect all of the energy cells you missed!", null, Fonts.getFont1(), 125, app.color(255), app.CENTER, app.TOP);
	    title.setTranslation(new PVector(1940, 120));
	    titleShadow = new TextComponent("Congratulations!\nYou've restored order to the world!\nNow try to go back and\ncollect all of the energy cells you missed!", null, Fonts.getFont1(), 125, app.color(100), app.CENTER, app.TOP);
	    titleShadow.setTranslation(new PVector(1920, 108));
	    
	    backgroundShapes = new ContainerComponent();
	    
	    //create TransforMan shape
	    centralShape = new TitleScreenCharacter(orbitCenter.x, orbitCenter.y, 150, 0, CENTRAL);
	    
	    continueButton = new TitleScreenButton(3000,1800,700,240, "MAIN MENU"){
	    	public void released(){
	    		app.setStage(new TitleScreen());
	    	}
	    };
	    Mouse.addListener(continueButton);
	    
	    
	    addChild(backgroundShapes);
	    addChild(centralShape);
	    addChild(continueButton);
	    addChild(titleShadow);
	    addChild(title);
	    addChild(new ImageComponent(PROF_IMAGE, 1200, 800, 560, 1280));
	}
	
	public void update(){
	    //update timers
	    t++;
	    backgroundTimer++;
	    
	    //spawn background shape
	    if(backgroundTimer >= 5){
	    	backgroundTimer -= 5;
	    	
	    	backgroundShapes.addChild( new TitleScreenCharacter(app.random(3840), 0.0f, 48, (int)app.random(4), FALLING) );
	    }
	    
	    //update background shapes
	    for (int i=0; i<backgroundShapes.getChildCount(); i++){
	    	TitleScreenCharacter bgShape = (TitleScreenCharacter) backgroundShapes.getChild(i);
	    	
	    	bgShape.translate(new PVector(0, 6));
	    	
	    	//remove them if they exit the screen
	    	if(bgShape.getTranslation().y > 2160){
	    		backgroundShapes.removeChild(i);
	    		i--;
	    	}
	    }
	    
	    //update shape of central character
	    centralShape.setCharacter( (t/120)%TitleScreenCharacter.NUM_CHARACTERS );
	    titleShadow.setFill(centralShape.getFill());
	    
	    updateChildren();
	}
	
	public void drawTransformed(){
	    app.background(0);
	    drawChildren();
	}
}
