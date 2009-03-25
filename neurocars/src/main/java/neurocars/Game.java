package neurocars;

import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import neurocars.controllers.KeyboardController;
import neurocars.entities.Car;
import neurocars.gui.IGUI;
import neurocars.gui.Java2DGUI;
import neurocars.utils.ServiceException;
import neurocars.valueobj.CarSetup;
import neurocars.valueobj.Scenario;
import neurocars.valueobj.TerrainSetup;
import neurocars.valueobj.Track;
import neurocars.valueobj.WayPoint;

import org.apache.log4j.Logger;

/**
 * Zakladni trida ovladanici logiku hry
 * 
 * @author Lukas Holcik
 * 
 */
public class Game {

  private static final Logger log = Logger.getLogger(Game.class);
  // delka herniho cyklu v ms
  private static final int CYCLE_DELAY = 50;

  // stavova/ladici hlaska
  private String statusMessage = "xxx";

  // pocitadlo cyklu - kvuli mereni casu
  private long cycleCounter;
  // ukoncovac programu
  private long finalCycle = Long.MAX_VALUE;

  private final IGUI gui;
  private final int xScreenSize;
  private final int yScreenSize;
  private final Track track;
  private final TerrainSetup terrain;
  private final List<Car> cars;
  private final int laps;

  // format desetinnych cisel
  private NumberFormat nf = new DecimalFormat("0.000");
  private DateFormat df = new SimpleDateFormat("m:ss.SSS");

  /**
   * Vytvori a inicializuje hru
   * 
   * @throws ServiceException
   */
  public Game(String scenarioFile) throws ServiceException {
    Scenario sc = new Scenario(this, scenarioFile);

    this.xScreenSize = sc.getXScreenSize();
    this.yScreenSize = sc.getYScreenSize();

    this.cars = sc.getCars();
    this.track = sc.getTrack();
    this.terrain = sc.getTerrain();
    this.laps = sc.getLaps();

    this.gui = new Java2DGUI(this);
  }

  /**
   * Vytvori a inicializuje hru
   * 
   * @throws ServiceException
   */
  public Game(int xScreenSize, int yScreenSize, Track track,
      TerrainSetup terrain, int laps) throws ServiceException {
    this.xScreenSize = xScreenSize;
    this.yScreenSize = yScreenSize;
    this.track = track;
    this.terrain = terrain;
    this.laps = laps;

    this.cars = new ArrayList<Car>();

    this.gui = new Java2DGUI(this);

    KeyboardController kc = new KeyboardController(KeyEvent.VK_UP,
        KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
    CarSetup setup = new CarSetup("cars/model1.properties");
    Car c = new Car(this, "player1", setup, kc);
    cars.add(c);
  }

  public void start() throws ServiceException {
    boolean[] keyboard = gui.getKeyboard();
    cycleCounter = 0;

    WayPoint start = getTrack().getWayPoints().get(0);

    int index = 0;
    int distance = 15;
    for (Car c : cars) {
      c.openReplayLog();

      c.setX(start.getX() + distance * Math.cos(Math.toRadians(index * 60)));
      c.setY(start.getY() + distance * Math.sin(Math.toRadians(index * 60)));
      index++;
    }

    gui.init();

    // long startTime = System.currentTimeMillis();
    long lastLoopTime = System.currentTimeMillis();

    try {
      while (!keyboard[KeyEvent.VK_ESCAPE] && cycleCounter < finalCycle) {
        long delta = System.currentTimeMillis() - lastLoopTime;
        lastLoopTime = System.currentTimeMillis();

        for (int i = 0; i < cars.size(); i++) {
          Car c = cars.get(i);
          // zpracovani vstupu
          c.processInput();

          // for (int j = i; j < cars.size(); j++) {
          // Car opponent = cars.get(j);
          // if (c.checkCollision(opponent)) {
          // c.collision(opponent);
          // }
          // }

          // zmena pozice
          c.updateLocation();

          if (c.getController() instanceof KeyboardController) {
            c.writeReplayEntry();
          }

          if (log.isDebugEnabled() && c.getId().endsWith("debug")) {
            statusMessage = "X=" + nf.format(c.getX()) + ";Y="
                + nf.format(c.getY()) + ";speed=" + nf.format(c.getSpeed())
                + ";angle=" + nf.format(c.getAngle()) + ";steeringWheel="
                + nf.format(c.getSteeringWheel()) + ";nextWayPoint="
                + c.getNextWayPoint() + ";lap=" + c.getLap() + ";lapTimes="
                + c.getLapTimes() + ";cycleTime=" + delta;
          }

        }

        gui.refresh();

        try {
          Thread.sleep(CYCLE_DELAY - delta);
        } catch (Exception e) {
        }

        cycleCounter++;
      }

      // zavod byl dokoncen
      if (cycleCounter >= finalCycle) {
        this.printWinners();
      }
    } finally {
      this.finish();
    }
  }

  private void finish() throws ServiceException {
    for (Car c : this.getCars()) {
      c.closeReplayLog();
    }
  }

  public void checkCarsFinished() {
    boolean finished = true;
    for (Car c : this.getCars()) {
      if (!c.isFinished()) {
        finished = false;
        break;
      }
    }

    if (finished) {
      finalCycle = cycleCounter + 50;
    }
  }

  public void printWinners() {
    long[] times = new long[cars.size()];

    for (int i = 0; i < cars.size(); i++) {
      Car c = cars.get(i);
      long time = 0;
      for (int j = 0; j < c.getLapTimes().size(); j++) {
        time += c.getLapTimes().get(j);
      }
      times[i] = time;

      Date d = new Date(time * CYCLE_DELAY);
      System.out.println(c.getId() + ": " + df.format(d));
    }

  }

  public int getXScreenSize() {
    return xScreenSize;
  }

  public int getYScreenSize() {
    return yScreenSize;
  }

  public List<Car> getCars() {
    return Collections.unmodifiableList(cars);
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

  public int getLaps() {
    return laps;
  }

  public static void main(final String[] args) {
    try {
      Game g = new Game("scenario/scenario1.properties");
      g.start();
    } catch (ServiceException e) {
      System.err.println("Fatal application exception: " + e.getMessage());
      e.printStackTrace();
    }

    System.exit(0);
  }

}
