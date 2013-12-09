/**
 * 
 */
package sample.tree.calculators;

import java.util.ArrayList;

import sample.instrument.ConvertibleBond;
import sample.tree.BinomialTree;
import sample.tree.JDBinomialNode;

/**
 * This class takes a BinomialTree object as an input
 * then populate all the nodes with stock prices
 * 
 * Optionally, Conditional hazard rate will also be populated
 * 
 * @author erfangc
 *
 */
public class StockPriceGenerator {

	private BinomialTree tree;
	private double s0, vol, rf, divYield, yrsToMaturity;
	
	/**
	 * Populates the tree with a stock price using CRR for each node given the parameters (standard for a Binomial tree)
	 * 
	 * @param s0 Initial Stock Price
	 * @param vol Constant Volatility used to Extract Up/Down movements
	 * @param rf Risk Free Rate
	 * @param divYld Dividend Yield
	 */
	
	public void generateStockPrice(double s0, double vol, double rf, double divYld, double yrsToMaturity) {
		
		double upMove, dnMove = 0;
		double dt = (yrsToMaturity / tree.getNumSteps());
		upMove = Math.exp(vol * Math.sqrt(dt));
		dnMove = Math.exp(-1 * vol * Math.sqrt(dt));
		
		ArrayList<ArrayList<JDBinomialNode>> mainTree = tree.getMasterTree();
		tree.getRootNode().setNodeEquityValue(s0);
		
		for (ArrayList<JDBinomialNode> step : mainTree) {
			for (JDBinomialNode node : step) {
				genChildrenStockPrice(node, upMove, dnMove);
			}
		}
		
	}
	
	/**
	 * 
	 * @param node The node for which children will be retrieved
	 * @param u Up Move = 1 + Up Amount
	 * @param d Down Move
	 */
	private void genChildrenStockPrice(JDBinomialNode node, double u, double d) {
		((JDBinomialNode) node.getChildren().get(0)).setNodeEquityValue(node.getNodeEquityValue() * u); // Up
		((JDBinomialNode) node.getChildren().get(0)).setNodeEquityValue(node.getNodeEquityValue() * d); // Up
	}
	
	public void generateStockPrice() {
		this.generateStockPrice(s0, vol, rf, divYield, yrsToMaturity);
	}
	
	public StockPriceGenerator() {
		super();
	}

	public StockPriceGenerator(BinomialTree tree, ConvertibleBond cb) {
		this.tree = tree;
		setVol(cb.getUnderlyingStock().getVolatility());
		setS0(cb.getUnderlyingStock().getCurrentPrice());
		setDivYield(cb.getUnderlyingStock().getDivYld());
		setRf(0);
	}



	//Setters and Getters
	public BinomialTree getTree() {
		return tree;
	}

	public void setTree(BinomialTree tree) {
		this.tree = tree;
	}

	public double getS0() {
		return s0;
	}

	public void setS0(double s0) {
		this.s0 = s0;
	}

	public double getVol() {
		return vol;
	}

	public void setVol(double vol) {
		this.vol = vol;
	}

	public double getRf() {
		return rf;
	}

	public void setRf(double rf) {
		this.rf = rf;
	}

	public double getDivYield() {
		return divYield;
	}

	public void setDivYield(double divYield) {
		this.divYield = divYield;
	}

	public double getYrsToMaturity() {
		return yrsToMaturity;
	}

	public void setYrsToMaturity(double yrsToMaturity) {
		this.yrsToMaturity = yrsToMaturity;
	}
	
}
