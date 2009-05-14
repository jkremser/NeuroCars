package neurocars.neuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OutputLayer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7051009155576853841L;
	private List<OutputNode> nodes;
	
	public OutputLayer(int size){
	    nodes = new ArrayList<OutputNode>(size);
	    for (int i= 0; i< size; i++){
	    	nodes.add(new OutputNode());
	    }
	}
	
	public OutputNode getNode(int index){
		return nodes.get(index);
	}
	
	public void computeOutput(){
		for (OutputNode node: nodes){
			node.computeOutput();
		}
	}
	
	public int size(){
		return nodes.size();
	}
}
