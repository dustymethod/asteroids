import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ShipController extends KeyAdapter {
	private Ship ship;
	private boolean key_up;
	private boolean key_down;
	private boolean key_left;
	private boolean key_right;
	private boolean key_fire;

	public ShipController(Ship ship) {
		this.ship = ship;
	}
	public void keyPressed(KeyEvent e) {
		handleKeyInput(e.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent e) {
		handleKeyInput(e.getKeyCode(), false);
	}

	public void update() {
		if (key_up) {
			ship.move(0, -1);
		}
		if (key_left) {
			ship.move(-1, 0);
		}
		if (key_right) {
			ship.move(1, 0);
		}
		if (key_down) {
			ship.move(0, 1);
		}
		ship.setCannonTrigger(key_fire);
	}

	/**
	* @param k keyCode
	* @param wasPressed true if the event was a keyPressed event, false if keyReleased event
	*/
	private void handleKeyInput(int k, boolean wasPressed) {
		if (k == 87 || k == 38 || k == 104) { //up
			setKeyUp(wasPressed);
		} else if (k == 83 || k == 40 || k == 98) { //down
			setKeyDown(wasPressed);
		} else if (k == 65 || k == 37 || k == 100) { //left
			setKeyLeft(wasPressed);
		} else if (k == 68 || k == 39 || k == 102) { //right
			setKeyRight(wasPressed);
		} else if (k == 32) { //spacebar
			setKeyFire(wasPressed);
		}
	}

	private void setKeyUp(boolean isPressed) {
		key_up = isPressed;
	}
	private void setKeyDown(boolean isPressed) {
		key_down = isPressed;
	}
	private void setKeyLeft(boolean isPressed) {
		key_left = isPressed;
	}
	private void setKeyRight(boolean isPressed) {
		key_right = isPressed;
	}
	private void setKeyFire(boolean isPressed) {
		key_fire = isPressed;
	}
}
