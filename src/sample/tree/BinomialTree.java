package sample.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The Binomial Tree Object Seeks to Construct a array of nodes and Define their relationships
 * The class offers mechanism to create a empty N step binomial tree and will keep track of the terminal nodes
 * as well as the root node
 * 
 * @author erfangc
 *
 */
public class BinomialTree {

	private ArrayList<ArrayList<JDBinomialNode>> masterTree; // 2D array of all nodes, level 1 represent all nodes in a given step, level 2 represent nodes within the step
	private ArrayList<JDBinomialNode> terminalNodes;         // A subset of all nodes and only contain the terminal nodes on the tree
	private JDBinomialNode rootNode;                         // Entry point on the tree
	
	// Non-Recursive Implementation
	public void createEmptyTree(int nSteps) {
		
		// Reset All
		masterTree = null;
		terminalNodes = null;
		rootNode = null;
		
		rootNode = new JDBinomialNode(true, false, null, null);
		
		JDBinomialNode[][] nodes = new JDBinomialNode[nSteps-1][];
		JDBinomialNode[] rootWrapper = new JDBinomialNode[1];
		rootWrapper[0] = rootNode;
		nodes[0] = rootWrapper;
		
		// Parallel ArrayList Object
		masterTree = new ArrayList<ArrayList<JDBinomialNode>>();
		masterTree.add((ArrayList<JDBinomialNode>) Arrays.asList(nodes[0]));
		
		for (int n = 1; n < nSteps; n++) {
			
			boolean isTerminal = n == (nSteps-1) ? true : false;
			
			JDBinomialNode[] nodesCurrentStep = new JDBinomialNode[n];			
			// # of Nodes on a Given Step in the Tree Correspond to
			// the Current Step n/nSteps
			for (int i = 0; i < n; i++) {
				nodesCurrentStep[i] = new JDBinomialNode(false, isTerminal, new HashMap<String, Object>(), null);
				nodesCurrentStep[i].setStep(n);
				nodesCurrentStep[i].setNodeNumber(i);
			}
			
			nodes[n] = nodesCurrentStep;
			if (isTerminal) {
				terminalNodes = new ArrayList<JDBinomialNode>(Arrays.asList(nodesCurrentStep));
			}
			
			// Having created node[n-1] and node[n], we must associate nodes in n as children of nodes in n-1
			mapChildren(nodes[n], nodes[n-1]);
			
			// Convert current step to ArrayList to be added to masterTree
			ArrayList<JDBinomialNode> asArrList = (ArrayList<JDBinomialNode>) Arrays.asList(nodesCurrentStep);
			masterTree.add(asArrList);
			
		}
		
	}	
	
	public BinomialTree() {
		super();
		createEmptyTree(2);
	}
	
	public BinomialTree(int nSteps) {
		super();
		createEmptyTree(nSteps);
	}

	/**
	 * Children Must Contain 1 More Node than Parents
	 * 
	 * @param children
	 * @param parents
	 */
	public void mapChildren(JDBinomialNode[] children, JDBinomialNode[] parents) {
		// TODO Implement
	}

	// Getter/Setters
	public ArrayList<ArrayList<JDBinomialNode>> getMasterTree() {
		return masterTree;
	}

	public void setMasterTree(ArrayList<ArrayList<JDBinomialNode>> masterTree) {
		this.masterTree = masterTree;
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
	
}
