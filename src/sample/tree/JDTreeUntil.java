package sample.tree;

import sample.instrument.ConvertibleBond;

public class JDTreeUntil {

	/**
	 * 
	 * Resolve some basic parameters in the convert Jump-diffusion model and transform these into
	 * lattice friendly values to be consumed by the tree itself and the tree process
	 * 
	 * @param cb The Convertible Bond being Valued
	 * @return List of Args to Be passed to the Lattice
	 */
	public static BinomialTree initializeBinomialTreeWithParams(ConvertibleBond cb, int treeSteps) {
		
		BinomialTree tree = new BinomialTree(treeSteps);
		
		double rf = 0.0;
		double divYld = cb.getUnderlyingStock().getDivYld();
		double vol = cb.getUnderlyingStock().getVolatility();		
		double dt = cb.getYearsToMaturity() / treeSteps;
		
		// A little more involved stuff
		double upMove = Math.exp(vol * Math.sqrt(dt));
		double dnMove = Math.exp(-1*vol*Math.sqrt(dt));
		double upProb = (Math.exp((rf-divYld)*dt)-dnMove) / (upMove - dnMove);
		double dnProb = 1 - upProb;
		
		tree.setRf(rf);
		tree.setDivYld(divYld);
		tree.setVol(vol);
		tree.setDt(dt);
		
		tree.setUpMove(upMove);
		tree.setDnMove(dnMove);
		tree.setUpProb(upProb);
		tree.setDnProb(dnProb);
		
		tree.setCb(cb);
		
		return tree;
	}
	
}
