package neurocars.valueobj;

/**
 * Jednoduchy value objekt definujici bod v rovine
 * 
 * @author Lukas Holcik
 * 
 */
public class XY {

  private double x;
  private double y;

  public XY(double x, double y) {
    super();
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

}
