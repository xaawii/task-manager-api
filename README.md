# Task Manager Api

## Description
This is a RESTful microservices API developed in Java using Spring Boot, applying Hexagonal Architecture, API Gateway, Apache Kafka, and Spring Security for authentication.

This is a Task Manager microservices CRUD API that allows users to perform CRUD operations on both Users and Tasks.

Users must be registered and logged in to obtain a valid token. They can create, update, and delete their tasks, as well as view their tasks individually by ID or see all their tasks.

## Services
* **Eureka Service:** This service registers all the other services with a random port and uses a load balancer.

* **Gateway Service:** This service filters all requests and checks with the Auth Service to determine if the path requires authentication.

* **Auth Service:** This service uses Spring Security to create and authenticate users, as well as to generate and validate tokens. It also communicates with the User Service through FeignClient to save users or check their data.

* **User Service:** This service provides CRUD operations for users and uses its own MySQL database.

* **Task Service:** This service provides CRUD operations for tasks and uses its own MySQL database. When performing a CRUD action, an event is created and sent to a Kafka topic.

* **Notification Service:** This service listens to Kafka topics and handles various events. After processing an event, an email notification is sent to the user to notify them of the successful action.


## Docker Compose Configuration
Below is the docker-compose.yaml file that facilitates running this API on Docker.

**IMPORTANT:** Replace `SECRET_KEY` with your own Base64-encoded secret key and use a Gmail account with a generated app password and replace `MAIL_USERNAME` and `MAIL_PASSWORD` for being able to send notifications to users email.
```yaml
services:

  eureka-service:
    container_name: eureka-service
    image: xaawii/task-api-eureka:latest
    ports:
      - "8761:8761"
    environment:
      EUREKA_HOST: eureka-service
      PORT: 8761
    restart: always
    networks:
      - task-api-net
  
  mysql-auth:
    container_name: mysql-auth
    image: mysql:latest
    ports:
      - "8004:3306"
    environment:
      MYSQL_USER: xavi
      MYSQL_PASSWORD: password
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: Auth
    volumes:
      - auth-data-mysql:/var/lib/mysql
    restart: always
    networks:
      - task-api-net
  
  user-service:
    container_name: user-service
    image: xaawii/task-api-user:latest
    environment:
      DB_USER: xavi
      DB_PASSWORD: password
      DB_NAME: Auth
      DB_HOST: mysql-auth
      DB_PORT: 3306
      GATEWAY_HOST: gateway-service
      GATEWAY_PORT: 8080
      EUREKA_HOST: eureka-service
      EUREKA_PORT: 8761
      PORT: 0
    restart: always
    networks:
      - task-api-net
    depends_on:
      - mysql-auth
      - eureka-service
  
  auth-service:
    container_name: auth-service
    image: xaawii/task-api-auth:latest
    environment:
      GATEWAY_HOST: gateway-service
      GATEWAY_PORT: 8080
      EUREKA_HOST: eureka-service
      EUREKA_PORT: 8761
      SECRET_KEY: *your own Base64 key*
      KEY_EXPIRATION_MS: 3600000
      PORT: 0
    restart: always
    networks:
      - task-api-net
    depends_on:
      - mysql-auth
      - eureka-service
      - user-service


  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEPER_TICK_TIME: 2000
    ports:
       - 22181:2181
    restart: always
    networks:
      - task-api-net
  
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://kafka:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    restart: always
    networks:
      - task-api-net

  kafka_ui:
    image: provectuslabs/kafka-ui:latest
    depends_on:
      - kafka
    ports:
      - 8090:8080
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092
      KAFKA_CLUSTERS_0_ZOOKEPEER: zookeeper:2181
    restart: always
    networks:
      - task-api-net


  mysql-task:
    container_name: mysql-task
    image: mysql:latest
    ports:
      - "8005:3306"
    environment:
      MYSQL_USER: xavi
      MYSQL_PASSWORD: password
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: Task
    volumes:
      - task-data-mysql:/var/lib/mysql
    restart: always
    networks:
      - task-api-net

  task-service:
    container_name: task-service
    image: xaawii/task-api-task:latest
    environment:
      DB_USER: xavi
      DB_PASSWORD: password
      DB_NAME: Task
      DB_HOST: mysql-task
      DB_PORT: 3306
      GATEWAY_HOST: gateway-service
      GATEWAY_PORT: 8080
      EUREKA_HOST: eureka-service
      EUREKA_PORT: 8761
      KAFKA_BOOTSTRAP_HOST: kafka
      KAFKA_BOOTSTRAP_PORT: 29092
      PORT: 0
    restart: always
    networks:
      - task-api-net
    depends_on:
      - mysql-task
      - eureka-service
      - user-service
      - kafka_ui

  notification-service:
    container_name: notification-service
    image: xaawii/task-api-notification:latest
    environment:
      MAIL_USERNAME: example@gmail.com
      MAIL_PASSWORD: app-password
      EUREKA_HOST: eureka-service
      EUREKA_PORT: 8761
      KAFKA_BOOTSTRAP_HOST: kafka
      KAFKA_BOOTSTRAP_PORT: 29092
      PORT: 0
    restart: always
    networks:
      - task-api-net
    depends_on:
      - eureka-service
      - task-service
      - kafka_ui

  gateway-service:
    container_name: gateway-service
    image: xaawii/task-api-gateway:latest
    ports:
      - "8080:8080"
    environment:
      EUREKA_HOST: eureka-service
      EUREKA_PORT: 8761
      PORT: 8080
    restart: always
    networks:
      - task-api-net
    depends_on:
      - eureka-service
      - user-service
      - auth-service
      - task-service
      - notification-service
  

volumes:
  auth-data-mysql:
    name: auth-data-mysql
  task-data-mysql:
    name: task-data-mysql

networks:
  task-api-net:
    name: task-api-net
    driver: bridge
```

## Documentation for Endpoints
The documentation for endpoints is available on Swagger. You can explore and test them at localhost:8080/swagger-ui.html.

## Using Source Code
If you download the source code, remember to create your `.env` file using the `.env.example` file as a reference.

