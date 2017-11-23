import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.Polygon;
import java.awt.Shape;

//TODO: balance and design energy cannon; consumption, cooldowns, rate of fire
public class ShipImpl implements Ship {
   private static final double TOP_SPEED = 3;
	private static final double FRICTION = 0.7;
	private static final int HEIGHT = 20;
   private static final int WIDTH = 20;
   private static final int MAX_HEALTH = 100;
   private int health = MAX_HEALTH;
	private double pitch = 0.0;
   private boolean isActive;

   public static final double MAX_ENERGY = 150;
   private Double energy = new Double(MAX_ENERGY);
   private static final double rechargeRate = 0.5;
   private static final double ENERGY_COST = 12;
   // private double rechargeCooldown = 10;
   // private double rechargeTimer = 0;

   private Color color = new Color(0, 255, 0);
   private final Shape shape;
   private final Shape[] shapes; //additional shapes, ship details
	private final Cannon cannon;
	private final ShipTrail[] trail;

   private final Point2D.Double position = new Point2D.Double();
   private final Point2D.Double velocity = new Point2D.Double();
   private final Point2D.Double acceleration = new Point2D.Double();
	private final Rectangle2D.Double movementBounds = new Rectangle2D.Double();

   public ShipImpl(double x, double y, Color c) {
      position.setLocation(x, y);
      shape = new Polygon( new int[] {0,0,WIDTH},
			                  new int[] {0,HEIGHT,HEIGHT/2}, 3);
		shapes = new Shape[2];
		shapes[0] = shape;
		shapes[1] = new Ellipse2D.Double(WIDTH/4-2.5, HEIGHT/2-2.5, 5,5);
      final double cannonXOffset = 10;
      cannon = new Cannon(position, this::getEnergy);
      trail = new ShipTrail[3];
		for (int i = 0; i < trail.length; i++) {
			trail[i] = new ShipTrail(position, (int)(i * ShipTrail.GROWTH/trail.length)); //TODO: synchronize
		}
      isActive = true;
   }

   public boolean isAlive() {
      return health > 0;
   }

   public Cannon getCannon() {
      return cannon;
   }

   public void setCannonTrigger(boolean isDown) {
      cannon.setTrigger(isDown);
	}

   public void update() {
      if (isActive) {
         updatePitch();
         HelperUtil.addToPoint(velocity, acceleration);
   		HelperUtil.addToPoint(position, velocity);
   		HelperUtil.multPoint(velocity, FRICTION);
   		acceleration.setLocation(0, 0);
   		//movement bounds
   		if (position.x < movementBounds.getX() + WIDTH/2) {
   			position.x = movementBounds.getX() + WIDTH/2;
   		}
   		if (position.x > movementBounds.getWidth() - WIDTH) {
   			position.x = movementBounds.getWidth() - WIDTH;
   		}
   		if (position.y < movementBounds.getY() + HEIGHT/2) {
   			position.y = movementBounds.getY() + HEIGHT/2;
   		}
         if (position.y > movementBounds.getHeight() - HEIGHT/2) {
   			position.y = movementBounds.getHeight() - HEIGHT/2;
   		}
   		for (ShipTrail t : trail) {
   			t.update();
   		}
         try {
            cannon.update();
         } catch (Cannon.FireEvent e) {
            consumeEnergy();
         }
         rechargeEnergy();
      }
   }

   public void consumeEnergy() {
      energy -= ENERGY_COST;
      if (energy <= 0) {
         // rechargeTimer = rechargeCooldown;
         energy = 0.0;
      }
   }
   public void rechargeEnergy() {
      // if (rechargeTimer == 0) {
         energy = energy >= MAX_ENERGY ? MAX_ENERGY : energy + rechargeRate;
      // } else {
         // rechargeTimer = rechargeTimer > 0 ? rechargeTimer - 1 : 0;
      // }
   }

   private void updatePitch() {
      if (acceleration.y != 0) {
   		double pitchRate = 0.2;
   		pitch += pitchRate * Math.signum(acceleration.y);
   		double maxAngle = Math.PI/8;
   		if (pitch > maxAngle) { //cap max pitch
   			pitch = maxAngle;
   		} else if (pitch < -maxAngle) {
   			pitch = -maxAngle;
   		}
   	} else {
   		pitch *= 0.79; ////how fast the ship returns to an even level
   	}
   }

   public void move(int dx, int dy) {
      final Point2D.Double force = new Point2D.Double(dx * TOP_SPEED, dy * TOP_SPEED);
		HelperUtil.addToPoint(acceleration, force);
   }

   public void damage(int dam) {
      health -= dam; //TODO: handle negative health
   }
   public void destroy() {
      isActive = false;
   }

   public void setMovementBounds(Rectangle2D bounds) {
      movementBounds.setRect(bounds);
   }

   public void draw(Graphics2D g2) {
      if (isActive) {
         for (ShipTrail t : trail) {
   			t.draw(g2);
   		}
         AffineTransform at = g2.getTransform();
   		g2.translate(position.getX(), position.getY() - HEIGHT/2);
   		g2.rotate(pitch, WIDTH/2, HEIGHT/2);
   		g2.setColor(Color.BLACK);
         g2.fill(shape);
   		g2.setColor(color);
   		for (Shape s : shapes) {
   			g2.draw(s);
   		}
   		g2.setTransform(at);
      }
      cannon.draw(g2); //includes drawing projectiles
   }

   public double getHealthRemaining() {
      return  (double)health/MAX_HEALTH;
   }
   public double getEnergy() {
      return energy.doubleValue();
   }

   public Point2D.Double getPosition() {
      return position;
   }

   public Point2D.Double getVelocity() {
      return velocity;
   }

   public Shape getShape() {
      return shape;
   }
   public Area getShapeArea() {
      Area a = new Area(getShape()); //TODO: mirror
		AffineTransform at = new AffineTransform();
		at.translate(position.x, position.y - HEIGHT/2);
		at.rotate(pitch, WIDTH/2, HEIGHT/2);
		a.transform(at);
		return a;
   }
   public boolean intersects(Intersectable other) {
      return false;
   }
}
