import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.function.DoubleSupplier;

public class Cannon {
   // private final static int MAX_HEAT = 255;
   // private final int heat;
   private final Point2D.Double position;
   private final int NUM_BULLETS = 50;
   private final CannonProjectile[] projectiles;
   private final int cooldownDuration = 10; //rate of fire
   private final int cooldownRate = 1;
   private int cooldownTime = 0;
   private boolean isTriggerDown;
   private static final double minEnergyReserve = 15;
   public class FireEvent extends RuntimeException {}
   private DoubleSupplier shipEnergy;

   public Cannon(Point2D.Double pos, DoubleSupplier energy) {
      position = pos;
      shipEnergy = energy;
      projectiles = new CannonProjectile[NUM_BULLETS];
      for (int i = 0; i < projectiles.length; i++) {
         projectiles[i] = new CannonProjectile();
      }
   }

   public CannonProjectile[] getProjectiles() { return projectiles; }

   public void update() {
      if (isTriggerDown) {
         tryFiring();
      }
      if (cooldownTime > 0) {
         cooldownTime -= cooldownRate;
      }
      for (CannonProjectile p : projectiles) {
         p.update();
      }
      if (!sufficientEnergy()) {
         //TODO: display insufficient energy icon
      }
   }

   public void tryFiring() {
      if (cooldownTime == 0 && sufficientEnergy()) {
         fireCannon();
         cooldownTime = cooldownDuration;
         throw new FireEvent();
      }
   }
   public boolean sufficientEnergy() {
      return shipEnergy.getAsDouble() > minEnergyReserve;
   }

   public void setTrigger(boolean isDown) {
      isTriggerDown = isDown;
   }

   public void fireCannon() {
      double xOffset = 10; //TODO: shortcut for offsetting start position of cannonfire
      for (int i = 0; i < projectiles.length; i++) {
         CannonProjectile p = projectiles[i];
         if (!p.getIsActive()) {
            p.fire(position.getX() + xOffset, position.getY());
            return;
         }
      }
      System.out.println("No bullets");
   }

   public void draw(Graphics2D g2) {
      for (CannonProjectile p : projectiles) {
         p.draw(g2);
      }
   }

}
