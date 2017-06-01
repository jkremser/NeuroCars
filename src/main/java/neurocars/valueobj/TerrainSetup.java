package neurocars.valueobj;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Charakteristika terenu
 * 
 * @author Lukas Holcik
 * 
 */
public class TerrainSetup {

  private double friction;

  public static final Map<String, TerrainSetup> terrainTypes = new ConcurrentHashMap<String, TerrainSetup>();

  public static final TerrainSetup STANDARD = new TerrainSetup(0.5);

  public static final TerrainSetup ICE = new TerrainSetup(0.1);

  static {
    terrainTypes.put("standard", STANDARD);
    terrainTypes.put("ice", ICE);
  }

  public static TerrainSetup getTerrainType(String type) {
    return terrainTypes.get(type);
  }

  public TerrainSetup(double friction) {
    super();
    this.friction = friction;
  }

  public void setFriction(double friction) {
    this.friction = friction;
  }

  public double getFriction() {
    return friction;
  }

}
