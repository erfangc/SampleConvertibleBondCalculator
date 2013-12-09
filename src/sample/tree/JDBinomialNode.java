package sample.tree;

import java.util.ArrayList;
import java.util.Map;

public class JDBinomialNode extends Node {

	@Override
	public String toString() {
		return "JDBinomialNode [upProb=" + upProb + ", dnProb=" + dnProb
				+ ", nodeEquityValue=" + stockPrice + ", hazardRate="
				+ hazardRate + ", bondValue=" + bondValue + ", state=" + state
				+ ", getStep()=" + getStep() + ", getId()=" + getNodeNumber() + "]";
	}

	private double upProb, dnProb, stockPrice, hazardRate, bondValue;
	private NodeState state;
	
	public enum NodeState {
		CONVERTED,
		CALLED,
		PUT,
		MATURED,
		DEFAULTED
	}
	
	public JDBinomialNode(boolean isRoot, boolean isTerminal,
			Map<String, Object> data, ArrayList<Node> children) {
		super(isRoot, isTerminal, data, children);
	}

	// Setters and Getters
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

	public double getNodeEquityValue() {
		return stockPrice;
	}

	public void setNodeEquityValue(double nodeEquityValue) {
		this.stockPrice = nodeEquityValue;
	}

	public double getHazardRate() {
		return hazardRate;
	}

	public void setHazardRate(double hazardRate) {
		this.hazardRate = hazardRate;
	}

	public double getBondValue() {
		return bondValue;
	}

	public void setBondValue(double bondValue) {
		this.bondValue = bondValue;
	}

	public NodeState getState() {
		return state;
	}

	public void setState(NodeState state) {
		this.state = state;
	}
	
	
}
