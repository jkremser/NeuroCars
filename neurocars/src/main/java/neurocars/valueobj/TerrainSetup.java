package neurocars.valueobj;

/**
 * Charakteristika terenu
 * 
 * @author Lukas Holcik
 * 
 */
public class TerrainSetup {

  private double friction;

  public static final TerrainSetup STANDARD = new TerrainSetup(1);

  public static final TerrainSetup ICE = new TerrainSetup(0.1);

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
