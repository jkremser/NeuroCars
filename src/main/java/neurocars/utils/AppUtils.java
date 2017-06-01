package neurocars.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.math.util.MathUtils;

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

  /**
   * Odchylka uhlu mezi dvema body od zadaneho uhlu
   * 
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @param angle
   * @return
   */
  public static double getAngleDeviation(double x1, double y1, double x2,
      double y2, double angle) {
    return MathUtils.normalizeAngle(Math.atan2(y2 - y1, x2 - x1) - angle, 0);
  }

  /**
   * Uhel trojuhelnika ve vrcholu [x1,y1]
   * 
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @param x3
   * @param y3
   * @return
   */
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

  public static String doublesToString(Collection<Double> c) {
    StringBuffer result = new StringBuffer("[");
    for (Iterator<Double> i = c.iterator(); i.hasNext();) {
      result.append(getNumberFormat().format(i.next()));
      if (i.hasNext()) {
        result.append(", ");
      }
    }

    result.append("]");
    return result.toString();
  }
}
