package neurocars.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import neurocars.Game;
import neurocars.controllers.Controller;
import neurocars.utils.AppUtils;
import neurocars.utils.ServiceException;
import neurocars.valueobj.CarSetup;
import neurocars.valueobj.NeuralNetworkInput;
import neurocars.valueobj.WayPoint;

import org.apache.commons.math.util.MathUtils;

/**
 * API auta
 * 
 * @author Lukas Holcik
 * 
 */
public class Car extends Entity {

  private final String id;
  private Controller controller;
  private final CarSetup setup;

  private double angle; // uhel natoceni auta - rad

  private double steeringWheel; // -1 .. 1 - natoceni volantu
  private double speed; // rychlost motoru

  private int nextWayPoint;
  private double nextWayPointDistance;

  private final boolean saveReplay;
  private BufferedWriter replayLog;

  // cislo aktualniho kola
  private int lap;
  // cas zacatku kola
  private long lapStartTime;
  // casy za jednotliva kola
  private List<Long> lapTimes = new ArrayList<Long>();
  // dokoncil zavod
  private boolean finished = false;

  public Car(Game game, String id, CarSetup setup, Controller controller,
      boolean saveReplay) {
    super(game);

    if (id == null) {
      throw new IllegalArgumentException("id=null");
    }
    if (setup == null) {
      throw new IllegalArgumentException("setup=null");
    }
    if (controller == null) {
      throw new IllegalArgumentException("controller=null");
    }

    this.id = id;
    this.setup = setup;
    this.controller = controller;
    this.saveReplay = saveReplay;
  }

  /**
   * Vyhodnoti vstup z controlleru
   * 
   * @throws ServiceException
   */
  public void processInput() throws ServiceException {
    controller.next(this);

    if (finished) {
      // jestli jsem dojel, tak uz jenom zabrzdim
      this.brake();
    } else {
      // jinak muzu jeste jet

      // zrychleni
      if (controller.accelerate()) {
        this.accelerate();
      }

      // brzda
      if (controller.brake()) {
        this.brake();
      }
    }

    // zataceni
    if (controller.right() ^ controller.left()) { // XOR
      double steeringWheel = getSteeringWheel();
      double k = (controller.right() ? +1 : -1);
      steeringWheel += k * setup.getSteeringPower() / (speed > 1 ? speed : 1);
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
    angle = MathUtils.normalizeAngle(angle, Math.PI);
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
   * Zrychleni
   */
  private void accelerate() {
    double speed = getSpeed();
    speed += setup.getEnginePower();
    if (speed > setup.getMaxForwardSpeed()) {
      speed = setup.getMaxForwardSpeed();
    }
    setSpeed(speed);
  }

  /**
   * Brzda
   */
  private void brake() {
    double speed = getSpeed();
    speed -= setup.getBrakePower();
    if (speed < -setup.getMaxBackwardSpeed()) {
      speed = -setup.getMaxBackwardSpeed();
    }
    setSpeed(speed);
  }

  public void openReplayLog() throws ServiceException {
    if (!saveReplay) {
      return;
    }
    if (replayLog != null) {
      throw new ServiceException("Log already opened!");
    }
    try {
      File logFile = new File(id + "_replay.txt");
      replayLog = new BufferedWriter(new FileWriter(logFile));
      replayLog.write(getSetup().toString() + "\n");
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  public void writeReplayEntry() throws ServiceException {
    if (replayLog == null) {
      throw new ServiceException("Log not opened!");
    }
    // nezapisujeme nic pokud se nehybu a nic nemackam nebo zavod je ukoncen
    if ((getSpeed() == 0 && (controller.accelerate() || controller.brake()
        || controller.left() || controller.right()) == false)
        || isFinished()) {
      return;
    }

    try {
      replayLog.write(controller.toString() + ";"
          + getNeuralNetworkInput().toString());
      replayLog.newLine();
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  public void closeReplayLog() throws ServiceException {
    if (!saveReplay) {
      return;
    }
    try {
      if (replayLog != null) {
        replayLog.close();
      }
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  /**
   * Prepocita informace o nasledujicim bodu trasy
   */
  private void updateWayPoint() {
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
        if (lap > game.getLaps()) {
          finished = true;
          game.checkCarsFinished();
        }
      }
    }
  }

  public NeuralNetworkInput getNeuralNetworkInput() {
    NeuralNetworkInput d = new NeuralNetworkInput();

    // procentualni ... <0,1>
    d.setSpeed(speed / getSetup().getMaxForwardSpeed());
    d.setSteeringWheel(steeringWheel);

    List<WayPoint> wayPoints = game.getTrack().getWayPoints();

    WayPoint wp1 = wayPoints.get(nextWayPoint);

    double wayPointAngle = AppUtils.getAngleDeviation(x, y, wp1.getX(),
        wp1.getY(), angle);
    double wayPointDistance = Math.sqrt(Math.pow(wp1.getX() - x, 2)
        + Math.pow(wp1.getY() - y, 2))
        - wp1.getSize() / 2;

    // normalizuju vzdalenost do intervalu <0,20>, protoze ty vstupy pak
    // jsou pomerne hodne nepomerne (<0,1> X <0,1###>)
    wayPointDistance = ((wayPointDistance / 1000) * 20);

    d.setWayPointAngle(wayPointAngle);
    d.setWayPointDistance(wayPointDistance);

    WayPoint wp2 = wayPoints.get((nextWayPoint + 1)
        % getGame().getTrack().getWayPoints().size());
    double wayPointAngle2 = AppUtils.getAngleDeviation(x, y, wp2.getX(),
        wp2.getY(), angle);
    double curveAngle = MathUtils.normalizeAngle(AppUtils.getAngle(wp1.getX(),
        wp1.getY(), wp2.getX(), wp2.getY(), this.getX(), this.getY()), 0);
    curveAngle *= Math.signum(MathUtils.normalizeAngle(wayPointAngle
        - wayPointAngle2, 0));

    d.setCurveAngle(curveAngle);

    return d;
  }

  public String getId() {
    return id;
  }

  public Controller getController() {
    return controller;
  }

  public double getAngle() {
    return angle;
  }

  public void setAngle(double angle) {
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

  public boolean isFinished() {
    return finished;
  }

  public boolean isSaveReplay() {
    return saveReplay;
  }

  public void setController(Controller controller) {
    this.controller = controller;
  }

}
