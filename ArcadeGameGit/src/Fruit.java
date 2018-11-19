import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Fruit - A class that increases the score when collected by the hero.
 * 
 * @author Arudrra, Nick
 *
 */

public class Fruit extends AbstractGamePiece {
	private boolean killed;
	private int timeAlive;

	public Fruit(double x, double y, BufferedImage image) {
		super(x, y, image);
		this.killed = false;
		this.setWidth(30);
		this.setyVelocity(-25);
		this.setHeight(30);
		this.timeAlive = 0;
		if (this.getCenterPoint().getX() > 400) {
			this.setxVelocity(-1);
		} else {
			this.setxVelocity(1);
		}
	}

	public void draw(Graphics g2) {
		try {
			BufferedImage fruitImage = ImageIO.read(new File("Fruit.png"));
			g2.drawImage(fruitImage, (int) (centerPoint.getX() - this.getWidth() / 2), (int) (centerPoint.getY() - this.getHeight() / 2), null);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

	public void timePassed() {
		timeAlive++;
		super.timePassed();
		if (this.getContactBottom()) {
			this.setxVelocity(0);
		}
	}

	@Override
	public boolean timeToDie() {
		if (this.killed == true) {
			return true;
		}
		return false;
	}

	public void onCollideWithHero(AbstractGamePiece hero) {
		if (this.timeAlive > 100) {
			this.killed = true;
			((Hero) hero).addFruitConsumed();
		}
	}

	@Override
	public void hasCollided(AbstractGamePiece piece) {

	}

	@Override
	public void onHorizontalBlockCollide(int[] blockSides, String side) {
		if (side.equals("left")) {
			setCenterPoint(new Point2D.Double(blockSides[0] - getWidth() / 2 - 0, centerPoint.getY()));
		} else {
			setCenterPoint(new Point2D.Double(blockSides[1] + getWidth() / 2 + 0, centerPoint.getY()));
		}
	}

}
