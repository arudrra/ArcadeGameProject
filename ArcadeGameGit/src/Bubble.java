import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

/**
 * Bubble - A class that creates a Bubble object when the spacebar
 * is hit. Hero shoots a bubble in the direction of its movement.
 * Bubbles have a lifespan of 2400 ticks and can capture monsters.
 * If the hero collides with the bubble, it dies.
 * @author Arudrra, Nick
 *
 */
public class Bubble extends AbstractWeapon {
	private int timeAlive;
	private boolean killedByHero;
	private boolean hasMonster;
	private ArrayList<AbstractGamePiece> piecesToAdd;
	private BufferedImage fruitImage;

	public Bubble(double x, double y, AbstractGamePiece hero, ArrayList<AbstractGamePiece> piecesToAdd,
			BufferedImage image, BufferedImage fruitImage) {
		super(x, y, hero, hero.getLastDirection(), image);
		this.piecesToAdd = piecesToAdd;
		this.timeAlive = 0;
		this.setWidth(50);
		this.setHeight(50);
		this.killedByHero = false;
		this.hasMonster = false;
		this.fruitImage = fruitImage;
	}

	public void timePassed() {
		timeAlive++;
		if (timeAlive < 50) {
			this.setxVelocity(getDirection() * 4);
		} else {
			this.setxVelocity(0);
			if (this.getCenterPoint().getY() <= 125) {
				this.setyVelocity(0);
				if (this.getCenterPoint().getX() < 400) {
					this.setxVelocity(1);
				} else if (this.getCenterPoint().getX() > 400) {
					this.setxVelocity(-1);
				} else {
					this.setxVelocity(0);
				}
			} else {
				this.setyVelocity(-1);
			}
		}
		setCenterPoint(
				new Point2D.Double(getCenterPoint().getX() + getxVelocity(), getCenterPoint().getY() + getyVelocity()));
	}

	public boolean timeToDie() {
		if (this.timeAlive >= 2400)
			return true;
		if (this.killedByHero)
			return true;
		else
			return false;
	}

	public boolean getHasMonster() {
		return this.hasMonster;
	}

	public int getTimeAlive() {
		return timeAlive;
	}

	public boolean getKilledByHero() {
		return killedByHero;
	}

	@Override
	public void hasCollided(AbstractGamePiece piece) {
		piece.onCollideWithBubble(this);
	}

	@Override
	public void onCollideWithMonster(AbstractGamePiece monster) {
		if (!this.hasMonster) {
			if (this.timeAlive <= 50) {
				if (!monster.isHit()) {
					monster.onCollideWithBubble(this);
					this.hasMonster = true;
				}
			}
		}
	}

	@Override
	public void onCollideWithHero(AbstractGamePiece hero) {
		if (this.timeAlive > 50) {
			this.killedByHero = true;
			if (this.hasMonster)
				piecesToAdd.add(new Fruit(this.getCenterPoint().getX(), this.getCenterPoint().getY(), fruitImage));
		}
	}

	@Override
	public void onHorizontalBlockCollide(int[] blockSides, String string) {
		if (timeAlive <= 50) {
			timeAlive = 51;
		}
	}

}
