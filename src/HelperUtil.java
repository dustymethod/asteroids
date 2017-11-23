import java.awt.geom.Point2D;
import java.awt.Color;

public class HelperUtil {

	/** set p1 to sum of p1 and p2 */
	public static void addToPoint(Point2D.Double p1, Point2D.Double p2) {
		p1.setLocation(p1.getX() + p2.getX(), p1.getY() + p2.getY());
	}

	public static void multPoint(Point2D.Double p, double factor) {
		p.setLocation(p.getX() * factor, p.getY() * factor);
	}

	public static double getMagnitude(Point2D.Double p) {
		double mag = (p.getX() * p.getX()) + (p.getY() * p.getY());
		if (mag >= 0) {
			mag = Math.sqrt(mag);
		} else {
			throw new java.lang.ArithmeticException("error finding magnitude.");
		}
		return mag;
	}

	public static void normalizePoint(Point2D.Double p) {
		double mag = getMagnitude(p);
		if (mag > 0) {
			double x = p.getX() / mag;
			double y = p.getY() / mag;
			p.setLocation(x, y);
		} else if (mag == 0) {
		} else {
			throw new ArithmeticException("error normalizing.");
		}
	}

   // normalize() {
   //    let mag = this.getMag();
   //    if (mag > 0) {
   //       this.x /= mag;
   //       this.y /= mag;
   //    } else if (mag === 0) {
   //    } else {
   //       console.log("normalize() error");
   //    }
   // }

	public static int randInt(int min, int max) {
		return (int)(Math.random() * (max - min) + min);
	}

	public static double randDouble(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	//returns 1 or -1
	public static int randSign() {
		return (int)(Math.random() * 2) * 2 -1;
	}

	//return a new color consisting of the given color and alpha values
	public static Color getRGBAColor(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
}
