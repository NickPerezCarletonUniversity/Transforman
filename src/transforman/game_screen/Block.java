/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.game_screen;

import processing.core.PVector;
import transforman.component.GameComponent;

public class Block extends GameComponent {
	public static final int WOOD = 0;
	public static final int STONE = 1;
	public static final int BOUNCY = 2;
	public static final int TERMINAL = 3;
	public static final int COLLECTIBLE = 4;
	public static final int BARRIER = 5;
	public static final int POISON = 6;
	
	
	private int material;
	private int baseColor, color;
	private int frameCounter = 0;
	private boolean hidden = false;
	
	//for collectibles
	private int collectibleID = -1;
	private boolean collected = false;
	
	
	public Block(int material, int c){
		this.material = material;
		baseColor = c;
		color = varyColor(baseColor);
	}
	
	private static int varyColor(int c, int amount){
		c += app.color(app.random(-amount, amount));
		
		if(app.red(c) < 0) app.color(0, app.green(c), app.blue(c));
		if(app.green(c) < 0) app.color(app.red(c), 0, app.blue(c));
		if(app.blue(c) < 0) app.color(app.red(c), app.green(c), 0);
		
		if(app.red(c) > 255) app.color(255, app.green(c), app.blue(c));
		if(app.green(c) > 255) app.color(app.red(c), 255, app.blue(c));
		if(app.blue(c) > 255) app.color(app.red(c), app.green(c), 255);////doesn't actually help
		
		return c;
	}
	
	private static int varyColor(int c){
		return varyColor(c, 20);
	}
	
	public void hide(){ hidden = true; }
	public void unhide(){ hidden = false; }
	public boolean isHidden(){ return hidden; }
	
	public int getMaterial(){ return material; }
	public void setMaterial(int material){ this.material = material; }
	
	public int getCollectibleID(){ return collectibleID; }
	public void setCollectibleID(int n){ collectibleID = n; }
	
	public boolean getCollected(){ return collected; }
	public void setCollected(boolean collected){ this.collected = collected; }
	
	public boolean isPhysical(){
		switch(material){
		case          WOOD: return !hidden;
		case         STONE: return true;
		case        BOUNCY: return true;
		
		case      TERMINAL: return false;
		case   COLLECTIBLE: return false;
		
		case       BARRIER: return true;
		
		case 		POISON: return false;
		
		default: return false;
		}
	}
	
	public void update(){
		frameCounter++;
	}
	
	public void drawTransformed(){
		float S = GameScreen.BLOCK_SIZE;
		float r2 = app.sqrt(2);
		
		if(hidden) return;
		
		if(frameCounter % 15 == 0){
			if(material == POISON) color = varyColor(baseColor);
		}
		
		if(material == COLLECTIBLE){
			rotate(app.PI/20, localToParent(new PVector(S/2 +1, S/2 +1)));
			
			app.fill(color);
			if(collected) app.fill(app.red(color), app.green(color), app.blue(color), 100);
			app.noStroke();
			app.rect(S/2 - S*r2/4 +2, S/2 - S*r2/4 +2, S*r2/2 -1, S*r2/2 -1);
		} else {
			app.fill(color);
			app.noStroke();
			app.rect(-2, -2, S+4, S+4);
		}
	}
}