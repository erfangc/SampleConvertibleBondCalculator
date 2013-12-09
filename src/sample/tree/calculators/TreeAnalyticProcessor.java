/**
 * 
 */
package sample.tree.calculators;

import sample.instrument.ConvertibleBond;
import sample.tree.BinomialTree;
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

	private BinomialTree tree;
	private ConvertibleBond cb;
	private double price = Double.NaN; // Output
	
	public TreeAnalyticProcessor(ConvertibleBond cb,
			double price) {
		this.cb = cb;
		this.price = price;
	}

	/**
	 * Compute the price of a bond
	 * The basic steps involved here are:
	 * 
	 * 1) Create a empty Binomial tree
	 * 2) Populate the Shared parameters for every node (vol, Up Move size ... etc )
	 * 3) Generate the Equity prices associated with each node
	 * 4) Generate hazard rate based on the equity prices
	 * 5) Generate up/down/default probabilities
	 * 6) Backward Induct the Bond's price by calling getDerivValue() on the Terminal nodes then move backwards
	 *  
	 * @return price of the bond
	 */
	private double computePrice() {
		
		// 1,2) Create an empty binomial tree and populate it with the correct parameters
		tree = JDTreeUntil.initializeBinomialTreeWithParams(cb, 2);
		
		//TODO Implement the Other Steps		
		
		return 0.0;
	}
	
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

	public double getPrice() {
		if (price == Double.NaN) {
			setPrice(computePrice());			
		}
		return this.price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
}
