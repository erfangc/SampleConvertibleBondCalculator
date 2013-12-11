package sample.bondfeature.impl;

import sample.bondfeature.Feature;
import sample.bondfeature.FeatureProcessor.FeatureType;
import sample.instrument.ConvertibleBond;
import sample.tree.JDBinomialNode;

public class ConversionFeature implements Feature {

	private ConvertibleBond cb;
	
	public ConversionFeature(ConvertibleBond cb) {
		this.cb = cb;
	}

	@Override
	public double featureValue(JDBinomialNode node) {
		double convertRatio = getCb().getConvertRatio();
		double convertNotional = getCb().getNotionalAmt();
		
		double numberShrsPer100 = convertRatio / convertNotional * 100;
		return Math.max( node.getStockPrice() * numberShrsPer100, 0);
	}

	@Override
	public boolean isForced(JDBinomialNode node) {
		return false;
	}

	@Override
	public String getFeatureName() {
		return FeatureType.CONVERSION.toString();
	}

	public ConvertibleBond getCb() {
		return cb;
	}

	public void setCb(ConvertibleBond cb) {
		this.cb = cb;
	}

	@Override
	public FeatureType getFeatureEnum() {
		return FeatureType.CONVERSION;
	}

}
