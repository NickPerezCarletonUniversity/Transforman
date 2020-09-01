/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.core;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import processing.core.PApplet;
import processing.core.PSurface;
import processing.event.MouseEvent;
import transforman.component.GameComponent;
import transforman.component.StageManager;
import transforman.fonts.Fonts;
import transforman.input.Keyboard;
import transforman.input.Mouse;
import transforman.title_screen.InterludeScreen;
import transforman.title_screen.OptionsScreen;

/**
 * The main class for the game.
 * Extends PApplet for Processing functions.
 */
public class TransforManApplet extends PApplet {
	//a single static instance for easy reference
	private static TransforManApplet instance;
	
	//the display will be scaled down from a 4K coordinate system
	//these numbers are essentially arbitrary and will not slow down rendering
	public static final int UNSCALED_WIDTH = 3840, UNSCALED_HEIGHT = 2160;
	
	//for detecting window resizing
	private int prevWidth, prevHeight;
	
	private StageManager stageManager;
	
	//music control
	private Clip backgroundClip;
	private float currentVolume = -30.0f;
	private String currentSound = "";
	private FloatControl gainControl;
	long clipTime;
	
	//main: run as a Processing sketch
	public static void main(String[] args) {
		TransforManApplet.instance = new TransforManApplet();
		
	    String[] a = {"MAIN"};
	    PApplet.runSketch(a, TransforManApplet.instance);
	}
	
	public static TransforManApplet getInstance(){
		return instance;
	}
	
	//use P2D for fast rendering
	public void settings(){
		size(displayWidth/2, displayHeight/2, P2D);
	}
	
	public PSurface getSurface(){
		return this.surface;
	}
	
	public void setup(){
		Fonts.loadFonts();
		surface.setResizable(true);
		smooth();
		
		stageManager = new StageManager(width, height);
		//initial stage: intro screen
		stageManager.setStage(new InterludeScreen(0, 0));
		
		prevWidth = width;
		prevHeight = height;
	}
	
	//update game and draw display
	public void draw(){
		//detect and adapt to window size change
		if(width != prevWidth || height != prevHeight){
			stageManager.setWindowSize(width, height);
			prevWidth = width;
			prevHeight = height;
		}
		
		stageManager.update();
		stageManager.draw();
	}
	
	public void keyPressed(){
		Keyboard.addKey(keyCode);
	}
	
	public void keyReleased(){
		Keyboard.removeKey(keyCode);
	}
	
	public void mousePressed(MouseEvent e){
		Mouse.mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e){
		Mouse.mouseReleased(e);
	}
	
	
	public GameComponent getStage(){
		return stageManager.getStage();
	}
	
	public void setStage(GameComponent stage){
		stageManager.setStage(stage);
	}
	
	
	//music playback functions
	
	public void playMusic(File soundFile){
		//http://freemusicarchive.org/genre/Chiptune/
		try{
			if(!currentSound.equals(soundFile.getName())){
				this.stopMusic();
				currentSound = soundFile.getName();
				
				backgroundClip = AudioSystem.getClip();
				backgroundClip.open(AudioSystem.getAudioInputStream(soundFile));
				gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(currentVolume); // Reduce volume by 20 decibels.
				OptionsScreen.setVolumeText(currentVolume);
				backgroundClip.start();
				backgroundClip.loop(backgroundClip.LOOP_CONTINUOUSLY);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void stopMusic(){
		if(backgroundClip != null){
			backgroundClip.stop();
		}
	}
	public void setVolume(float decibels){
		try{
			if((currentVolume >= -60 || decibels >= 0) && (currentVolume < 5 || decibels <= 0)){
				currentVolume += decibels;
				gainControl.setValue(currentVolume);
				OptionsScreen.setVolumeText(currentVolume);
			}
		}
		catch(Exception e){
			System.out.println("That volume is out of range!");
		}
		
	}
	
}
