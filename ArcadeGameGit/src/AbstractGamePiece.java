import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * AbstractGamePiece - A class that creates a generic game piece object with
 * fields including a center point, x and y velocities, and alive/dead boolean
 * fields. It also handles the drawing of images while checking collisions
 * between itself, blocks, and other Abstract Game Pieces
 * 
 * @author Arudrra, Nick
 *
 */
public abstract class AbstractGamePiece implements Movable, Temporal {
	protected Point2D centerPoint;
	private int xVelocity;
	private int yVelocity;
	private boolean contactBottom;
	private int lastDirection;
	private int width;
	private int height;
	private BufferedImage image;

	/**
	 * AbstractGamePiece - A constructor that initializes the center point,
	 * velocities, and image
	 * 
	 * @param x
	 * @param y
	 * @param image
	 */
	public AbstractGamePiece(double x, double y, BufferedImage image) {
		this.centerPoint = new Point2D.Double(x, y);
		this.xVelocity = 0;
		this.yVelocity = 0;
		this.lastDirection = 1;
		this.image = image;
	}

	/**
	 * draw - A method that draws the image of the AbstractGamePiece
	 * 
	 * @param g2
	 */
	public void draw(Graphics g2) {
		g2.drawImage(image, (int) (centerPoint.getX() - width / 2), (int) (centerPoint.getY() - height / 2), null);
	}

	public void die() {

	}

	public abstract boolean timeToDie();

	public void timePassed() {
		if (getyVelocity() >= 2)
			setyVelocity(0);
		if (getyVelocity() < -1) {
			setyVelocity(getyVelocity() + 1);
			setCenterPoint(new Point2D.Double(getCenterPoint().getX() + getxVelocity(),
					getCenterPoint().getY() + getyVelocity() / 2));
		} else {
			setyVelocity(getyVelocity() + 2);
			setCenterPoint(new Point2D.Double(getCenterPoint().getX() + getxVelocity(),
					getCenterPoint().getY() + getyVelocity()));
		}
	}

	@Override
	public Point2D getCenterPoint() {
		return this.centerPoint;
	}

	public void setCenterPoint(Point2D point) {
		this.centerPoint = point;
	}

	public int getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	/**
	 * getXCorners - A method that returns the x bounds of the Abstract Game Piece
	 * 
	 * @return xCorners
	 */
	public int[] getXCorners() {
		int[] xCorners = new int[2];
		xCorners[0] = (int) centerPoint.getX() - 12;
		xCorners[1] = (int) centerPoint.getX() + 12;
		return xCorners;
	}

	/**
	 * getYCorners - A method that returns the y bounds of the Abstract Game Piece
	 * 
	 * @return yCorners
	 */
	public int[] getYCorners() {
		int[] yCorners = new int[2];
		yCorners[0] = (int) centerPoint.getY() - 26;
		yCorners[1] = (int) centerPoint.getY() + 26;
		return yCorners;
	}
	
	/**
	 * checkCollision - A method that checks for collision between objects by
	 * comparing their x and y ranges to each other (uses getXCorners and
	 * getYCorners)
	 * 
	 * @param piece
	 */
	public void checkCollision(AbstractGamePiece piece) {
		if (Math.abs(piece.getCenterPoint().getX() - this.getCenterPoint().getX()) < (piece.getWidth() / 2
				+ this.getWidth() / 2)) {
			if (Math.abs(piece.getCenterPoint().getY() - this.getCenterPoint().getY()) < (piece.getHeight() / 2
					+ this.getHeight() / 2)) {
				this.hasCollided(piece);
			}
		}
	}

	public void setContactBottom(boolean contact) {
		this.contactBottom = contact;
	}

	public boolean getContactBottom() {
		return contactBottom;
	}

	public void setCenterPoint(double x, double y) {
		this.centerPoint.setLocation(x, y);
	}

	public int getLastDirection() {
		return lastDirection;
	}

	public void setLastDirection(int direction) {
		this.lastDirection = direction;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return this.width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return this.height;
	}

	public void setWeapon(AbstractGamePiece piece) {

	}

	public boolean hasWeapon() {
		return false;
	}

	public boolean isHit() {
		return false;
	}

	public int getTimeAlive() {
		return 0;
	}

	public boolean getKilledByHero() {
		return false;
	}

	public void setKilledByHero() {

	}

	public int getLives() {
		return 0;
	}

	public abstract void hasCollided(AbstractGamePiece piece);

	public void onCollideWithBubble(Bubble bubble) {
		// TODO Auto-generated method stub
	}

	public void onCollideWithMonster(AbstractGamePiece monster) {
		// TODO Auto-generated method stub
	}

	public void onCollideWithHero(AbstractGamePiece hero) {
		// TODO Auto-generated method stub
	}

	public void onCollideWithBullet(Bullet bullet) {
		// TODO Auto-generated method stub
	}

	public abstract void onHorizontalBlockCollide(int[] blockSides, String string);

	public int getScore() {
		return 0;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return this.image;
	}
}
