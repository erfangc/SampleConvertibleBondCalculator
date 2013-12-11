package sample.tree;

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
	private boolean isRoot, isTerminal;

	private Node childUp, childDn;
	
	private int step, nodeNumber;

	public Node(boolean isRoot, boolean isTerminal,
			Node childUp, Node childDn, int step, int nodeNumber) {
		this.isRoot = isRoot;
		this.isTerminal = isTerminal;
		this.childUp = childUp;
		this.childDn = childDn;
		this.step = step;
		this.nodeNumber = nodeNumber;
	}

	@Override
	public int compareTo(Node o) {
		return 0;
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

	public Node getChildUp() {
		return childUp;
	}

	public void setChildUp(Node childUp) {
		this.childUp = childUp;
	}

	public Node getChildDn() {
		return childDn;
	}

	public void setChildDn(Node childDn) {
		this.childDn = childDn;
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

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}
	
}