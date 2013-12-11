package sample.general;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.date.SerialDate;
import org.jfree.date.SpreadsheetDate;

import sample.instrument.ConvertibleBond;
import sample.instrument.Stock;
import sample.tree.BinomialTree.GraphicType;
import sample.tree.calculators.TreeAnalyticProcessor;

public class Driver {

	public static final Logger LOG = Logger.getLogger(Driver.class);
	public static SerialDate analysisDate = new SpreadsheetDate(10,12,2013);
	public static double riskFreeRate = 0.01;
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("resources/log4j.cfg");
		
		boolean calcDelta = true, printTree = false;
		
		// 3% Dividend, 20% Volatility
		double stockPx = 55, divYld = 0.03, vol = 0.23;
		Stock stock = new Stock(stockPx, divYld, vol);
		
		// 5 Years to Maturity, 5% Coupon
		double cvr = 20, parAmt = 1000, cpn = 5.0, convertPrice = 110, assumedHazardRate = 0.05;
		ConvertibleBond cb = new ConvertibleBond(cvr, parAmt, new SpreadsheetDate(31,12,2015), cpn, stock);		
		TreeAnalyticProcessor p = new TreeAnalyticProcessor(cb);		
		
		// Valuation Test
		LOG.info(cb);		
		LOG.info("Computed Price: "+p.calcPrice(assumedHazardRate));
		if (printTree) {
			LOG.info(p.getTree().getGraphic(GraphicType.STOCK_PRICE));
			LOG.info(p.getTree().getGraphic(GraphicType.EXERCISE_VALUE));
			LOG.info(p.getTree().getGraphic(GraphicType.CONVERTIBLE_VALUE));
			LOG.info(p.getTree().getGraphic(GraphicType.CONTINUATION_VALUE));
			LOG.info(p.getTree().getGraphic(GraphicType.DEFAULT_PROBABILITY));
		}
		
		// Calibration Test
		LOG.info("Assuming Price="+convertPrice);
		double calibrResult = p.calibrate(convertPrice);
		LOG.info("Calibrated Constant to: "+calibrResult);
		
		if (calcDelta==true) {
			calcDelta(cb, p, assumedHazardRate);
		}
		
	}

	public static void calcDelta(ConvertibleBond cb, TreeAnalyticProcessor p, double hazardRateConstant) {
		
		double origUndPx = cb.getUnderlyingStock().getCurrentPrice(), shkSize = 0.1;
		LOG.info("Computing Delta using Shock Size="+shkSize);
		
		cb.getUnderlyingStock().setCurrentPrice(origUndPx+shkSize);
		double upPx = p.calcPrice(hazardRateConstant);
		
		cb.getUnderlyingStock().setCurrentPrice(origUndPx-shkSize);
		double dnPx = p.calcPrice(hazardRateConstant);
		
		double delta = Math.max(0,(upPx - dnPx) / (2*shkSize));
		double adjDelta = Math.max(0,(( cb.getNotionalAmt() / 100 )) * delta / cb.getConvertRatio());
		
		LOG.info("upPx="+upPx+" dnPx="+dnPx+" delta="+delta+" adjDelta="+adjDelta+" conversionPrice="+cb.getConversionPrice()+" stockPrice="+origUndPx);
	}

}
