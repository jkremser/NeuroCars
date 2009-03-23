package neurocars.entities;

import neurocars.Game;

/**
 * Zakladni trida pro herni objekty
 * 
 * @author Lukas Holcik
 * 
 */
public abstract class Entity {

  /** odkaz na instanci hry */
  protected final Game game;

  /** x souradnice */
  protected double x;
  /** y souradnice */
  protected double y;
  /** x-ova rychlost v jednom hernim cyklu */
  protected double vx;
  /** y-ova rychlost v jednom hernim cyklu */
  protected double vy;

  public Entity(Game game) {
    this.game = game;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getVx() {
    return vx;
  }

  public void setVx(double dx) {
    this.vx = dx;
  }

  public double getVy() {
    return vy;
  }

  public void setVy(double dy) {
    this.vy = dy;
  }

  public Game getGame() {
    return game;
  }

}
