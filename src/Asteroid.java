import java.awt.geom.Point2D;

public interface Asteroid extends Intersectable {
   public void spawn();

   public void reset();
   public void damage(int dam);
   // public void destroy();
   public boolean isAlive();
   public boolean isOffScreen();
   public boolean getIsActive();
   public Point2D.Double getPosition();
}
