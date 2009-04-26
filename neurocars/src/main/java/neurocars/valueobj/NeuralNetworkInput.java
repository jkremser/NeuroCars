package neurocars.valueobj;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Popis stavu auta - vstupni informace pro neuronovou sit
 * 
 * @author Lukas Holcik
 * 
 */
public class NeuralNetworkInput {

  private final DecimalFormat df = new DecimalFormat("0.0####",
      new DecimalFormatSymbols(Locale.US));

  // rychlost auta
  private double speed;
  // natoceni volantu
  private double steeringWheel;

  // uhel k dalsim bodum cesty - odkloneni od meho smeru
  private double wayPointAngle[];
  // vzdalenost k dalsim bodum cesty
  private double wayPointDistance[];
  // jak daleko musim projet od dalsich bodu trasy (velikost way-pointu)
  private int wayPointSize[];

  // uhel k nejblizsimu oponentovi
  private double angleToOpponent;
  // vzdalenost k nejblizsimu oponentovi
  private double distanceToOpponent;

  // smer nejblizsiho oponenta - odkloneni od meho smeru
  private double opponentAngle;
  // rychlost oponenta
  private double opponentVelocity;

  /**
   * Zkonstruuje zaznam pro replay/vstup pro neuronovou sit
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(df.format(speed) + ";");
    sb.append(df.format(steeringWheel) + ";");
    for (int i = 0; i < wayPointDistance.length; i++) {
      sb.append(df.format(wayPointDistance[i]) + ";");
      sb.append(df.format(wayPointAngle[i]) + ";");
      sb.append(df.format(wayPointSize[i]) + ";");
    }

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

  public double[] getWayPointAngle() {
    return wayPointAngle;
  }

  public void setWayPointAngle(double[] angleToNextPoint) {
    this.wayPointAngle = angleToNextPoint;
  }

  public double[] getWayPointDistance() {
    return wayPointDistance;
  }

  public void setWayPointDistance(double[] distanceToNextPoint) {
    this.wayPointDistance = distanceToNextPoint;
  }

  public double getAngleToOpponent() {
    return angleToOpponent;
  }

  public void setAngleToOpponent(double angleToOpponent) {
    this.angleToOpponent = angleToOpponent;
  }

  public double getDistanceToOpponent() {
    return distanceToOpponent;
  }

  public void setDistanceToOpponent(double distanceToOpponent) {
    this.distanceToOpponent = distanceToOpponent;
  }

  public double getOpponentAngle() {
    return opponentAngle;
  }

  public void setOpponentAngle(double opponentAngle) {
    this.opponentAngle = opponentAngle;
  }

  public double getOpponentVelocity() {
    return opponentVelocity;
  }

  public void setOpponentVelocity(double opponentVelocity) {
    this.opponentVelocity = opponentVelocity;
  }

  public void setWayPointSize(int wayPointSize[]) {
    this.wayPointSize = wayPointSize;
  }

  public int[] getWayPointSize() {
    return wayPointSize;
  }

}
