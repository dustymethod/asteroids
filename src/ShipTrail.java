import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class ShipTrail extends BaseParticle {
	private static final int START_X_OFFSET = 10;
	private static final int WIDTH = 5;
	private static final int HEIGHT = 10;
   private static final int SPEED = -2;
   private Point2D.Double origin = new Point2D.Double();
	private double scale;
	private long sleepTime; //offset start time
   public static int GROWTH = 20;

   public ShipTrail() {
      growth = 20;
      velocity.setLocation(SPEED, 0);
   }

   public ShipTrail(Point2D.Double o, long t) {
      this();
      origin = o;
      sleepTime = t;
   }
   public void setOrigin(Point2D.Double o) {
      origin = o;
   }
   public boolean isAlive() {
      return age < terminalAge;
   }

	public void reset() {
		position.setLocation(origin.getX(), origin.getY());
		scale = 1;
		age = 0;
	}

   public void update() {
		if (sleepTime >= 0) {
			sleepTime --;
		} else {
			age += growth;
			scale *= 0.9;
         HelperUtil.addToPoint(position, velocity);
			if (!isAlive()) {
				reset();
			}
		}
	}

   public int getLifeRemaining() {
      return (int)(terminalAge - age);
   }

   public void draw(Graphics2D g2) {
		g2.setColor(HelperUtil.getRGBAColor(color, getLifeRemaining()));
		AffineTransform at = g2.getTransform();
		g2.translate(position.getX() - START_X_OFFSET, position.getY());
		g2.drawOval(-WIDTH/2, -HEIGHT/2, (int)(WIDTH * scale), (int)(HEIGHT));
		g2.setTransform(at);
	}
}
