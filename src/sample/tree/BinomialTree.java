package sample.tree;

import java.util.ArrayList;
import java.util.HashMap;

import sample.instrument.ConvertibleBond;

/**
 * The Binomial Tree Object Seeks to Construct a array of nodes and Define their relationships
 * The class offers mechanism to create a empty N step binomial tree and will keep track of the terminal nodes
 * as well as the root node
 * 
 * @author erfangc
 *
 */
public class BinomialTree {

	private ArrayList<ArrayList<JDBinomialNode>> nodesAL; // 2D array of all nodes, level 1 represent all nodes in a given step, level 2 represent nodes within the step
	private ArrayList<JDBinomialNode> terminalNodes;         // A subset of all nodes and only contain the terminal nodes on the tree
	private JDBinomialNode rootNode;                         // Entry point on the tree
	private int numSteps; 
	private ConvertibleBond cb;
	
	// Shared Parameters for all nodes, from which the node specific fields are determined
	private double rf, divYld, vol, upProb, dnProb, upMove, dnMove, dt;
	
	// Non-Recursive Implementation
	public void createEmptyTree(int nSteps) {
		
		System.out.println("Creating Tree with "+nSteps+" Steps");
		
		// Reset All
		nodesAL = null;
		terminalNodes = null;
		rootNode = null;
		
		rootNode = new JDBinomialNode(true,false,new HashMap<String, Object>(),null, null, 0, 0);
		
		JDBinomialNode[][] nodes = new JDBinomialNode[nSteps][];		
		JDBinomialNode[] rootWrapper = new JDBinomialNode[1];
		rootWrapper[0] = rootNode;
		nodes[0] = rootWrapper;
		
		// Parallel ArrayList Object
		nodesAL = new ArrayList<ArrayList<JDBinomialNode>>();
		ArrayList<JDBinomialNode> rootArrayList = new ArrayList<JDBinomialNode>();
		rootArrayList.add(rootNode);
		nodesAL.add(rootArrayList);
		
		for (int n = 1; n < nSteps; n++) {
			
			boolean isTerminal = n == (nSteps-1) ? true : false;
			
			JDBinomialNode[] currentStep = new JDBinomialNode[n+1];			
			// # of Nodes on a Given Step in the Tree Correspond to
			// the Current Step n/nSteps
			ArrayList<JDBinomialNode> currentStepAL = new ArrayList<JDBinomialNode>();
			for (int i = 0; i < n + 1; i++) {
				currentStep[i] = new JDBinomialNode(false, isTerminal, new HashMap<String, Object>(), null, null, n, i);
				currentStep[i].setStep(n);
				currentStep[i].setNodeNumber(i);
				currentStep[i].setMyTree(this);
				currentStepAL.add(currentStep[i]);
			}
			
			nodes[n] = currentStep;
			if (isTerminal) {
				terminalNodes = currentStepAL;
			}
			
			// Having created node[n-1] and node[n], we must associate nodes in n as children of nodes in n-1
			mapChildren(nodes[n], nodes[n-1]);
			
			// Convert current step to ArrayList to be added to masterTree
			nodesAL.add(currentStepAL);
			
		}
		
	}	
	
	public BinomialTree() {
		super();
		createEmptyTree(2); // Default Tree is 2 Steps
	}
	
	public BinomialTree(int nSteps) {
		super();
		this.numSteps = nSteps;
		createEmptyTree(nSteps);
	}

	/**
	 * Children Must Contain 1 More Node than Parents
	 * 
	 * @param children
	 * @param parents
	 */
	public void mapChildren(JDBinomialNode[] children, JDBinomialNode[] parents) {
		for (int i = 0; i < parents.length; i++) {
			parents[i].setChildUp(children[i]);
			parents[i].setChildDn(children[i+1]);
		}
	}

	// Getter and Setters
	public ArrayList<ArrayList<JDBinomialNode>> getMasterTree() {
		return nodesAL;
	}

	public void setMasterTree(ArrayList<ArrayList<JDBinomialNode>> masterTree) {
		this.nodesAL = masterTree;
	}

	public ArrayList<JDBinomialNode> getTerminalNodes() {
		return terminalNodes;
	}

	public void setTerminalNodes(ArrayList<JDBinomialNode> terminalNodes) {
		this.terminalNodes = terminalNodes;
	}

	public JDBinomialNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(JDBinomialNode rootNode) {
		this.rootNode = rootNode;
	}

	public int getNumSteps() {
		return numSteps;
	}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public ArrayList<ArrayList<JDBinomialNode>> getTree() {
		return nodesAL;
	}

	public void setTree(ArrayList<ArrayList<JDBinomialNode>> tree) {
		this.nodesAL = tree;
	}

	public double getRf() {
		return rf;
	}

	public void setRf(double rf) {
		this.rf = rf;
	}

	public double getDivYld() {
		return divYld;
	}

	public void setDivYld(double divYld) {
		this.divYld = divYld;
	}

	public double getVol() {
		return vol;
	}

	public void setVol(double vol) {
		this.vol = vol;
	}

	public double getUpProb() {
		return upProb;
	}

	public void setUpProb(double upProb) {
		this.upProb = upProb;
	}

	public double getDnProb() {
		return dnProb;
	}

	public void setDnProb(double dnProb) {
		this.dnProb = dnProb;
	}

	public double getUpMove() {
		return upMove;
	}

	public void setUpMove(double upMove) {
		this.upMove = upMove;
	}

	public double getDnMove() {
		return dnMove;
	}

	public void setDnMove(double dnMove) {
		this.dnMove = dnMove;
	}

	public double getDt() {
		return dt;
	}

	public void setDt(double dt) {
		this.dt = dt;
	}

	public ConvertibleBond getCb() {
		return cb;
	}

	public void setCb(ConvertibleBond cb) {
		this.cb = cb;
	}
	
}
