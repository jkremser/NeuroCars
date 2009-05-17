package neurocars.gui.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import neurocars.entities.Car;
import neurocars.valueobj.WayPoint;

public class CarCircleRenderer implements ICarRenderer {

  public void draw(Graphics g, Car car, Color color) {
    g.setColor(color);
    int x = (int) car.getX();
    int y = (int) car.getY();
    int size = 10;
    g.drawOval(x - size / 2, y - size / 2, size, size);

    int length = 20;
    g.drawLine(x, y, (int) (x + length * Math.cos(car.getAngle())),
        (int) (y + length * Math.sin(car.getAngle())));

    WayPoint nextWayPoint = car.getGame().getTrack().getWayPoints().get(
        car.getNextWayPoint());
    double nextWayPointAngle = Math.atan2(nextWayPoint.getY() - car.getY(),
        nextWayPoint.getX() - car.getX());

    g.setColor(Color.YELLOW);
    // double steeringAngle = car.getAngle() + car.getSteeringWheel()
    // * car.getSetup().getSteeringPower();
    g.drawLine(x, y, (int) (x + length * Math.cos(nextWayPointAngle) / 2),
        (int) (y + length * Math.sin(nextWayPointAngle) / 2));
  }

  public void erase(Graphics g, Car car, BufferedImage background) {
    int x1 = (int) (car.getX() - car.getVx() - 20);
    int y1 = (int) (car.getY() - car.getVy() - 20);
    int x2 = (int) (car.getX() - car.getVx() + 20);
    int y2 = (int) (car.getY() - car.getVy() + 20);
    g.drawImage(background, x1, y1, x2, y2, x1, y1, x2, y2, null);
  }

}
