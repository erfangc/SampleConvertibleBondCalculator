package sample.bondfeature;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sample.bondfeature.impl.ConversionFeature;
import sample.instrument.ConvertibleBond;
import sample.tree.JDBinomialNode;

// Every Should Tree has One of These
public class FeatureProcessor {
	
	public enum FeatureList {
		CONVERSION("Conversion");
		private String featureName;
		@Override
		public String toString() {
			return featureName;
		}
		private FeatureList(String name) {
			featureName = name;
		}
	}
	
	public static final Logger LOG = Logger.getLogger(FeatureProcessor.class);
	private Map<FeatureList, Feature> features;

	public Map<String, Double> processNode(JDBinomialNode node) {
		Map<String, Double> results = new HashMap<String, Double>();
		for (Feature f : features.values()) {
			results.put(f.getFeatureName(), f.featureValue(node));
		}
		return results;
	}
	
	public double processFeature(FeatureList feature, JDBinomialNode node) {		
		if (!getFeatures().containsKey(feature)) {
			LOG.error("Cannot Find Feature: "+feature);
			return Double.NaN;
		}		
		return getFeatures().get(feature).featureValue(node);		
	}
	
	public Feature get(Object key) {
		return features.get(key);
	}

	public Feature put(FeatureList key, Feature value) {
		return features.put(key, value);
	}

	// Constructor
	public FeatureProcessor(ConvertibleBond cb) {
		features = new HashMap<FeatureList, Feature>();
		// Add Features - In Reality We will Need to Query the Bond itself to Determine What Features are Availiable
		features.put(FeatureList.CONVERSION, new ConversionFeature(cb));
	}

	public Map<FeatureList, Feature> getFeatures() {
		return features;
	}

	public void setFeatures(Map<FeatureList, Feature> features) {
		this.features = features;
	}
	
}
