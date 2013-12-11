package sample.bondfeature.impl;

import java.util.Map;

import org.jfree.date.SerialDate;

import sample.bondfeature.Feature;
import sample.bondfeature.FeatureProcessor.FeatureType;
import sample.instrument.ConvertibleBond;
import sample.tree.JDBinomialNode;

public class CallFeature implements Feature {

	private Map<SerialDate, Double> callDatePriceMap;
	private ConvertibleBond cb;
	
	public CallFeature(Map<SerialDate, Double> callDatePriceMap,
			ConvertibleBond cb) {
		this.callDatePriceMap = callDatePriceMap;
		this.cb = cb;
	}

	@Override
	public double featureValue(JDBinomialNode node) {
		//TODO Return the Call Price if a Call is Effective
		return 0;
	}

	@Override
	public boolean isForced(JDBinomialNode node) {
		return true;
	}

	@Override
	public String getFeatureName() {
		return FeatureType.EUROPEAN_CALL.toString();
	}

	public Map<SerialDate, Double> getCallDatePriceMap() {
		return callDatePriceMap;
	}

	public void setCallDatePriceMap(Map<SerialDate, Double> callDatePriceMap) {
		this.callDatePriceMap = callDatePriceMap;
	}

	public ConvertibleBond getCb() {
		return cb;
	}

	public void setCb(ConvertibleBond cb) {
		this.cb = cb;
	}

	@Override
	public FeatureType getFeatureEnum() {
		return FeatureType.EUROPEAN_CALL;
	}

}
