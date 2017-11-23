import java.awt.Color;
import java.awt.Graphics2D;

public class EnergyBar {
   private static int MAX_WIDTH = 200;
   private static int HEIGHT = 5;
   private static final int X = 0;
   private static final int Y = 0;
   private static final int X_OFFSET = 15;
   private static final int Y_OFFSET = 45;

   private double value = MAX_WIDTH;
   private static Color color = new Color(0, 255, 0);

   public void setValue(double percent) {
      value = (int)(MAX_WIDTH * percent);
   }

   public void draw(Graphics2D g2) {
      g2.setColor(color);
      g2.fillRect(X + X_OFFSET, Y + 35, (int)value, HEIGHT);
   }
}
