package com.solar.repo;

import com.solar.entity.Feedback;
import com.solar.entity.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepo extends MongoRepository<Feedback,String> {
    
}
