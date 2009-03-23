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

  private List<WayPoint> wayPoints = new ArrayList<WayPoint>();

  public Track() {
    super();
  }

  public void addWayPoint(int x, int y, int size) {
    getWayPoints().add(new WayPoint(x, y, size));
  }

  public List<WayPoint> getWayPoints() {
    return wayPoints;
  }
}
