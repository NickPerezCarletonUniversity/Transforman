/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.title_screen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import processing.core.PImage;
import transforman.component.ContainerComponent;
import transforman.input.Mouse;

/**
 * Includes buttons for each of the unlocked levels,
 * as well as a 'next page', 'previous page', and 'main menu' button
 */
public class LevelSelectScreen extends ContainerComponent {
	public static final PImage[] LEVEL_IMAGES = loadLevelImages();
	public static final PImage LOCKED_IMAGE = app.loadImage("locked.gif");
	
	public static final int LEVELS_PER_PAGE = 10;
	
	
	private int unlockNum;
	private Integer[] collectibleCounts, collectibleTotals;
	
	public LevelSelectScreen(int page){
		Mouse.clearListeners();
		
		readProfile();
		app.playMusic(new File("castle_theme.wav"));
		
		//organize level select buttons in a grid
		
		int n = LEVELS_PER_PAGE*page;
		for(int r=0; r<2; r++){
			for(int c=0; c<5; c++){
				if(n<LEVEL_IMAGES.length){
					
					//check if unlocked
					
					if(n+1 <= unlockNum){
						LevelSelectButton btn = new LevelSelectButton(n+1, LEVEL_IMAGES[n], collectibleCounts[n], collectibleTotals[n], 50 + c*(88*8+50), 50 + r*(64*8+200));
						addChild(btn);
						Mouse.addListener(btn);
					} else {
						LevelSelectButton btn = new LevelSelectButton(0, LOCKED_IMAGE, -1, -1, 50 + c*(88*8+50), 50 + r*(64*8+200));
						addChild(btn);
					}
				}
				n++;
			}
		}
		
		//only display next and previous page buttons if there is a next/previous page
		
		if(page > 0){
			TitleScreenButton prev = new TitleScreenButton(180, 1900, 140, 140, "<"){
				public void released(){
					app.setStage(new LevelSelectScreen(page-1));
				}
			};
			addChild(prev);
			Mouse.addListener(prev);
		}
		
		if((page+1)*LEVELS_PER_PAGE < LEVEL_IMAGES.length){
			TitleScreenButton next = new TitleScreenButton(3520, 1900, 140, 140, ">"){
				public void released(){
					app.setStage(new LevelSelectScreen(page+1));
				}
			};
			addChild(next);
			Mouse.addListener(next);
		}
		
		//main menu button
		
		TitleScreenButton mainMenuButton = new TitleScreenButton(1520,1800,800,240, "MAIN MENU"){
	    	public void released(){
	    		app.setStage(new TitleScreen());
	    	}
	    };
	    Mouse.addListener(mainMenuButton);
	    addChild(mainMenuButton);
	}
	
	//load all level images
	private static PImage[] loadLevelImages(){
		ArrayList<PImage> list = new ArrayList<PImage>();
		
		int n=1;
		while(true){
			PImage img = app.loadImage(n+".gif");
			if(img == null) break;
			
			list.add(img);
			n++;
		}
		
		return list.toArray(new PImage[list.size()]);
	}
	
	//check profile for unlocks and collectibles
	private void readProfile(){
		ArrayList<Integer> counts = new ArrayList<Integer>();
		ArrayList<Integer> totals = new ArrayList<Integer>();
		
		try{
			BufferedReader reader = app.createReader("profile.txt");
			if(reader != null){
				//read each line
				while(true){
					String line = reader.readLine();
					if(line == null) break;
					else {
						//unlock progress
						String[] split = line.split(":");
						if(split[0].equals("unlocked")){
							unlockNum = Integer.parseInt(split[1]);
						}
						
						//collectibles
						else if(split[0].startsWith("level ")){
							String[] collectibleStrings = split[1].split(",");
							totals.add(collectibleStrings.length);
							
							int count = 0;
							for(int i=0; i<collectibleStrings.length; i++){
								if(collectibleStrings[i].equals("x")) count++;
							}
							counts.add(count);
						}
					}
				}
			}
			
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		collectibleCounts = counts.toArray(new Integer[counts.size()]);
		collectibleTotals = totals.toArray(new Integer[totals.size()]);
	}
	
	public void draw(){
		app.background(0);
	    drawChildren();
	}
}
