package neurocars.entities;

import neurocars.Game;
import neurocars.controllers.IController;
import neurocars.utils.AngleUtils;
import neurocars.valueobj.VehicleSetup;
import neurocars.valueobj.XY;

public class Vehicle extends Entity {

  private final String id;
  private final IController controller;
  private final VehicleSetup model;

  private double angle; // uhel natoceni auta - rad

  private double steeringWheel; // -1 .. 1 - natoceni volantu
  private double speed; // rychlost motoru

  private int nextWayPoint;

  private double nextWayPointDistance;

  public Vehicle(Game game, String id, VehicleSetup carModel,
      IController controller) {
    super(game);
    this.id = id;
    this.model = carModel;
    this.controller = controller;
  }

  public void input() {
    // zrychleni
    if (controller.accelerate()) {
      double speed = getSpeed();
      speed += model.getEnginePower();
      if (speed > model.getMaxForwardSpeed()) {
        speed = model.getMaxForwardSpeed();
      }
      setSpeed(speed);
    }

    // brzda
    if (controller.brake()) {
      double speed = getSpeed();
      speed -= model.getBrakePower();
      if (speed < -model.getMaxBackwardSpeed()) {
        speed = -model.getMaxBackwardSpeed();
      }
      setSpeed(speed);
    }

    // zataceni
    if (controller.right() ^ controller.left()) { // XOR
      double steeringWheel = getSteeringWheel();
      double k = (controller.right() ? +1 : -1);
      steeringWheel += k * model.getSteeringPower();
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
      if (Math.abs(steeringWheel) < model.getSteeringPower() * koef) {
        steeringWheel = 0;
      } else {
        double k = -Math.signum(steeringWheel);
        steeringWheel += k * model.getSteeringPower() * koef;
      }
      setSteeringWheel(steeringWheel);
    }
  }

  public void update() {
    double angle = getAngle() + steeringWheel * model.getTurnRange();
    angle = AngleUtils.normalizeAngle(angle);
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
  }

  public void updateWayPoint(XY wayPoint) {
    nextWayPointDistance = Math.sqrt(Math.pow(getX() - wayPoint.getX(), 2)
        + Math.pow(getY() - wayPoint.getY(), 2));
    // TODO: distance
    if (nextWayPointDistance <= 50) {
      this.nextWayPoint++;
      this.nextWayPoint %= getGame().getTrack().getWayPoints().size();
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
    if (velocity > model.getMaxForwardSpeed()
        || velocity < -model.getMaxBackwardSpeed()) {
      throw new IllegalArgumentException("" + velocity);
    }
    this.speed = velocity;
  }

  public VehicleSetup getModel() {
    return model;
  }

  public int getNextWayPoint() {
    return nextWayPoint;
  }

}
