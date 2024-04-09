package com.bookmyconsultation.paymentservice.configuration;

import com.bookmyconsultation.paymentservice.model.Payment;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Value("${kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    ProducerFactory<String, Payment> producerFactory(){
        return new DefaultKafkaProducerFactory<>(getConfig());
    }

    private Map<String,Object> getConfig() {
        Map<String,Object> configProps = new HashMap<>();
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put("security.protocol", "SASL_SSL");
        configProps.put("sasl.mechanism", "PLAIN");
        configProps.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"UF7G4M5VP7NJ23U2\" password=\"Bi/dTodQNp3Tu1mNIJdiovetkDqyDN4MMUZlbWR7KAoAn0bgllsNu5T7vx7/8j3j\";");
        configProps.put("basic.auth.credentials.source", "USER_INFO");
        configProps.put("basic.auth.user.info", "UF7G4M5VP7NJ23U2:Bi/dTodQNp3Tu1mNIJdiovetkDqyDN4MMUZlbWR7KAoAn0bgllsNu5T7vx7/8j3j");
        configProps.put("schema.registry.url", "https://pkc-xrnwx.asia-south2.gcp.confluent.cloud:443");
        return  configProps;
    }

    @Bean
    public KafkaTemplate<String,Payment> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }


}
