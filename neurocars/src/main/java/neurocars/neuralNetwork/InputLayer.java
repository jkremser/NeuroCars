package neurocars.neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InputLayer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7295108673751428247L;
	private List<InputNode> nodes;
	
	public InputLayer(int size, int hiddenLayerSize){
	    nodes = new ArrayList<InputNode>(size);
	    for (int i= 0; i< size; i++){
	    	nodes.add(new InputNode(hiddenLayerSize));
	    }
	}
	
	public InputNode getNode(int index){
		return nodes.get(index);
	}
	
		
}
