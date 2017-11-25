public class ParticleFactory {
	public static Particle getParticle(String type) {
		if (type.equals("HitParticle")) {
			return new HitParticle();
		} else if (type.equals("StarParticle")) {
			return new StarParticle();
		} else if (type.equals("FastStarParticle")) {
			return new FastStarParticle();
		}
		return null;
	}

	public static ParticleSystem getSystem(String type, int numParticles) {
		Particle[] p = new Particle[numParticles];
		for (int i = 0; i < numParticles; i++) {
			p[i] = getParticle(type);
		}
		return new ParticleSystem(p);
	}

	public static PSManager getManager(String type, int numParticles, int numSystems) {
		ParticleSystem[] ps = new ParticleSystem[numSystems];
		for (int i = 0; i < numSystems; i++) {
			ps[i] = getSystem(type, numParticles);
		}
		return new PSManager(ps);
	}

	public static AsteroidPSManager getAsteroidPSManager(int numSystems) {
		AsteroidParticleSystem[] systems = new AsteroidParticleSystem[numSystems];
		AsteroidParticle[] particles = new AsteroidParticle[1];
		particles[0] = new AsteroidParticle();
		for (int i = 0; i < numSystems; i++) {
			systems[i] = new AsteroidParticleSystem(particles);
		}
		return new AsteroidPSManager(systems);
	}
}
