/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.game_screen;

import java.awt.Polygon;

public class StarPlayer extends PlayerComponent {
	public StarPlayer(GameScreen gameScreen) {
		super(gameScreen);
		
		int starPoints = 4;
		int[] xArray = new int[starPoints*2];
		int[] yArray = new int[starPoints*2];
		for(int i=0; i<starPoints*2; i++){
			float dist = GameScreen.BLOCK_SIZE*app.sqrt(2);
			if(i%2 == 0) dist *= 0.4f;
			
			xArray[i] = Math.round(GameScreen.BLOCK_SIZE + dist*app.cos(app.TWO_PI * i / (starPoints*2)));
			yArray[i] = Math.round(GameScreen.BLOCK_SIZE + dist*app.sin(app.TWO_PI * i / (starPoints*2)));
		}
		setShape(new Polygon(xArray, yArray, starPoints*2));
		
		setFill(app.color(100, 100, 255));
		setStroke(0);
		setStrokeWeight(2);
	}
	
	public StarPlayer(PlayerComponent oldPlayer){
		this(oldPlayer.gameScreen);
		copyAttributes(oldPlayer);
	}
	
	public void update(){
		super.update();
	}
	
	public void updateVelocity(){
		super.updateVelocity();
	}
	
	public void jump(){
		//no jump
	}
	
	public void interactWith(int c, int r, boolean inside){
		if(this != gameScreen.getPlayer()) return;
		super.interactWith(c, r, inside);
		
		Block currentBlock = gameScreen.getBlocks()[c][r];
		if(currentBlock == null) return;
		
		if(currentBlock.getMaterial() == Block.WOOD){
			gameScreen.getBlocks()[c][r].hide();
		}
	}
}
