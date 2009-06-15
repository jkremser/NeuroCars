package neurocars.gui.sprites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Swingove graficke rozhrani hernich objektu
 * 
 * @author Lukas Holcik
 * 
 */
public interface ISprite {

  public void draw(Graphics2D g);

  public void erase(Graphics2D g, BufferedImage background);

}
