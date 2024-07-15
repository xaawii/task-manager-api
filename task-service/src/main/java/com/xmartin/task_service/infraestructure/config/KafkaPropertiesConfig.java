package com.xmartin.task_service.infraestructure.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "topics.kafka")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaPropertiesConfig {

    private String createTask;
    private String deleteTask;
    private String updateTask;

}
