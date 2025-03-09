package com.selflearntech.techblogbackend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.username}")
    private String mongoUser;
    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%s@localhost:27017/blog?authSource=admin", mongoUser, mongoPassword));
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(settings);

    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "blog");
    }
}
