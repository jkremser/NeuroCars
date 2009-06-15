package neurocars.valueobj;

import java.text.NumberFormat;

import neurocars.utils.AppUtils;

/**
 * Popis stavu auta - vstupni informace pro neuronovou sit
 * 
 * @author Lukas Holcik
 * 
 */
public class NeuralNetworkInput {

  private final NumberFormat df = AppUtils.getNumberFormat();

  // rychlost auta
  private double speed;
  // natoceni volantu
  private double steeringWheel;

  // uhel k dalsim bodum cesty - odkloneni od meho smeru
  private double wayPointAngle;
  // vzdalenost k dalsim bodum cesty
  private double wayPointDistance;
  // uhel v zatacce
  private double curveAngle;

  /**
   * Zkonstruuje zaznam pro replay/vstup pro neuronovou sit
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(df.format(speed) + ";");
    // sb.append(df.format(steeringWheel) + ";");
    sb.append(df.format(wayPointDistance) + ";");
    sb.append(df.format(wayPointAngle) + ";");
    sb.append(df.format(curveAngle) + ";");

    return sb.toString();
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double velocity) {
    this.speed = velocity;
  }

  public double getSteeringWheel() {
    return steeringWheel;
  }

  public void setSteeringWheel(double steeringWheel) {
    this.steeringWheel = steeringWheel;
  }

  public void setCurveAngle(double curveAngle) {
    this.curveAngle = curveAngle;
  }

  public double getCurveAngle() {
    return curveAngle;
  }

  public double getWayPointAngle() {
    return wayPointAngle;
  }

  public void setWayPointAngle(double wayPointAngle) {
    this.wayPointAngle = wayPointAngle;
  }

  public double getWayPointDistance() {
    return wayPointDistance;
  }

  public void setWayPointDistance(double wayPointDistance) {
    this.wayPointDistance = wayPointDistance;
  }

}
