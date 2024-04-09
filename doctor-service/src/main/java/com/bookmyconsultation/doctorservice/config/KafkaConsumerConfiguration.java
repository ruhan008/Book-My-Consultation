package com.bookmyconsultation.doctorservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    @Value("${kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Bean
    ConsumerFactory<String,Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConfig());
    }

    private Map<String,Object> getConfig() {
        Map<String,Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        configProps.put("security.protocol", "SASL_SSL");
        configProps.put("sasl.mechanism", "PLAIN");
        configProps.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"UF7G4M5VP7NJ23U2\" password=\"Bi/dTodQNp3Tu1mNIJdiovetkDqyDN4MMUZlbWR7KAoAn0bgllsNu5T7vx7/8j3j\";");
        configProps.put("basic.auth.credentials.source", "USER_INFO");
        configProps.put("basic.auth.user.info", "UF7G4M5VP7NJ23U2:Bi/dTodQNp3Tu1mNIJdiovetkDqyDN4MMUZlbWR7KAoAn0bgllsNu5T7vx7/8j3j");
        configProps.put("schema.registry.url", "https://pkc-xrnwx.asia-south2.gcp.confluent.cloud:443");
        return configProps;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String,Object> concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String,Object> concurrentKafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        return concurrentKafkaListenerContainerFactory;
    }
}
