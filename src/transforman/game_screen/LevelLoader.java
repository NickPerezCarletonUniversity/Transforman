/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.game_screen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


import processing.core.PImage;
import processing.core.PVector;
import transforman.core.TransforManApplet;
import transforman.title_screen.InterludeScreen;

public class LevelLoader {
	public static TransforManApplet app = TransforManApplet.getInstance();
	
	public static final String[] MATERIAL_NAMES = {"wood", "stone", "bouncy", "terminal", "collectible", "barrier", "poison"};
	public static final int[] PARSE_COLORS = {/*Wood*/app.color(101,67,33), /*Stone*/app.color(255,0,0), /*Bouncy*/app.color(0,0,255), /*Terminal*/app.color(86,68,142), /*Collectible*/app.color(255,255,0), /*Barrier*/app.color(0,0,0), /*Poison*/app.color(48,167,20)};
	private int[] displayColors = {/*Wood*/app.color(101,67,33), /*Stone*/app.color(90, 110, 110), /*Bouncy*/app.color(20, 90, 230), /*Terminal*/app.color(20,230,230), /*Collectible*/app.color(235,235,0), /*Barrier*/app.color(0,0,0), /*Poison*/app.color(48,167,20)};
	
	private String levelName;
	private PVector startLocation;
	private PImage levelImage; // Level image files should be of size 88*64
	private PImage backgroundImage;
	private String message = "";
	private File music;
	private boolean[] transformPermissions = {/*Square*/true, /*Sticky*/true, /*Spiky*/true, /*Switchy*/true};
	private int startingTransformation = GameScreen.SQUARE;
	private int unlockedNum;
	
	private boolean[] collectibles;
	String musicName = "";
	
	public LevelLoader(String levelName){
		this.levelName = levelName;
		
		levelImage = app.loadImage(levelName+".gif");
		
		parseTextFile(levelName+".txt");
		parseTextFile("profile.txt");
		music = new File(musicName);
	}
	
	public static void writeCollectibles(boolean[] c, String lvl, int newUnlockedNum){
		StringBuilder strBuild = new StringBuilder();
		try{
			BufferedReader reader = app.createReader("profile.txt");
			if(reader != null){
				while(true){
					String line = reader.readLine();
					
					if(line == null) break;
					else {
						String[] split = line.split(":");
						if(split[0].equals("unlocked")){
							line = split[0]+":"+newUnlockedNum;
						}
						if(split[0].equals("level "+lvl)){
							line = split[0]+":";
							for(int i=0; i<c.length; i++){
								line += c[i] ? 'x' : '_';
								if(i < c.length-1) line += ',';
							}
						}
					}
					
					strBuild.append(line + '\n');
				}
			}
			reader.close();
			
			
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
	
	public void parseTextFile(String fileName){
		try{
			BufferedReader reader = app.createReader(fileName);
			if(reader != null){
				while(true){
					String line = reader.readLine();
					if(line == null) break;
					else parseLine(line);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void parseLine(String line){System.out.println(line+" "+line.indexOf(':'));
		if(!line.contains(":")) return;
		
		String tag = line.substring(0, line.indexOf(':'));
		line = line.substring(line.indexOf(':')+1);
		
		switch(tag){
		case "message":
			message = line;
			break;
			
		case "world":
			String worldNumber = line;
			parseTextFile("world" + worldNumber + ".txt");
			break;
			
		case "background":
			backgroundImage = app.loadImage(line);
			break;
		
		case "music":
			musicName = line;
			break;
			
		case "no":
			String[] no = line.split(",");
			for(int i=0; i<no.length; i++){
				switch(no[i]){
				case "square":
					transformPermissions[GameScreen.SQUARE] = false;
					break;
				case "sticky":
					transformPermissions[GameScreen.STICKY] = false;
					break;
				case "spiky":
					transformPermissions[GameScreen.SPIKY] = false;
					break;
				case "switchy":
					transformPermissions[GameScreen.SWITCHY] = false;
					break;
				}
			}
			break;
			
		case "start":
			switch(line){
			case "square":
				startingTransformation = GameScreen.SQUARE;
				break;
			case "sticky":
				startingTransformation = GameScreen.STICKY;
				break;
			case "spiky":
				startingTransformation = GameScreen.SPIKY;
				break;
			case "switchy":
				startingTransformation = GameScreen.SWITCHY;
				break;
			}
			break;
		}
		
		for(int mat=0; mat<MATERIAL_NAMES.length; mat++){
			if(tag.equals(MATERIAL_NAMES[mat])){
				String[] rgbStrings = line.split(",");
				int[] rgb = new int[3];
				for(int i=0; i<3; i++) rgb[i] = Integer.parseInt(rgbStrings[i]);
				int color = app.color(rgb[0], rgb[1], rgb[2]);
				displayColors[mat] = color;
			}
		}
		
		//in profile.txt
		if(tag.equals("level "+levelName)){
			String[] collectibleStrings = line.split(",");
			collectibles = new boolean[collectibleStrings.length];
			for(int i=0; i<collectibleStrings.length; i++){
				collectibles[i] = collectibleStrings[i].charAt(0) == 'x';
			}
		}
		else if(tag.equals("unlocked")){
			unlockedNum = Integer.parseInt(line);
		}
	}
	
	public Block[][] getBlocks(){
		Block[][] array = new Block[88][64];
		int collectibleCount = 0;
		
		for(int c = 0; c < 88; c++){
			for(int r = 0; r < 64; r++){
				int pixelColor = levelImage.get(c, r);
				for(int mat=0; mat<PARSE_COLORS.length; mat++){
					if(pixelColor == PARSE_COLORS[mat]){
						array[c][r] = new Block(mat, displayColors[mat]);
						array[c][r].setTranslation( new PVector(c*GameScreen.BLOCK_SIZE, r*GameScreen.BLOCK_SIZE) );
						
						if(mat == Block.COLLECTIBLE){
							array[c][r].setCollectibleID(collectibleCount);
							array[c][r].setCollected(collectibles[collectibleCount]);
							collectibleCount++;
						}
					}
				}
				
				if(startLocation == null && pixelColor == app.color(255,165,0)){
					startLocation = new PVector(c*GameScreen.BLOCK_SIZE, r*GameScreen.BLOCK_SIZE);
				}
			}
		}
		
		return array;
	}
	
	
	
	public PImage getBackground(){
		return backgroundImage;
	}
	
	public PVector getStart(){
		return startLocation;
	}
	
	public String getMessage(){
		return message;
	}
	
	public int getStartingTransformation(){
		return startingTransformation;
	}
	
	public boolean[] getTransformPermissions(){
		return transformPermissions;
	}
	
	public boolean[] getCollectibles(){
		return collectibles;
	}
	
	public File getMusic(){
		return music;
	}
	
	public int getUnlockedNum(){
		return unlockedNum;
	}
	
}
