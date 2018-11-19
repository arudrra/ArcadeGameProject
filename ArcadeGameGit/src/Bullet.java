import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Bullet - An object created by ArmedMonsters that moves and kills the hero on contact.
 * 
 * @author Arudrra, Nick
 *
 */

public class Bullet extends AbstractWeapon {
	private boolean shouldDie;
	
	public Bullet(double x, double y, AbstractGamePiece armedMonster, int direction, BufferedImage image) {
		super(x, y, armedMonster, direction, image);
		this.shouldDie = false;
		this.setCenterPoint(armedMonster.getCenterPoint().getX(), (int) (armedMonster.getCenterPoint().getY() - 8));
		this.setxVelocity(getDirection() * 3);
		this.setWidth(10);
	}
		
	public void timePassed() {
		if(this.getCenterPoint().getX() < 100 || this.getCenterPoint().getX() > 720) {
			this.shouldDie = true;
		}
		setCenterPoint(new Point2D.Double(getCenterPoint().getX() + getxVelocity(), getCenterPoint().getY() + getyVelocity()));
	}
	
	public boolean timeToDie() {
		if(this.shouldDie) {
			return true;
		}
		return false;
	}
	
	public void setShouldDie() {
		shouldDie = true;
	}

	@Override
	public void hasCollided(AbstractGamePiece piece) {
		piece.onCollideWithBullet(this);
	}
	
	public void onCollideWithHero(AbstractGamePiece hero) {
		shouldDie = true;
		hero.onCollideWithBullet(this);
	}
	@Override
	public void onHorizontalBlockCollide(int[] blockSides, String string) {
		shouldDie = true;
	}
}