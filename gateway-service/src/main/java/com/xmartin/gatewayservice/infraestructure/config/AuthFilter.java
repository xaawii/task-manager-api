package com.xmartin.gatewayservice.infraestructure.config;

import com.xmartin.gatewayservice.infraestructure.dto.RequestDto;
import com.xmartin.gatewayservice.infraestructure.dto.TokenDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webclient;

    public AuthFilter(WebClient.Builder webclient) {
        super(Config.class);
        this.webclient = webclient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();
            if (isSwaggerPath(path)) {
                return chain.filter(exchange);
            }

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                return onError(exchange, HttpStatus.UNAUTHORIZED);

            String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] chunks = tokenHeader.split(" ");
            if ((chunks.length != 2) || !(chunks[0].equals("Bearer")))
                return onError(exchange, HttpStatus.UNAUTHORIZED);


            return webclient.build()
                    .post()
                    .uri("http://auth-service/auth/validate?token=" + chunks[1])
                    .bodyValue(RequestDto
                            .builder()
                            .uri(exchange.getRequest().getPath().toString())
                            .method(exchange.getRequest().getMethod().toString())
                            .build())
                    .retrieve().bodyToMono(TokenDto.class)
                    .map(tokenDto -> {
                        tokenDto.getToken();
                        return exchange;
                    }).flatMap(chain::filter)
                    .onErrorResume(throwable -> {
                        if (throwable instanceof WebClientResponseException webClientException) {
                            return onError(exchange, HttpStatus.resolve(webClientException.getStatusCode().value()));
                        }
                        return onError(exchange, HttpStatus.UNAUTHORIZED);
                    });
        });
    }

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    private boolean isSwaggerPath(String path) {
        return path.contains("/swagger-ui.html") ||
                path.contains("/swagger-resources") ||
                path.contains("/v2/api-docs") ||
                path.contains("/v3/api-docs") ||
                path.contains("/webjars/springfox-swagger-ui");
    }


    public static class Config {
    }
}
