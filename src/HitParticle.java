import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class HitParticle extends BaseParticle {
	private static final double FRICTION = 0.62;
	private static final double SPEED = 25;
   private double length, rotation;
   public HitParticle() {
      reset();
      growth = 30;
   }
   @Override
   public void spawn(double x, double y) {
      position.setLocation(x, y);
      isActive = true;
   }
   public void reset() {
      double vx = HelperUtil.randDouble(-SPEED, SPEED);
		double vy = HelperUtil.randDouble(-SPEED, SPEED);
		velocity.setLocation(vx, vy);
		length = HelperUtil.randDouble(5, 13); //TODO: magic numbers
		rotation = Math.atan2(velocity.getY(), velocity.getX());
      age = 0;
      isActive = false;
   }
   public void update() {
      if (isActive) {
         HelperUtil.addToPoint(position, velocity);
   		HelperUtil.multPoint(velocity, FRICTION);
   		age += growth;
   		length *= 0.87; //TODO: magic num
   		if (!isAlive()) { //age >= LIFETIME
            reset();
   		}
      }
	}
	public boolean isAlive() {
	   return age < terminalAge;
	}
   public void draw(Graphics2D g2) {
		if (isActive) {
			AffineTransform at = g2.getTransform(); //TODO: mirror
			g2.translate(position.getX(), position.getY());
			g2.rotate(rotation);
			g2.setColor(HelperUtil.getRGBAColor(color, getLifeRemaining()));
			g2.drawLine(0, 0, (int)length, 0);
			g2.setTransform(at);
		}
	}
	public int getLifeRemaining() {
      return terminalAge - age >= 0 ? terminalAge - age : 0;
   }
}
