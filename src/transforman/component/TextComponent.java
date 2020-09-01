/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.component;

import processing.core.PFont;
import processing.core.PVector;
import transforman.fonts.Fonts;

/**
 * Component for formatting and displaying text.
 */
public class TextComponent extends GameComponent {
	private String text;
	private PVector dimension; // size of textbox, set to null for no restrictions
	private PFont font;
	private float size;
	private int fill;
	private int hAlign, vAlign;
	
	public TextComponent(String text, PVector dimension, PFont font, float size, int colour, int hAlign, int vAlign){
		super();
		
		this.text = text;
		this.dimension = dimension;
		this.font = font;
		this.size = size;
		this.fill = colour;
		this.hAlign = hAlign;
		this.vAlign = vAlign;
	}
	
	public TextComponent(String text, float width, float height){
		this(text, new PVector(width, height), Fonts.getDefault(), 60, app.color(0), app.LEFT, app.TOP);
	}
	
	public TextComponent(String text){
		this(text, null, Fonts.getDefault(), 60, app.color(0), app.LEFT, app.TOP);
	}
	
	public TextComponent(){
		this("");
	}
	
	public void drawTransformed(){
		app.textFont(font, size);
		app.fill(fill);
		app.textAlign(hAlign, vAlign);
		
		if(dimension == null) app.text(text, 0, 0);
		else app.text(text, 0, 0, dimension.x, dimension.y);
	}
	
	///////////////////////////////////////////////////NO contains() METHOD
	///////////////////////////////////////////////////NO getBounds() METHOD
	
	//getters
	
	public String getText(){
		return text;
	}
	public PVector getDimension(){
		return dimension;
	}
	public PFont getFont(){
		return font;
	}
	public float getSize(){
		return size;
	}
	public int getFill(){
		return fill;
	}
	public int getHorizontalAlignment(){
		return hAlign;
	}
	public int getVerticalAlignment(){
		return vAlign;
	}
	
	//setters
	
	public void setText(String t){
		text = t;
	}
	public void setDimension(PVector v){
		dimension = v;
	}
	public void setFont(PFont f){
		font = f;
	}
	public void setSize(float s){
		size = s;
	}
	public void setFill(int c){
		fill = c;
	}
	public void setHorizontalAlignment(int h){
		hAlign = h;
	}
	public void setVerticalAlignment(int v){
		vAlign = v;
	}
}
