package neurocars.utils;

import junit.framework.TestCase;

public class AppUtilsTest extends TestCase {

  private static final double EPS = 1e-6;

  public void testGetAngle1() {
    assertEquals(Math.PI / 2, AppUtils.getAngle(0, 4, 3, 4, 0, 0), EPS);
    assertEquals(Math.PI / 2, AppUtils.getAngle(0, 4, -3, 4, 0, 0), EPS);
  }

  public void testGetAngle2() {
    assertEquals(Math.PI / 2, AppUtils.getAngle(3, 4, 5), EPS);
  }

  public void testGetAngleDeviation() {
    // [0,0] -> [1,0]
    assertEquals(-Math.PI / 2, AppUtils.getAngleDeviation(0, 0, 1, 0,
        Math.PI / 2), EPS);
    assertEquals(Math.PI / 2, AppUtils.getAngleDeviation(0, 0, 1, 0,
        -Math.PI / 2), EPS);
    assertEquals(-Math.PI, AppUtils.getAngleDeviation(0, 0, 1, 0, Math.PI), EPS);
    assertEquals(0, AppUtils.getAngleDeviation(0, 0, 1, 0, 0), EPS);

    // [0,0] -> [-1,0]
    assertEquals(Math.PI / 2, AppUtils.getAngleDeviation(0, 0, -1, 0,
        Math.PI / 2), EPS);
    assertEquals(-Math.PI / 2, AppUtils.getAngleDeviation(0, 0, -1, 0,
        -Math.PI / 2), EPS);
    assertEquals(-Math.PI, AppUtils.getAngleDeviation(0, 0, -1, 0, 0), EPS);
    assertEquals(0, AppUtils.getAngleDeviation(0, 0, -1, 0, Math.PI), EPS);
  }

}
