import java.awt.Graphics2D;

public class ParticleSystem {
   private Particle[] particles;
   protected boolean isActive;

   public ParticleSystem(Particle[] p) {
      particles = p;
   }

   public void setParticles(Particle[] p) {
      particles = p;
   }

   public void update() {
      if (isActive) {
         boolean active = false;
         for (Particle p : particles) {
            if (p.getIsActive()) {
               p.update();
               active = true;
            }
         }
         if (!active) {
            isActive = false;
         }
      }
   }
   public boolean getIsActive() {
      return isActive;
   }
   public void spawn(double x, double y) {
      for (Particle p : particles) {
         p.spawn(x, y);
      }
      isActive = true;
   }
   public void reset() {
      for (Particle p : particles) {
         p.reset();
      }
      isActive = false;
   }
   public void draw(Graphics2D g2) {
      if (isActive) {
         for (Particle p : particles) {
            p.draw(g2);
         }
      }
   }
}
