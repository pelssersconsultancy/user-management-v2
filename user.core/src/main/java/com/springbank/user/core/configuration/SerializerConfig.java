package com.springbank.user.core.configuration;

import com.thoughtworks.xstream.XStream;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SerializerConfig {
    // By default, we want the XStreamSerializer
    @Bean
    @Primary
    public Serializer defaultSerializer() {
        // Set the secure types on the xStream instance
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{
                "org.axonframework.**",
                "com.springbank.user.**",
        });
        return XStreamSerializer.builder()
                .xStream(xStream)
                .build();
    }

    // But for all our messages we'd prefer the JacksonSerializer due to JSON's smaller format
    @Bean
    @Qualifier("messageSerializer")
    public Serializer messageSerializer() {
        return JacksonSerializer.defaultSerializer();
    }
}

