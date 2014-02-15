package sample.general;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.date.SerialDate;
import org.jfree.date.SpreadsheetDate;

import sample.instrument.ConvertibleBond;
import sample.instrument.Stock;
import sample.tree.JDBinomialTree.GraphicType;
import sample.tree.calculators.TreeAnalyticProcessor;
import sample.tree.calculators.TreeAnalyticProcessor.TreeSubtype;

public class Driver {

	public static final Logger LOG = Logger.getLogger(Driver.class);
	public static SerialDate analysisDate = new SpreadsheetDate(10, 12, 2013);
	public static double riskFreeRate = 0.02;

	public static boolean calcDelta = true, printTree = true,
			calibrate = false;

	public static void main(String[] args) {

		PropertyConfigurator.configure("resources/log4j.cfg");

		// 3% Dividend, 20% Volatility
		double stockPx = 55, divYld = 0.04, vol = 0.23;
		Stock stock = new Stock(stockPx, divYld, vol);

		// 5 Years to Maturity, 5% Coupon
		double cvr = 17.5, parAmt = 1000, cpn = 5.0, convertPrice = 110, assumedHazardRate = 0.1;
		ConvertibleBond cb = new ConvertibleBond(cvr, parAmt,
				new SpreadsheetDate(31, 12, 2015), cpn, stock);
		TreeAnalyticProcessor p = new TreeAnalyticProcessor(cb);

		// Valuation Test
		LOG.info(cb);
		LOG.info("Computed Price of a Daily Step Tree: "
				+ p.calcPrice(assumedHazardRate));
		p.setTree(null);
		LOG.info("Computed Price of a 5 Step Tree: "
				+ p.calcPrice(assumedHazardRate, TreeSubtype.ForcedStepTree, 5));
		p.setTree(null);
		LOG.info("Computed Price of a 10 Step Tree: "
				+ p.calcPrice(assumedHazardRate, TreeSubtype.ForcedStepTree, 10));
		p.setTree(null);
		LOG.info("Computed Price of a 50 Step Tree: "
				+ p.calcPrice(assumedHazardRate, TreeSubtype.ForcedStepTree, 50));
		p.setTree(null);
		LOG.info("Computed Price of a 100 Step Tree: "
				+ p.calcPrice(assumedHazardRate, TreeSubtype.ForcedStepTree,
						100));
		p.setTree(null);
		LOG.info("Computed Price of a 200 Step Tree: "
				+ p.calcPrice(assumedHazardRate, TreeSubtype.ForcedStepTree,
						200));
		p.setTree(null);
		LOG.info("Computed Price of a 500 Step Tree: "
				+ p.calcPrice(assumedHazardRate, TreeSubtype.ForcedStepTree,
						500));
		p.setTree(null);

		if (printTree) {
			p.setTree(null);
			LOG.info("Computed Price of a 10 Step Tree: "
					+ p.calcPrice(assumedHazardRate,
							TreeSubtype.ForcedStepTree, 10));
			LOG.info(p.getTree().getGraphic(GraphicType.STOCK_PRICE));
			LOG.info(p.getTree().getGraphic(GraphicType.CONVERSION_VALUE));
			LOG.info(p.getTree().getGraphic(GraphicType.CONVERTIBLE_VALUE));
			LOG.info(p.getTree().getGraphic(GraphicType.CONTINUATION_VALUE));
			LOG.info(p.getTree().getGraphic(GraphicType.DEFAULT_PROBABILITY));
		}

		// Calibration Test (Currently Failing Miserably with my own
		// Implementation of the Secant Method)
		if (calibrate == true) {
			LOG.info("Assuming Price=" + convertPrice);
			double calibrResult = p.calibrate(convertPrice);
			LOG.info("Calibrated Constant to: " + calibrResult);
		}

		// Delta Test
		if (calcDelta == true) {
			calcDelta(cb, p, assumedHazardRate);
		}

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		LOG.info("Execution Complete at " + dateFormat.format(new Date()));

	}

	public static void calcDelta(ConvertibleBond cb, TreeAnalyticProcessor p,
			double hazardRateConstant) {
		double origUndPx = cb.getUnderlyingStock().getCurrentPrice(), shkSize = 0.01;
		LOG.info("Computing Delta using Shock Size=" + shkSize);

		cb.getUnderlyingStock().setCurrentPrice(origUndPx + shkSize);
		double upPx = p.calcPrice(hazardRateConstant);

		cb.getUnderlyingStock().setCurrentPrice(origUndPx - shkSize);
		double dnPx = p.calcPrice(hazardRateConstant);
		double delta = Math.max(0, (upPx - dnPx) / (2 * shkSize));
		double adjDelta = Math.max(0, ((cb.getNotionalAmt() / 100)) * delta
				/ cb.getConvertRatio());

		LOG.info("upPx=" + upPx + " dnPx=" + dnPx + " delta=" + delta
				+ " adjDelta=" + adjDelta + " conversionPrice="
				+ cb.getConversionPrice() + " stockPrice=" + origUndPx);
	}

}
