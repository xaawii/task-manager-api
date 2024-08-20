package com.xmartin.authservice.infraestructure.converters;

import com.xmartin.authservice.domain.model.PasswordTokenModel;
import com.xmartin.authservice.infraestructure.entity.PasswordTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class PasswordTokenConverter {

    public PasswordTokenModel fromEntityToModel(PasswordTokenEntity passwordTokenEntity) {
        return PasswordTokenModel.builder()
                .id(passwordTokenEntity.getId())
                .email(passwordTokenEntity.getEmail())
                .token(passwordTokenEntity.getToken())
                .expiryDate(passwordTokenEntity.getExpiryDate())
                .build();
    }

    public PasswordTokenEntity fromModelToEntity(PasswordTokenModel passwordTokenModel) {
        return PasswordTokenEntity.builder()
                .id(passwordTokenModel.getId())
                .email(passwordTokenModel.getEmail())
                .token(passwordTokenModel.getToken())
                .expiryDate(passwordTokenModel.getExpiryDate())
                .build();
    }
}
