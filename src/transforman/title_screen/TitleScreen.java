/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import java.io.File;

import processing.core.PVector;
import transforman.component.ContainerComponent;
import transforman.component.TextComponent;
import transforman.fonts.Fonts;
import transforman.input.Mouse;

public class TitleScreen extends ContainerComponent {
	//MenuCharacter type flags
	public static final int ORBITING = 0, FALLING = 1, CENTRAL = 2;
	
	
	//drawn shapes
	ContainerComponent backgroundShapes;
	ContainerComponent orbitingShapes;
	TitleScreenCharacter centralShape;
	
	//title text
	TextComponent title, titleShadow;
	
	//title screen buttons
	TitleScreenButton playButton, optionsButton, creditsButton;
	
	//orbit and background variables
	int t;
	int backgroundTimer;
	PVector orbitCenter, orbitSize;
	
	public TitleScreen(){
		app.playMusic(new File("castle_theme.wav"));
		Mouse.clearListeners();
		
		t = 0;
	    backgroundTimer = 0;
	    orbitCenter = new PVector(1920, 970);
	    orbitSize = new PVector(1100, 380);
	    
	    title = new TextComponent("TransforMan", null, Fonts.getFont1(), 360, app.color(255), app.CENTER, app.TOP);
	    title.setTranslation(new PVector(1940, 120));
	    titleShadow = new TextComponent("TransforMan", null, Fonts.getFont1(), 360, app.color(100), app.CENTER, app.TOP);
	    titleShadow.setTranslation(new PVector(1920, 108));
	    
	    backgroundShapes = new ContainerComponent();
	    
	    //create orbiting shapes
	    orbitingShapes = new ContainerComponent();
	    for(int i=0; i<TitleScreenCharacter.NUM_CHARACTERS; i++){
	    	orbitingShapes.addChild( new TitleScreenCharacter(0, 0, 100, i, ORBITING) );
	    }
	    
	    //create center shape
	    centralShape = new TitleScreenCharacter(orbitCenter.x, orbitCenter.y, 150, 0, CENTRAL);
	    
	    playButton = new TitleScreenButton(180, 1800, 800, 240, "PLAY"){
	    	public void released(){
	    		app.setStage(new LevelSelectScreen(0));
	    	}
	    };
	    Mouse.addListener(playButton);
	    
	    
	    optionsButton = new TitleScreenButton(1520, 1800, 800, 240, "OPTIONS"){
	    	public void released(){
	    		app.setStage(new OptionsScreen());
	    	}
	    };
	    Mouse.addListener(optionsButton);
	    
	    creditsButton = new TitleScreenButton(2800,1800,800,240, "CREDITS"){
	    	public void released(){
	    		app.setStage(new CreditsScreen());
	    	}
	    };
	    Mouse.addListener(creditsButton);
	    
	    addChild(backgroundShapes);
	    addChild(orbitingShapes);
	    addChild(centralShape);
	    addChild(playButton);
	    addChild(optionsButton);
	    addChild(creditsButton);
	    addChild(titleShadow);
	    addChild(title);

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
	    
	    //update orbiting shapes
	    for(int i=0; i<TitleScreenCharacter.NUM_CHARACTERS; i++){
	    	TitleScreenCharacter orbiting = (TitleScreenCharacter) orbitingShapes.getChild(i);
	    	PVector orbitDistance = orbitSize.copy().add( orbitSize.copy().mult(0.3f*app.sin(app.TWO_PI*t/120.0f)) );
	    	float x = orbitCenter.x + orbitDistance.x*app.cos(app.TWO_PI*t/240.0f + app.TWO_PI/TitleScreenCharacter.NUM_CHARACTERS*i);
	    	float y = orbitCenter.y + orbitDistance.y*app.sin(app.TWO_PI*t/240.0f + app.TWO_PI/TitleScreenCharacter.NUM_CHARACTERS*i);
	    	orbiting.setTranslation(new PVector(x, y));
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
