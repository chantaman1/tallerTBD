package com.tbd.twitter;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.tbd.twitter.Service.TweetService;
import com.tbd.twitter.sentimentanalysis.SentimentAnalysisService;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Configurable
public class TwitterListener {
	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private TwitterStream twitterStream;
	@Autowired
	private TweetService tweetService;
	@Autowired
	private SentimentAnalysisService sentimentAnalysisSercice;

	@PostConstruct
	public void run() {
		twitterStream.addListener(new StatusListener() {
			public void onStatus(Status status) {
				if(status.getPlace() == null && status.getGeoLocation() == null) {
					tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), 0, 0, "", "", status.getUser().getId(), status.getUser().getName());
				}
				else if(status.getPlace() == null && status.getGeoLocation() != null) {
					tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude(), "", "", status.getUser().getId(), status.getUser().getName());
				}
				else if(status.getPlace() != null && status.getGeoLocation() == null) {
					tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), 0, 0, status.getPlace().getName(), status.getPlace().getCountry(), status.getUser().getId(), status.getUser().getName());
				}
				else {
					tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude(), status.getPlace().getName(), status.getPlace().getCountry(), status.getUser().getId(), status.getUser().getName());
				}
				HashMap<String, Double> result = sentimentAnalysisSercice.classify(status.getText());
				double negative = result.get("negative");
				double positive = result.get("positive");
				if(positive > negative) {
					
				}
				else {
					
				}
				System.out.println("Tweet recibido.");
			}

			@Override
			public void onException(Exception arg0) {
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
			}
		});
		String[] bow=null;
		try {
			Resource resource=resourceLoader.getResource("classpath:bagofwords.txt");
			Scanner sc=new Scanner(resource.getFile());
			List<String> lines=new ArrayList<String>();
			while (sc.hasNextLine()) {
			  lines.add(sc.nextLine());
			}
			bow=lines.toArray(new String[0]);
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FilterQuery filter = new FilterQuery();
		filter.track(bow);
		filter.language(new String[]{"es"});
		twitterStream.filter(filter);
	}

	public TwitterStream getTwitterStream() {
		return twitterStream;
	}

	public void setTwitterStream(TwitterStream twitterStream) {
		this.twitterStream = twitterStream;
	}
}
