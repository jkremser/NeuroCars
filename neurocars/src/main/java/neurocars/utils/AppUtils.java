package neurocars.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
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

  public static NumberFormat getNumberFormat() {
    return new DecimalFormat("0.0#####", new DecimalFormatSymbols(Locale.US));
  }

  public static double getAngle(double x1, double y1, double x2, double y2,
      double x3, double y3) {
    double a = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    double b = Math.sqrt(Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2));
    double c = Math.sqrt(Math.pow(x2 - x3, 2) + Math.pow(y2 - y3, 2));

    return getAngle(a, b, c);
  }

  /**
   * Vypocita uhel protilehly strane c
   * 
   * @param a
   * @param b
   * @param c
   * @return
   */
  public static double getAngle(double a, double b, double c) {
    return Math.acos((a * a + b * b - c * c) / (2 * a * b));
  }
}
