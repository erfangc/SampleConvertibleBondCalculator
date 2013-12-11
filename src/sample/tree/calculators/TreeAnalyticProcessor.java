/**
 * 
 */
package sample.tree.calculators;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import sample.instrument.ConvertibleBond;
import sample.tree.BinomialTree;
import sample.tree.JDBinomialNode;
import sample.tree.JDTreeUntil;

/**
 * This is a command class that will call upon nodes in a Binomial tree to populate values
 * in the correct sequence with the ultimate goal of arriving at a price
 * 
 * See the signature computePrice method for details   
 * 
 * @author erfangc
 *
 */
public class TreeAnalyticProcessor {

	public static final Logger LOG = Logger.getLogger(TreeAnalyticProcessor.class);
	
	private BinomialTree tree;
	private ConvertibleBond cb;
	
	public TreeAnalyticProcessor(ConvertibleBond cb) {
		this.cb = cb;
	}
	
	/**
	 * Given a input price, iterate with various initial hazard rate until reaching the correct price
	 * 
	 * @param price
	 * @return the implied spread
	 */
	public double calibrate(double price) {
		
		final double TOLERANCE = 1E-10;
		final long MAX_ITER = 500;
		
		double haz = 0.5;
		double hazPrev = 0.05;
		double hazNext = 0;		
				
		double error = 1;
		int iter = 1;
		
	    while ((Math.abs(error) > TOLERANCE ) && (iter < MAX_ITER) && rootFunction(hazNext, price)!=0) {
	    
	      double currentY = rootFunction(haz, price); 
	      double prevY = rootFunction(hazPrev, price);
	      
	      double deltaY = currentY - prevY, deltaX = haz-hazPrev;
	      
	      hazNext = haz - currentY *(deltaX)/deltaY;
	      
	      hazPrev = haz;
	      haz = hazNext;
	      error = hazNext-haz;
	      
	      iter++;
	    }
	    
	    if (iter >= MAX_ITER && Math.abs(error)>TOLERANCE) LOG.info("calibration cannot find root to match price " + price + " after " + MAX_ITER + " iterations");
	    LOG.info("Calibration Status: error="+error+" iter="+iter+" hazNext="+hazNext);
	    		
		return hazNext;
	}
	
	// Helper for calibrate
	public double rootFunction(double cnst, double price) {
		return calcPrice(cnst) - price;
	}

	/**
	 * Compute the price of a bond
	 * The basic steps involved here are:
	 * 
	 * 1) Create a empty Binomial tree
	 * 2) Populate the Shared parameters for every node, which is stored in the tree (not in the nodes) (Volatility, Up Move Size ... etc )
	 * 3) Generate the Equity prices associated with each node
	 * 4) Backward Induct the Bond's price by calling getDerivValue() on the Terminal nodes then move backwards
	 *  
	 * @return price of the bond
	 */
	public double calcPrice(double hazardRate) {
			
		double hrCalibrCoeff = JDTreeUntil.hazardRateToCalibrationCoefficent(hazardRate, cb);
		// 1,2) Create an empty binomial tree and populate it with the correct parameters
		if (tree == null) {
			tree = JDTreeUntil.getDailySteppedTree(cb, hrCalibrCoeff);
		} else {
			tree.setHazardRateCalibrCnst(hrCalibrCoeff);
			tree.resetNodes();
		}
		
		// 3) Generate Equity Process for each node
		tree.getRootNode().setStockPrice(cb.getUnderlyingStock().getCurrentPrice());
		ArrayList<ArrayList<JDBinomialNode>> treeNodes = tree.getMasterTree();
		// Note Here we are Forward Inducting - Need to Know the Stock Price for t before we can deduce Price for t+1
		for (ArrayList<JDBinomialNode> step : treeNodes) {
			for (JDBinomialNode node : step) {
				node.setChildrenStockPrice();
			}
		}
		
		// 4) Generate Derivative (CB) price based on the generated Equity prices
		// Notice we are iterating backwards, we need to know the payoff at time t+1 before we can know the expected value at time t
		for(int step = treeNodes.size() - 1; step >= 0; step--){
			  for (JDBinomialNode node : treeNodes.get(step)) {
				node.getConvertibleValue();
			}
		}

		return tree.getRootNode().getConvertibleValue();	
		
	}
	
	// Setters and Getters
	public BinomialTree getTree() {
		return tree;
	}

	public void setTree(BinomialTree tree) {
		this.tree = tree;
	}

	public ConvertibleBond getCb() {
		return cb;
	}

	public void setCb(ConvertibleBond cb) {
		this.cb = cb;
	}
	
}
