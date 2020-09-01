/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import processing.core.PVector;
import transforman.component.ContainerComponent;
import transforman.component.TextComponent;
import transforman.fonts.Fonts;
import transforman.input.Mouse;

public class OptionsScreen extends ContainerComponent{
	//option buttons
	TitleScreenButton soundMinus, soundPlus, mainMenu, reset;
	
	TextComponent title, resetText, menuText, soundText, volumeText;
	
	private static int currentVolume;
	
	float volumeInterval = 5.0f;
	public OptionsScreen(){
		Mouse.clearListeners();
		
		title = new TextComponent("Options", null, Fonts.getFont1(), 360, app.color(255), app.CENTER, app.TOP);
	    title.setTranslation(new PVector(1940, 120));
	    addChild(title);
	    
	    //reset button for resetting progress
	    resetText = new TextComponent("Use this button to reset\nyour current profile.\nAll game progress will be lost.", null, Fonts.getFont1(),60,app.color(255),app.CENTER, app.TOP);
	    resetText.setTranslation(new PVector(1900, 500));
	    addChild(resetText);
	    
	    soundText = new TextComponent("Use these two buttons to adjust\nthe game volume.\n- to decrease, + to increase", null, Fonts.getFont1(), 60, app.color(255), app.CENTER, app.TOP);
	    soundText.setTranslation(new PVector(1900, 1100));
	    addChild(soundText);
	    
	    //volume control buttons/display
	    
	    soundPlus = new TitleScreenButton(2000, 1350, 100, 100, "+"){
	    	public void released(){
		    	app.setVolume(volumeInterval);
	    	}
	    };
	    addChild(soundPlus);
	    Mouse.addListener(soundPlus);
	    
	    volumeText = new TextComponent(getVolume(), null, Fonts.getFont1(), 60, app.color(255), app.CENTER, app.TOP);
	    volumeText.setTranslation(new PVector(1900,1375));
	    addChild(volumeText);
	    
	    soundMinus = new TitleScreenButton(1700,1350,100,100,"-"){
		    public void released(){
	    		app.setVolume(-volumeInterval);
	    	}
	    };
	    addChild(soundMinus);
	    Mouse.addListener(soundMinus);
	    
	    //main menu button
	    
	    mainMenu = new TitleScreenButton(1550,1700,700,240, "MAIN MENU"){
	    	public void released(){
	    		app.setStage(new TitleScreen());
	    	}
	    };
	    addChild(mainMenu);
	    Mouse.addListener(mainMenu);
	    
	    menuText = new TextComponent("Use this to return to the main menu.", null, Fonts.getFont1(), 60, app.color(255), app.CENTER, app.TOP);
	    menuText.setTranslation(new PVector(1900, 1600));
	    addChild(menuText);
	    
	    reset = new TitleScreenButton(1550,750,700,240,"RESET"){
	    	public void released(){
	    		resetProfile();
	    	}
	    };
	    addChild(reset);
	    Mouse.addListener(reset);
	    
	}
	
	//handles volume
	public void update(){
		super.update();
		removeChild(volumeText);
		volumeText = new TextComponent(getVolume(), null, Fonts.getFont1(), 60, app.color(255), app.CENTER, app.TOP);
	    volumeText.setTranslation(new PVector(1900,1375));
	    addChild(volumeText);
	}
	public void drawTransformed(){
		app.background(0);
		drawChildren();
	}
	
	public static void setVolumeText(float volume){
		System.out.println(volume);
		currentVolume = (int)(volume + 65.0f);
	}
	
	public String getVolume(){
		return Integer.toString(currentVolume);
	}
	
	//reset save file game progress
	public void resetProfile(){
		//read file
		
		StringBuilder strBuild = new StringBuilder();
		try{
			BufferedReader reader = app.createReader("profile.txt");
			if(reader != null){
				while(true){
					String line = reader.readLine();
					
					if(line == null) break;
					else {
						//set unlocked level to 1
						String[] split = line.split(":");
						if(split[0].equals("unlocked")){
							line = split[0]+":1";
						}
						//set all collectibles to 'not collected'
						if(split[0].startsWith("level ")){
							line = split[0]+":";
							int c = split[1].split(",").length;
							for(int i=0; i<c; i++){
								line += '_';
								if(i < c-1) line += ',';
							}
						}
					}
					
					strBuild.append(line + '\n');
				}
			}
			reader.close();
			
			//overwrite file
			
			String[] lines = strBuild.toString().split("\n");
			PrintWriter writer = app.createWriter("profile.txt");
			for(int i=0; i<lines.length; i++){
				writer.println(lines[i]);
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
