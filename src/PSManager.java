import java.awt.Graphics2D;

public class PSManager {
	private ParticleSystem[] ps;
	public PSManager() { //TODO
		ps = new ParticleSystem[0];
	}
	public PSManager(ParticleSystem[] ps) {
		this.ps = ps;
	}

	public void spawnSystem(double x, double y) {
		for (int i = 0; i < ps.length; i++) {
			if (!ps[i].getIsActive()) {
				ps[i].spawn(x, y);
				return;
			}
		}
		System.out.println("no available ParticleSystems");
	}
	public void update() {
		for (ParticleSystem s : ps) {
			s.update();
		}
	}
	public void draw(Graphics2D g2) {
		for (ParticleSystem s : ps) {
			s.draw(g2);
		}
	}
}
