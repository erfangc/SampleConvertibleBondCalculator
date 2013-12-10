package sample.general;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sample.instrument.ConvertibleBond;
import sample.instrument.Stock;
import sample.tree.calculators.TreeAnalyticProcessor;

public class Driver {

	public static final Logger LOG = Logger.getLogger(Driver.class);
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("resources/log4j.cfg");
		
		// 100 px, 3% div, 20% vol
		Stock stock = new Stock(100, 0.03, 0.2);
		
		// CvR=0.8, Per 100, 5 Years to Maturity, 5% Coupon
		ConvertibleBond cb = new ConvertibleBond(0.8, 100, 5 , 5.0, stock);
		
		TreeAnalyticProcessor p = new TreeAnalyticProcessor(cb);		
		
		LOG.info(cb);
		
		LOG.info("Computed Price: "+p.computePrice());
	}

}
