package sample.tree;

import org.apache.log4j.Logger;

import sample.general.Driver;
import sample.instrument.ConvertibleBond;

public class JDTreeUntil {

	public static final Logger LOG = Logger.getLogger(JDTreeUntil.class);
	
	/** 
	 * Resolve some basic parameters in the convert Jump-diffusion model and transform these into
	 * lattice friendly values to be consumed by the tree itself and the tree process
	 * 
	 * @param cb The Convertible Bond being Valued
	 * @param hazardInit The initial hazard rate constant
	 * @return The Binomial Tree Object with all the Shared Parameters Resolved
	 */
	public static JDBinomialTree getTreeForceSteps(ConvertibleBond cb, int treeSteps, double hazardInit) {
				
		JDBinomialTree tree = new JDBinomialTree(treeSteps, cb);
		
		double rf = Driver.riskFreeRate;
		double divYld = cb.getUnderlyingStock().getDivYld();
		double vol = cb.getUnderlyingStock().getVolatility();		
		double dt = cb.getYearsToMaturity(Driver.analysisDate) / treeSteps;
		
		// A little more involved stuff
		double upMove = Math.exp(vol * Math.sqrt(dt));
		double dnMove = Math.exp(-1*vol*Math.sqrt(dt));
		double upProb = (Math.exp((rf-divYld)*dt)-dnMove) / (upMove - dnMove);
		double dnProb = 1 - upProb;
		
		tree.setRf(rf);
		tree.setDivYld(divYld);
		tree.setVol(vol);
		tree.setDt(dt);
		tree.setHazardRateCalibrCnst(hazardInit);
		
		tree.setUpMove(upMove);
		tree.setDnMove(dnMove);
		tree.setUpProb(upProb);
		tree.setDnProb(dnProb);
		
		LOG.info(tree);
		
		return tree;
	}
	
	/**
	 * This Method Creates a Binomial Tree with a Step for Every Day Until Maturity
	 * 
	 * @param cb The Convertible Bond
	 * @param hazardInit The initial hazard rate constant
	 * @return The Binomial Tree Object with all the Shared Parameters Resolved
	 */
	public static JDBinomialTree getDailySteppedTree(ConvertibleBond cb, double hazardInit) {

		// Resolve the # of Steps Needed
		int treeSteps = cb.getDaysToMaturity(Driver.analysisDate);
		return JDTreeUntil.getTreeForceSteps(cb, treeSteps, hazardInit);
		
	}
	
	/**
	 * Returns the calibration coefficient given the hazard rate
	 * @param hazardRate The Hazard Rate
	 * @param cb Convertible Bond Object, Underlying Price will Be Retrieved from This
	 * @return The coefficient to arrive at node specific hazard rate 
	 */
	public static double hazardRateToCalibrationCoefficent(double hazardRate, ConvertibleBond cb) {
		return hazardRate * Math.pow(cb.getUnderlyingStock().getCurrentPrice(), 2);
	}
	
}
