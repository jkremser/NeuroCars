package neurocars.valueobj;

/**
 * Vystup neuronove site
 * 
 * @author Lukas Holcik
 * 
 */
public class NeuralNetworkOutput {

  private boolean accelerate;
  private boolean brake;
  private boolean left;
  private boolean right;

  public boolean isAccelerate() {
    return accelerate;
  }

  public void setAccelerate(boolean accelerate) {
    this.accelerate = accelerate;
  }

  public boolean isBrake() {
    return brake;
  }

  public void setBrake(boolean brake) {
    this.brake = brake;
  }

  public boolean isLeft() {
    return left;
  }

  public void setLeft(boolean left) {
    this.left = left;
  }

  public boolean isRight() {
    return right;
  }

  public void setRight(boolean right) {
    this.right = right;
  }

}
