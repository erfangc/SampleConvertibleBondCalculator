package sample.tree;

import java.util.Map;

public class JDBinomialNode extends Node {
	
	public JDBinomialNode(boolean isRoot, boolean isTerminal,
			Map<String, Object> data, Node childUp, Node childDn, int step,
			int nodeNumber) {
		super(isRoot, isTerminal, data, childUp, childDn, step, nodeNumber);
	}

	// Private Fields that Store Crucial Node level Valuation Ingredients
	private double stockPrice, hazardRate, bondPV, contValue, exerciseValue, nodeDerivValue;
	private BinomialTree myTree; // Since many metrics crucial to valuation are stored at the tree level
	private NodeState state;
	
	public enum NodeState {
		CONVERTED,
		CALLED,
		PUT,
		MATURED,
		DEFAULTED
	}
	
	/*
	 * Calculation Functions
	 */
	
	// Simple Equity-Credit link: Lamda = 1000 / S^2
	public double getEquityLinkedHazardRate() {
		return 1000/Math.pow(getStockPrice(), 2);
	}
	
	public void setChildrenStockPrice() {
	//TODO Implement This, Populate Children's Stock Price 
	}
	
	//TODO Implement Other Analytic Values Calculations, Including Default Probability
	
	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double nodeEquityValue) {
		this.stockPrice = nodeEquityValue;
	}

	public double getHazardRate() {
		return hazardRate;
	}

	public void setHazardRate(double hazardRate) {
		this.hazardRate = hazardRate;
	}

	public double getBondPV() {
		return bondPV;
	}

	public void setBondPV(double bondValue) {
		this.bondPV = bondValue;
	}

	public NodeState getState() {
		return state;
	}

	public void setState(NodeState state) {
		this.state = state;
	}

	public double getExerciseValue() {
		return exerciseValue;
	}

	public void setExerciseValue(double exerciseValue) {
		this.exerciseValue = exerciseValue;
	}

	public double getContValue() {
		return contValue;
	}

	public void setContValue(double contValue) {
		this.contValue = contValue;
	}

	public double getNodeDerivValue() {
		return nodeDerivValue;
	}

	public void setNodeDerivValue(double nodeDerivValue) {
		this.nodeDerivValue = nodeDerivValue;
	}

	public BinomialTree getMyTree() {
		return myTree;
	}

	public void setMyTree(BinomialTree myTree) {
		this.myTree = myTree;
	}
	
	
}
