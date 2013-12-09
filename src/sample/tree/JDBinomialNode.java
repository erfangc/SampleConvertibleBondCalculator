package sample.tree;

import java.util.Map;

public class JDBinomialNode extends Node {
	
	public JDBinomialNode(boolean isRoot, boolean isTerminal,
			Map<String, Object> data, Node childUp, Node childDn, int step,
			int nodeNumber) {
		super(isRoot, isTerminal, data, childUp, childDn, step, nodeNumber);
	}

	// Private Fields that Store Crucial Node level Valuation Ingredients
	private double stockPrice, hazardRate, bondPV, continueValue, exerciseValue, convertibleValue = Double.NaN;
	private BinomialTree myTree; // Since many metrics crucial to valuation are stored at the tree level
		
	/*
	 * Calculation Functions
	 */
	
	// Simple Equity-Credit link: Lambda = 1000 / S^2
	public void setHazardRate() {
		setHazardRate(1000/Math.pow(getStockPrice(), 2));
	}
	
	public void setChildrenStockPrice() {
		
		if (isTerminal())
			return;
		
		JDBinomialNode upChild = (JDBinomialNode) getChildUp();
		JDBinomialNode dnChild = (JDBinomialNode) getChildDn();
		
		if (upChild != null && upChild.getStockPrice() == Double.NaN)
			upChild.setStockPrice(getStockPrice() * myTree.getUpMove());
		
		if (dnChild != null && dnChild.getStockPrice() == Double.NaN)
			dnChild.setStockPrice(getStockPrice() * myTree.getDnMove());
		
	}
	
	public void computeContValue() {
		
		// Continuation Value at Terminal Nodes Should be Either Par or Conversion Value or Defaulted Recovery Value
		if (isTerminal()) {
			setContinueValue(100);
			return;
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
		double defaultProb = Math.exp(getHazardRate()*dt);
		double valueGivenDefault = 30;
		
		value = valueSurvival * (1-defaultProb) + valueGivenDefault * (defaultProb);
		
		setContinueValue(value);
		
	}
	
	public void computeExerciseValue() {
		
		double convertRatio = myTree.getCb().getConvertRatio();
		double convertNotional = myTree.getCb().getNotionalAmt();
		double conversionPrice = convertNotional / convertRatio;
		
		double numberShrsPer100 = convertRatio / convertNotional * 100;
		
		setExerciseValue(Math.max( (getStockPrice() - conversionPrice) * numberShrsPer100, 0));		
		
	}
	
	public void computeNodeDerivValue() {
		setConvertibleValue(Math.max(getExerciseValue(), getContinueValue()));
	}
	
	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double px) {
		this.stockPrice = px;
	}

	public double getHazardRate() {
		if (hazardRate == Double.NaN)
			setHazardRate();
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

	public double getExerciseValue() {
		if (exerciseValue == Double.NaN)
			computeExerciseValue();
		return exerciseValue;
	}

	public void setExerciseValue(double exerciseValue) {
		this.exerciseValue = exerciseValue;
	}

	public double getContinueValue() {
		if (continueValue == Double.NaN)
			computeContValue();
		return continueValue;
	}

	public void setContinueValue(double contValue) {
		this.continueValue = contValue;
	}

	public double getConvertibleValue() {
		if (convertibleValue == Double.NaN)
			computeNodeDerivValue();
		return convertibleValue;
	}

	public void setConvertibleValue(double nodeDerivValue) {
		this.convertibleValue = nodeDerivValue;
	}

	public BinomialTree getMyTree() {
		return myTree;
	}

	public void setMyTree(BinomialTree myTree) {
		this.myTree = myTree;
	}
	
	
}
