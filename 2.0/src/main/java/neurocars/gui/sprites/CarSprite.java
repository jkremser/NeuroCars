package neurocars.gui.sprites;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import neurocars.entities.Car;
import neurocars.gui.renderer.ICarRenderer;

/**
 * Graficka reprezentace zavodniho auta
 * 
 * @author Lukas Holcik
 * 
 */
public class CarSprite implements ISprite {

  private final Car car;
  private final ICarRenderer renderer;

  public CarSprite(Car v, ICarRenderer renderer) {
    this.car = v;
    this.renderer = renderer;
  }

  public Car getCar() {
    return car;
  }

  public void draw(Graphics g) {
    renderer.draw(g, car);
  }

  public void erase(Graphics g, BufferedImage background) {
    renderer.erase(g, car, background);
  }
}
