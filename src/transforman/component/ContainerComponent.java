/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.component;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;

/**
 * Component used to contain other components.
 * This components transformations will be applied to all child components when drawn.
 */
public class ContainerComponent extends GameComponent {
	//a list of components that will be drawn through this one
	private ArrayList<GameComponent> children;
	
	//constructors
	public ContainerComponent(float x, float y){
		super(x, y);
		children = new ArrayList<GameComponent>();
	}
	
	public ContainerComponent(){
		this(0, 0);
	}
	
	//calling update() will update all children
	
	public void update(){
		updateChildren();
	}
	
	public void updateChildren(){
		for(int i=0; i<children.size(); i++){
			children.get(i).update();
		}
	}
	
	//calling draw() or drawTransformed() will draw all children
	
	public void drawTransformed(){
		drawChildren();
	}
	
	public void drawChildren(){
		for(int i=0; i<children.size(); i++){
			children.get(i).draw();
		}
	}
	
	//gets the bounding rectangle surrounding all child components
	
	public Rectangle2D.Float getBounds(){
		if(getChildCount() == 0) return new Rectangle2D.Float();
		
		Rectangle2D.Float bounds = getChild(0).getBounds();
		
		for(int i=1; i<getChildCount(); i++){
			Rectangle2D b = getChild(i).getBounds();
			
			//leftmost left bound between all children
			float x1 = (float) Math.min(bounds.getX(), b.getX());
			//topmost top bound between all children
			float y1 = (float) Math.min(bounds.getY(), b.getY());
			//rightmost right bound between all children
			float x2 = (float) Math.max(bounds.getX()+bounds.getWidth(), b.getX()+b.getWidth());
			//bottom-most bottom bound between all children
			float y2 = (float) Math.max(bounds.getY()+bounds.getHeight(), b.getY()+b.getHeight());
			
			//set new width and height
			float w = x2 - x1;
			float h = y2 - y1;
			
			bounds.setRect(x1, y1, w, h);
		}
		
		return bounds;
	}
	
	//contains returns true if it should return true for any of the child components
	public boolean contains(PVector p){
		for(int i=0; i<getChildCount(); i++){
			if(getChild(i).contains(getChild(i).parentToLocal(p))) return true;
		}
		return false;
	}
	
	// children list access and modification
	
	public GameComponent[] getChildren(){
		return children.toArray(new GameComponent[children.size()]);
	}
	
	public int getChildCount(){
		return children.size();
	}
	
	public GameComponent getChild(int index){
		return children.get(index);
	}
	
	public GameComponent addChild(GameComponent c){
		children.add(c);
		c.parent = this;
		return c;
	}
	public GameComponent addChild(int index, GameComponent c){
		children.add(index, c);
		c.parent = this;
		return c;
	}
	
	public void removeChild(GameComponent c){
		if(c==null) return;
		c.parent = null;
		children.remove(c);
	}
	public void removeChild(int index){
		children.get(index).parent = null;
		children.remove(index);
	}
}
