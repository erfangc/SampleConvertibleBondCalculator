package sample.tree;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import sample.bondfeature.FeatureProcessor;
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

	public static final Logger LOG = Logger.getLogger(BinomialTree.class);

	// FeatureProcess enables nodes to process complicated Bond Features such as Call/Puts
	private FeatureProcessor featureProcessor;
	
	// Shared Parameters for all nodes, from which the node specific fields are determined
	private double rf, divYld, vol, upProb, dnProb, upMove, dnMove, dt, hazardRateCalibrCnst;
	
	// 2D array of all nodes, level 1 represent all nodes in a given step, level 2 represent nodes within the step
	private ArrayList<ArrayList<JDBinomialNode>> nodesAL;

	// Entry point on the tree
	private JDBinomialNode rootNode;                         
	
	private int numSteps; 
	private ConvertibleBond cb;
	
	public enum GraphicType {

		STOCK_PRICE("Stock Price Tree"),
		CONTINUATION_VALUE("Continuation Value Tree"),
		DEFAULT_PROBABILITY("Default Probability Tree"),
		HAZARD_RATE("Hazard Rate Tree"),
		CONVERTIBLE_VALUE("Convertible Value Tree"),
		EXERCISE_VALUE("Exercise Value Tree");
		
		private String label;
		
		@Override
		public String toString() {
			return label;
		}
		
		GraphicType(String label) {
			this.label = label;
		}
	}
	
	// Non-Recursive Implementation
	public void createEmptyTree(int nSteps) {

		LOG.info("Creating Tree with "+nSteps+" Steps");

		// Reset All
		nodesAL = null;
		rootNode = null;

		JDBinomialNode[][] nodes = new JDBinomialNode[nSteps][]; // Array Representation of All the Nodes
		
		rootNode = new JDBinomialNode(true,false, null, null, 0, 0);
		rootNode.setMyTree(this);				
		nodes[0] = new JDBinomialNode[]{ rootNode };

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
				currentStep[i] = new JDBinomialNode(false, isTerminal, null, null, n, i);
				currentStep[i].setStep(n);
				currentStep[i].setNodeNumber(i);
				currentStep[i].setMyTree(this);
				currentStepAL.add(currentStep[i]);
			}
			
			nodes[n] = currentStep;
			
			// Having created node[n-1] and node[n], we must associate nodes in n as children of nodes in n-1
			mapChildren(nodes[n], nodes[n-1]);
			
			// Convert current step to ArrayList to be added to masterTree
			nodesAL.add(currentStepAL);
			
		}
		
	}	
		
	public BinomialTree(int nSteps, ConvertibleBond cb) {
		this.numSteps = nSteps;
		createEmptyTree(nSteps);
		featureProcessor = new FeatureProcessor(cb); // FeatureProcessor Object will initialize the proper features
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
    
	@Override
	public String toString() {
		return "BinomialTree [numSteps=" + numSteps + ", rf="
				+ rf + ", divYld=" + divYld + ", vol=" + vol + ", upProb="
				+ upProb + ", dnProb=" + dnProb + ", upMove=" + upMove
				+ ", dnMove=" + dnMove + ", dt=" + dt
				+ ", hazardRateCalibrCnst=" + hazardRateCalibrCnst + "]";
	}

	// Output Methods
	public String getGraphic(GraphicType type) {    
		
		String graphic = "\n"+type.toString()+"\n";
		
		if (getNumSteps() >= 25) {
			graphic += "Tree Display Suppressed - Too many Nodes\n";
			return graphic;
		}
		
		// Create an Array Big Enough to Hold the Entire Tree
		Object[][] displayObject = new Object[2*getNumSteps()-1][getNumSteps()]; 
		
		for (ArrayList<JDBinomialNode> step : nodesAL) {
			for (JDBinomialNode node : step) {
				int x = node.getStep(),
					y = ( getNumSteps() -1 ) - node.getStep() + 2 * node.getNodeNumber(); // Figure out how many cells in displayObject to skip
				
				double data = 0.0;
				// Determine Data to Print
				switch (type) {
				case EXERCISE_VALUE:
					data = node.getConversionValue();
					break;
				case CONTINUATION_VALUE:
					data = node.getContinueValue();
					break;
				case CONVERTIBLE_VALUE:
					data = node.getConvertibleValue();
					break;
				case STOCK_PRICE:
					data = node.getStockPrice();
					break;
				case DEFAULT_PROBABILITY:
					data = node.getDefaultProb();
					break;
				case HAZARD_RATE:
					data = node.getHazardRate();
					break;
				default:
					data = (double) node.getNodeNumber();
					break;
				}
				
				displayObject[y][x] = data;
			}
		}
		
		for (Object[] row : displayObject) {
			String rowStr = "";
			for (Object cell : row) {
				if (cell == null) {
					rowStr += String.format("%-13s", " ");
				} else {
					rowStr += String.format("%13s", String.format("%-10.2f", cell));
				}		
			}
			graphic += rowStr + "\n";
		}
		
		return graphic;
	}
	
	// Getter and Setters
	public ArrayList<ArrayList<JDBinomialNode>> getMasterTree() {
		return nodesAL;
	}

	public void setMasterTree(ArrayList<ArrayList<JDBinomialNode>> masterTree) {
		this.nodesAL = masterTree;
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

	public double getHazardRateCalibrCnst() {
		return hazardRateCalibrCnst;
	}

	public void setHazardRateCalibrCnst(double hazardRateCalibrCnst) {
		this.hazardRateCalibrCnst = hazardRateCalibrCnst;
	}
	
	public void resetNodes() {
		for (ArrayList<JDBinomialNode> step : nodesAL) {
			for (JDBinomialNode node : step) {
				node.reset();
			}
		}
	}

	public FeatureProcessor getFeatureProcessor() {
		return featureProcessor;
	}

	public void setFeatureProcessor(FeatureProcessor featureProcessor) {
		this.featureProcessor = featureProcessor;
	}
	
}
