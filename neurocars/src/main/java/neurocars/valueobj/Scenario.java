package neurocars.valueobj;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import neurocars.Game;
import neurocars.controllers.Controller;
import neurocars.controllers.KeyboardController;
import neurocars.controllers.ReplayController;
import neurocars.entities.Car;
import neurocars.utils.AppUtils;
import neurocars.utils.ServiceException;

/**
 * Konfigurace herniho prostredi, zavodniku, trate ...
 * 
 * @author Lukas Holcik
 * 
 */
public class Scenario {

  public static final String CONTROLLER_KEYBOARD = "keyboard";
  public static final String CONTROLLER_REPLAY = "replay";

  private final int xScreenSize;
  private final int yScreenSize;
  private final int laps;
  private final Track track;
  private final List<Car> cars;
  private final TerrainSetup terrain;

  /**
   * Vytvori nastaveni pro danou hru z daneho konfiguracniho souboru
   * 
   * @param game
   * @param configFile
   * @throws ServiceException
   */
  public Scenario(Game game, String configFile) throws ServiceException {
    Properties p = new Properties();
    InputStream is = this.getClass().getClassLoader().getResourceAsStream(
        configFile);
    try {
      p.load(is);

      this.xScreenSize = AppUtils.getIntValue(p, "xscreensize");
      this.yScreenSize = AppUtils.getIntValue(p, "yscreensize");
      this.laps = AppUtils.getIntValue(p, "laps");

      this.cars = this.configCars(game, p);
      this.track = this.configTrack(p);
      this.terrain = this.configTerrain(p);
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  /**
   * Nakonfiguruje zavodniky
   * 
   * @param game
   * @param p
   * @return
   * @throws ServiceException
   */
  public List<Car> configCars(Game game, Properties p) throws ServiceException {
    List<Car> cars = new ArrayList<Car>();

    int index = 1;
    while (p.containsKey("car." + index + ".id")) {
      final String prefix = "car." + index;
      final String id = p.getProperty(prefix + ".id");
      final String modelFile = "cars/" + p.getProperty(prefix + ".setup")
          + ".properties";
      // MODEL
      final CarSetup model = new CarSetup(modelFile);

      // CONTROLLER
      final String controllerKey = prefix + ".controller";
      Controller c = null;
      if (CONTROLLER_KEYBOARD.equals(p.getProperty(controllerKey))) {
        // KeyboardController
        int up = AppUtils.getIntValue(p, controllerKey + ".accelerate");
        int down = AppUtils.getIntValue(p, controllerKey + ".brake");
        int left = AppUtils.getIntValue(p, controllerKey + ".left");
        int right = AppUtils.getIntValue(p, controllerKey + ".right");
        KeyboardController kc = new KeyboardController(up, down, left, right);
        c = kc;
      } else if (CONTROLLER_REPLAY.equals(p.getProperty(controllerKey))) {
        // ReplayController
        String replayFile = p.getProperty(controllerKey + ".file");

        ReplayController rc = new ReplayController(replayFile);
        c = rc;
      } else {
        throw new ServiceException("unknown value: " + controllerKey + "="
            + p.getProperty(controllerKey));
      }

      Car car = new Car(game, id, model, c);
      cars.add(car);

      index++;
    }
    return cars;
  }

  /**
   * Nakonfiguruje drahu
   * 
   * @param p
   * @return
   */
  public Track configTrack(Properties p) {
    // TODO
    Track track = new Track();
    int size = 100;
    track.addWayPoint(100, 100, size);
    track.addWayPoint(xScreenSize / 2, yScreenSize / 3, size);
    track.addWayPoint(xScreenSize - 100, 100, size);
    track.addWayPoint(xScreenSize * 4 / 5, yScreenSize * 2 / 3, size);
    track.addWayPoint(xScreenSize - 100, yScreenSize - 100, size);
    track.addWayPoint(xScreenSize / 5, yScreenSize - 100, size);
    track.addWayPoint(100, yScreenSize * 2 / 3, size);
    track.addWayPoint(100, 200, size);
    return track;
  }

  public TerrainSetup configTerrain(Properties p) {
    String terrain = p.getProperty("terrain");
    return TerrainSetup.getTerrainType(terrain);
  }

  public Track getTrack() {
    return track;
  }

  public List<Car> getCars() {
    return cars;
  }

  public int getXScreenSize() {
    return xScreenSize;
  }

  public int getYScreenSize() {
    return yScreenSize;
  }

  public TerrainSetup getTerrain() {
    return terrain;
  }

  public int getLaps() {
    return laps;
  }

}
