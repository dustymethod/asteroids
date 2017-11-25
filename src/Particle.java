public interface Particle extends Sprite {
	public boolean getIsActive();
	public void reset();
	public void spawn(double x, double y);
	// public void spawn(double x, double y, double rot, Shape s);
}
