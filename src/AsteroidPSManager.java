import java.awt.Shape;

public class AsteroidPSManager extends PSManager {
   private AsteroidParticleSystem[] ps;

   public AsteroidPSManager(AsteroidParticleSystem[] ps) {
      super(ps);
      this.ps = ps;
   }

   public void spawnSystem(double x, double y, Shape[] segments, double rot) {
      for (int i = 0; i < ps.length; i++) {
         if (!ps[i].getIsActive()) {
            ps[i].spawn(x, y, rot, segments);
            return;
         }
      }
      System.out.println("no available systems");
   }
}
