package com.xmartin.authservice.infraestructure.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic passwordToken(final KafkaPropertiesConfig kafkaProperties) {
        return TopicBuilder
                .name(kafkaProperties.getPasswordToken())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic resetPassword(final KafkaPropertiesConfig kafkaProperties) {
        return TopicBuilder
                .name(kafkaProperties.getResetPassword())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic registerUser(final KafkaPropertiesConfig kafkaProperties) {
        return TopicBuilder
                .name(kafkaProperties.getRegisterUser())
                .partitions(1)
                .replicas(1)
                .build();
    }


}
