package neurocars.valueobj;

import neurocars.utils.AngleUtils;

/**
 * Konfigurace fyzikalnich parametru vozidla
 * 
 * @author Lukas Holcik
 * 
 */
public class VehicleSetup {

  private double mass;
  private double maxForwardSpeed;
  private double maxBackwardSpeed;
  private double enginePower;
  private double brakePower;
  private double steeringPower;
  private double turnRange;

  public static final VehicleSetup STANDARD = new VehicleSetup(10, // mass
      15, // maxForwardSpeed
      0, // maxBackwardSpeed
      0.3, // enginePower
      0.5, // brakePower
      AngleUtils.DEGREE * 20, // turnRange
      0.03 // steeringSpeed
  );

  public static final VehicleSetup SLOW = new VehicleSetup(10, // mass
      5, // maxForwardSpeed
      0, // maxBackwardSpeed
      0.1, // enginePower
      0.15, // brakePower
      AngleUtils.DEGREE * 20, // turnRange
      0.03 // steeringSpeed
  );

  public VehicleSetup() {
    super();
  }

  public VehicleSetup(double mass, double maxForwardVelocity,
      double maxBackwardVelocity, double enginePower, double brakePower,
      double turnRange, double steeringSpeed) {
    super();
    this.mass = mass;
    this.maxForwardSpeed = maxForwardVelocity;
    this.maxBackwardSpeed = maxBackwardVelocity;
    this.turnRange = turnRange;
    this.brakePower = brakePower;
    this.enginePower = enginePower;
    this.steeringPower = steeringSpeed;
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
