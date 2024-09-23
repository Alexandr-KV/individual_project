package project.controller;

import project.repository.UserRepository;
import io.javalin.http.Context;
import project.entities.User;
import project.exception.LoginException;
import project.exception.RegistrationException;
import project.request.LoginRequest;
import project.request.RegistrationRequest;
import project.utils.JwtUtils;

import java.sql.SQLException;
import java.util.Objects;

public class UserController {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public UserController(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public void registrationUser(Context ctx) throws SQLException, RegistrationException {
        RegistrationRequest registrationRequest = ctx.bodyAsClass(RegistrationRequest.class);
        registrationRequest.valid();
        if (userRepository.isSuchUserExist(registrationRequest.getEmail(), registrationRequest.getNickname())) {
            throw new RegistrationException("Такой пользователь уже существует");
        }
        userRepository.writeUser(registrationRequest.getNickname(), registrationRequest.getEmail(), String.valueOf(registrationRequest.getPassword().hashCode()));
    }

    public void loginUser(Context ctx) throws SQLException {
        LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);
        loginRequest.valid();
        User user;
        if (loginRequest.getEmail() == null) {
            user = userRepository.getUserByNickname(loginRequest.getNickname());
        } else {
            user = userRepository.getUserByEmail(loginRequest.getEmail());
        }
        String password = user.getPassword();
        String email = user.getEmail();
        if (!Objects.equals(password, String.valueOf(loginRequest.getPassword().hashCode()))) {
            throw new LoginException("Неверный password, email или nickname");
        }
        ctx.result(jwtUtils.builder(email));
    }
}
