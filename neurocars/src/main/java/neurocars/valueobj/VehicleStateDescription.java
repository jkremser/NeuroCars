package neurocars.valueobj;

/**
 * Popis stavu auta - vstupni informace pro neuronovou sit
 * 
 * @author Lukas Holcik
 * 
 */
public class VehicleStateDescription {

  // rychlost auta
  private double velocity;
  // natoceni volantu
  private double steeringWheel;

  // uhel k dalsim bodum cesty - odkloneni od meho smeru
  private double angleToNextPoint[];
  // vzdalenost k dalsim bodum cesty
  private double distanceToNextPoint[];

  // uhel k nejblizsimu oponentovi
  private double angleToOpponent;
  // vzdalenost k nejblizsimu oponentovi
  private double distanceToOpponent;

  // smer nejblizsiho oponenta - odkloneni od meho smeru
  private double opponentAngle;
  // rychlost oponenta
  private double opponentVelocity;

  public double getVelocity() {
    return velocity;
  }

  public void setVelocity(double velocity) {
    this.velocity = velocity;
  }

  public double getSteeringWheel() {
    return steeringWheel;
  }

  public void setSteeringWheel(double steeringWheel) {
    this.steeringWheel = steeringWheel;
  }

  public double[] getAngleToNextPoint() {
    return angleToNextPoint;
  }

  public void setAngleToNextPoint(double[] angleToNextPoint) {
    this.angleToNextPoint = angleToNextPoint;
  }

  public double[] getDistanceToNextPoint() {
    return distanceToNextPoint;
  }

  public void setDistanceToNextPoint(double[] distanceToNextPoint) {
    this.distanceToNextPoint = distanceToNextPoint;
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

}
