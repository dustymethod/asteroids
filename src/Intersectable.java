import java.awt.geom.Area;
import java.awt.Shape;

public interface Intersectable extends Sprite {
   public Area getShapeArea();
   public boolean intersects(Intersectable other);
}