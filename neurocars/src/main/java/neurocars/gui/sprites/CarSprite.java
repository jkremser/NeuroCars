package neurocars.gui.sprites;

import java.awt.Color;
import java.awt.Graphics;

import neurocars.entities.Car;

/**
 * Graficka reprezentace zavodniho auta
 * 
 * @author Lukas Holcik
 * 
 */
public class CarSprite implements ISprite {

  private final Car car;

  private final Color color;

  public CarSprite(Car v, Color color) {
    this.car = v;
    this.color = color;
  }

  public Car getCar() {
    return car;
  }

  public void draw(Graphics g) {
    g.setColor(color);
    int x = (int) car.getX();
    int y = (int) car.getY();
    int size = 10;
    g.drawOval(x - size / 2, y - size / 2, size, size);

    int length = 5;
    g.drawLine(x, y, (int) (x + length * Math.cos(car.getAngle())),
        (int) (y + length * Math.sin(car.getAngle())));

    g.setColor(Color.GREEN);
    double steeringAngle = car.getAngle() + car.getSteeringWheel()
        * car.getSetup().getSteeringPower();
    g.drawLine(x, y, (int) (x + length * car.getSpeed()
        * Math.cos(steeringAngle)), (int) (y + length * car.getSpeed()
        * Math.sin(steeringAngle)));
  }
}
