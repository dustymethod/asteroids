import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
/*
   a star-like particle that slowly moves off screen, then resets.
*/
public class StarParticle extends BaseParticle {
   private double diameter;
   private static final int X_RANGE = 100;
   private double minSpd = -0.5;
   private double maxSpd = -1;

   private static final Rectangle2D.Double spawnBounds = new Rectangle2D.Double(GameFrame.WIDTH, 0, GameFrame.WIDTH + X_RANGE, GameFrame.HEIGHT);
   public StarParticle() {
      isActive = true;
      reset();
      double x = HelperUtil.randDouble(0, GameFrame.WIDTH + X_RANGE);
      double y = HelperUtil.randDouble(0, GameFrame.HEIGHT);
      position.setLocation(x, y);
   }

   public void setSpeed(double min, double max) {
      minSpd = min;
      minSpd = max;
   }

   public void reset() {
      double x = HelperUtil.randDouble(spawnBounds.getX(), spawnBounds.getWidth());
      double y = HelperUtil.randDouble(spawnBounds.getY(), spawnBounds.getHeight());
      position.setLocation(x, y);
      diameter = HelperUtil.randDouble(1, 2.5);
      double dx = HelperUtil.randDouble(maxSpd, minSpd);
      velocity.setLocation(dx, 0);
   }

   public void update() {
      HelperUtil.addToPoint(position, velocity);
      if (!isAlive()) {
         reset();
      }
   }

   public boolean isAlive() {
      return position.getX() > 0;
   }

   public void draw(Graphics2D g2) {
      g2.setColor(color);
      g2.fillRect((int)position.getX(), (int)position.getY(), (int)diameter, (int)diameter);
   }
}
