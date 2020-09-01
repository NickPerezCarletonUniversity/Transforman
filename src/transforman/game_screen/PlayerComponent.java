/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.game_screen;

import processing.core.PImage;
import processing.core.PVector;
import transforman.component.ImageComponent;
import transforman.component.ShapeComponent;
import transforman.input.Keyboard;

/**
 * All playable transformations extend this class.
 * Handles movement, collision, and block interactions.
 */
public class PlayerComponent extends ShapeComponent {
	protected static final PImage EYES = app.loadImage("eyes.png");
	protected ImageComponent eyesComponent;
	
	protected PVector velocity;
	
	protected static final float WALK_SPEED = 1.0f;
	protected static final float DIRECTIONAL_INFLUENCE = 0.6f;
	protected static final float JUMP_SPEED = 30.0f;
	protected float friction = 0.2f;
	
	protected boolean grounded = false;
	protected boolean reset = false;
	
	protected GameScreen gameScreen;
	
	//set from game screen
	public PlayerComponent(GameScreen gameScreen){
		this.gameScreen = gameScreen;
		setTranslation(gameScreen.getStart());
		velocity = new PVector();
		
		eyesComponent = new ImageComponent(EYES, 10, 7, 50, 15);
	}
	
	//set from previous transformation
	public PlayerComponent(PlayerComponent oldPlayer){
		this(oldPlayer.gameScreen);
		copyAttributes(oldPlayer);
	}
	
	//copy from previous transformation
	public void copyAttributes(PlayerComponent oldPlayer){
		setVelocity( oldPlayer.getVelocity() );
		setTranslation( oldPlayer.getTranslation() );
		setRotation( oldPlayer.getRotation() );
		setScale( oldPlayer.getScale() );
		grounded = oldPlayer.grounded;
	}
	
	public void update(){
		updateVelocity();
		
		//collide, then continue movement for the remainder of the frame
		float t = 1.0f;
		t = doCollisions(t);
		if(t > 0.0f && t <= 1.0f) t = doCollisions(t);
		
		//do block-specific interactions
		interactWithSurroundings();
	}
	
	
	public void drawTransformed(){
		super.drawTransformed();
		eyesComponent.draw();
	}
	
	public void updateVelocity(){
		if(Keyboard.isDown(app.LEFT)){//left arrow key
			if(grounded) velocity.x -= WALK_SPEED;
			else velocity.x -= DIRECTIONAL_INFLUENCE;
		}
		
		if(Keyboard.isDown(app.RIGHT)){//right arrow key
			if(grounded) velocity.x += WALK_SPEED;
			else velocity.x += DIRECTIONAL_INFLUENCE;
		}
		
		if(Keyboard.isDown(' ')){//spacebar
			jump();
		}
		grounded = false;
		
		velocity.y += GameScreen.GRAVITY;
	}
	
	//sets velocity, returns remaining t
	public float doCollisions(float t){
		final float S = GameScreen.BLOCK_SIZE;
		
		PVector v = velocity.copy().mult(t);
		
		if(v.x == 0 && v.y == 0) return 0.0f;
		
		float xInit = getTranslation().x;
		float yInit = getTranslation().y;
		
		float xFinal = xInit + v.x;
		float yFinal = yInit + v.y;
		
		int cInit = (int)(xInit / S);
		int rInit = (int)(yInit / S);
		
		int cFinal = (int)(xFinal / S);
		int rFinal = (int)(yFinal / S);
		
		
		PVector hEndPosition = null, vEndPosition = null;
		PVector hEndVelocity = v.copy(), vEndVelocity = v.copy();
		
		//check collisions with right edge
		if(v.x > 0){
			int c = cInit;
			if(xInit - cInit*S > 0) c++;
			
			for(; c <= cFinal; c++){
				float x = c*S;
				float y = yInit + (x-xInit) / v.x * v.y;
				
				int r = (int)(y/S);
				
				if(r < 0 || r >= GameScreen.ROWS) break;
				
				if(checkPhysical(c+2, r) || checkPhysical(c+2, r+1) || y - r*S > 0 && checkPhysical(c+2, r+2) || y - r*S == 0 && v.y < 0 && checkPhysical(c+2, r-1) || y - r*S >= 0 && v.y > 0 && checkPhysical(c+2, r+2)){
					hEndPosition = new PVector(x, y);
					float f = -Math.signum(v.y) * Math.min(Math.abs(v.y), Math.abs(v.x) * friction);
					hEndVelocity.y += f;
					hEndVelocity.x = 0;
					break;
				}
			}
		}
		//check collisions with left edge
		else if(v.x < 0){
			for(int c = cInit-1; c >= cFinal; c--){
				float x = (c+1)*S;
				float y = yInit + (x-xInit) / v.x * v.y;
				
				int r = (int)(y/S);
				
				if(r < 0 || r >= GameScreen.ROWS) break;
				
				if(checkPhysical(c, r) || checkPhysical(c, r+1) || y - r*S > 0 && checkPhysical(c, r+2) || y - r*S == 0 && v.y < 0 && checkPhysical(c, r-1) || y - r*S >= 0 && v.y > 0 && checkPhysical(c, r+2)){
					c++;
					x = c*S;
					y = yInit + (x-xInit) / v.x * v.y;
					r = (int)(y/S);
					
					hEndPosition = new PVector(x, y);
					float f = -Math.signum(v.y) * Math.min(Math.abs(v.y), Math.abs(v.x) * friction);
					hEndVelocity.y += f;
					hEndVelocity.x = 0;
					break;
				}
			}
		}
		
		//check collisions with bottom edge
		if(v.y > 0){
			if(yInit - rInit*S == 0){
				if(checkPhysical(cInit, rInit+2) || checkPhysical(cInit+1, rInit+2) || xInit - cInit*S > 0 && checkPhysical(cInit+2, rInit+2)){
		        	vEndPosition = new PVector(xInit, yInit);
		        	float f = -Math.signum(v.x) * Math.min(Math.abs(v.x), Math.abs(v.y) * friction);
		        	vEndVelocity.y = 0;
		        	vEndVelocity.x += f;
		        }
			}
			
			else for(int r = rInit+1; r <= rFinal; r++){
				float y = r*S;
				float x = xInit + (y-yInit) / v.y * v.x;
				
				int c = (int)(x/S);
				
				if(c < 0 || c >= GameScreen.COLUMNS) break;
				
				if(checkPhysical(c, r+2) || checkPhysical(c+1, r+2) || x - c*S > 0 && checkPhysical(c+2, r+2)){
					vEndPosition = new PVector(x, y);
					float f = -Math.signum(v.x) * Math.min(Math.abs(v.x), Math.abs(v.y) * friction);
					vEndVelocity.y = 0;
					vEndVelocity.x += f;
					break;
				}
			}
		}
		//check collisions with top edge
		else if(v.y < 0){
			for(int r = rInit-1; r >= rFinal; r--){
				float y = (r+1)*S;
				float x = xInit + (y-yInit) / v.y * v.x;
				
				int c = (int)(x/S);
				
				if(c < 0 || c >= GameScreen.COLUMNS) break;
				
				if(checkPhysical(c, r) || checkPhysical(c+1, r) || x - c*S > 0 && checkPhysical(c+2, r)){
					r++;
					y = r*S;
					x = xInit + (y-yInit) / v.y * v.x;
					c = (int)(x/S);
					
					vEndPosition = new PVector(x, y);
					float f = -Math.signum(v.x) * Math.min(Math.abs(v.x), Math.abs(v.y) * friction);
					vEndVelocity.y = 0;
					vEndVelocity.x += f;
					break;
				}
			}
		}
		
		//handle end result
		
		float hEndDist = 0;
		if(hEndPosition != null) hEndDist = PVector.sub(hEndPosition, getTranslation(), null).mag();
		
		float vEndDist = 0;
		if(vEndPosition != null) vEndDist = PVector.sub(vEndPosition, getTranslation(), null).mag();
		
		if(vEndPosition == null && hEndPosition == null){
			translate(v);
			return 0;
		}
		else if(hEndPosition != null && hEndDist < vEndDist || vEndPosition == null){
			//hit left or right edge first
			float timeRemaining = t - (1.0f/t) * (hEndPosition.x-xInit) / v.x;
			setTranslation(hEndPosition);
			velocity = hEndVelocity;
			return timeRemaining;
		}
		else if(vEndDist < hEndDist || hEndPosition == null){
			//hit top or bottom edge first
			float timeRemaining = t - (1.0f/t) * (vEndPosition.y-yInit) / v.y;
			setTranslation(vEndPosition);
			velocity = vEndVelocity;
			return timeRemaining;
		} else {
			//hit at same time
			float timeRemaining = t - (1.0f/t) * (vEndPosition.y-yInit) / v.y;
			setTranslation(vEndPosition);
			velocity = vEndVelocity;
			return timeRemaining;
		}
	}
	
	public void jump(){
		if(grounded){
			velocity.y = 0;
			velocity.y -= JUMP_SPEED;
			grounded = false;
		}
	}
	
	//should check for collisions
	public boolean checkPhysical(int col, int row){
		Block b = gameScreen.getBlocks()[col][row];
		return b!=null && b.isPhysical();
	}
	public int checkMaterial(int col, int row){
		Block b = gameScreen.getBlocks()[col][row];
		if(b==null) return -1;
		else return b.getMaterial();
	}
	
	//interact with all surrounding blocks
	public void interactWithSurroundings(){
		final float S = GameScreen.BLOCK_SIZE;
		
		PVector pos = getTranslation();
		int c = (int)(pos.x / S);
		int r = (int)(pos.y / S);
		
		//these four will always be inside the player
		interactWith(c, r, true);
		interactWith(c+1, r, true);
		interactWith(c, r+1, true);
		interactWith(c+1, r+1, true);
		
		//if aligned horizontally, the blocks to the left and right will be touching the edges
		if(pos.x % S == 0){
			interactWith(c-1, r, false);
			interactWith(c-1, r+1, false);
			
			interactWith(c+2, r, false);
			interactWith(c+2, r+1, false);
			
			//if not aligned vertically, the third block down on the sides will also be touching the edges
			if(pos.y % S > 0){
				interactWith(c-1, r+2, false);
				interactWith(c+2, r+2, false);
			}
		}
		//if not aligned horizontally, the blocks to the right will be inside the player
		else {
			interactWith(c+2, r, true);
			interactWith(c+2, r+1, true);
		}
		
		//if aligned vertically, the blocks above and below will be touching the edges
		if(pos.y % S == 0){
			interactWith(c, r-1, false);
			interactWith(c+1, r-1, false);
			
			interactWith(c, r+2, false);
			interactWith(c+1, r+2, false);
			
			//if not aligned horizontally, the third block to the right, both above and below, will also be touching the edges
			if(pos.x % S > 0){
				interactWith(c+2, r-1, false);
				interactWith(c+2, r+2, false);
			}
		}
		//if not aligned vertically, the blocks below will be inside the player
		else {
			interactWith(c, r+2, true);
			interactWith(c+1, r+2, true);
		}
		
		//if not aligned horizontally or vertically, the block below to the right will be inside the player
		if(pos.x % S > 0 && pos.y % S > 0) interactWith(c+2, r+2, true);
	}
	
	public void interactWith(int c, int r, boolean inside){
		if(this != gameScreen.getPlayer()) return;
		
		Block currentBlock = gameScreen.getBlocks()[c][r];
		if(currentBlock == null || currentBlock.isHidden()) return;
		
		
		PVector pos = getTranslation();
		int playerC = (int)(pos.x / GameScreen.BLOCK_SIZE);
		int playerR = (int)(pos.y / GameScreen.BLOCK_SIZE);
		
		//check if bottom edge is touching (grounded)
		if(r == playerR + 2 && (c == playerC || c == playerC + 1 || pos.x % GameScreen.BLOCK_SIZE > 0 && c == playerC + 2)){
			if(currentBlock.isPhysical()){
				grounded = true;
			}
		}
		
		
		//material-specific behavior
		switch (currentBlock.getMaterial()){
		case Block.STONE:
			break;
			
		case Block.POISON:
			gameScreen.reset();
			break;
			
		case Block.BOUNCY:
			jump();
			break;
		
		case Block.TERMINAL:
			gameScreen.endLevel();
			break;
			
		case Block.COLLECTIBLE:
			if(inside) gameScreen.collect(gameScreen.getBlocks()[c][r]);
			break;
		}
	}
	
	public PVector getVelocity(){
		return velocity;
	}

	public void setVelocity(PVector v) {
		velocity = v;
	}

}
