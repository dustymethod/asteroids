import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.Shape;

public abstract class BaseParticle implements Particle {
   protected final Point2D.Double position = new Point2D.Double();
   protected final Point2D.Double velocity = new Point2D.Double();
   protected int terminalAge = 255; //particle becomes inactive if it's age reaches or exceeds this number.
   protected int growth = 0; //rate this particle ages
   protected int age = 0; //current age of this particle;
   protected Color color = new Color(0, 255, 0); //default color
   protected Shape shape;
   protected boolean isActive;
   public boolean getIsActive() { return isActive; }
   public abstract boolean isAlive(); //determine if age reaches or exceeds terminal age
   public Shape getShape() { return shape; }
   public Point2D.Double getPosition() { return position; }
   public void spawn(double x, double y){}
   // public void spawn(double x, double y, double rot, Shape s){}
}
