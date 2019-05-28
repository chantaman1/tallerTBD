package com.tbd.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
@ConditionalOnClass({TwitterStreamFactory.class,TwitterStream.class,TwitterListener.class})
@EnableConfigurationProperties(TwitterProperties.class)
public class TwitterAppConfiguration {

	@Autowired
	private TwitterProperties properties;
	
    @Bean
    @ConditionalOnMissingBean
    public TwitterStreamFactory twitterStreamFactory() {
    	ConfigurationBuilder configurationBuilder=new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(false)
				.setOAuthConsumerKey(properties.getTwitter().getConsumerKey())
				.setOAuthConsumerSecret(properties.getTwitter().getConsumerSecret())
				.setOAuthAccessToken(properties.getTwitter().getAccessToken())
				.setOAuthAccessTokenSecret(properties.getTwitter().getAccessTokenSecret());
		return new TwitterStreamFactory(configurationBuilder.build());
    }
    @Bean
    @ConditionalOnMissingBean
    public TwitterStream twitterStream(TwitterStreamFactory twitterStreamFactory) {
    	return twitterStreamFactory.getInstance();
    }
    @Bean
    @ConditionalOnMissingBean
    public TwitterListener twitterListener() {
    	return new TwitterListener();
    }
}
