package com.xmartin.notification_service.infraestructure.config;

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
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createTask(final KafkaPropertiesConfig kafkaProperties) {
        return TopicBuilder
                .name(kafkaProperties.getCreateTask())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic deleteTask(final KafkaPropertiesConfig kafkaProperties) {
        return TopicBuilder
                .name(kafkaProperties.getDeleteTask())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic updateTask(final KafkaPropertiesConfig kafkaProperties) {
        return TopicBuilder
                .name(kafkaProperties.getUpdateTask())
                .partitions(1)
                .replicas(1)
                .build();
    }


}
