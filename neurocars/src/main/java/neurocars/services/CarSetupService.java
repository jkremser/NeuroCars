package neurocars.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import neurocars.utils.AppUtils;
import neurocars.utils.ServiceException;
import neurocars.valueobj.CarSetup;

/**
 * Sluzba na praci s fyzikalnimi modely auta
 * 
 * @author Lukas Holcik
 * 
 */
public class CarSetupService extends ServiceBase {

  private static CarSetupService instance = null;

  private CarSetupService() {
    super();
  }

  public static CarSetupService getInstance() {
    if (instance == null) {
      instance = new CarSetupService();
    }
    return instance;
  }

  /**
   * Nacte konfiguraci ze souboru
   * 
   * @param file
   * @throws ServiceException
   */
  public CarSetup readCarSetup(String file) throws ServiceException {
    Properties p = new Properties();
    InputStream is = getClass().getClassLoader().getResourceAsStream(file);
    if (is == null) {
      throw new ServiceException("File not found: " + file);
    }
    try {
      p.load(is);

      double mass = AppUtils.getDoubleValue(p, "mass");
      double maxForwardSpeed = AppUtils.getDoubleValue(p, "maxForwardSpeed");
      double maxBackwardSpeed = AppUtils.getDoubleValue(p, "maxBackwardSpeed");
      double turnRange = Math.toRadians(AppUtils.getDoubleValue(p, "turnRange"));
      double brakePower = AppUtils.getDoubleValue(p, "brakePower");
      double enginePower = AppUtils.getDoubleValue(p, "enginePower");
      double steeringPower = AppUtils.getDoubleValue(p, "steeringPower");

      CarSetup s = new CarSetup(mass, maxForwardSpeed, maxBackwardSpeed,
          enginePower, brakePower, turnRange, steeringPower);

      return s;
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }
}
