package com.tbd.twitter.sentimentanalysis;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SentimentAnalysisService {
	@Autowired
	private Classifier classifier;

    public HashMap<String,Double> classify(String text) {
        return this.classifier.classify(text);
    }
	public Classifier getClassifier() {
		return classifier;
	}
	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

}
