package neurocars.valueobj;

import neurocars.entities.Car;

/**
 * Value objekt pro vysledky zavodu
 * 
 * @author Lukas Holcik
 * 
 */
public class RaceResult {

  private Car car;
  private long laps;
  private double total;
  private double min;
  private double max;
  private double avg;
  private double standardDeviation;

  public Car getCar() {
    return car;
  }

  public void setCar(Car car) {
    this.car = car;
  }

  public long getLaps() {
    return laps;
  }

  public void setLaps(long laps) {
    this.laps = laps;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  public double getMax() {
    return max;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public double getAvg() {
    return avg;
  }

  public void setAvg(double avg) {
    this.avg = avg;
  }

  public double getStandardDeviation() {
    return standardDeviation;
  }

  public void setStandardDeviation(double standardDeviation) {
    this.standardDeviation = standardDeviation;
  }

}
