/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.component;

import java.awt.geom.Rectangle2D;

import processing.core.PMatrix2D;
import processing.core.PVector;
import transforman.core.TransforManApplet;

/**
 * The core class to extend for any object to be displayed to the screen.
 * This class handles applies 2D affine transformations in a specific order before drawing.
 * To display content, override drawTransformed() rather
 * than draw() to apply transformations automatically.
 */
public class GameComponent implements Drawable {
	//easy access to the instance of TransforManApplet (mostly for calling Processing's PApplet functions)
	public static TransforManApplet app = TransforManApplet.getInstance();
	
	//simple PVector and float values control transformations
	private PVector translation, scale;
	private float rotation;
	
	//the above values are combined by buildMatrix() and stored here
	private PMatrix2D matrix;
	
	//for access to a parent container
	protected ContainerComponent parent;
	
	//constructors
	
	public GameComponent(float x, float y){
		translation = new PVector(x, y);
		scale = new PVector(1, 1);
		rotation = 0;
		
		matrix = new PMatrix2D();
	}
	
	public GameComponent(){
		this(0, 0);
	}
	
	//override this method for update functionality
	public void update(){}
	
	//typically it is not necessary to override this
	public void draw(){
		app.pushMatrix();
		
		//apply transformations
		app.applyMatrix(matrix);
		
		//draw normally
		drawTransformed();
		
		app.popMatrix();
	}
	
	//override this method for drawing functionality
	public void drawTransformed(){}
	
	public ContainerComponent getParent(){
		return parent;
	}
	
	/**
	 * Returns the hierarchy of nested components that this component is in.
	 * [0] is this component's parent and [length-1] is the outermost component.
	 */
	public ContainerComponent[] getHeirarchy(){
		//calculate length
		ContainerComponent comp = parent;
		int length = 0;
		
		while(comp != null){
			length++;
			comp = comp.getParent();
		}
		
		//fill array
		comp = parent;
		ContainerComponent[] hierarchy = new ContainerComponent[length];
		
		for(int i=0; i<length; i++){
			hierarchy[i] = comp;
			comp = comp.getParent();
		}
		
		return hierarchy;
	}
	
	public Rectangle2D.Float getBounds(){
		return new Rectangle2D.Float();
	}
	
	public boolean contains(PVector p){
		return false;
	}
	
	//calls contains() on the mouse coordinates
	public boolean containsMouse(){
		PVector mouse = globalToLocal( new PVector(app.mouseX, app.mouseY) );
		return contains(mouse);
	}
	
	// coordinate conversion methods
	// - 'local' is the vector space inside this component
	// - 'parent' is the vector space containing this component
	// - 'global' is the vector space containing all components
	
	public PVector localToParent(PVector l){
		PMatrix2D m = matrix.get();
		return m.mult(l, null);
	}
	
	public PVector parentToLocal(PVector p){
		PMatrix2D m = matrix.get();
		
		if(m.invert()) return m.mult(p, null);
		else return null;
	}
	
	public PVector localToGlobal(PVector l){
		PVector v = localToParent(l);
		GameComponent[] heirarchy = getHeirarchy();
		
		for(int i=0; i<heirarchy.length; i++){
			v = heirarchy[i].localToParent(v);
		}
		
		return v;
	}
	
	public PVector globalToLocal(PVector g){
		PVector v = g;
		GameComponent[] heirarchy = getHeirarchy();
		
		for(int i=heirarchy.length-1; i>=0; i--){
			v = heirarchy[i].parentToLocal(v);
		}
		
		return parentToLocal(v);
	}
	
	//transformation getters
	
	public PVector getTranslation(){
		return translation;
	}
	public PVector getScale(){
		return scale;
	}
	public float getRotation(){
		return rotation;
	}
	
	/**
	 * Updates the 2D matrix from the component's properties
	 * (From the perspective of the viewer, applies scale values, then rotation, then translation.)
	 */
	public PMatrix2D buildMatrix(){
		PMatrix2D m = new PMatrix2D();
		if(translation.x != 0 || translation.y != 0) m.translate(translation.x, translation.y);
		if(rotation != 0) m.rotate(rotation);
		if(scale.x != 1 || scale.y != 1) m.scale(scale.x, scale.y);
		
		matrix = m;
		return matrix;
	}
	
	public PMatrix2D getMatrix(){
		return matrix;
	}
	
	//transformation setters
	
	public void setTranslation(PVector t){
		translation.set(t.x, t.y);
		buildMatrix();
	}
	public void setScale(PVector s){
		scale.set(s.x, s.y);
		buildMatrix();
	}
	public void setRotation(float r){
		rotation = r;
		buildMatrix();
	}
	
	//transformation modifiers
	
	public void translate(PVector t){
		translation.add(t);
		buildMatrix();
	}
	
	public void scale(PVector s){
		scale.x *= s.x;
		scale.y *= s.y;
		buildMatrix();
	}
	
	//INCOMPLETE METHOD, FORGOT TO ACCOUNT FOR ROTATION (translation is correct, scale is not)
//	public void scale(PVector s, PVector origin){
//		translation.x = origin.x + s.x*(translation.x-origin.x);
//		translation.y = origin.y + s.y*(translation.y-origin.y);
//		
//		scale.x *= s.x;
//		scale.y *= s.y;
//	}
	
	public void rotate(float r){
		rotation += r;
		buildMatrix();
	}
	//rotate around an anchor point
	public void rotate(float r, PVector origin){
		float xDiff = translation.x - origin.x;
		float yDiff = translation.y - origin.y;
		float dist = app.sqrt(xDiff*xDiff + yDiff*yDiff);
		
		translation.x = origin.x + app.cos( app.atan2(yDiff, xDiff) + r )*dist;
		translation.y = origin.y + app.sin( app.atan2(yDiff, xDiff) + r )*dist;
		
		rotation += r;
		buildMatrix();
	}
}
