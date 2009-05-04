package neurocars.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import neurocars.utils.ServiceException;
import neurocars.valueobj.Track;
import neurocars.valueobj.WayPoint;

/**
 * Generator nahodnych trati
 * 
 * @author Lukas Holcik
 * 
 */
public class TrackService extends ServiceBase {

  private static TrackService instance = null;

  private TrackService() {
    super();
  }

  public static TrackService getInstance() {
    if (instance == null) {
      instance = new TrackService();
    }
    return instance;
  }

  /**
   * Vygeneruje nahodne trate a ulozi je do souboru
   * 
   * @param prefix
   *          - prefix souboru
   * @param sizeX
   *          - X velikost trate
   * @param sizeY
   *          - Y velikost trate
   * @param wpSize
   *          - velikost WayPointu
   * @param length
   *          - delka trate
   * @param count
   *          - pocet trati
   */
  public void generateTrackFiles(String dir, String prefix, int sizeX,
      int sizeY, int wpSize, int length, int count) throws ServiceException {
    final Random r = new Random(System.currentTimeMillis());

    for (int i = 0; i < count; i++) {
      Track track = new Track(prefix + "_" + sizeX + "x" + sizeY + "x" + length
          + "_" + (i + 1));

      for (int k = 0; k < length; k = track.getWayPoints().size()) {
        WayPoint w = new WayPoint(r.nextInt(sizeX - 2 * wpSize) + wpSize,
            r.nextInt(sizeY - 2 * wpSize) + wpSize, wpSize);
        boolean ok = true;
        for (int j = 0; j < k; j++) {
          WayPoint t = track.getWayPoints().get(j);
          if (Math.sqrt(Math.pow(w.getX() - t.getX(), 2)
              + Math.pow(w.getY() - t.getY(), 2)) <= 2 * wpSize) {
            ok = false;
            break;
          }
        }
        if (ok) {
          track.getWayPoints().add(w);
        }
      }

      String filename = dir + "/" + track.getName() + ".conf";
      this.storeTrack(track, filename);
    }
  }

  /**
   * Ulozi trat do souboru
   * 
   * @param track
   * @param file
   * @throws ServiceException
   */
  public void storeTrack(Track track, String file) throws ServiceException {
    try {
      FileWriter out = new FileWriter(file);

      for (WayPoint wp : track.getWayPoints()) {
        String w = wp.getX() + "," + wp.getY() + "," + wp.getSize() + "\n";
        out.write(w);
      }
      out.close();
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  /**
   * Nacte trat ze souboru
   * 
   * @param file
   * @return
   * @throws ServiceException
   */
  public Track readTrack(String file) throws ServiceException {
    try {
      File trackFile = new File(file);
      String trackName = trackFile.getName();
      trackName = trackName.substring(0, trackName.lastIndexOf('.'));
      Track track = new Track(trackName);

      BufferedReader in = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = in.readLine()) != null) {
        String[] tokens = line.split(",");
        int x = Integer.valueOf(tokens[0]);
        int y = Integer.valueOf(tokens[1]);
        int size = Integer.valueOf(tokens[2]);

        track.addWayPoint(x, y, size);
      }
      return track;
    } catch (IOException e) {
      throw new ServiceException(e);
    }
  }

  public static void main(String[] args) throws ServiceException {
    String dir = "./config/tracks";
    String prefix = "t";
    getInstance().generateTrackFiles(dir, prefix, 1024, 768, 100, 8, 10);
  }

}
