import java.awt.image.BufferedImage;
/**
 * AbstractWeapon - A class that creates a weapon object of type AbstractGamePiece
 * with a direction field
 * @author Arudrra, Nick
 *
 */
public abstract class AbstractWeapon extends AbstractGamePiece {
	private int direction;
	
	public AbstractWeapon(double x, double y, AbstractGamePiece gamePiece, int direction, BufferedImage image) {
		super(x, y, image);
		this.direction = direction;
	}
	
	public int getDirection() {
		return direction;
	}
		
}