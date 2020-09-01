/**
 * Mitchell Blanchard, Nathan Marshall, Nick Perez (2016)
 */

package transforman.input;

import java.util.ArrayList;

/**
 * Keeps track of all keys being pressed.
 * Allows for easy access to isDown() function for keys.
 */
public class Keyboard {
	private static ArrayList<Integer> keys  = new ArrayList<Integer>();
	
	//used by TransforManApplet
	
	public static void addKey(int keyCode){
		if(!isDown(keyCode)){
			getKeys().add(keyCode);
		}
		
	}
	
	public static void removeKey(int keyCode){
		getKeys().remove(new Integer (keyCode));
		
	}
	
	//check from anywhere if a key is down
	
	public static boolean isDown(int keyCode){
		return getKeys().contains(keyCode);
	}
	public static boolean isDown(char key){
		return getKeys().contains((int)key);
	}

	public static ArrayList<Integer> getKeys() {
		return keys;
	}

	public static void setKeys(ArrayList<Integer> keys) {
		Keyboard.keys = keys;
	}
}
