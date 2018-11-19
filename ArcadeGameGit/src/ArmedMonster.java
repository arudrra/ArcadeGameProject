import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * ArmedMonster - A class that creates a special kind of Monster object.
 * ArmedMonster shoots a bullet object at hero when the reloadTime is
 * zero and the heights of both pieces are the same
 * @author Arudrra, Nick
 *
 */
public class ArmedMonster extends Monster {
	private AbstractGamePiece hero;
	private int reloadTime;
	private ArrayList<AbstractGamePiece> piecesToAdd;
	private BufferedImage bulletImage;

	public ArmedMonster(double x, double y, AbstractGamePiece hero, ArrayList<AbstractGamePiece> piecesToAdd, BufferedImage image, BufferedImage bulletImage) {
		super(x, y, hero, image);
		this.reloadTime = 1000;
		this.piecesToAdd = piecesToAdd;
		this.hero = hero;
		this.bulletImage = bulletImage;
	}

	public void shoot() {
		int direction = 0;
		if (hero.getCenterPoint().getX()-this.getCenterPoint().getX() < 0) 
			direction = -1;
		else
			direction = 1;

		AbstractGamePiece bullet = new Bullet(-1, 1, this, direction, bulletImage);
		piecesToAdd.add(bullet);
		this.reloadTime = 1000;
	}

	@Override
	public void timePassed() {
		if (reloadTime > 0) {
			reloadTime--;
		} else {
			if(!this.hasWeapon()) {
				if (this.getCenterPoint().getY() < hero.getCenterPoint().getY() + 5 
						&& this.getCenterPoint().getY() > hero.getCenterPoint().getY() - 5) {
					shoot();
				}
			}
		}
		
		super.timePassed();
		
		try {
			if(this.getxVelocity() > 0) {
				this.setImage(ImageIO.read(new File("ArmedMonsterRight.png")));
			} else {
				this.setImage(ImageIO.read(new File("ArmedMonsterLeft.png")));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean canShoot() {
		if (reloadTime == 0) {
			return true;
		}
		return false;
	}
}
