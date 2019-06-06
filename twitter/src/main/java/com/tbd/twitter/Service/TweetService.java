package com.tbd.twitter.Service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tbd.twitter.Models.Tweet;
import com.tbd.twitter.Repository.TweetRepository;

@Component
public class TweetService {
	@Autowired
	TweetRepository tweetRepository;
	
	public Tweet create(long tweetId, String text, Date createdAt, double latitude, double longitude, String city, String country, long userId, String userName, int followersCount, String sentimentAnalysis) {
		return tweetRepository.save(new Tweet(tweetId, text, createdAt, latitude, longitude, city, country, userId, userName, followersCount, sentimentAnalysis));
	}
}
