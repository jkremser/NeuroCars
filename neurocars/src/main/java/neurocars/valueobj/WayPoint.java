package neurocars.valueobj;

/**
 * Jednoduchy value objekt definujici bod v rovine
 * 
 * @author Lukas Holcik
 * 
 */
public class WayPoint {

  private final int x;
  private final int y;
  private final int size;

  public WayPoint(int x, int y, int size) {
    super();
    this.x = x;
    this.y = y;
    this.size = size;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getSize() {
    return size;
  }

}
