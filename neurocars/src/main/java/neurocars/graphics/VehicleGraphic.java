package neurocars.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import neurocars.entities.Vehicle;

/**
 * Graficka reprezentace zavodniho auta
 * 
 * @author Lukas Holcik
 * 
 */
public class VehicleGraphic implements IGraphic {

  private final Vehicle vehicle;
  private final Color color;

  public VehicleGraphic(Vehicle v, Color color) {
    this.vehicle = v;
    this.color = color;
  }

  public void draw(Graphics2D g) {
    g.setColor(color);
    int x = (int) vehicle.getX();
    int y = (int) vehicle.getY();
    int size = 10;
    g.drawOval(x - size / 2, y - size / 2, size, size);

    int length = 5;
    g.drawLine(x, y, (int) (x + length * Math.cos(vehicle.getAngle())),
        (int) (y + length * Math.sin(vehicle.getAngle())));

    g.setColor(Color.GREEN);
    double steeringAngle = vehicle.getAngle() + vehicle.getSteeringWheel()
        * vehicle.getModel().getSteeringPower();
    g.drawLine(x, y, (int) (x + length * vehicle.getSpeed()
        * Math.cos(steeringAngle)), (int) (y + length * vehicle.getSpeed()
        * Math.sin(steeringAngle)));
  }
}
