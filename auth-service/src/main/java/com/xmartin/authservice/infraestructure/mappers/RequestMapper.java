package com.xmartin.authservice.infraestructure.mappers;

import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.infraestructure.dto.RequestDto;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public RequestModel requestDtoToModel(RequestDto requestDto) {
        return RequestModel.builder()
                .uri(requestDto.getUri())
                .method(requestDto.getMethod())
                .build();
    }

    public RequestDto requestDtoToModel(RequestModel requestModel) {
        return RequestDto.builder()
                .uri(requestModel.getUri())
                .method(requestModel.getMethod())
                .build();
    }
}
