package neurocars.neuralNetwork;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import neurocars.neuralNetwork.service.InputManager;
import neurocars.neuralNetwork.service.InputManagerDumbImpl;
import neurocars.neuralNetwork.service.InputManagerImpl;

public class Main {

	/**
	 * format argumentu: uceni: -o sit -i maxIter -t trainSoubor [-te
	 * testSoubor] deleni souboru: -s split -i input -t trFile -te teFile
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String programName = "Network";
		String syntax = "Pouziti:\n======\nUceni site:\n----------\n"
				+ programName
				+ " -o sit -i maxIter -t trainSoubor [-te testSoubor] -e error [-l learningConstant] [-hl hiddenLayers] -hls hiddenLayerSize\n"
				+ "\nRozdeleni souboru na soubory k testovani a trenovani:\n-----------------------------------------------------\n"
				+ programName + " -s split -i input -t trFile -te teFile";
		Options options = new Options();
		options.addOption("o", "output", true, "outputFile");
		options.addOption("i", true, "maxIterations");
		options.addOption("t", "trainFile", true, "trainFile");
		options.addOption("te", "testFile", true, "testFile");
		options.addOption("e", "error", true, "error");
		options.addOption("l", true, "learningConstant");
		options.addOption("hl", "hiddenLayers", true, "hiddenLayers");
		options.addOption("hls", "hiddenLayerSize", true, "hiddenLayerSize");
		options.addOption("s", "split", false, "splitFile");
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (cmd.hasOption("o")) {// uceni site
			learnNetwork(cmd, syntax);
		} else if (cmd.hasOption("s")) {// deleni souboru na test. a tren.
			// TODO
		} else {// spatne parametry
			System.out.println(syntax);
			return;
		}

	}

	private static void learnNetwork(CommandLine cmd, String syntax) {
		File outFile = new File(cmd.getOptionValue("o"));
		int maxIterations = -1;
		File trainFile;
		int hiddenLayerSize = -1;
		double maxError = 0;

		if (cmd.hasOption("i")) {
			try {
				maxIterations = Integer.parseInt(cmd.getOptionValue("i"));
			} catch (NumberFormatException e) {
				System.out.println("maxIterations musi byt cele cislo");
				System.out.println(syntax);
				return;
			}
			if (maxIterations < 1) {
				System.out.println("maxIterations musi byt kladne cislo");
				System.out.println(syntax);
				return;
			}
		} else {
			System.out.println("Chybi volba -i");
			System.out.println(syntax);
			return;
		}

		if (cmd.hasOption("t")) {
			trainFile = new File(cmd.getOptionValue("t"));
		} else {
			System.out.println("Chybi volba -t");
			System.out.println(syntax);
			return;
		}

		if (cmd.hasOption("e")) {
			try {
				maxError = Double.parseDouble(cmd.getOptionValue("e"));
			} catch (NumberFormatException e) {
				System.out.println("maxError musi byt realne cislo");
				System.out.println(syntax);
				return;
			}
			if (maxError <= 0) {
				System.out.println("maxError musi byt kladne cislo");
				System.out.println(syntax);
				return;
			}
		} else {
			System.out.println("Chybi volba -e");
			System.out.println(syntax);
			return;
		}

		if (cmd.hasOption("hls")) {
			try {
				hiddenLayerSize = Integer.parseInt(cmd.getOptionValue("hls"));
			} catch (NumberFormatException e) {
				System.out.println("hiddenLayerSize musi byt cele cislo");
				System.out.println(syntax);
				return;
			}
			if (hiddenLayerSize <= 0) {
				System.out.println("hiddenLayerSize musi byt kladne cislo");
				System.out.println(syntax);
				return;
			}
		} else {
			System.out.println("Chybi volba -hls");
			System.out.println(syntax);
			return;
		}
		String testFileStr = cmd.getOptionValue("te");
		File testFile = (testFileStr == null) ? null : new File(testFileStr);

		String hiddenLayersStr = cmd.getOptionValue("hl");
		int hiddenLayers = Network.DEFAULT_HIDDEN_LAYERS_NUMBER;
		try {
			if (hiddenLayersStr != null) {
				hiddenLayers = Integer.parseInt(hiddenLayersStr);
			}
		} catch (NumberFormatException e) {
			System.out.println("hiddenLayers musi byt realne cislo");
			System.out.println(syntax);
			return;
		}
		if (hiddenLayers <= 0) {
			System.out.println("hiddenLayers musi byt kladne cislo");
			System.out.println(syntax);
			return;
		}

		String learningConstantStr = cmd.getOptionValue("l");
		double learningConstant = Network.DEFAULT_LEARNING_CONSTANT;
		try {
			if (learningConstantStr != null) {
				Double.parseDouble(learningConstantStr);
			}
		} catch (NumberFormatException e) {
			System.out.println("learningConstant musi byt realne cislo");
			System.out.println(syntax);
			return;
		}
		if (learningConstant <= 0) {
			System.out.println("learningConstant musi byt kladne cislo");
			System.out.println(syntax);
			return;
		}

		Network net = new Network(maxError, outFile, hiddenLayers,
				hiddenLayerSize, learningConstant, maxIterations);
		InputManager manager = new InputManagerImpl(trainFile, testFile);
		net.setInputManager(manager);
		net.learn();
	}
}