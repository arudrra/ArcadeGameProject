import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Block - A method that creates a Block object that checks collisions to
 * keep the game pieces within the bounds. Block checks for collisions on
 * all sides, teleporting is handled in GameComponent
 * @author Arudrra, Nick
 *
 */
public class Block {
	private int xPos;
	private int yPos;
	private int[] xCorners;
	private int[] yCorners;
	private BufferedImage image;

	private boolean isPlatform;

	public void draw(Graphics g2) {
		g2.drawImage(image, xPos, yPos, null);
	}
	
	public Block(int x, int y, boolean isPlatform, BufferedImage image) {
		xPos = x;
		yPos = y;
		this.setPlatform(isPlatform);
		xCorners = new int[2];
		yCorners = new int[2];
		xCorners[0] = xPos;
		xCorners[1] = xPos + 50;
		yCorners[0] = yPos;
		yCorners[1] = yPos + 25;
		this.isPlatform = isPlatform;
		this.image = image;
	}

	public int[] getXCorners() {
		return xCorners;
	}

	public int[] getYCorners() {
		return yCorners;
	}

	public double getDistanceFrom(int heroX, int heroY) {
		double distX = xPos - heroX;
		double distY = yPos - heroY;
		double distTotal = Math.sqrt(distX * distX + distY * distY);
		return distTotal;
	}

	public void checkCollideSides(AbstractGamePiece piece) {
		int[] pieceHeights = piece.getYCorners();
		int[] pieceSides = piece.getXCorners();
		int[] blockHeights = yCorners;
		int[] blockSides = xCorners;

		if (pieceSides[0] < blockSides[1] + 1 && pieceSides[0] > blockSides[1] - 5) {
			if (pieceHeights[0] > blockHeights[0] - piece.getHeight()) {
				if (pieceHeights[0] < blockHeights[0] + 25) {
					piece.onHorizontalBlockCollide(blockSides, "right");
				}
			}
		} else if (pieceSides[1] > blockSides[0] - 1 && pieceSides[1] < blockSides[0] + 5) {
			if (pieceHeights[0] > blockHeights[0] - piece.getHeight()) {
				if (pieceHeights[0] < blockHeights[0] + 25) {
					piece.onHorizontalBlockCollide(blockSides, "left");
				}
			}
		}
	}

	public void checkCollideBottom(AbstractGamePiece piece) {
		int[] blockHeights = yCorners;
		int[] blockSides = xCorners;
		int[] pieceHeights = piece.getYCorners();
		int[] pieceSides = piece.getXCorners();

		if ((blockHeights[0] >= pieceHeights[0])){
			if (blockHeights[0] <= pieceHeights[1]) {
				if (pieceSides[0] > blockSides[0] && pieceSides[0] < blockSides[1]) {
					if (piece.getyVelocity() >= 0) {
						piece.setyVelocity(0);
						piece.setCenterPoint(new Point2D.Double(piece.centerPoint.getX(), blockHeights[0] - piece.getHeight()/2-2));
						piece.setContactBottom(true);
					}
				} else if (pieceSides[1] > blockSides[0] && pieceSides[1] < blockSides[1]) {
					if (piece.getyVelocity() >= 0) {
						piece.setyVelocity(0);
						piece.setCenterPoint(new Point2D.Double(piece.centerPoint.getX(), blockHeights[0] - piece.getHeight()/2-2));
						piece.setContactBottom(true);
					}
				}
			}
		} else {
			piece.setContactBottom(false);
		}
	}

	public boolean getIsPlatform() {
		return isPlatform;
	}

	public void setPlatform(boolean isPlatform) {
		this.isPlatform = isPlatform;
	}
}
