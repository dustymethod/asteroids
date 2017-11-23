import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.Shape;

public interface Sprite {
   public void update();
   public void draw(Graphics2D g2);
   public Point2D.Double getPosition();
   public Shape getShape();
}
