package com.xmartin.authservice.infraestructure.mappers;

import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.infraestructure.dto.RequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {

    @InjectMocks
    private RequestMapper requestMapper;

    @Test
    void requestDtoToModel() {
        RequestDto requestDto = RequestDto.builder()
                .uri("/test")
                .method("GET")
                .build();


        RequestModel requestModel = requestMapper.requestDtoToModel(requestDto);

        assertEquals(requestDto.getUri(), requestModel.getUri());
        assertEquals(requestDto.getMethod(), requestModel.getMethod());
    }

    @Test
    void requestModelToDto() {
        RequestModel requestModel = RequestModel.builder()
                .uri("/test")
                .method("GET")
                .build();

        RequestDto requestDto = requestMapper.requestDtoToModel(requestModel);

        assertEquals(requestModel.getUri(), requestDto.getUri());
        assertEquals(requestModel.getMethod(), requestDto.getMethod());
    }

}