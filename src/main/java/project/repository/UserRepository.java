package project.repository;

import project.entities.User;
import project.exception.LoginException;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserRepository {
    private final Connection connection;
    private final Statement statement;
    private final RoleRepository roleRepository;

    public UserRepository(Connection connection, Statement statement, RoleRepository roleRepository) {
        this.connection = connection;
        this.statement = statement;
        this.roleRepository = roleRepository;
    }

    public boolean isSuchUserExist(String email, String nickname) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE email = (?) AND nickname = (?)");
        ps.setString(1, email);
        ps.setString(2, nickname);
        var resSet = ps.executeQuery();
        return email.equals(resSet.getString("email")) || nickname.equals(resSet.getString("nickname"));
    }

    public void writeUser(String nickname, String email, String password) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into users (email, nickname, password) values (?, ?, ?);", RETURN_GENERATED_KEYS);
        ps.setString(1, email);
        ps.setString(2, nickname);
        ps.setString(3, password);
        ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        Long id = (long) generatedKeys.getInt(1);
        roleRepository.writeNewClientIntoUserRoleLink(id);
    }

    public User getUserByEmail(String email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE email = (?)");
        ps.setString(1, email);
        var resSet = ps.executeQuery();
        if (resSet == null) {
            throw new LoginException("Неверный email");
        }
        String nickname = resSet.getString("nickname");
        email = resSet.getString("email");
        String password = resSet.getString("password");
        Long id = resSet.getLong("id");
        return new User(nickname, email, password, id);
    }

    public User getUserByNickname(String nickname) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE nickname = (?)");
        ps.setString(1, nickname);
        var resSet = ps.executeQuery();
        if (resSet == null) {
            throw new LoginException("Неверный nickname");
        }
        nickname = resSet.getString("nickname");
        String email = resSet.getString("email");
        String password = resSet.getString("password");
        Long id = resSet.getLong("id");
        return new User(nickname, email, password, id);
    }
}
