package neurocars;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import neurocars.controllers.KeyboardController;
import neurocars.entities.Car;
import neurocars.gui.IGUI;
import neurocars.gui.Java2DGUI;
import neurocars.utils.ServiceException;
import neurocars.valueobj.CarSetup;
import neurocars.valueobj.TerrainSetup;
import neurocars.valueobj.Track;

import org.apache.log4j.Logger;

/**
 * Zakladni trida ovladanici logiku hry
 * 
 * @author Lukas Holcik
 * 
 */
public class Game {

  private static final Logger log = Logger.getLogger(Game.class);

  // stavova/ladici hlaska
  private String statusMessage = "xxx";

  // pocitadlo cyklu - kvuli mereni casu
  private long cycleCounter;

  private final IGUI gui;
  private final int xScreenSize;
  private final int yScreenSize;
  private final Track track;
  private final TerrainSetup terrain;
  private final Set<Car> cars;

  // format desetinnych cisel
  private NumberFormat nf = new DecimalFormat("0.000");

  /**
   * Construct our game and set it running.
   * 
   * @throws ServiceException
   */
  public Game(int xScreenSize, int yScreenSize, Track track,
      TerrainSetup terrain) throws ServiceException {
    this.xScreenSize = xScreenSize;
    this.yScreenSize = yScreenSize;
    this.track = track;
    this.terrain = terrain;

    this.cars = new HashSet<Car>();

    this.gui = new Java2DGUI(this);

    KeyboardController kc = new KeyboardController(gui.getKeyboard(),
        KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
    CarSetup setup = new CarSetup("cars/model1.properties");
    Car c = new Car(this, "player1", setup, kc);

    // TODO inicializace pozice hracu
    c.setX(track.getWayPoints().get(0).getX());
    c.setY(track.getWayPoints().get(0).getY());

    this.cars.add(c);
  }

  public void start() {
    // long lastLoopTime = System.currentTimeMillis();
    boolean[] keyboard = gui.getKeyboard();
    cycleCounter = 0;

    gui.init();

    while (!keyboard[KeyEvent.VK_ESCAPE]) {
      // long delta = System.currentTimeMillis() - lastLoopTime;
      // lastLoopTime = System.currentTimeMillis();

      for (Car c : cars) {
        // zpracovani vstupu
        c.processInput();
        // zmena pozice
        c.updateLocation();

        if (log.isDebugEnabled()) {
          statusMessage = "X=" + nf.format(c.getX()) + ";Y="
              + nf.format(c.getY()) + ";speed=" + nf.format(c.getSpeed())
              + ";angle=" + nf.format(c.getAngle()) + ";steeringWheel="
              + nf.format(c.getSteeringWheel()) + ";nextWayPoint="
              + c.getNextWayPoint() + ";lap=" + c.getLap() + ";lapTimes="
              + c.getLapTimes();
        }
      }

      gui.refresh();

      try {
        Thread.sleep(10);
      } catch (Exception e) {
      }
      cycleCounter++;
    }
  }

  public int getXScreenSize() {
    return xScreenSize;
  }

  public int getYScreenSize() {
    return yScreenSize;
  }

  public Set<Car> getCars() {
    return Collections.unmodifiableSet(cars);
  }

  public Track getTrack() {
    return track;
  }

  public TerrainSetup getTerrain() {
    return terrain;
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  public long getCycleCounter() {
    return cycleCounter;
  }

  public static void main(final String[] args) {
    final int xScreenSize = 1000;
    final int yScreenSize = 700;

    Track track = createDemoTrack(xScreenSize, yScreenSize);
    TerrainSetup terrain = TerrainSetup.ICE;

    try {
      Game g = new Game(xScreenSize, yScreenSize, track, terrain);
      g.start();
    } catch (ServiceException e) {
      System.err.println("Fatal application exception: " + e.getMessage());
      e.printStackTrace();
    }

    System.exit(0);
    // }
    //
    // });
  }

  /**
   * Vytvori drahu
   * 
   * @return
   */
  public static Track createDemoTrack(int xScreenSize, int yScreenSize) {
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

}
