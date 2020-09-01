/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.game_screen;

import java.io.File;

import processing.core.PVector;
import transforman.component.ContainerComponent;
import transforman.component.ImageComponent;
import transforman.component.TextComponent;
import transforman.fonts.Fonts;
import transforman.input.Keyboard;
import transforman.input.Mouse;
import transforman.title_screen.EndGameScreen;
import transforman.title_screen.InterludeScreen;
import transforman.title_screen.LevelSelectScreen;
import transforman.title_screen.TitleScreen;
import transforman.title_screen.TitleScreenButton;

public class GameScreen extends ContainerComponent {
	private int levelNumber;
	
	//side menu components
	public TitleScreenButton mainMenuButton, resetButton, nextLevelButton, previousLevelButton;
	private TextComponent levelNumberText, messageText;
	
	private boolean levelEnded = false;
	
	//properties taken from levelLoader
    private ImageComponent background;
    private Block[][] blockArray;
    private PlayerComponent player;
    private String message;
    private PVector startPosition;
    private int startingTransformation;
    private boolean[] transformPermissions;
    private boolean[] collectibles;
    private File music;
    private int unlockedNum;
	
	public static final float GRAVITY = (float) 1.5;
	public static final int BLOCK_SIZE = 35;
	public static final int COLUMNS = 88, ROWS = 64;
	
	public static final int SQUARE = 0, STICKY = 1, SPIKY = 2, SWITCHY = 3;
	
	//initialize level based on properties loaded in levelLoader
	
	public GameScreen(int lNumber){
		Mouse.clearListeners();
		
		levelNumber = lNumber;
		
		//loads and parses level from files
		LevelLoader levelLoader= new LevelLoader(""+levelNumber);
		
		//set background image
		if(levelLoader.getBackground() != null){
			background = new ImageComponent(levelLoader.getBackground(), 0, 0, COLUMNS*BLOCK_SIZE, ROWS*BLOCK_SIZE);
			addChild(background);
		}
		
		//set up 2D array of level (all blocks)
		
		blockArray = levelLoader.getBlocks();
		
		for(int c = 0; c < COLUMNS; c++){
			for(int r = 0; r < ROWS; r++){
				if(blockArray[c][r] != null) addChild(blockArray[c][r]);
			}
		}
		
		message = levelLoader.getMessage();
		startPosition = levelLoader.getStart();
		startingTransformation = levelLoader.getStartingTransformation();
		transformPermissions = levelLoader.getTransformPermissions();
		collectibles = levelLoader.getCollectibles();
		unlockedNum = levelLoader.getUnlockedNum();
		
		//set player in starting position
		resetPlayer();
		
		//start music
		music = levelLoader.getMusic();
		app.playMusic(music);
		
		//set up side menu
	    createMenuItems();
	}
	
	//set to starting position and transformation
	private void resetPlayer(){
		PlayerComponent p;
		switch(startingTransformation){
		default:
		case SQUARE:
			p = new SquarePlayer(this);
			break;
			
		case STICKY:
			p = new CirclePlayer(this);
			break;
			
		case SPIKY:
			p = new StarPlayer(this);
			break;
			
		case SWITCHY:
			p = new TrianglePlayer(this);
			break;
		}
		setPlayer(p);
	}
	
	//reset player and all blocks
	public void reset(){
		resetPlayer();
		for(int c = 0; c < COLUMNS; c++){
			for(int r = 0; r < ROWS; r++){
				if(blockArray[c][r] != null) blockArray[c][r].unhide();
			}
		}
	}
	
	//set up GUI for side menu
	private void createMenuItems(){
		//main menu button
		
		mainMenuButton = new TitleScreenButton(3100,1900,700,240, "MAIN MENU"){
	    	public void released(){
	    		
	    		app.setStage(new TitleScreen());
	    	}
	    };
	    addChild(mainMenuButton);
	    Mouse.addListener(mainMenuButton);
	    
	    //reset level button
	    
	    resetButton = new TitleScreenButton(3100,1640,700,240, "RESET"){
	    	public void released(){
	    		reset();
	    	}
	    };
	    addChild(resetButton);
	    Mouse.addListener(resetButton);
	    
	    //next level button
	    
	    nextLevelButton = new TitleScreenButton(3480,1380,320,200, "Next", 20){
	    	public void released(){
	    		nextLevel();
	    	}
	    };
	    addChild(nextLevelButton);
	    Mouse.addListener(nextLevelButton);
	    
	    //previous level button
	    
	    previousLevelButton = new TitleScreenButton(3100,1380,320,200, "Previous", 20){
	    	public void released(){
	    		previousLevel();
	    	}
	    };
	    addChild(previousLevelButton);
	    Mouse.addListener(previousLevelButton);
	    
	    //level number and level message
	    
	    levelNumberText = new TextComponent(Integer.toString(levelNumber), null, Fonts.getFont1(), 360, app.color(255), app.CENTER, app.TOP);
	    levelNumberText.setTranslation(new PVector(3450, 108));
	    addChild(levelNumberText);
	    
	    messageText = new TextComponent(message, new PVector(700, 1000), Fonts.getFont1(), 50, app.color(255), app.CENTER, app.TOP);
	    messageText.setTranslation(new PVector(3100, 500));
	    addChild(messageText);
	    
	}
	
	//set to a new transformation
	public void setPlayer(PlayerComponent p){
		if(player != null) removeChild(player);
		addChild(p);
		player = p;
	}
	
	public void update(){
		checkShapeShift();
		super.update();
	}
	
	//save progress and move on to the next level
	public void endLevel(){
		if(levelEnded) return;
		levelEnded = true;
		
		//unlock next level
		if(levelNumber == unlockedNum) unlockedNum++;
		
		//save
		LevelLoader.writeCollectibles(collectibles, levelNumber+"", unlockedNum);
		
		nextLevel();
	}
	
	//set up interlude sequence if applicable, then move on to the next level
	public void nextLevel(){
		//intro sequence
		if(levelNumber+1 == 2)       app.setStage(new InterludeScreen(levelNumber+1, 0));
		else if(levelNumber+1 == 6)  app.setStage(new InterludeScreen(levelNumber+1, 1));
		else if(levelNumber+1 == 10) app.setStage(new InterludeScreen(levelNumber+1, 2));
		else if(levelNumber+1 == 14) app.setStage(new InterludeScreen(levelNumber+1, 3));
		
		else if(levelNumber+1 <= LevelSelectScreen.LEVEL_IMAGES.length && levelNumber+1 <= unlockedNum){
			//next level
			app.setStage(new GameScreen(levelNumber+1));
		} else if(levelNumber +1 > LevelSelectScreen.LEVEL_IMAGES.length){
			//beat the entire game
			app.setStage(new EndGameScreen());
		}
		else{
			//next level not unlocked
			app.setStage(new LevelSelectScreen(levelNumber/LevelSelectScreen.LEVELS_PER_PAGE));
		}
	}
	
	public void previousLevel(){
		if(levelNumber-1 > 0){
			app.setStage(new GameScreen(levelNumber-1));
		} else {
			app.setStage(new LevelSelectScreen(levelNumber/LevelSelectScreen.LEVELS_PER_PAGE));
		}
	}
	
	//shapeshift if the transformation is available and the user presses the
	//associated button on the keyboard
	public void checkShapeShift(){
		if(Keyboard.isDown('Q')){
			if(!(player instanceof TrianglePlayer) && transformPermissions[SWITCHY]){
				setPlayer(new TrianglePlayer(player));
			}
		}
		if(Keyboard.isDown('W')){
			if(!(player instanceof StarPlayer) && transformPermissions[SPIKY]){
				setPlayer(new StarPlayer(player));
			}
		}
		if(Keyboard.isDown('E')){
			if(!(player instanceof CirclePlayer) && transformPermissions[STICKY]){
				setPlayer(new CirclePlayer(player));
			}
		}
		if(Keyboard.isDown('R')){
			if(!(player instanceof SquarePlayer) && transformPermissions[SQUARE]){
				setPlayer(new SquarePlayer(player));
			}
		}
	}
	
	public Block[][] getBlocks(){
		return blockArray;
	}
	
	public PlayerComponent getPlayer(){
		return player;
	}
	
	public PVector getStart(){
		return startPosition;
	}
	
	//collects a collectable
	public void collect(Block b){
		if(b.getMaterial() != Block.COLLECTIBLE) return;
		
		b.hide();
		collectibles[b.getCollectibleID()] = true;
	}
	
	public void draw(){
		app.background(0);
	    drawChildren();
	}
}