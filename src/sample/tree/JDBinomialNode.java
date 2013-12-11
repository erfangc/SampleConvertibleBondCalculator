package sample.tree;

import org.apache.log4j.Logger;
import org.jfree.date.SerialDate;
import org.jfree.date.SpreadsheetDate;

import sample.bondfeature.FeatureProcessor.FeatureList;

public class JDBinomialNode extends Node {
	
	public static final Logger LOG = Logger.getLogger(JDBinomialNode.class);
	
	// Private Fields that Store Crucial Node level Valuation Ingredients
	private double stockPrice = Double.NaN, bondPV = Double.NaN, continueValue = Double.NaN, defaultProb = Double.NaN, convertibleValue = Double.NaN, hazardRate = Double.NaN, conversionValue = Double.NaN;
	private SerialDate date; // Important for Determining Eligibility for Call/Put/Makewhole etc ... and for Accrued Interest Computation
	private BinomialTree myTree; // Since many metrics crucial to valuation are stored at the tree level

	public JDBinomialNode(boolean isRoot, boolean isTerminal, Node childUp, Node childDn, int step,
			int nodeNumber) {
		super(isRoot, isTerminal, childUp, childDn, step, nodeNumber);
	}
	
	/*
	 * Calculation Functions
	 */
	
	// Simple Equity-Credit link: Lambda = c / S^2
	public double getHazardRate() {
		if (Double.isNaN(hazardRate))
			hazardRate = getMyTree().getHazardRateCalibrCnst()/Math.pow(getStockPrice(), 2); 
		return hazardRate;
	}
	
	public double getDefaultProb() {
		if (Double.isNaN(defaultProb)) {
			double dt = myTree.getDt();
			defaultProb = 1-Math.exp(-1*getHazardRate()*dt);
		}
		
		return defaultProb;
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
		if (Double.isNaN(continueValue)) {
			// Continuation Value at Terminal Nodes Should be Either Par or Conversion Value or Defaulted Recovery Value
			if (isTerminal()) {
				continueValue = Math.max(getConversionValue(), 100);
			} else {				
				// Average of 3 Possible Future States
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
				
				continueValue = valueSurvival * (1-defaultProb) + valueGivenDefault * (defaultProb);
			}
		}
		return continueValue;
	}
	
	public double getConversionValue() {
		if (Double.isNaN(conversionValue)) {
			conversionValue = myTree.getFeatureProcessor().processFeature(FeatureList.CONVERSION, this);
		}
		return conversionValue;		
	}
	
	public double getConvertibleValue() {
		// For example, even exerciseValue should the result of a feature, namely the conversion feature
		if (Double.isNaN(convertibleValue)) {
			convertibleValue = Math.max(getConversionValue(), getContinueValue());
		}
		return convertibleValue;
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
	
	/**
	 * Resets The Node Parameters
	 */
	public void reset() {
		
		stockPrice = Double.NaN;
		bondPV = Double.NaN;
		continueValue = Double.NaN;
		defaultProb = Double.NaN;
		convertibleValue = Double.NaN;
		hazardRate = Double.NaN;
		conversionValue = Double.NaN;
		
	}

	public SerialDate getDate() {
		if (date == null) {
			// The Date of the Current Node Depends on the 
			// Node's Step and the Maturity of the Convert
			// TODO Figure out how to retrieve an accurate date given the step # of the current node, the total # of nodes and the Bond's maturity
			date = new SpreadsheetDate(0, 1, 1970);			
		}
		return date;
	}

	public void setDate(SerialDate date) {
		this.date = date;
	}

}
