package com.tbd.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Map;
import java.util.HashMap;

@Configuration
@ConditionalOnClass({TwitterStreamFactory.class,TwitterStream.class,TwitterListener.class})
@EnableConfigurationProperties(TwitterProperties.class)
public class TwitterAppConfiguration {
	 @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

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

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
 
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
