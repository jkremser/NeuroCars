package neurocars;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import neurocars.controllers.KeyboardController;
import neurocars.entities.Vehicle;
import neurocars.graphics.VehicleGraphic;
import neurocars.valueobj.TerrainSetup;
import neurocars.valueobj.Track;
import neurocars.valueobj.VehicleSetup;
import neurocars.valueobj.XY;

import org.apache.log4j.Logger;

/**
 * 
 * @author Lukas Holcik
 * 
 */
public class Game extends Canvas {

  private static final String TITLE = "ne_uroc/ars";

  private static final Logger log = Logger.getLogger(Game.class);

  /** The message to display which waiting for a key press */
  private String statusMessage = "";
  /** True if we're holding up game play until a key has been pressed */
  private boolean waitingForKeyPress = false;
  /** True if the left cursor key is currently pressed */
  private BufferStrategy strategy;
  private GraphicsConfiguration gc;

  private final boolean[] keyCodes = new boolean[65535];

  private final int xScreenSize;
  private final int yScreenSize;
  private final Track track;
  private final TerrainSetup terrain;

  /**
   * Construct our game and set it running.
   */
  public Game(int xScreenSize, int yScreenSize, Track track,
      TerrainSetup terrain) {

    this.xScreenSize = xScreenSize;
    this.yScreenSize = yScreenSize;
    this.track = track;
    this.terrain = terrain;
    // TODO : hraci

    // Acquiring the current Graphics Device and Graphics Configuration
    GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice graphDevice = graphEnv.getDefaultScreenDevice();
    gc = graphDevice.getDefaultConfiguration();

    // create a frame to contain our game
    JFrame frame = new JFrame(gc);
    frame.setTitle(TITLE);

    // get hold the content of the frame and set up the resolution of the game
    JPanel panel = (JPanel) frame.getContentPane();
    panel.setPreferredSize(new Dimension(getXScreenSize(), getYScreenSize()));
    panel.setLayout(null);

    // setup our canvas size and put it into the content of the frame
    setBounds(0, 0, getXScreenSize(), getYScreenSize());
    panel.add(this);

    // Tell AWT not to bother repainting our canvas since we're
    // going to do that our self in accelerated mode
    setIgnoreRepaint(true);

    // finally make the window visible
    frame.pack();
    frame.setResizable(false);

    // add a listener to respond to the user closing the window. If they
    // do we'd like to exit the game
    frame.addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    // add a key input system (defined below) to our canvas
    // so we can respond to key pressed
    addKeyListener(new KeyInputHandler());

    // request the focus so key events come to us
    requestFocus();
    frame.setFocusTraversalKeysEnabled(false);
    this.setFocusTraversalKeysEnabled(false);

    // create the buffering strategy which will allow AWT
    // to manage our accelerated graphics
    createBufferStrategy(2);
    strategy = getBufferStrategy();

    frame.setVisible(true);
  }

  public void loop() {
    // long lastLoopTime = System.currentTimeMillis();
    KeyboardController kc = new KeyboardController(keyCodes, KeyEvent.VK_UP,
        KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
    Vehicle v = new Vehicle(this, "player1", VehicleSetup.SLOW, kc);
    VehicleGraphic vg = new VehicleGraphic(v, Color.WHITE);

    v.setX(track.getWayPoints().get(0).getX());
    v.setY(track.getWayPoints().get(0).getY());

    NumberFormat nf = new DecimalFormat("0.000");

    BufferedImage background = drawBackground(track);

    while (!keyCodes[KeyEvent.VK_ESCAPE]) {
      // long delta = System.currentTimeMillis() - lastLoopTime;
      // lastLoopTime = System.currentTimeMillis();

      v.input();
      v.update();
      v.updateWayPoint(getTrack().getWayPoints().get(v.getNextWayPoint()));

      Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
      g.drawImage(background, 0, 0, null);

      // g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
      // RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      vg.draw(g);

      statusMessage = "X=" + nf.format(v.getX()) + ";Y=" + nf.format(v.getY())
          + ";speed=" + nf.format(v.getSpeed()) + ";angle="
          + nf.format(v.getAngle()) + ";steeringWheel="
          + nf.format(v.getSteeringWheel()) + ";nextWayPoint="
          + (v.getNextWayPoint() + 1);
      // STATUS MESSAGE
      this.drawStatusMessage(g);

      // finally, we've completed drawing so clear up the graphics
      // and flip the buffer over
      g.dispose();
      strategy.show();
      // Toolkit.getDefaultToolkit().sync();

      try {
        Thread.sleep(10);
      } catch (Exception e) {
      }
    }
  }

  private void drawStatusMessage(Graphics2D g2) {
    g2.setColor(Color.WHITE);
    g2.drawString(statusMessage, 10, getYScreenSize() - 20);
  }

  /**
   * Namaluje drahu jako obrazek na pozadi
   * 
   * @param track
   * @return
   */
  private BufferedImage drawBackground(Track track) {
    BufferedImage background = gc.createCompatibleImage(getXScreenSize(),
        getYScreenSize(), Transparency.OPAQUE);
    Graphics g = background.getGraphics();
    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getXScreenSize(), getYScreenSize());

    final int pointSize = 100;
    for (int p = 0; p < track.getWayPoints().size(); p++) {
      XY point = track.getWayPoints().get(p);
      g.setColor(Color.WHITE);
      g.drawOval((int) (point.getX() - pointSize / 2),
          (int) (point.getY() - pointSize / 2), pointSize, pointSize);
      this.drawCentered(g, "" + (p + 1), (int) point.getX(), (int) point.getY());

      if (p > 0) {
        g.setColor(Color.ORANGE);
        g.drawLine((int) track.getWayPoints().get(p - 1).getX(),
            (int) track.getWayPoints().get(p - 1).getY(), (int) point.getX(),
            (int) point.getY());
      }
    }
    g.dispose();
    return background;
  }

  public void drawCentered(Graphics g, String message, int x, int y) {
    int h = g.getFontMetrics().getHeight();
    int w = g.getFontMetrics().stringWidth(message);
    g.drawString(message, x - w / 2, y - h / 2);
  }

  public int getXScreenSize() {
    return xScreenSize;
  }

  public int getYScreenSize() {
    return yScreenSize;
  }

  public Track getTrack() {
    return track;
  }

  public TerrainSetup getTerrain() {
    return terrain;
  }

  /**
   * A class to handle keyboard input from the user. The class handles both
   * dynamic input during game play, i.e. left/right and shoot, and more static
   * type input (i.e. press any key to continue)
   * 
   * This has been implemented as an inner class more through habbit then
   * anything else. Its perfectly normal to implement this as seperate class if
   * slight less convienient.
   * 
   * @author Kevin Glass
   */
  private class KeyInputHandler extends KeyAdapter {

    /** The number of key presses we've had while waiting for an "any key" press */
    private int pressCount = 1;

    /**
     * Notification from AWT that a key has been pressed. Note that a key being
     * pressed is equal to being pushed down but *NOT* released. Thats where
     * keyTyped() comes in.
     * 
     * @param e
     *          The details of the key that was pressed
     */
    public void keyPressed(KeyEvent e) {
      // if we're waiting for an "any key" typed then we don't
      // want to do anything with just a "press"
      if (waitingForKeyPress) {
        return;
      }

      if (log.isTraceEnabled()) {
        log.trace("Key pressed: " + e.getKeyCode());
      }

      keyCodes[e.getKeyCode()] = true;
    }

    /**
     * Notification from AWT that a key has been released.
     * 
     * @param e
     *          The details of the key that was released
     */
    public void keyReleased(KeyEvent e) {
      // if we're waiting for an "any key" typed then we don't
      // want to do anything with just a "released"
      if (waitingForKeyPress) {
        return;
      }

      if (log.isTraceEnabled()) {
        log.trace("Key released: " + e.getKeyCode());
      }

      keyCodes[e.getKeyCode()] = false;
    }

    /**
     * Notification from AWT that a key has been typed. Note that typing a key
     * means to both press and then release it.
     * 
     * @param e
     *          The details of the key that was typed.
     */
    public void keyTyped(KeyEvent e) {
      // if we're waiting for a "any key" type then
      // check if we've recieved any recently. We may

      // have had a keyType() event from the user releasing

      // the shoot or move keys, hence the use of the "pressCount"
      // counter.

      if (waitingForKeyPress) {
        if (pressCount == 1) {
          if (log.isDebugEnabled()) {
            log.debug("Key pressed!");
          }
          // since we've now recieved our key typed

          // event we can mark it as such and start

          // our new game
          waitingForKeyPress = false;
          // startGame();
          pressCount = 0;
        } else {
          pressCount++;
        }
      }

      // if we hit escape, then quit the game

      // if (e.getKeyChar() == 27) {
      // System.exit(0);
      // }
    }
  }

  public static void main(final String[] args) {
    final int xScreenSize = 800;
    final int yScreenSize = 600;

    Track track = createDemoTrack(xScreenSize, yScreenSize);

    Game g = new Game(xScreenSize, yScreenSize, track, TerrainSetup.ICE);
    g.loop();

    System.exit(0);
    // }
    //
    // });
  }

  /**
   * Vytvori drahu
   * 
   * @return
   */
  public static Track createDemoTrack(int xScreenSize, int yScreenSize) {
    Track track = new Track();
    track.addWayPoint(100, 100);
    track.addWayPoint(xScreenSize / 2, yScreenSize / 3);
    track.addWayPoint(xScreenSize - 100, 100);
    track.addWayPoint(xScreenSize * 4 / 5, yScreenSize * 2 / 3);
    track.addWayPoint(xScreenSize - 100, yScreenSize - 100);
    track.addWayPoint(xScreenSize / 5, yScreenSize - 100);
    track.addWayPoint(100, yScreenSize * 2 / 3);
    track.addWayPoint(100, 200);
    return track;
  }

}
