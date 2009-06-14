package neurocars.valueobj;

import java.text.NumberFormat;

import neurocars.utils.AppUtils;

/**
 * Konfigurace fyzikalnich parametru vozidla
 * 
 * @author Lukas Holcik
 * 
 */
public class CarSetup {

  private final double mass;
  private final double maxForwardSpeed;
  private final double maxBackwardSpeed;
  private final double enginePower;
  private final double brakePower;
  private final double steeringPower;
  private final double turnRange;

  public CarSetup(double mass, double maxForwardSpeed, double maxBackwardSpeed,
      double enginePower, double brakePower, double turnRange,
      double steeringPower) {
    super();
    this.mass = mass;
    this.maxForwardSpeed = maxForwardSpeed;
    this.maxBackwardSpeed = maxBackwardSpeed;
    this.turnRange = turnRange;
    this.brakePower = brakePower;
    this.enginePower = enginePower;
    this.steeringPower = steeringPower;
  }

  public String toString() {
    NumberFormat nf = AppUtils.getNumberFormat();

    return "[mass=" + nf.format(mass) + ";maxForwardSpeed="
        + nf.format(maxForwardSpeed) + ";maxBackwardSpeed="
        + nf.format(maxBackwardSpeed) + ";turnRange=" + nf.format(turnRange)
        + ";brakePower=" + nf.format(brakePower) + ";enginePower="
        + nf.format(enginePower) + ";steeringPower=" + nf.format(steeringPower)
        + "]";
  }

  public double getMass() {
    return mass;
  }

  public double getMaxForwardSpeed() {
    return maxForwardSpeed;
  }

  public double getMaxBackwardSpeed() {
    return maxBackwardSpeed;
  }

  public double getTurnRange() {
    return turnRange;
  }

  public double getBrakePower() {
    return brakePower;
  }

  public double getEnginePower() {
    return enginePower;
  }

  public double getSteeringPower() {
    return steeringPower;
  }

}
