package com.xmartin.task_service.infraestructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

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
