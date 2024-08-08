package com.xmartin.authservice.infraestructure.adapters;

import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.domain.ports.out.AuthUserServicePort;
import com.xmartin.authservice.infraestructure.client.UserClient;
import com.xmartin.authservice.infraestructure.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserServiceAdapter implements AuthUserServicePort {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserModel save(RegisterModel registerModel) {

        String password = passwordEncoder.encode(registerModel.getPassword());

        UserModel userModel = UserModel.builder()
                .name(registerModel.getName())
                .email(registerModel.getEmail())
                .password(password)
                .role("ROLE_USER")
                .build();

        return userClient.saveUser(userModel);
    }

    @Override
    public String login(LoginModel loginModel) throws WrongPasswordException, UserNotFoundException {
        Optional<UserModel> user = userClient.getUserByEmail(loginModel.getEmail());
        if (user.isEmpty()) throw new UserNotFoundException();
        if (passwordEncoder.matches(loginModel.getPassword(), user.get().getPassword())) {
            return jwtProvider.createToken(user.get());
        } else {
            throw new WrongPasswordException();
        }
    }

    @Override
    public String validateRequestAndToken(String token, RequestModel requestModel) throws InvalidTokenException {
        if (!jwtProvider.validate(token, requestModel)) throw new InvalidTokenException();

        String email = jwtProvider.getEmailFromToken(token);
        if (!userClient.getUserExistsByEmail(email)) throw new InvalidTokenException();

        return token;
    }

    @Override
    public String validateToken(String token) throws InvalidTokenException {
        if (!jwtProvider.validateOnlyToken(token)) throw new InvalidTokenException();

        String email = jwtProvider.getEmailFromToken(token);
        if (!userClient.getUserExistsByEmail(email)) throw new InvalidTokenException();

        return token;
    }


}
