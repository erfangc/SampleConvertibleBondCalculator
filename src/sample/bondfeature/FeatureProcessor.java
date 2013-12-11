package sample.bondfeature;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sample.bondfeature.impl.ConversionFeature;
import sample.instrument.ConvertibleBond;
import sample.tree.JDBinomialNode;

// Every Should Tree has One of These
public class FeatureProcessor {
	
	public enum FeatureType {
		
		CONVERSION("Conversion"),
		EUROPEAN_CALL("European Call");
		
		private String featureName;
		@Override
		public String toString() {
			return featureName;
		}
		private FeatureType(String name) {
			featureName = name;
		}
	}
	
	public static final Logger LOG = Logger.getLogger(FeatureProcessor.class);
	private Map<FeatureType, Feature> features;

	public Map<String, Double> processNode(JDBinomialNode node) {
		Map<String, Double> results = new HashMap<String, Double>();
		for (Feature f : features.values()) {
			results.put(f.getFeatureName(), f.featureValue(node));
		}
		return results;
	}
	
	public double processFeature(FeatureType feature, JDBinomialNode node) {		
		if (!getFeatures().containsKey(feature)) {
			LOG.error("Cannot Find Feature: "+feature);
			return Double.NaN;
		}		
		return getFeatures().get(feature).featureValue(node);		
	}
	
	public Feature get(Object key) {
		return features.get(key);
	}

	public Feature put(FeatureType key, Feature value) {
		return features.put(key, value);
	}

	// Constructor
	public FeatureProcessor(ConvertibleBond cb) {
		features = new HashMap<FeatureType, Feature>();
		// Add Features - In Reality We will Need to Query the Bond itself to Determine What Features are Available
		features.put(FeatureType.CONVERSION, new ConversionFeature(cb));
	}

	public Map<FeatureType, Feature> getFeatures() {
		return features;
	}

	public void setFeatures(Map<FeatureType, Feature> features) {
		this.features = features;
	}
	
}
