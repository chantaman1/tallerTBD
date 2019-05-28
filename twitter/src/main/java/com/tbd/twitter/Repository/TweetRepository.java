package com.tbd.twitter.Repository;
import com.tbd.twitter.Models.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TweetRepository extends MongoRepository<Tweet, String>{

}
