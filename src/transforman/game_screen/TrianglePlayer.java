/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.game_screen;

import java.awt.Polygon;

import processing.core.PVector;
import transforman.input.Keyboard;

public class TrianglePlayer extends PlayerComponent{
	private static final Polygon NORMAL_SHAPE = new Polygon( new int[]{0, GameScreen.BLOCK_SIZE, 2*GameScreen.BLOCK_SIZE}, 
			new int[]{2*GameScreen.BLOCK_SIZE, 0, 2*GameScreen.BLOCK_SIZE}, 3 );
	
	private static final Polygon INVERTED_SHAPE = new Polygon( new int[]{0, GameScreen.BLOCK_SIZE, 2*GameScreen.BLOCK_SIZE}, 
			new int[]{0, 2*GameScreen.BLOCK_SIZE, 0}, 3 );
	
	private boolean topTouching = false;
	private boolean gravityReversed = false;

	public TrianglePlayer(GameScreen gameScreen) {
		super(gameScreen);
		
		setShape(NORMAL_SHAPE);
		eyesComponent.setTranslation(new PVector(10, 30));
		
		setFill(app.color(255, 100, 255));
		setStroke(0);
		setStrokeWeight(2);
		
		friction = 0.09f;
	}
	
	public TrianglePlayer(PlayerComponent oldPlayer){
		this(oldPlayer.gameScreen);
		copyAttributes(oldPlayer);
	}
	
	public void update(){
		super.update();
	}
	
	public void jump(){
		if(gravityReversed && topTouching){
			velocity.y = 0;
			velocity.y += JUMP_SPEED;
			topTouching = false;
		}
		if(!gravityReversed && grounded){
			velocity.y = 0;
			velocity.y -= JUMP_SPEED;
			grounded = false;
		}
	}
	
	public void updateVelocity(){
		if(Keyboard.isDown(app.LEFT)){//left arrow key
			if(grounded && !gravityReversed || topTouching && gravityReversed) velocity.x -= WALK_SPEED;
			else velocity.x -= DIRECTIONAL_INFLUENCE;
		}
		
		if(Keyboard.isDown(app.RIGHT)){//right arrow key
			if(grounded && !gravityReversed || topTouching && gravityReversed) velocity.x += WALK_SPEED;
			else velocity.x += DIRECTIONAL_INFLUENCE;
		}
		
		if(Keyboard.isDown(' ')){//spacebar
			jump();
		}
		grounded = false;
		topTouching = false;
		
		if(gravityReversed) velocity.y -= 4*GameScreen.GRAVITY;
		else velocity.y += 4*GameScreen.GRAVITY;
	}
	
	public void interactWith(int c, int r, boolean inside){
		PVector pos = getTranslation();
		int playerC = (int)(pos.x / GameScreen.BLOCK_SIZE);
		int playerR = (int)(pos.y / GameScreen.BLOCK_SIZE);
		
		//reverse gravity if within 1 block from the ceiling/floor
		if(inside){
			if(r-1 == playerR - 1 && (c == playerC || c == playerC + 1 || pos.x % GameScreen.BLOCK_SIZE > 0 && c == playerC + 2)){
				if(gameScreen.getBlocks()[c][r-1] != null && gameScreen.getBlocks()[c][r-1].isPhysical()){
					gravityReversed = true;
					setShape(INVERTED_SHAPE);
					eyesComponent.setTranslation(new PVector(10, 7));
				}
			}
			
			if(r+1 == playerR + 2 && (c == playerC || c == playerC + 1 || pos.x % GameScreen.BLOCK_SIZE > 0 && c == playerC + 2)){
				if(gameScreen.getBlocks()[c][r+1] != null && gameScreen.getBlocks()[c][r+1].isPhysical()){
					gravityReversed = false;
					setShape(NORMAL_SHAPE);
					eyesComponent.setTranslation(new PVector(10, 30));
				}
			}
		}
		
		Block currentBlock = gameScreen.getBlocks()[c][r];
		if(currentBlock == null) return;
		
		//check if top edge is touching
		if(!inside && r == playerR - 1 && (c == playerC || c == playerC + 1 || pos.x % GameScreen.BLOCK_SIZE > 0 && c == playerC + 2)){
			if(currentBlock.isPhysical()){
				topTouching = true;
			}
		}
		
		super.interactWith(c, r, inside);
	}
	
	
	public void drawTransformed(){
		super.drawTransformed();
	}
}