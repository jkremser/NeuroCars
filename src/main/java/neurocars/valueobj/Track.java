package neurocars.valueobj;

import java.util.ArrayList;
import java.util.List;

/**
 * Value object popisujici zavodni trat
 * 
 * @author Lukas Holcik
 * 
 */
public class Track {

  private final String name;
  private final List<WayPoint> wayPoints = new ArrayList<WayPoint>();

  public Track(String name) {
    super();
    this.name = name;
  }

  public void addWayPoint(int x, int y, int size) {
    getWayPoints().add(new WayPoint(x, y, size));
  }

  public List<WayPoint> getWayPoints() {
    return wayPoints;
  }

  public String getName() {
    return name;
  }

}
