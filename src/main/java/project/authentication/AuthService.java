package project.authentication;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import project.entities.User;
import project.exception.AuthException;
import project.repository.RoleRepository;
import project.repository.UserRepository;
import project.utils.JwtUtils;

import java.sql.SQLException;
import java.util.Set;

import static project.authentication.Role.NOT_REGISTERED;

public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    public void handleAccess(Context ctx) throws SQLException {

        var routeRoles = ctx.routeRoles();
        if (routeRoles.contains(NOT_REGISTERED)) {
            return;
        }

        var userRoles = getUserRoles(ctx);

        routeRoles.retainAll(userRoles);
        if (!routeRoles.isEmpty()) {
            return;
        }

        if (!userRoles.isEmpty()){
            ctx.status(HttpStatus.FORBIDDEN);
            throw new AuthException("У тебя нет доступа!");
        }

        ctx.status(HttpStatus.UNAUTHORIZED);
        throw new AuthException("У тебя нет доступа!");
    }

    private Set<Role> getUserRoles(Context ctx) throws SQLException {
        var token = ctx.header("Authorization");
        if (token == null) {
            return Set.of(NOT_REGISTERED);
        }
        String email = jwtUtils.parse(token);
        User user = userRepository.getUserByEmail(email);
        var roles = roleRepository.getRolesByUserId(user.getId());
        user.setRoles(roles);
        ctx.attribute("user", user);
        return roles;
    }
}
