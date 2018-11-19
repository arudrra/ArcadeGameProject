import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Monster - A class that serves as the opponents to the hero. Monsters move intelligently, occasionally
 * jump, and can be captured by bubbles. They are killed if inside a bubble that is popped.
 * 
 * @author Arudrra, Nick
 *
 */

public class Monster extends AbstractGamePiece {
	private AbstractGamePiece hero;
	private int xDirection;
	private Bubble bubble;
	private boolean killed;
	private boolean shouldBounce = false;
	private boolean hasBubble;

	public Monster(double x, double y, AbstractGamePiece hero, BufferedImage image) {
		super(x, y, image);
		this.hero = hero;
		this.setxVelocity(1);
		if (hero.getCenterPoint().getX() < this.getCenterPoint().getX()) {
			this.xDirection = -1;
		} else {
			this.xDirection = 1;
		}
		this.setWidth(30);
		this.setHeight(40);
		this.killed = false;
		this.bubble = null;
		this.hasBubble = false;
	}

	public void timePassed() {
		try {
			if(this.getxVelocity() > 0) {
				this.setImage(ImageIO.read(new File("MonsterRight.png")));
			} else {
				this.setImage(ImageIO.read(new File("MonsterLeft.png")));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		if(!shouldBounce) {
			chooseDirection();
		} else {
			this.xDirection *= -1;
			this.setxVelocity(getxVelocity() * -1);
			this.shouldBounce = false;
		}
		
		if(this.bubble == null) {
			super.timePassed();
		} else {
			setCenterPoint(new Point2D.Double(bubble.getCenterPoint().getX(), bubble.getCenterPoint().getY()));
		}
		
		if(this.bubble != null) {
			if(this.bubble.getKilledByHero()) {
				this.killed = true;
			}
			if (this.bubble.getTimeAlive() == 2400) {
				this.hasBubble = false;
				this.bubble = null;
			}
		}
	}
	
	public void chooseDirection() {
		if(Math.random() < .005) {
			if(this.xDirection > 0) {
				this.xDirection = -1;
			} else {
				this.xDirection = 1;
			}
		}
		
		if(this.getContactBottom()) {
			if(hero.getCenterPoint().getY() < this.getCenterPoint().getY() - 100) {
				if(Math.random() < .005) {
					this.setyVelocity(-30);
					this.setContactBottom(false);
				}
			}
		}
		
		if(xDirection > 0) {
			this.setxVelocity(1);
		} else {
			this.setxVelocity(-1);
		}
	}


	public boolean isHit() {
		if (this.bubble == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean timeToDie() {
		if (this.killed == true) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasWeapon() {
		return hasBubble;
	}

	@Override
	public void hasCollided(AbstractGamePiece piece) {
		piece.onCollideWithMonster(this);
	}

	@Override
	public void onCollideWithBubble(Bubble bubble) {
		if (!this.isHit()) {
			if(bubble.getTimeAlive() <= 50) {
				if(!bubble.getHasMonster()) {
					this.bubble = bubble;
					this.hasBubble = true;
					bubble.onCollideWithMonster(this);
				}
			}
		}
	}

	public void onCollideWithHero(AbstractGamePiece hero) {
		hero.onCollideWithMonster(this);
	}

	@Override
	public void onHorizontalBlockCollide(int[] blockSides, String string) {
		shouldBounce = true;
	}
}
