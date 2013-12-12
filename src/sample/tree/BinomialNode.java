package sample.tree;

/**
 * The Node Class model a node on a Tree
 * Node can point to other nodes (as children)
 * and each note contains a set information defined by the specific application (such as Stock Price, BondFlr, Put/Call value etc ... )
 * 
 * @author erfangc
 *
 */
public class BinomialNode implements Comparable<BinomialNode>    
{
	private boolean isRoot, isTerminal;

	private BinomialNode childUp, childDn;
	
	private int step, nodeNumber;

	public BinomialNode(boolean isRoot, boolean isTerminal,
			BinomialNode childUp, BinomialNode childDn, int step, int nodeNumber) {
		this.isRoot = isRoot;
		this.isTerminal = isTerminal;
		this.childUp = childUp;
		this.childDn = childDn;
		this.step = step;
		this.nodeNumber = nodeNumber;
	}

	@Override
	public int compareTo(BinomialNode o) {
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

	public BinomialNode getChildUp() {
		return childUp;
	}

	public void setChildUp(BinomialNode childUp) {
		this.childUp = childUp;
	}

	public BinomialNode getChildDn() {
		return childDn;
	}

	public void setChildDn(BinomialNode childDn) {
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