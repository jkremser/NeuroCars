package neurocars.neuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class InputLayer {

	private List<InputNode> nodes;
	
	public InputLayer(int size){
	    nodes = new ArrayList<InputNode>(size);
	    for (int i= 0; i< size; i++){
	    	nodes.add(new InputNode());
	    }
	}
	
	public InputNode getNode(int index){
		return nodes.get(index);
	}
	
}
