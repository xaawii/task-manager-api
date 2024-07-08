package com.xmartin.authservice.service.impl;

import com.xmartin.authservice.client.UserClient;
import com.xmartin.authservice.controller.dto.LoginDto;
import com.xmartin.authservice.controller.dto.RegisterDto;
import com.xmartin.authservice.controller.dto.RequestDto;
import com.xmartin.authservice.controller.dto.TokenDto;
import com.xmartin.authservice.model.UserModel;
import com.xmartin.authservice.security.JwtProvider;
import com.xmartin.authservice.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserModel save(RegisterDto registerDto) {
        Optional<UserModel> user = userClient.getUserByEmail(registerDto.getEmail());
        if (user.isPresent()) return null;

        String password = passwordEncoder.encode(registerDto.getPassword());

        UserModel userModel = UserModel.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(password)
                .role("ROLE_USER")
                .build();

        return userClient.saveUser(userModel);
    }

    @Override
    public TokenDto login(LoginDto loginDto) {
        Optional<UserModel> user = userClient.getUserByEmail(loginDto.getEmail());
        if (!user.isPresent()) return null;
        if (passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
            return new TokenDto(jwtProvider.createToken(user.get()));
        } else {
            return null;
        }
    }

    @Override
    public TokenDto validate(String token, RequestDto requestDto) {
        if (!jwtProvider.validate(token, requestDto)) return null;

        String email = jwtProvider.getEmailFromToken(token);
        if (userClient.getUserByEmail(email).isEmpty()) return null;

        return new TokenDto(token);
    }


}
