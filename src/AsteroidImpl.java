import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;

public class AsteroidImpl implements Asteroid {
	private static final Rectangle2D.Double BOUNDS = new Rectangle2D.Double();
	private final Point2D.Double position = new Point2D.Double();
	private final Point2D.Double velocity = new Point2D.Double();
	private static double startX = 0;
	private static double startYRange = 0;

	private static double baseSpeed = 1;
	private static double maxSpeed = 5;
	private static final double MAX_SPEED = 15;

	private static final double minRadius = 15;
	private static double maxRadius = 23;
	private static double MAX_RADIUS = 40;
	private static double radiusScaleRate = 0.05;
	private static double maxDiameter = MAX_RADIUS * 2;

	private static final double minScale = 0;
	private static double maxScale = 0;
	private static final double MAX_SCALE = 40;
	private static double scaleScalRate = 0.08;

	private static double minRotSpd = 0.0001;
	private static double maxRotSpd = 0.065;

	private static final int MAX_HEALTH = 10;
	private int health;

	private double rotation;
	private double rotationSpeed;
	// private boolean hasItem;
	private boolean isActive;

	Color color = new Color(0, 255, 0);
	// Color fillColor = new Color(4, 11, 2, 255);
	Color fillColor = new Color(15, 14, 18, 255);
	private Shape shape;
	private Shape[] segments;
	// private final Shape shape, crack;

	public AsteroidImpl() {
		//TODO: implement asteroid variable modifiers (ex speed, size)
		double tempScale = HelperUtil.randDouble(0, 30);
		createAsteroidShape(tempScale);
		reset();
	}

	public static void setStartBounds(double x, double y) {
		startX = x + maxDiameter; //TODO: scale values if scale changes
		startYRange = y + maxDiameter;
	}

	public static void setMovementBounds(Rectangle2D.Double bounds) {
		BOUNDS.setRect(bounds);
	}

	public void reset() {
		createAsteroidShape(0);
		double speed = HelperUtil.randDouble(baseSpeed, maxSpeed);
		velocity.setLocation(-speed, HelperUtil.randDouble(-1, 1)); //TODO: magic numbers
		rotationSpeed = randRot();
		rotation = 0;
		// health = (int)(MAX_HEALTH + maxScale);
		health = MAX_HEALTH;
		isActive = false;
	}

	public void spawn() {
		position.setLocation(startX, HelperUtil.randDouble(0, startYRange));
		isActive = true;
	}

	public void update() {
		if (isActive) {
			move();
			rotate();
		}
	}
	
	public void move() {
		HelperUtil.addToPoint(position, velocity);
	}

	public void rotate() {
		rotation += rotationSpeed;
	}

	public void damage(int dam) {
		health -= dam;
	}

	public void draw(Graphics2D g2) {
		if (isActive) {
			AffineTransform at = g2.getTransform();
			g2.translate(position.getX(), position.getY());
			g2.rotate(rotation);
			g2.setColor(fillColor);
			g2.fill(shape);
			g2.setColor(color);
			g2.draw(shape);
			// //if (isDamaged()) {
			// //	g.draw(crack);
			// //}
			g2.setTransform(at);
		}
	}

	public static void incrVars() {
		if (maxSpeed < MAX_SPEED) maxSpeed += radiusScaleRate;
		// if (maxRadius < MAX_RADIUS) maxRadius += 0.1;
		if (maxScale < MAX_SCALE) maxScale += scaleScalRate;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public boolean isOffScreen() {
		if (position.getX() < BOUNDS.getX() - maxDiameter) {
			return true;
		}
		if (position.getY() < BOUNDS.getX() - maxDiameter || position.getY() > GameFrame.HEIGHT + maxDiameter) {
			return true;
		}
		return false;
	}

	public boolean isAlive() {
		return health > 0;
	}

	public Point2D.Double getPosition() { return position; }
	public Shape getShape() { return shape; }

	public Area getShapeArea() {
		Area a = new Area(getShape());
		AffineTransform at = new AffineTransform(); //TODO: repeating transformations
		at.translate(position.x, position.y);
		at.rotate(rotation);
		a.transform(at);
		return a;
	}

	public boolean intersects(Intersectable other) {
		Area intersecting;
		intersecting = getShapeArea();
		intersecting.intersect(other.getShapeArea());
		return !intersecting.isEmpty();
	}

	private void createAsteroidShape(double aScale) {
		final Polygon p = new Polygon();
		final int minPts = 7;
		final int maxPts = 18;
		final int numPoints = HelperUtil.randInt(minPts, maxPts);
		double angleIncr = 2*Math.PI / numPoints;
		double angle = 0.0;
		double scale = HelperUtil.randDouble(minScale, maxScale);

		ArrayList<Line2D> segs = new ArrayList<Line2D>();
		boolean startSeg = true;
		Point2D.Double oldPoint = new Point2D.Double();
		for (int i = 0; i < numPoints; i++) {
			int x = (int)(Math.cos(angle) * (randRadius() + scale));
			int y = (int)(Math.sin(angle) * (randRadius() + scale));
			if (i == 0) {
				oldPoint.setLocation(x, y);
			} else {
				segs.add(new Line2D.Double(x, y, oldPoint.getX(), oldPoint.getY()));
				oldPoint.setLocation(x, y);
			}
			if (i == numPoints -1) {
				segs.add(new Line2D.Double(segs.get(0).getX2(), segs.get(0).getY2(), oldPoint.getX(), oldPoint.getY()));
			}
			angle += angleIncr;
			p.addPoint(x, y);
		}
		shape = p;
		segments = new Shape[numPoints];
		segments = segs.toArray(segments);

	}

	public Shape[] getSegments() {
		return segments;
	}

	public double getRotation() {
		return rotation;
	}

	private double randRadius() {
		return (int)(HelperUtil.randDouble(minRadius, maxRadius));
	}
	private double randRot() {
		int dir = HelperUtil.randSign();
		return HelperUtil.randDouble(minRotSpd, maxRotSpd) * dir;
	}
}
