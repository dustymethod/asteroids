import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Shape;

public class CannonProjectile implements Sprite {
	private static final Rectangle2D.Double BOUNDS = new Rectangle2D.Double(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
	private final static int LENGTH = 30;
	private final static double SPEED = 20;
	private final Point2D.Double position = new Point2D.Double();
	private final Point2D.Double velocity = new Point2D.Double(SPEED, 0);
	private final Shape shape = new Line2D.Double(0, 0, LENGTH, 0);
	private double rotation;
	private boolean isActive;

	public Point2D.Double getPosition() { return position; }

	public boolean getIsActive() { return isActive; }

	public void fire(double x, double y) {
		position.setLocation(x, y);
		isActive = true;
	}

	public void reset() {
		isActive = false;
	}

	public void update() {
		if (isActive) {
			HelperUtil.addToPoint(position, velocity);
		}
		if (position.getX() > BOUNDS.getWidth()) {
			reset();
		}
	}

	public void draw(Graphics2D g2) {
		if (isActive) {
			AffineTransform at = g2.getTransform();
			g2.translate(position.getX(), position.getY());
			g2.rotate(rotation);
			g2.draw(shape);
			g2.setTransform(at);
		}
	}

	public Shape getShape() { return shape; }

	public Area getShapeArea() {
		Area a = new Area(new Rectangle2D.Double(0, 0, LENGTH, 0.5));
		AffineTransform at = new AffineTransform(); //TODO: repeating transformations
		at.translate(position.getX(), position.getY());
		at.rotate(rotation);
		a.transform(at);
		return a;
	}

	public boolean intersects(Intersectable other) {
		Area intersecting = getShapeArea();
		intersecting.intersect(other.getShapeArea());
		return !intersecting.isEmpty();
	}
}
