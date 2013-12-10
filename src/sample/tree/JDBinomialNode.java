package sample.tree;

import java.util.Map;

public class JDBinomialNode extends Node {
	
	// Private Fields that Store Crucial Node level Valuation Ingredients
	private double stockPrice = Double.NaN, bondPV = Double.NaN;
	private BinomialTree myTree; // Since many metrics crucial to valuation are stored at the tree level

	public JDBinomialNode(boolean isRoot, boolean isTerminal,
			Map<String, Object> data, Node childUp, Node childDn, int step,
			int nodeNumber) {
		super(isRoot, isTerminal, data, childUp, childDn, step, nodeNumber);
	}
	
	/*
	 * Calculation Functions
	 */
	
	// Simple Equity-Credit link: Lambda = c / S^2
	public double getHazardRate() {
		return(getMyTree().getHazardRateCalibrCnst()/Math.pow(getStockPrice(), 2));
	}
	
	public double getDefaultProb() {
		double dt = myTree.getDt();
		return(1-Math.exp(-1*getHazardRate()*dt));
	}
	
	public void setChildrenStockPrice() {
		
		if (isTerminal())
			return;
		
		JDBinomialNode upChild = (JDBinomialNode) getChildUp();
		JDBinomialNode dnChild = (JDBinomialNode) getChildDn();

		if (upChild != null && Double.isNaN(upChild.getStockPrice()))
			upChild.setStockPrice(getStockPrice() * getMyTree().getUpMove());
		
		if (dnChild != null && Double.isNaN(dnChild.getStockPrice()))
			dnChild.setStockPrice(getStockPrice() * getMyTree().getDnMove());		
		
	}
	
	public double getContinueValue() {
		
		// Continuation Value at Terminal Nodes Should be Either Par or Conversion Value or Defaulted Recovery Value
		if (isTerminal()) {
			return(100);
		}
		
		// Average of 3 Possible Future States
		double value = 0;
		double upProb = myTree.getUpProb();
		double dnProb = myTree.getDnProb();
		double dt = myTree.getDt();
		double rf = myTree.getRf();
		double divYld = myTree.getDivYld();
		
		double valueSurvival = Math.exp(-1 * (rf-divYld) * dt) *
								(upProb * ((JDBinomialNode) getChildUp()).getConvertibleValue() 
										+ dnProb * ((JDBinomialNode) getChildDn()).getConvertibleValue());
		double defaultProb = getDefaultProb();
		double valueGivenDefault = 30;
		
		value = valueSurvival * (1-defaultProb) + valueGivenDefault * (defaultProb);
		
		return(value);
		
	}
	
	public double getExerciseValue() {
		
		double convertRatio = myTree.getCb().getConvertRatio();
		double convertNotional = myTree.getCb().getNotionalAmt();
		
		double numberShrsPer100 = convertRatio / convertNotional * 100;
		
		return(Math.max( getStockPrice() * numberShrsPer100, 0));		
		
	}
	
	public double getConvertibleValue() {
		return(Math.max(getExerciseValue(), getContinueValue()));
	}
	
	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double px) {
		this.stockPrice = px;
	}

	public double getBondPV() {
		return bondPV;
	}

	public void setBondPV(double bondValue) {
		this.bondPV = bondValue;
	}

	public BinomialTree getMyTree() {
		return myTree;
	}

	public void setMyTree(BinomialTree myTree) {
		this.myTree = myTree;
	}

}
