package neurocars;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

import neurocars.controllers.NeuroController;
import neurocars.entities.Car;
import neurocars.gui.IGUI;
import neurocars.gui.NoGUI;
import neurocars.services.GUIService;
import neurocars.utils.AppUtils;
import neurocars.utils.ServiceException;
import neurocars.valueobj.RaceResult;
import neurocars.valueobj.Scenario;
import neurocars.valueobj.TerrainSetup;
import neurocars.valueobj.Track;
import neurocars.valueobj.WayPoint;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
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
  private String statusMessage = "On the road again, bejby!";

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
  private NumberFormat nf = AppUtils.getNumberFormat();

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

    this.gui = GUIService.getInstance().getGUI(sc.getGuiId(), this);
  }

  /**
   * Pro testovani neuronove site ... nastavi neuronovou sit jako ovladac vsech
   * aut
   * 
   * @param scenarioFile
   * @param nc
   * @throws ServiceException
   */
  public Game(String scenarioFile, NeuroController nc) throws ServiceException {
    this(scenarioFile);

    for (Car c : cars) {
      c.setController(nc);
      nc.setCar(c);
    }
  }

  /**
   * Spusti hru
   * 
   * @throws ServiceException
   */
  public RaceResult[] run() throws ServiceException {
    cycleCounter = 0;

    WayPoint start = getTrack().getWayPoints().get(0);
    WayPoint next = getTrack().getWayPoints().get(1);
    double startAngle = Math.atan2(next.getY() - start.getY(), next.getX()
        - start.getX());

    int index = 0;
    // int distance = 15;
    for (Car c : cars) {
      if (c.isSaveReplay()) {
        c.openReplayLog();
      }

      c.setX(start.getX());
      c.setY(start.getY());
      c.setAngle(startAngle);
      index++;
    }

    gui.init();

    // long startTime = System.currentTimeMillis();
    long lastLoopTime = System.currentTimeMillis();

    try {
      while (!gui.isEscapePressed() && cycleCounter < finalCycle) {
        long delta = System.currentTimeMillis() - lastLoopTime;
        lastLoopTime = System.currentTimeMillis();

        for (int i = 0; i < cars.size(); i++) {
          Car c = cars.get(i);
          // zpracovani vstupu
          c.processInput();

          // zmena pozice
          c.updateLocation();

          if (c.isSaveReplay()) {
            c.writeReplayEntry();
          }

          if (log.isDebugEnabled() && c.getId().endsWith("debug")
              && !(gui instanceof NoGUI)) {
            // statusMessage = "X=" + nf.format(c.getX()) + ";Y="
            // + nf.format(c.getY()) + ";speed=" + nf.format(c.getSpeed())
            // + ";angle=" + nf.format(c.getAngle()) + ";steeringWheel="
            // + nf.format(c.getSteeringWheel()) + ";nextWayPoint="
            // + c.getNextWayPoint() + ";lap=" + c.getLap() + ";lapTimes="
            // + c.getLapTimes() + ";cycleTime=" + delta;
            statusMessage = "nextWayPoint=" + c.getNextWayPoint() + ";lap="
                + c.getLap() + ";lapTimes=" + c.getLapTimes() + ";cycleTime="
                + delta;
          }
        }

        gui.refresh();

        if (gui.getCycleDelay() > 0) {
          try {
            Thread.sleep(gui.getCycleDelay() - delta);
          } catch (Exception e) {
          }
        }

        cycleCounter++;
      }

      RaceResult[] results = this.getResults();

      this.printResults(results);

      return results;
    } finally {
      this.cleanUp();
    }
  }

  private void cleanUp() throws ServiceException {
    for (Car c : this.getCars()) {
      if (c.isSaveReplay()) {
        c.closeReplayLog();
      }
    }
  }

  /**
   * Vygeneruje statisticky prehled vysledku
   * 
   * @return
   */
  private RaceResult[] getResults() {
    RaceResult[] results = new RaceResult[cars.size()];

    for (int i = 0; i < cars.size(); i++) {
      Car c = cars.get(i);
      DescriptiveStatistics ds = new DescriptiveStatistics();
      for (Long lap : c.getLapTimes()) {
        ds.addValue(lap);
      }
      RaceResult r = new RaceResult();
      r.setLaps(ds.getN());
      r.setTotal(ds.getSum());
      r.setMax(ds.getMax());
      r.setMin(ds.getMin());
      r.setAvg(ds.getMean());
      r.setStandardDeviation(ds.getStandardDeviation());
      results[i] = r;
    }

    return results;
  }

  /**
   * Zkontroluje, zda uz vsechna auta dokoncila zavod
   */
  public void checkCarsFinished() {
    boolean finished = true;
    for (Car c : this.getCars()) {
      if (!c.isFinished()) {
        finished = false;
        break;
      }
    }

    if (finished) {
      // necha hru jeste chvilku bezet a pak ji ukonci
      finalCycle = cycleCounter + 50;
    }
  }

  /**
   * Vypise vysledky zavodu
   * 
   * @param results
   */
  public void printResults(RaceResult[] results) {
    for (int i = 0; i < results.length; i++) {
      RaceResult r = results[i];
      System.out.println("Car #" + (i + 1) + "[" + cars.get(i).getId()
          + "]: laps=" + r.getLaps() + ";total=" + r.getTotal() + "; max="
          + r.getMax() + "; min=" + r.getMin() + "; avg="
          + nf.format(r.getAvg()) + "; sd="
          + nf.format(r.getStandardDeviation()));
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
    String scenario = (args.length == 1 ? args[0]
        : "./config/scenario/scenario-1player.properties");
    try {
      Game g = new Game(scenario);
      g.run();
    } catch (Exception e) {
      System.err.println("Fatal application exception: " + e.getMessage());
      e.printStackTrace();
    }

    System.exit(0);
  }
}
