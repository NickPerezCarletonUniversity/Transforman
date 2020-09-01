/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.fonts;

import processing.core.PFont;
import transforman.core.TransforManApplet;

/**
 * A class for easy access to fonts.
 */
public class Fonts {
	private static PFont COOPER_BLACK;
	private static PFont CALIBRI_ITALIC;
	
	public static void loadFonts(){
		COOPER_BLACK = TransforManApplet.getInstance().loadFont("CooperBlack-48.vlw");
		CALIBRI_ITALIC = TransforManApplet.getInstance().loadFont("Calibri-Italic-48.vlw");
	}
	
	public static PFont getFont1(){
		return COOPER_BLACK;
	}
	
	public static PFont getFont2(){
		return CALIBRI_ITALIC;
	}
	public static PFont getDefault(){
		return COOPER_BLACK;
	}
}
