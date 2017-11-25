import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.Shape;

public class AsteroidParticle extends BaseParticle {
	private static final double FRICTION = 0.62;
	private static final double SPEED = 25;
	private double rotation, rotSpeed;
	private double rootRotation = 0;
	private double length;
	private double friction;
	public AsteroidParticle() {
		reset();
	}

	public void update() {
		if (isActive) {
			HelperUtil.addToPoint(position, velocity);
			HelperUtil.multPoint(velocity, friction);
			rotation += rotSpeed;
			age += growth;
			if (!isAlive()) {
				reset();
			}
		}
	}

	public void spawn(double x, double y, double rot, Shape shape) {
		position.setLocation(x, y);
		this.shape = shape;
		rootRotation = rot;
		isActive = true;
	}

	public void reset() {
		double vx = HelperUtil.randDouble(-1.5, 1.5);
		double vy = HelperUtil.randDouble(-1.5, 1.5);
		velocity.setLocation(vx, vy);
		friction = HelperUtil.randDouble(0.89, 0.95); //TODO: magic numbers
		growth = HelperUtil.randInt(4, 10);
		rotSpeed = HelperUtil.randDouble(0.01, 0.06) * HelperUtil.randSign();
		rotation = 0;
		age = 0;
		isActive = false;
	}

	public boolean isAlive() {
		return age < terminalAge;
	}

	public int getLifeRemaining() {
		return terminalAge - age >= 0 ? terminalAge - age : 0;
	}
	
	public void draw(Graphics2D g2) {
		if (isActive) {
			AffineTransform at = g2.getTransform(); //TODO: mirror
			g2.translate(position.getX(), position.getY());
			Line2D.Double line = (Line2D.Double)shape;
			double midX = (line.getX1() + line.getX2()) /2;
			double midY = (line.getY1() + line.getY2()) /2;
			g2.rotate(rootRotation);
			g2.rotate(rotation, midX, midY);
			g2.setColor(HelperUtil.getRGBAColor(color, getLifeRemaining()));
			g2.draw(line);
			g2.setTransform(at);
		}
	}
}
