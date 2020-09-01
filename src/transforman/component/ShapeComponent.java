/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.component;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import processing.core.PVector;

/**
 * Component for rendering a shape. Accepts a java.awt.Shape.
 */
public class ShapeComponent extends GameComponent {
	private Shape shape;
	private int fill, stroke;
	private float strokeWeight;
	
	public ShapeComponent(Shape shape, int colour, int strokeColour, float strokeWeight){
		super();
		
		this.shape = shape;
		this.fill = colour;
		this.stroke = strokeColour;
		this.strokeWeight = strokeWeight;
	}
	
	public ShapeComponent(Shape shape, int colour){
		this(shape, colour, app.color(0), 10);
	}
	
	public ShapeComponent(Shape shape){
		this(shape, app.color(255), app.color(0), 10);
	}
	
	public ShapeComponent(){
		this(new Polygon(), app.color(255), app.color(0), 10);
	}
	
	// Use PathIterator to render the shape.
	// In hindsight, this is probably somewhat ineffecient,
	// but I made it because PShape lacks contains() functionality.
	public void drawTransformed(){
		app.fill(fill);
		app.stroke(stroke);
		app.strokeWeight(strokeWeight);
		
		boolean inside = true;////////////////////////////////////////// MAKE USE OF THIS TO DO CONTOURS
		float[] lastMoveTo = null;
		PathIterator iterator = shape.getPathIterator(null);
		while(!iterator.isDone()){
			float[] point = new float[6];
			int type = iterator.currentSegment(point);
			switch(type){
			case PathIterator.SEG_MOVETO:
				app.beginShape();
				lastMoveTo = point;
				app.vertex(point[0], point[1]);
				break;
			case PathIterator.SEG_LINETO:
				app.vertex(point[0], point[1]);
				break;
			case PathIterator.SEG_QUADTO:
				app.quadraticVertex(point[0], point[1], point[2], point[3]);
				break;
			case PathIterator.SEG_CUBICTO:
				app.bezierVertex(point[0], point[1], point[2], point[3], point[4], point[5]);
				break;
			case PathIterator.SEG_CLOSE:
				app.vertex(lastMoveTo[0], lastMoveTo[1]);
				app.endShape();
				break;
			}
			
			iterator.next();
		}
	}
	
	public Rectangle2D.Float getBounds(){
		return (Rectangle2D.Float) shape.getBounds2D();
	}
	
	public boolean contains(PVector p){
		return shape.contains(p.x, p.y);
	}

	//getters
	
	public Shape getShape(){
		return shape;
	}
	public int getFill(){
		return fill;
	}
	public int getStroke(){
		return stroke;
	}
	public float getStrokeWeight(){
		return strokeWeight;
	}
	
	//setters
	
	public void setShape(Shape s){
		shape = s;
	}
	public void setFill(int c){
		fill = c;
	}
	public void setStroke(int c){
		stroke = c;
	}
	public void setStrokeWeight(float w){
		strokeWeight = w;
	}
}
