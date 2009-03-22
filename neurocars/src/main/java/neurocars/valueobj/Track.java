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

  private List<XY> wayPoints = new ArrayList<XY>();

  public Track() {
    super();
  }

  public void addWayPoint(int x, int y) {
    getWayPoints().add(new XY(x, y));
  }

  public List<XY> getWayPoints() {
    return wayPoints;
  }
}
