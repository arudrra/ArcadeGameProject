import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * GameListener - A class that uses KeyListener to register input from the player to move the hero.
 * 
 * @author Arudrra, Nick
 *
 */

public class GameListener implements KeyListener {
	private Hero hero;
	private boolean movingUp;
	private boolean leftPressed = false;
	private boolean rightPressed = false;

	public GameListener(AbstractGamePiece hero, ArrayList<AbstractGamePiece> piecesToAdd) {
		movingUp = false;
		this.hero = (Hero) hero;
		leftPressed = false;
		rightPressed = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 39) {
			hero.setxVelocity(2);
			hero.setLastDirection(1);
			leftPressed = true;
		}
		if (e.getKeyCode() == 37) {
			hero.setxVelocity(-2);
			hero.setLastDirection(-1);
			rightPressed = true;
		}

		if (hero.getContactBottom()) {
			if (e.getKeyCode() == 38) {
				movingUp = true;
				hero.setyVelocity(-30);
				hero.setContactBottom(false);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 39) {
			if (!rightPressed)
				hero.setxVelocity(0);
			leftPressed = false;
		}
		if (e.getKeyCode() == 37) {
			if (!leftPressed)
				hero.setxVelocity(0);
			rightPressed = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * @return the movingUp
	 */
	public boolean isMovingUp() {
		return movingUp;
	}

	/**
	 * @param movingUp the movingUp to set
	 */
	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

}
