import javax.swing.JFrame;

/**
* A side-scroller.  Dont crash into an asteroid!
* @author Jerem Hilliker
* @version 2017-02(Feb)-27(Mon) 16:00
*/
public class GameFrame extends JFrame {
	public final static int WIDTH = 800;
	public final static int HEIGHT = 600;

	private final GameComponent comp;

	public GameFrame() {
		setResizable(false);
		comp = new GameComponent();
		add(comp);
	}

	public void setVisible(boolean vis) {
		super.setVisible(vis);
		if(vis) comp.start();
	}

	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Gladius");
		frame.setVisible(true);
	}
}