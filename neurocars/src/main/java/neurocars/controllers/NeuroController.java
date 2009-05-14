package neurocars.controllers;

import neurocars.entities.Car;
import neurocars.neuralNetwork.Network;
import neurocars.valueobj.NeuralNetworkInput;
import neurocars.valueobj.NeuralNetworkOutput;

/**
 * Ovladac vozidla prostrednictvim neuronove site
 * 
 * @author Lukas Holcik
 */
public class NeuroController extends Controller {

	private Car car;
	private NeuralNetworkOutput out;
	private Network net;

	public NeuroController(Network net) {
		System.out.println(net);
		this.net = net;
	}

	public void next() {
		NeuralNetworkInput in = car.getNeuralNetworkInput();
		boolean flip = false;
		if (in.getCurveAngle() < 0) { // zaporny uhel zatacky, musime klopit
			flip = true;
			in.setCurveAngle(-in.getCurveAngle());
			in.setWayPointAngle(-in.getWayPointAngle());
			in.setSteeringWheel(-in.getSteeringWheel());
		}
		this.out = net.runNetwork(in);
		if (flip) {
			boolean aux = out.isLeft();
			out.setLeft(out.isRight());
			out.setRight(aux);
		}
		System.out.println(out);
	}

	public boolean accelerate() {
		return out.isAccelerate();
	}

	public boolean brake() {
		return out.isBrake();
	}

	public boolean left() {
		return out.isLeft();
	}

	public boolean right() {
		return out.isRight();
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Car getCar() {
		return car;
	}

}
