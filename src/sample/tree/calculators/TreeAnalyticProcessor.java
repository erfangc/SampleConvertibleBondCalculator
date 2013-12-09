/**
 * 
 */
package sample.tree.calculators;

import java.util.ArrayList;

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

	private BinomialTree tree;
	private ConvertibleBond cb;
	private double price = Double.NaN; // Output
	
	public TreeAnalyticProcessor(ConvertibleBond cb) {
		this.cb = cb;
	}

	/**
	 * Compute the price of a bond
	 * The basic steps involved here are:
	 * 
	 * 1) Create a empty Binomial tree
	 * 2) Populate the Shared parameters for every node (vol, Up Move size ... etc )
	 * 3) Generate the Equity prices associated with each node
	 * 4) Backward Induct the Bond's price by calling getDerivValue() on the Terminal nodes then move backwards
	 *  
	 * @return price of the bond
	 */
	public double computePrice() {
		
		// 1,2) Create an empty binomial tree and populate it with the correct parameters
		tree = JDTreeUntil.initializeBinomialTreeWithParams(cb, 5);
		
		// 3) Generate Equity Process for each node
		ArrayList<ArrayList<JDBinomialNode>> treeNodes = tree.getMasterTree();
		for (ArrayList<JDBinomialNode> step : treeNodes) {
			for (JDBinomialNode node : step) {
				node.setChildrenStockPrice();
			}
		}
		
		// 4) Generate Derivative (CB) price based on the generated Equity prices
		// Notice we are iterating backwards
		for(int step = treeNodes.size() - 1; step >= 0; step--){
			  for (JDBinomialNode node : treeNodes.get(step)) {
				double value = node.getConvertibleValue();
				System.out.println("value="+value+"\n node info:\n"+node);
			}
		}
		setPrice(tree.getRootNode().getConvertibleValue());
		return  getPrice(); // Price of the Convertible Bond
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
