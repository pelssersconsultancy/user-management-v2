package com.springbank.user.core.configuration;


import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;

import com.thoughtworks.xstream.XStream;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoFactory;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoSettingsFactory;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;

@Configuration
@Import( { SerializerConfig.class })
public class AxonConfig {

    @Value("${spring.data.mongodb.host:127.0.0.1}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database:user}")
    private String mongoDatabase;


    @Bean
    public MongoClient mongo() {
        var mongoFactory = new MongoFactory();
        var mongoSettingsFactory = new MongoSettingsFactory();
        mongoSettingsFactory.setMongoAddresses(Collections.singletonList(new ServerAddress(mongoHost, mongoPort)));
        mongoFactory.setMongoClientSettings(mongoSettingsFactory.createMongoClientSettings());

        return mongoFactory.createMongo();
    }


    // Step 2: Create a MongoTemplate using the MongoClient
    @Bean
    public MongoTemplate axonMongoTemplate() {
        return DefaultMongoTemplate.builder()
            .mongoDatabase(mongo(), mongoDatabase)
            .build();
    }

    @Bean
    public TokenStore tokenStore(@Qualifier("messageSerializer") Serializer serializer) {
        return MongoTokenStore.builder().mongoTemplate(axonMongoTemplate()).serializer(serializer).build();
    }

    @Bean
    public EventStorageEngine storageEngine(MongoClient client, XStream xStream) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(DefaultMongoTemplate.builder()
                        .mongoDatabase(client)
                        .build())
                .snapshotSerializer(XStreamSerializer.builder().xStream(xStream).build())
                .eventSerializer(XStreamSerializer.builder().xStream(xStream).build())
                .build();
    }

    // TODO: this code is not exactly the same (see video 13)
    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .build();
    }


}
