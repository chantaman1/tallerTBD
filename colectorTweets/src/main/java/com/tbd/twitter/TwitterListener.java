package com.tbd.twitter;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
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
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;

@Service
@Configurable
public class TwitterListener {
	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private TwitterStream twitterStream;

	@Autowired
	private KafkaTemplate<String,String> kafkaTemplate;

	@Value(value = "${kafka.topicName}")
   private String topicName;

	@PostConstruct
	public void run() {
		twitterStream.addListener(new StatusListener() {
			public void onStatus(Status status) {
				System.out.println(status.toString());
				kafkaTemplate.send(topicName,status.toString());
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
