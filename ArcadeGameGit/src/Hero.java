import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Hero - A class that creates a hero object that inherits from AbstractGamePiece.
 * Hero is the player's character who has the ability to move, jump, shoot bubbles,
 * kill monsters, and eat fruit.
 * @author Arudrra, Nick
 *
 */
public class Hero extends AbstractGamePiece {
	private int lives;
	private int score;
	private int respawnCounter;
	private int fruitCollected;

	public Hero(double x, double y, BufferedImage image) {
		super(x, y, image);
		this.setWidth(24);
		this.setHeight(50);
		this.lives = 5;
		this.respawnCounter = 0;
		this.fruitCollected = 0;
	}
	
	public void draw(Graphics g2) {
		if(this.respawnCounter % 8 == 0) {
			g2.drawImage(this.getImage(), (int) (centerPoint.getX() - this.getWidth() / 2), (int) (centerPoint.getY() - this.getHeight() / 2), null);
		}
	}
	
	@Override
	public int getLives() {
		return lives;
	}
	
	public int getScore() {
		return score;
	}

	public void timePassed() {
		try {
			if(this.getxVelocity() > 0) {
				this.setImage(ImageIO.read(new File("BobbleRight.png")));
			}
			if(this.getxVelocity() < 0) {
				this.setImage(ImageIO.read(new File("BobbleLeft.png")));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		if (this.respawnCounter > 0)
			this.respawnCounter--;
		super.timePassed();
	}
 
	@Override
	public boolean timeToDie() {
		if (lives == 0) {
			return true;
		}
		return false;
	}

	public void loseLife() {
		lives--;
		setCenterPoint(new Point2D.Double(100, 750));
	}

	@Override
	public void hasCollided(AbstractGamePiece piece) {
		piece.onCollideWithHero(this);
	}

	public void onCollideWithBullet(Bullet bullet) {
		bullet.setShouldDie();
		loseLife();
	}

	@Override
	public void onCollideWithMonster(AbstractGamePiece monster) {
		if (!monster.hasWeapon() && this.respawnCounter == 0) {
			loseLife();
			this.respawnCounter = 300;
		}
	}
	
	public void onHorizontalBlockCollide(int[] blockSides, String side) {
		if (side.equals("left")) {
			setCenterPoint(new Point2D.Double(blockSides[0] - getWidth() / 2 - 0, centerPoint.getY()));
		} else {
			setCenterPoint(new Point2D.Double(blockSides[1] + getWidth() / 2 + 0, centerPoint.getY()));
		}
	}
	
	public void addFruitConsumed() {
		score += 250;
		fruitCollected++;
	}

	public int getFruitCollected() {
		return fruitCollected;
	}
	
	public void setFruitCollected(int i) {
		this.fruitCollected = i;
	}
}
