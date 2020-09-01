/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.input;

import java.util.ArrayList;

import processing.event.MouseEvent;

/**
 * Handles a static list of mouse listeners.
 */
public class Mouse {
	private static ArrayList<Integer> buttons  = new ArrayList<Integer>();
	
	private static ArrayList<MouseListener> listeners = new ArrayList<MouseListener>();
	
	/**
	 * Returns whether the given mouse button is pressed.
	 * (use the PApplet fields LEFT, RIGHT, CENTER)
	 */
	public static boolean isDown(int number){
		return buttons.contains(number);
	}
	
	public static void addListener(MouseListener l){
		listeners.add(l);
	}
	
	public static void removeListener(MouseListener l){
		listeners.remove(l);
	}
	
	public static void clearListeners(){
		listeners.clear();
	}
	
	//notify all listeners of a mouse press
	public static void mousePressed(MouseEvent e){
		buttons.add(e.getButton());
		
		MouseListener[] array = listeners.toArray(new MouseListener[listeners.size()]);
		
		for(int i=0; i<array.length; i++){
			array[i].mousePressed();
		}
	}
	
	//notify all listeners of a mouse release
	public static void mouseReleased(MouseEvent e){
		buttons.remove(new Integer(e.getButton()));
		
		MouseListener[] array = listeners.toArray(new MouseListener[listeners.size()]);
		
		for(int i=0; i<array.length; i++){
			array[i].mouseReleased();
		}
	}
	
}
