package com.xmartin.authservice.infraestructure.adapters;

import com.xmartin.authservice.domain.exceptions.TokenNotFoundException;
import com.xmartin.authservice.domain.model.PasswordTokenModel;
import com.xmartin.authservice.domain.ports.out.PasswordTokenRepositoryPort;
import com.xmartin.authservice.infraestructure.converters.PasswordTokenConverter;
import com.xmartin.authservice.infraestructure.repository.JpaPasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordTokenRepositoryAdapter implements PasswordTokenRepositoryPort {
    private final JpaPasswordTokenRepository repository;
    private final PasswordTokenConverter converter;

    @Override
    public PasswordTokenModel save(PasswordTokenModel passwordTokenModel) {
        return converter.fromEntityToModel(repository.save(converter.fromModelToEntity(passwordTokenModel)));
    }

    @Override
    public PasswordTokenModel findByToken(String token) throws TokenNotFoundException {
        return converter.fromEntityToModel(repository.findByToken(token).orElseThrow(TokenNotFoundException::new));
    }

    @Override
    public void remove(String token) {
        repository.findByToken(token).ifPresent(repository::delete);
    }
}
