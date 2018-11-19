import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GameComponent - A class that creates a game Component to play the game. It
 * reads a textfile and draws the block positions while handling the system game
 * display and object interactions including collisions, creation, and death of
 * objects.
 * 
 * @author Arudrra, Nick
 *
 */

public class GameComponent extends JComponent {

	private ArrayList<Block> blocks;
	private AbstractGamePiece hero;
	private int level;
	private int bubbleCount;
	private Color color;
	private ArrayList<AbstractGamePiece> pieces;
	private ArrayList<AbstractGamePiece> piecesToAdd;
	private HashMap<String, BufferedImage> images;
	private boolean firstTimeRunning;
	private Font scoreFont;
	private int monsterCount;
	private Timer advanceStateTimer;

	/**
	 * GameComponent - A constructor that initializes fields while adding an
	 * ActionListener for user key input
	 * 
	 * @param frame
	 */
	public GameComponent(JFrame frame) {
		//initializing fields
		images = new HashMap<String, BufferedImage>();
		try {
			images.put("Scorebar.png", ImageIO.read(new File("Scorebar.png")));
			images.put("BobbleRight.png", ImageIO.read(new File("BobbleRight.png")));
			images.put("Scorebar.png", ImageIO.read(new File("Scorebar.png")));
			images.put("Cooldownbar.png", ImageIO.read(new File("Cooldownbar.png")));
			images.put("Victory.png", ImageIO.read(new File("Victory.png")));
			images.put("Defeat.png", ImageIO.read(new File("Defeat.png")));
			images.put("Bubble.png", ImageIO.read(new File("Bubble.png")));
			images.put("MonsterRight.png", ImageIO.read(new File("MonsterRight.png")));
			images.put("ArmedMonsterRight.png", ImageIO.read(new File("ArmedMonsterRight.png")));
			images.put("Fruit.png", ImageIO.read(new File("Fruit.png")));
			images.put("Bullet.png", ImageIO.read(new File("Bullet.png")));
			images.put("Portal.png", ImageIO.read(new File("Portal.png")));
			images.put("heroImage.png", ImageIO.read(new File("BobbleRight.png")));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		level = 1;
		drawBlocks();
		color = Color.GREEN;
		blocks = new ArrayList<>();
		this.pieces = new ArrayList<AbstractGamePiece>();
		this.piecesToAdd = new ArrayList<AbstractGamePiece>();
		this.firstTimeRunning = true;
		this.scoreFont = new Font("Serif", Font.BOLD, 40);
		this.monsterCount = 0;
		this.bubbleCount = 0;

		advanceStateTimer = new Timer(7, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timePassed();
				repaint();
			}
		});
		advanceStateTimer.start();
		this.hero = new Hero(100, 420, images.get("heroImage.png"));
		pieces.add(hero);

		KeyListener heroListener = new GameListener(hero, piecesToAdd);
		frame.addKeyListener(heroListener);

		/**
		 * KeyListener - An ActionListener that sets up u and d for level switching, and
		 * spacebar for bubble creation
		 */
		KeyListener levelSwitchingListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 85) {
					if (level < 8) {
						level++;
						clearBoardExceptHero();
						firstTimeRunning = true;
					}
				}
				if (e.getKeyCode() == 68) {
					if (level > 1) {
						level--;
						clearBoardExceptHero();
						firstTimeRunning = true;
					}
				}
				if (bubbleCount < 7) {
					if (e.getKeyCode() == 32) {
						piecesToAdd.add(new Bubble(hero.getCenterPoint().getX(), hero.getCenterPoint().getY() - 3, hero,
								piecesToAdd, images.get("Bubble.png"), images.get("Fruit.png")));
						bubbleCount++;
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		};
		frame.addKeyListener(levelSwitchingListener);

	}

	/**
	 * paintComponent - A JComponent that draws the images for each piece at its
	 * current position for each frame of the game. It includes the clear screen for
	 * new level function along with the defeat screen
	 * 
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillRect(0, 75, 800, 800);

		try {
			drawLevelFromFile(g2, "Level" + level);
		} catch (FileNotFoundException e) {
			System.out.println("Filename level" + level + " not found");
		}

		for (AbstractGamePiece piece : pieces) {
			piece.draw(g2);
		}
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).timeToDie()) {
				if (i != 0) {
					pieces.remove(i);
					i--;
				} else {
					clearBoardExceptHero();
					advanceStateTimer.stop();
					g2.drawImage(images.get("Defeat.png"), 0, 75, null);
				}

			} else {
				pieces.get(i).draw(g2);
			}
		}
	}

	/**
	 * drawLevelFromFile - A method that reads and draws the level from a textfile,
	 * specifically the blocks along with the initial monster and hero positions. It
	 * also draws the cooldown bar along with the lives.
	 * 
	 * @param g2
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public void drawLevelFromFile(Graphics g2, String filename) throws FileNotFoundException {
		g2.drawImage(images.get("Scorebar.png"), 0, 0, null);
		g2.drawImage(images.get("Cooldownbar.png"), 0, 875, null);
		g2.setColor(color);
		g2.setFont(scoreFont);
		g2.drawString("" + pieces.get(0).getScore(), 550, 50);
		for (int i = 0; i < pieces.get(0).getLives(); i++) {
			g2.drawImage(images.get("BobbleRight.png"), i * 50 + 150, 10, null);
		}
		if ((bubbleCount > 0) && (!firstTimeRunning)) {
			g2.fillRect(245, 896, ((this.bubbleCount) * 77), 33);
		}

		blocks = new ArrayList<Block>();
		try {
			Scanner in = new Scanner(new File(filename));
			int col = 0;
			while (in.hasNext()) {
				String str = in.nextLine();
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == 'X') {
						boolean isPlatform = false;
						if (col != 0 && col != 31) {
							if (i != 0 && i != 15) {
								isPlatform = true;
							}
						}
						Block block = new Block(i * 50, col * 25 + 75, isPlatform, images.get("Block.png"));
						blocks.add(block);
						block.draw(g2);
					}
					if (str.charAt(i) == 'P') {
						g2.drawImage(images.get("Portal.png"), i * 50, col * 25 + 75, null);
					}
					if (firstTimeRunning) {
						drawBlocks();
						hero.setCenterPoint(100, 825);
						if (str.charAt(i) == 'M') {
							AbstractGamePiece monster = new ArmedMonster(i * 50, col * 25 + 75, hero, piecesToAdd,
									images.get("ArmedMonsterRight.png"), images.get("Bullet.png"));
							pieces.add(monster);
							monsterCount++;
						}
						if (str.charAt(i) == 'm') {
							AbstractGamePiece monster = new Monster(i * 50, col * 25 + 75, hero, images.get("MonsterRight.png"));
							pieces.add(monster);
							monsterCount++;
						}
					}
				}
				col++;
			}
		} catch (IOException FileNotFound) {
			System.out.println("File not found");
		}
		
		if (!firstTimeRunning) {
			if (((Hero) hero).getFruitCollected() == monsterCount) {
				clearBoardExceptHero();
				if (level < 8) {
					level++;
					firstTimeRunning = true;
					((Hero) hero).setFruitCollected(0);
				} else {
					advanceStateTimer.stop();
					g2.drawImage(images.get("Victory.png"), 0, 75, null);
				}
			}
		} else {
			firstTimeRunning = false;
		}
		
	}

	/**
	 * timePassed - A method that simulates every moment of the game. Movement is updated
	 * for every piece followed by collision checking. If in-game pieces die, they are
	 * removed from the game and if pieces have been created, they are added to the game.
	 */
	public void timePassed() {
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			for (AbstractGamePiece piece1 : pieces) {
				block.checkCollideSides(piece1);
				block.checkCollideBottom(piece1);
				// portal logic
				if (piece1.getCenterPoint().getY() - 30 < 100) {
					piece1.setyVelocity(-1);
				}
				if (piece1.getCenterPoint().getY() > 855) {
					piece1.setCenterPoint(piece1.getCenterPoint().getX(), 100);
				}
				if (piece1.getCenterPoint().getX() < 50) {
					piece1.setyVelocity(0);
					piece1.setCenterPoint(700, piece1.getCenterPoint().getY());
				}
				if (piece1.getCenterPoint().getX() > 750) {
					piece1.setxVelocity(0);
					piece1.setCenterPoint(100, piece1.getCenterPoint().getY());
				}
			}
		}

		for (int i = 0; i < pieces.size(); i++) {
			AbstractGamePiece piece = pieces.get(i);
			piece.timePassed();

			for (int n = i + 1; n < pieces.size(); n++) {
				piece.checkCollision(pieces.get(n));
			}

			if (piece.timeToDie()) {
				if (i != 0) {
					pieces.remove(pieces.get(i));
					i--;
				}
			}
		}

		for (AbstractGamePiece piece : piecesToAdd) {
			pieces.add(piece);
		}
		piecesToAdd.clear();

		bubbleCount = pieces.size() - (this.monsterCount + 1) + ((Hero) (hero)).getFruitCollected();
	}

	/**
	 * clearBoardExceptHero - A method that clears all objects except for the hero. It is used to
	 * change levels and display winning/losing screens.
	 */
	public void clearBoardExceptHero() {
		this.monsterCount = 0;
		this.bubbleCount = 0;
		for (int i = 1; i < pieces.size(); i++) {
			pieces.remove(i);
			i--;
		}
	}
	
	/**
	 * drawBlocks - A method that draws the blocks in a specific color theme based on the percentage of
	 * levels completed.
	 */
	public void drawBlocks() {
		BufferedImage image = null;
		try {
			if ((double) (level) / 8 <= 0.25) {
				image = ImageIO.read(new File("Block1.png"));
				color = Color.YELLOW;
			} else if ((double) (level) / 8 <= 0.5) {
				image = ImageIO.read(new File("Block2.png"));
				color = Color.DARK_GRAY;
			} else if ((double) (level) / 8 <= 0.75) {
				image = ImageIO.read(new File("Block3.png"));
				color = Color.magenta;
			} else {
				image = ImageIO.read(new File("Block4.png"));
				color = Color.ORANGE;
			}
		} catch (IOException e) {
		}
		images.put("Block.png", image);

	}
}
