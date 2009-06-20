package neurocars.gui.sprites;

import java.awt.Graphics2D;
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
  private final int color;

  public CarSprite(Car v, ICarRenderer renderer, int color) {
    this.car = v;
    this.renderer = renderer;
    this.color = color;
  }

  public Car getCar() {
    return car;
  }

  public void draw(Graphics2D g) {
    renderer.draw(g, car, color);
  }

  public void erase(Graphics2D g, BufferedImage background) {
    renderer.erase(g, car, background);
  }
}
