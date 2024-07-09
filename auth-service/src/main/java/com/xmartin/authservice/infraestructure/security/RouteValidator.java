package com.xmartin.authservice.infraestructure.security;

import com.xmartin.authservice.infraestructure.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@ConfigurationProperties(prefix = "admin-paths")
@Getter
@Setter
public class RouteValidator {

    private List<RequestDto> paths;

    public boolean isAdminPath(RequestDto requestDto) {
        return paths.stream()
                .anyMatch(p ->
                        Pattern.matches(p.getUri(), requestDto.getUri())
                                && p.getMethod().equals(requestDto.getMethod()));
    }
}
