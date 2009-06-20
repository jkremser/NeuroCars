package neurocars.valueobj;

/**
 * Vystup neuronove site
 * 
 * @author Lukas Holcik
 * 
 */
public class NeuralNetworkOutput {

  private double speed;
  private double turn;

  public String toString() {
    return "[speed=" + speed + ";turn=" + turn + "]";
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public double getTurn() {
    return turn;
  }

  public void setTurn(double turn) {
    this.turn = turn;
  }
}
