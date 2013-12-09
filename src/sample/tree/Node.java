package sample.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

 
/**
 * The Node Class model a node on a Tree
 * Node can point to other nodes (as children)
 * and each note contains a set information defined by the specific application (such as Stock Price, BondFlr, Put/Call value etc ... )
 * 
 * @author erfangc
 *
 */
public class Node implements Comparable<Node>    
{
	private boolean isRoot;
	private boolean isTerminal;
	
	private Map<String, Object> data;
	private ArrayList<Node> children;
	private Node defaultNode;
	
	private int step, nodeNumber;

	public Node(boolean isRoot, boolean isTerminal, Map<String, Object> data,
			ArrayList<Node> children) {
		this.isRoot = isRoot;
		this.isTerminal = isTerminal;
		this.data = data;
		this.children = children;
		
		defaultNode = new Node(false, false, new HashMap<String, Object>(), null);
		
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public Node getDefaultNode() {
		return defaultNode;
	}

	public void setDefaultNode(Node defaultNode) {
		this.defaultNode = defaultNode;
	}

	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(int id) {
		this.nodeNumber = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeNumber;
		result = prime * result + step;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (nodeNumber != other.nodeNumber)
			return false;
		if (step != other.step)
			return false;
		return true;
	}

	@Override
	public int compareTo(Node o) {
		
		if (this.getNodeNumber() == o.getNodeNumber())
			return 0;
		
		return ( o.getNodeNumber() > 1 ? 1 : -1 );
	}
	
}