import javax.swing.JFrame;

/**
 * The main class that instantiates the arcade game. Creates a JFrame and JComponent
 * that contain the execution of the game.
 * 
 * @author Arudrra, Nick
 *
 */

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		int frameWidth = 818;
		int frameHeight = 997;
		
		frame.setSize(frameWidth, frameHeight);
		frame.setTitle("Bubble Bobble");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GameComponent myComponent = new GameComponent(frame);
		frame.add(myComponent);
		
		frame.setVisible(true);
	}

}
