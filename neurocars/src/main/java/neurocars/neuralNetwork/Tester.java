package neurocars.neuralNetwork;

import java.io.File;

import neurocars.neuralNetwork.service.InputManager;

/**
 * Slouzi jako sandbox k testovani jednotlivych casti mimo cely projekt
 * 
 * @author freon
 * 
 */
public class Tester {

  public static void main(String... args) {

    // IO
    InputManager in = new InputManager(new File(
        "/home/freon/skola/NeuroN/neurocars/neurocars/player4_replay.txt"),
        null);
    in.initTrainData();
    DataItem it = null;
    int i = 0;
    while ((it = in.getNextTrainItem()) != null) {
      System.out.println(i++ + " @ " + it);
    }

    // perzistence
    Network net = new Network(null, null, 3.14159, new File("net"), 1, 3145926,
        0.5, 100);
    net.serializeNetwork();

    Network sit = Network.loadNetwork(new File("net"));
    System.out.println(sit.getHiddenLayerSize());
  }

}
