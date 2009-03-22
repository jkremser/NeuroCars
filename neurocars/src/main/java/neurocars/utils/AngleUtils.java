package neurocars.utils;

/**
 * Trida pomocnych metod na praci s uhly
 * 
 * @author Lukas Holcik
 * 
 */
public class AngleUtils {

  public static final double DEGREE = 2 * Math.PI / 360.0;

  /**
   * Prevede uhel do intervalu [0 .. 2 * PI]
   * 
   * @param angle
   * @return
   */
  public static double normalizeAngle(double angle) {
    while (angle >= 2 * Math.PI) {
      angle -= 2 * Math.PI;
    }
    while (angle < 0) {
      angle += 2 * Math.PI;
    }
    return angle;
  }

}
