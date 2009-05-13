package neurocars.neuralNetwork;

import java.io.File;

import neurocars.neuralNetwork.service.InputManager;

/**
 * Slouzi jako sandbox k testovani jednotlivych casti mimo cely projekt
 * 
 * @author freon
 * 
 */
public class MrsTeacher {

	String[] args;

	public MrsTeacher(String... args) {
		this.args = args;
	}

	public static void main(String... args) {

		// IO
		InputManager in = new InputManager(
				new File(
				// "/home/freon/skola/NeuroN/neurocars/neurocars/player4_replay.txt"));
						"C:\\neurocars\\player1_replay.txt"));
		in.initTrainData();
		DataItem it = null;
		int i = 0;
		while ((it = in.getNextTrainItem()) != null) {
			System.out.println(i++ + " @ " + it);
		}

		// perzistence
		Network net = new Network(null, null, 2.4, new File("net"), 5, 10, 0.5,
				2001);
		net.serializeNetwork();

		Network sit = Network.loadNetwork(new File("net"));
		System.out.println(sit.getHiddenLayerSize());
	}

	/**
	 * Spusti uceni site
	 * 
	 * format argumentu: -o sit -i maxIter -t trainSoubor [-te testSoubor]
	 */
	public void learn() {
		if ((args.length != 6 && args.length != 8) || (!"-o".equals(args[0]))
				|| (!"-i".equals(args[2])) || (!"-t".equals(args[4]))
				|| (args.length == 8 && !"-te".equals(args[6]))) {
			System.err
					.println("Pouziti:\njava -jar jmeno_programu -o sit -i maxIter -t trainSoubor [-te testSoubor]");
		} else {

			File network = new File(args[1]);
			if (!network.exists()) {
				System.err.println("Soubor se siti "
						+ network.getAbsolutePath() + " neexistuje.");
			}
			int maxIter = 5000;
			try {
				maxIter = Integer.parseInt(args[3]);
			} catch (NumberFormatException nfe) {
				System.err.println("Maximalni pocet iteraci musi byt cislo.");
			}
			File trainFile = new File(args[5]);
			if (!trainFile.exists()) {
				System.err.println("Soubor s trenovacimi daty "
						+ trainFile.getAbsolutePath() + " neexistuje.");
			}
			File testFile = null;
			if (args.length == 8) {
				testFile = new File(args[7]);
				if (!testFile.exists()) {
					System.err.println("Soubor s testovacimi daty "
							+ testFile.getAbsolutePath() + " neexistuje.");
				}
			}
			Network net = Network.loadNetwork(network);
			net.setMaxIterations(maxIter);
			if (args.length == 8) {
				net.setInputManager(new InputManager(trainFile, testFile));
			} else {
				net.setInputManager(new InputManager(trainFile));
			}

			System.out.println(net);

			// net.initNetwork();
			/*
			 * tady mi to padalo, asi proto, ze jsem tam tu sit inicioval Fatal
			 * application exception: null java.lang.NullPointerException at
			 * neurocars
			 * .neuralNetwork.InputNode.addNextLayerNode(InputNode.java:36)
			 */
			// net.learn();
		}
	}

}
