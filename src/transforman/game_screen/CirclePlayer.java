/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.game_screen;

import java.awt.geom.Ellipse2D;

import processing.core.PVector;

public class CirclePlayer extends PlayerComponent {
	private boolean leftTouching = false;
	private boolean rightTouching = false;

	public CirclePlayer(GameScreen gameScreen) {
		super(gameScreen);
		
		friction = 10f;
		
		setShape(new Ellipse2D.Float(0, 0, GameScreen.BLOCK_SIZE*2, GameScreen.BLOCK_SIZE*2));
		setFill(app.color(0, 190, 0));
		setStroke(0);
		setStrokeWeight(2);
	}
	
	public CirclePlayer(PlayerComponent oldPlayer){
		this(oldPlayer.gameScreen);
		copyAttributes(oldPlayer);
	}
	
	public void update(){
		super.update();
	}
	
	public void updateVelocity(){
		super.updateVelocity();
		leftTouching = false;
		rightTouching = false;
	}
	
	public void jump(){
		float angle = app.PI/4;
		
		if(leftTouching){
			velocity.y = -JUMP_SPEED * app.sin(app.PI/2 - angle);
			velocity.x = JUMP_SPEED * app.cos(app.PI/2 - angle);
			leftTouching = false;
		}
		
		if(rightTouching){
			velocity.y = -JUMP_SPEED * app.sin(app.PI/2 + angle);
			velocity.x = JUMP_SPEED * app.cos(app.PI/2 + angle);
			rightTouching = false;
		}
	}
	
	public void interactWith(int c, int r, boolean inside){
		Block currentBlock = gameScreen.getBlocks()[c][r];
		if(currentBlock == null) return;
		
		PVector pos = getTranslation();
		int playerC = (int)(pos.x / GameScreen.BLOCK_SIZE);
		int playerR = (int)(pos.y / GameScreen.BLOCK_SIZE);
		
		//check if left edge is touching
		if(!inside && c == playerC - 1 && (r == playerR || r == playerR + 1 || pos.y % GameScreen.BLOCK_SIZE > 0 && r == playerR + 2)){
			if(currentBlock.isPhysical()){
				leftTouching = true;
			}
		}
		
		//check if right edge is touching
		if(c == playerC + 2 && (r == playerR || r == playerR + 1 || pos.y % GameScreen.BLOCK_SIZE > 0 && r == playerR + 2)){
			if(currentBlock.isPhysical()){
				rightTouching = true;
			}
		}
		
		super.interactWith(c, r, inside);
	}
}