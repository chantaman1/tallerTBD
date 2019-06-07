package com.tbd.twitter;

import javax.annotation.PostConstruct;

import com.tbd.twitter.Models.ListaPalabra;
import com.tbd.twitter.Service.ListaPalabraService;
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
import java.util.*;

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
	private SentimentAnalysisService sentimentAnalysisService;
	@Autowired
	private ListaPalabraService listaPalabraService;

	@PostConstruct
	public void run() {
		twitterStream.addListener(new StatusListener() {
			public void onStatus(Status status) {
				/*String userLocation = status.getUser().getLocation();
				int isFromChile = userLocation.indexOf("chile");
				if(isFromChile > 0){

				}*/
				HashMap<String, Double> result = sentimentAnalysisService.classify(status.getText());
				double negative = result.get("negative");
				double positive = result.get("positive");
				if(status.getPlace() == null && status.getGeoLocation() == null) {
					if(positive > negative) {
						tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), 0, 0, "", "", status.getUser().getId(), status.getUser().getName(), status.getUser().getFollowersCount(), "positive");
					}
					else {
						tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), 0, 0, "", "", status.getUser().getId(), status.getUser().getName(), status.getUser().getFollowersCount(), "negative");
					}
				}
				else if(status.getPlace() == null && status.getGeoLocation() != null) {
					if(positive > negative){
						tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude(), "", "", status.getUser().getId(), status.getUser().getName(), status.getUser().getFollowersCount(), "positive");
					}
					else{
						tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude(), "", "", status.getUser().getId(), status.getUser().getName(), status.getUser().getFollowersCount(), "negative");
					}
				}
				else if(status.getPlace() != null && status.getGeoLocation() == null) {
					if(positive > negative){
						tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), 0, 0, status.getPlace().getName(), status.getPlace().getCountry(), status.getUser().getId(), status.getUser().getName(), status.getUser().getFollowersCount(), "positive");
					}
					else{
						tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), 0, 0, status.getPlace().getName(), status.getPlace().getCountry(), status.getUser().getId(), status.getUser().getName(), status.getUser().getFollowersCount(), "negative");
					}
				}
				else {
					if(positive > negative){
						tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude(), status.getPlace().getName(), status.getPlace().getCountry(), status.getUser().getId(), status.getUser().getName(), status.getUser().getFollowersCount(), "positive");
					}
					else{
						tweetService.create(status.getId(), status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude(), status.getPlace().getName(), status.getPlace().getCountry(), status.getUser().getId(), status.getUser().getName(), status.getUser().getFollowersCount(), "negative");
					}
				}
				System.out.println("Tweet almacenado.");
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
		List<ListaPalabra> palabras = listaPalabraService.getAllPalabras();
		List<String> lines=new ArrayList<String>();
		for(ListaPalabra palabra : palabras){
			lines.add(palabra.getPalabra());
		}
		bow=lines.toArray(new String[0]);

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
