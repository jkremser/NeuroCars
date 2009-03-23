package neurocars.entities;

import java.util.ArrayList;
import java.util.List;

import neurocars.Game;
import neurocars.controllers.IController;
import neurocars.utils.AppUtils;
import neurocars.valueobj.CarSetup;
import neurocars.valueobj.WayPoint;

/**
 * API auta
 * 
 * @author Lukas Holcik
 * 
 */
public class Car extends Entity {

  private final String id;
  private final IController controller;
  private final CarSetup setup;

  private double angle; // uhel natoceni auta - rad

  private double steeringWheel; // -1 .. 1 - natoceni volantu
  private double speed; // rychlost motoru

  private int nextWayPoint;

  private double nextWayPointDistance;

  private int lap;
  private long lapStartTime;
  private List<Long> lapTimes = new ArrayList<Long>();

  public Car(Game game, String id, CarSetup setup, IController controller) {
    super(game);
    this.id = id;
    this.setup = setup;
    this.controller = controller;
  }

  /**
   * Vyhodnoti vstup z controlleru
   */
  public void processInput() {
    // zrychleni
    if (controller.accelerate()) {
      double speed = getSpeed();
      speed += setup.getEnginePower();
      if (speed > setup.getMaxForwardSpeed()) {
        speed = setup.getMaxForwardSpeed();
      }
      setSpeed(speed);
    }

    // brzda
    if (controller.brake()) {
      double speed = getSpeed();
      speed -= setup.getBrakePower();
      if (speed < -setup.getMaxBackwardSpeed()) {
        speed = -setup.getMaxBackwardSpeed();
      }
      setSpeed(speed);
    }

    // zataceni
    if (controller.right() ^ controller.left()) { // XOR
      double steeringWheel = getSteeringWheel();
      double k = (controller.right() ? +1 : -1);
      steeringWheel += k * setup.getSteeringPower();
      if (steeringWheel > 1.0) {
        steeringWheel = 1.0;
      }
      if (steeringWheel < -1.0) {
        steeringWheel = -1.0;
      }
      setSteeringWheel(steeringWheel);
    } else {
      // volant se vraci do puvodni polohy
      double steeringWheel = getSteeringWheel();
      // TODO: koef
      final double koef = 6.0;
      if (Math.abs(steeringWheel) < setup.getSteeringPower() * koef) {
        steeringWheel = 0;
      } else {
        double k = -Math.signum(steeringWheel);
        steeringWheel += k * setup.getSteeringPower() * koef;
      }
      setSteeringWheel(steeringWheel);
    }
  }

  /**
   * Prepocita rychlosti, souradnice, atd ...
   */
  public void updateLocation() {
    double angle = getAngle() + steeringWheel * setup.getTurnRange();
    angle = AppUtils.normalizeAngle(angle);
    setAngle(angle);

    double ddx = getSpeed() * Math.cos(angle);
    double ddy = getSpeed() * Math.sin(angle);

    double friction = getGame().getTerrain().getFriction();
    if (Math.abs(vx - ddx) <= friction) {
      vx = ddx;
    } else {
      vx += Math.signum(ddx - this.vx) * friction;
    }

    if (Math.abs(vy - ddy) <= friction) {
      vy = ddy;
    } else {
      vy += Math.signum(ddy - this.vy) * friction;
    }

    if (x + vx < 0 || x + vx > game.getXScreenSize()) {
      vx = -vx;
    }
    if (y + vy < 0 || y + vy > game.getYScreenSize()) {
      vy = -vy;
    }

    x += vx;
    y += vy;

    this.updateWayPoint();
  }

  /**
   * Prepocita informace o nasledujicim bode trasy
   */
  public void updateWayPoint() {
    WayPoint wayPoint = game.getTrack().getWayPoints().get(nextWayPoint);

    // prepocitani vzdalenosti
    nextWayPointDistance = Math.sqrt(Math.pow(getX() - wayPoint.getX(), 2)
        + Math.pow(getY() - wayPoint.getY(), 2));
    if (nextWayPointDistance <= wayPoint.getSize() / 2) {
      nextWayPoint++;
      if (nextWayPoint >= getGame().getTrack().getWayPoints().size()) {
        nextWayPoint = 0;
      }
      // smeruju na druhy bod trate ... zacal jsem dalsi kolo
      if (this.nextWayPoint == 1) {
        lap++;
        if (lap > 1) {
          lapTimes.add(game.getCycleCounter() - lapStartTime);
        }
        lapStartTime = game.getCycleCounter();
      }
    }
  }

  public String getId() {
    return id;
  }

  public IController getController() {
    return controller;
  }

  public double getAngle() {
    return angle;
  }

  private void setAngle(double angle) {
    this.angle = angle;
  }

  public double getSteeringWheel() {
    return steeringWheel;
  }

  private void setSteeringWheel(double steeringWheel) {
    if (steeringWheel < -1 || steeringWheel > 1) {
      throw new IllegalArgumentException("" + steeringWheel);
    }
    this.steeringWheel = steeringWheel;
  }

  public double getSpeed() {
    return speed;
  }

  private void setSpeed(double velocity) {
    if (velocity > setup.getMaxForwardSpeed()
        || velocity < -setup.getMaxBackwardSpeed()) {
      throw new IllegalArgumentException("" + velocity);
    }
    this.speed = velocity;
  }

  public CarSetup getSetup() {
    return setup;
  }

  public int getNextWayPoint() {
    return nextWayPoint;
  }

  public int getLap() {
    return lap;
  }

  public long getLapStartTime() {
    return lapStartTime;
  }

  public List<Long> getLapTimes() {
    return lapTimes;
  }

}
