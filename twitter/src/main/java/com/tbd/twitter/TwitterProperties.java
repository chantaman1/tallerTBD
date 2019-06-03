package com.tbd.twitter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class TwitterProperties {
	private Twitter twitter=new Twitter();
	
	public class Twitter{
		private String consumerKey;
		private String consumerSecret;
		private String accessToken;
		private String accessTokenSecret;
		
		public String getConsumerKey() {
			return consumerKey;
		}
		public void setConsumerKey(String consumerKey) {
			this.consumerKey = consumerKey;
		}
		public String getConsumerSecret() {
			return consumerSecret;
		}
		public void setConsumerSecret(String consumerSecret) {
			this.consumerSecret = consumerSecret;
		}
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public String getAccessTokenSecret() {
			return accessTokenSecret;
		}
		public void setAccessTokenSecret(String accessTokenSecret) {
			this.accessTokenSecret = accessTokenSecret;
		}
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}
}
