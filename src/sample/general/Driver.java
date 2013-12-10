package sample.general;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sample.instrument.ConvertibleBond;
import sample.instrument.Stock;
import sample.tree.BinomialTree.GraphicType;
import sample.tree.calculators.TreeAnalyticProcessor;

public class Driver {

	public static final Logger LOG = Logger.getLogger(Driver.class);
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("resources/log4j.cfg");
		
		// 100 px, 3% div, 20% vol
		Stock stock = new Stock(100, 0.03, 0.2);
		
		// CvR=0.8, Per 100, 5 Years to Maturity, 5% Coupon
		ConvertibleBond cb = new ConvertibleBond(1, 100, 5 , 5.0, stock);
		
		TreeAnalyticProcessor p = new TreeAnalyticProcessor(cb);		
		
		// Valuation Test
		LOG.info(cb);		
		LOG.info("Computed Price: "+p.calcPrice());
		LOG.info(p.getTree().getGraphic(GraphicType.STOCK_PRICE));
		LOG.info(p.getTree().getGraphic(GraphicType.CONTINUATION_VALUE));
		LOG.info(p.getTree().getGraphic(GraphicType.EXERCISE_VALUE));
		LOG.info(p.getTree().getGraphic(GraphicType.CONVERTIBLE_VALUE));
		LOG.info(p.getTree().getGraphic(GraphicType.DEFAULT_PROBABILITY));
		LOG.info(p.getTree().getGraphic(GraphicType.HAZARD_RATE));
		
		// Calibration Test
		double px = 109.54;
		LOG.info("Assuming Price="+px);
		double calibrResult = p.calibrate(px);
		LOG.info("Calibrated Constant to: "+calibrResult);		
		
		double origUndPx = cb.getUnderlyingStock().getCurrentPrice(), shkSize = 0.01;
		LOG.info("Computing Delta using Shock Size="+shkSize);
		
		cb.getUnderlyingStock().setCurrentPrice(origUndPx+shkSize);
		double upPx = p.calcPrice(calibrResult);
		
		cb.getUnderlyingStock().setCurrentPrice(origUndPx-shkSize);
		double dnPx = p.calcPrice(calibrResult);
		
		double delta = (upPx - dnPx) / (2*shkSize);
		LOG.info("upPx="+upPx+" dnPx="+dnPx+" delta="+delta);		
	}

}
