/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.game_screen;

import java.awt.geom.Rectangle2D;

public class SquarePlayer extends PlayerComponent{

	public SquarePlayer(GameScreen gameScreen) {
		super(gameScreen);
		
		setShape(new Rectangle2D.Float(0, 0, GameScreen.BLOCK_SIZE*2, GameScreen.BLOCK_SIZE*2));
		setFill(app.color(255, 130, 0));
		setStroke(0);
		setStrokeWeight(2);
	}
	
	public SquarePlayer(PlayerComponent oldPlayer){
		this(oldPlayer.gameScreen);
		copyAttributes(oldPlayer);
	}
}
