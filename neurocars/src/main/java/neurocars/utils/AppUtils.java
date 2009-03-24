package neurocars.utils;

import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Trida pomocnych metod na praci s uhly
 * 
 * @author Lukas Holcik
 * 
 */
public class AppUtils {

  /** Stupen v radianech */
  public static final double DEGREE = Math.PI / 180.0;
  /** Gravitacni zrychleni */
  public static final double G = 9.81;

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

  public static double getDoubleValue(Properties p, String key)
      throws ServiceException {
    if (!p.containsKey(key)) {
      throw new ServiceException("Required parameter not found: " + key);
    }
    return NumberUtils.toDouble(p.getProperty(key));
  }

  public static int getIntValue(Properties p, String key)
      throws ServiceException {
    if (!p.containsKey(key)) {
      throw new ServiceException("Required parameter not found: " + key);
    }
    return NumberUtils.toInt(p.getProperty(key));
  }

  public static int getBooleanAsNumber(boolean b) {
    return b ? 1 : 0;
  }

}
