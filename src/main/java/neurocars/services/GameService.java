package neurocars.services;

import java.text.NumberFormat;
import java.util.List;

import neurocars.entities.Car;
import neurocars.utils.AppUtils;
import neurocars.valueobj.RaceResult;

public class GameService extends ServiceBase {

  private static GameService instance = null;

  private GameService() {
    super();
  }

  public static GameService getInstance() {
    if (instance == null) {
      instance = new GameService();
    }
    return instance;
  }

  /**
   * Vypise vysledky zavodu
   * 
   * @param results
   */
  public void printResults(RaceResult[] results, List<Car> cars) {
    NumberFormat nf = AppUtils.getNumberFormat();

    for (int i = 0; i < results.length; i++) {
      RaceResult r = results[i];
      System.out.println("Car #" + (i + 1) + "[" + cars.get(i).getId()
          + "]: laps=" + r.getLaps() + ";total=" + r.getTotal() + "; max="
          + r.getMax() + "; min=" + r.getMin() + "; avg="
          + nf.format(r.getAvg()) + "; sd="
          + nf.format(r.getStandardDeviation()));
    }
  }

}
