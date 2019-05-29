package com.tbd.twitter.sentimentanalysis;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SentimentAnalysisController {
	@Autowired
	private Classifier classifier;

	@RequestMapping("/classify")
    public HashMap<String,Double> classify(@RequestParam(value="text") String text) {
        return this.classifier.classify(text);
    }
	public Classifier getClassifier() {
		return classifier;
	}
	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

}
