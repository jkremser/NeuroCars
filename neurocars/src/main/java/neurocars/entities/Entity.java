package neurocars.entities;

import neurocars.Game;

/**
 * Zakladni trida pro herni objekty
 * 
 * @author Lukas Holcik
 * 
 */
public abstract class Entity {

  // odkaz na instanci hry
  protected final Game game;

  /** The current x location of this entity */
  protected double x;
  /** The current y location of this entity */
  protected double y;
  /** The current speed of this entity horizontally (pixels/sec) */
  protected double vx;
  /** The current speed of this entity vertically (pixels/sec) */
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
