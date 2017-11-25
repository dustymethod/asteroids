import java.awt.geom.Rectangle2D;

public interface Ship extends Intersectable {
	public void setMovementBounds(Rectangle2D bounds);
	public void damage(int dam);
	public void move(int x, int y);
	public void setCannonTrigger(boolean b);
	public double getEnergy();
	public boolean isAlive();
	public void destroy();
	public double getHealthRemaining();
}
