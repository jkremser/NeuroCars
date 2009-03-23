package neurocars.valueobj;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import neurocars.utils.AppUtils;
import neurocars.utils.ServiceException;

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

  public static final CarSetup STANDARD = new CarSetup(10, // mass
      15, // maxForwardSpeed
      0, // maxBackwardSpeed
      0.3, // enginePower
      0.5, // brakePower
      20, // turnRange
      0.03 // steeringSpeed
  );

  public static final CarSetup SLOW = new CarSetup(10, // mass
      5, // maxForwardSpeed
      0, // maxBackwardSpeed
      0.1, // enginePower
      0.15, // brakePower
      20, // turnRange
      0.03 // steeringSpeed
  );

  /**
   * Nacte konfiguraci ze souboru
   * 
   * @param file
   * @throws ServiceException
   */
  public CarSetup(String file) throws ServiceException {
    Properties p = new Properties();
    InputStream is = getClass().getClassLoader().getResourceAsStream(file);
    if (is == null) {
      throw new ServiceException("File not found: " + file);
    }
    try {
      p.load(is);

      this.mass = AppUtils.getDoubleValue(p, "mass");
      this.maxForwardSpeed = AppUtils.getDoubleValue(p, "maxForwardSpeed");
      this.maxBackwardSpeed = AppUtils.getDoubleValue(p, "maxBackwardSpeed");
      this.turnRange = AppUtils.DEGREE
          * AppUtils.getDoubleValue(p, "turnRange");
      this.brakePower = AppUtils.getDoubleValue(p, "brakePower");
      this.enginePower = AppUtils.getDoubleValue(p, "enginePower");
      this.steeringPower = AppUtils.getDoubleValue(p, "steeringPower");
    } catch (IOException e) {
      throw new ServiceException();
    }
  }

  public CarSetup(double mass, double maxForwardVelocity,
      double maxBackwardVelocity, double enginePower, double brakePower,
      double turnRange, double steeringSpeed) {
    super();
    this.mass = mass;
    this.maxForwardSpeed = maxForwardVelocity;
    this.maxBackwardSpeed = maxBackwardVelocity;
    this.turnRange = AppUtils.DEGREE * turnRange;
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
