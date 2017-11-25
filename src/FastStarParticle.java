public class FastStarParticle extends StarParticle {
	// private final double minSpd = -5;
	// private final double maxSpd = -8;
	private final double minSpd = -5;
	private final double maxSpd = -8;
	public FastStarParticle() {
		super();
		setSpeed(minSpd, maxSpd);
	}
}
