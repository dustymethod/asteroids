import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
/*
TODO: projectiles /asteroids removed before drawing of next frame; 	projectiles appear to hit asteroid before appearing to touch
TODO: balance projectile damage and astetroid health. scale to single digits.
TODO: group health bar and energy bar classes
TODO: separate game behaviour better for game running and game over states
TODO: what happens when the game ends; reset game
*/

public class GameComponent extends JComponent {

	public enum GameState { GAME_RUNNING, GAME_OVER; }
	private GameState gameState = GameState.GAME_RUNNING;
	private final int WINDOW_TITLEBAR_HEIGHT = 30;
	private final int SHIP_INIT_X = 10;
	private final int SHIP_INIT_Y = GameFrame.HEIGHT/2;
	private final int NUM_ASTEROIDS = 100;
	
	private final Color defaultColor = new Color(0, 255, 0);
	private final Color bgColor = new Color(15, 14, 18, 255);
	private long time = 0; //keep track of frames passed
	private Timer timer;
	private final Ship ship;
	private final Asteroid[] asteroids;
	private final CannonProjectile[] shipProjectiles;
	private final ShipController shipController;
	private final HealthBar healthBar = new HealthBar();
	private final EnergyBar energyBar = new EnergyBar();
	private final PSManager hitParticles; //particles spawned when a projectile hits an asteroid
	private final PSManager starParticles;
	private final PSManager fastStarParticles;
	private final AsteroidPSManager asteroidParticles;


	public GameComponent() {
		AsteroidImpl.setMovementBounds(new Rectangle2D.Double(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT));
		AsteroidImpl.setStartBounds(GameFrame.WIDTH, GameFrame.HEIGHT);

		asteroids = new Asteroid[NUM_ASTEROIDS];
		for (int i = 0; i < NUM_ASTEROIDS; i++) {
			asteroids[i] = new AsteroidImpl();
		}

		ship = new ShipImpl(SHIP_INIT_X, SHIP_INIT_Y, defaultColor);
		ship.setMovementBounds(new Rectangle2D.Double(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT - WINDOW_TITLEBAR_HEIGHT)); //TODO: magic num
		shipController = new ShipController(ship);
		shipProjectiles = ((ShipImpl)ship).getCannon().getProjectiles();

		int numSystems = 50; //number of systems in each PSManager
		int numParticles = 8; //particles in each system
		hitParticles = ParticleFactory.getManager("HitParticle", 8, 50);
		starParticles = ParticleFactory.getManager("StarParticle", 250, 1);
		fastStarParticles = ParticleFactory.getManager("FastStarParticle", 30, 1);

		starParticles.spawnSystem(0, 0);
		fastStarParticles.spawnSystem(0, 0);
		asteroidParticles = ParticleFactory.getAsteroidPSManager(50);
		addKeyListener(shipController);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (gameState == GameState.GAME_OVER) {
					if (e.getKeyCode() == 82) { //r
						resetGame();
					}
				}
			}
		});
	}

	public void start() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new GameTimerTask(), 1, 30); //TODO: desired framerate
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				spawnAsteroid();
			}
		}, 1, 250);
	}

	public void resetGame() {
		System.out.println("reset game");
	}

	public void update() {
		requestFocusInWindow();

		switch(gameState) {
			case GAME_RUNNING:
			updateGameObjects();
			if (!ship.isAlive()) {
				gameOver();
			}
			time++;
			break;
			case GAME_OVER:
			updateGameObjects();
			break;
			default:
			break;
		}
		repaint();
	}

	private void updateGameObjects() {
		shipController.update();
		ship.update();
		hitParticles.update();
		starParticles.update();
		fastStarParticles.update();
		asteroidParticles.update();

		energyBar.setValue(ship.getEnergy()/ShipImpl.MAX_ENERGY);
		for (Asteroid a : asteroids) {
			a.update();
		}

		recycleInactiveRoids(); //recycle dead or offscreen asteroids
		checkProjectileCollision(); //check projectile collision with asteroids
		if (ship.isAlive()) {
			checkAsteroidCollision(); //check asteroid collision with ship
		}
	}

	private void spawnAsteroid() {
		for (int i = 0; i < asteroids.length; i++) {
			if (asteroids[i].getIsActive() == false) {
				asteroids[i].spawn();
				AsteroidImpl.incrVars();
				return;
			}
		}
	}

	//remove offscreen asteroids, and asteroids that are destroyed.
	//if destroyed, spawn particles
	private void recycleInactiveRoids() {
		for (int i = 0; i < asteroids.length; i++) {
			if (asteroids[i].getIsActive()) {
				if (asteroids[i].isOffScreen()) {
					asteroids[i].reset();
				}
				else if (!asteroids[i].isAlive()) {
					AsteroidImpl a = (AsteroidImpl)asteroids[i];
					//TODO: spawn destroyed asteroid particles or ITEMS here
					//if asteroids[i].hasItem
					// Point2D.Double pos = new Point2D.Double(a.getPosition().getX(), a.getPosition().getY());
					// Point2D.Double vel = new Point2D.Double(4, 0); //fake projectile velocity
					// asteroidParticles.spawnSystem(vel.getX(), vel.getY(), pos.getX(), pos.getY(), a.getSegments(), a.getRotation());
					asteroidParticles.spawnSystem(a.getPosition().getX(), a.getPosition().getY(), a.getSegments(), a.getRotation());
					a.reset();
				}
			}
		}
	}

	//check collision between ship's projectils and asteroids
	//spawn hit particles if a projectile hits an asteroid
	private void checkProjectileCollision() {
		for (CannonProjectile p : shipProjectiles) {
			if (p.getIsActive()) {
				for (Asteroid a : asteroids) {
					if (a.getIsActive()) {
						if (p.intersects(a)) {
							p.reset();
							a.damage(25);
							Point2D.Double pos = p.getPosition();
							hitParticles.spawnSystem(pos.getX(), pos.getY());
						}
					}
				}
			}
		}
	}

	private void checkAsteroidCollision() {
		for (Asteroid a : asteroids) {
			if (a.getIsActive() && a.intersects(ship)) {
				ship.damage(10); //TODO: get damage amount from asteroid
				healthBar.setFill(ship.getHealthRemaining());
				AsteroidImpl ai = (AsteroidImpl)a;
				Point2D.Double pos = new Point2D.Double(a.getPosition().getX(), a.getPosition().getY());
				// Point2D.Double vel = ((ShipImpl)ship).getVelocity();
				// asteroidParticles.spawnSystem(vel.getX(), vel.getY(), pos.getX(), pos.getY(), ai.getSegments(), ai.getRotation());
				asteroidParticles.spawnSystem(a.getPosition().getX(), a.getPosition().getY(), ai.getSegments(), ai.getRotation());
				a.reset();
			}
		}
	}

	private void gameOver() {
		// timer.cancel(); //stop timer
		// KeyListener[] listeners = getKeyListeners();
		// removeKeyListener(listeners[0]); //remove key listener
		ship.destroy();
		//spawn ship death particles
		gameState = GameState.GAME_OVER;
	}

	public void paint(Graphics g) {
		Image buffer = createImage(getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D) buffer.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paint(g2);
		g.drawImage(buffer, 0, 0, this);
	}

	private void paint(Graphics2D g2) {
		g2.setPaint(bgColor); //background
		g2.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
		switch(gameState) {
			case GAME_RUNNING:
			drawGameRunning(g2);
			break;
			case GAME_OVER:
			drawGameRunning(g2);
			drawGameOver(g2);
			break;
			default:
			break;
		}
	}

	private void drawGameRunning(Graphics2D g2) {
		hitParticles.draw(g2);
		starParticles.draw(g2);
		fastStarParticles.draw(g2);
		asteroidParticles.draw(g2);
		for (int i = 0; i < asteroids.length; i++) {
			asteroids[i].draw(g2); //draw asteroids
		}
		//draw health and energy bars
		healthBar.draw(g2);
		energyBar.draw(g2);
		ship.draw(g2);
		//draw timer
		g2.setColor(defaultColor);
		g2.drawString(""+time, 15, GameFrame.HEIGHT - 45);
	}

	private void drawGameOver(Graphics2D g2) {
		g2.setColor(Color.GREEN); //TODO: color management
		g2.drawString("Game Over", GameFrame.WIDTH/2, GameFrame.HEIGHT/2);
	}

	private class GameTimerTask extends TimerTask {
		public void run() {
			update();
		}
	}
}
