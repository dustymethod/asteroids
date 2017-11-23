import java.awt.Shape;

public class AsteroidParticleSystem extends ParticleSystem {
   private AsteroidParticle[] particles;

   public AsteroidParticleSystem(Particle[] p) {
      super(p);
      particles = (AsteroidParticle[])p;
   }

   public void spawn(double x, double y, double rot, Shape[] s) {
      particles = new AsteroidParticle[s.length];
      for (int i = 0; i < s.length; i++) {
         particles[i] = new AsteroidParticle();
         particles[i].spawn(x, y, rot, s[i]);
         isActive = true;
      }
      setParticles(particles); //TODO
   }
}
