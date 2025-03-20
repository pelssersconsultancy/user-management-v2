package com.springbank.user.core.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoClientSettingsFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

@Configuration
public class AxonConfig {

    @Value("${spring.data.mongodb.host:127.0.0.1}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database:user}")
    private String mongoDatabase;

    /*
     * Factory bean that creates the com.mongodb.client.MongoClient instance
     */
    @Bean
    public MongoClientFactoryBean mongo() {
        MongoClientFactoryBean factoryBean = new MongoClientFactoryBean();


        // Step 1: Customize MongoClientSettings
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                    builder.hosts(Collections.singletonList(new ServerAddress(mongoHost, mongoPort))))
                .build();

        factoryBean.setMongoClientSettings(settings);

        return factoryBean;
    }

    // Step 2: Create a MongoTemplate using the MongoClient
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, mongoDatabase); // Specify the database name
    }
}
