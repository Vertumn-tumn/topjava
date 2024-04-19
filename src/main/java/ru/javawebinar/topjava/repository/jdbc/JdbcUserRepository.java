package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.javawebinar.topjava.model.Role.ADMIN;
import static ru.javawebinar.topjava.model.Role.USER;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(@Valid @NotNull User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }

        jdbcTemplate.update("DELETE  FROM user_role WHERE user_id=?", user.getId());

        if (!user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                jdbcTemplate.update("INSERT INTO user_role (user_id, role) VALUES (?, ?)", user.getId(), role.toString());
            }
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(@Min(value = 100000) int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(@Min(value = 100000) int id) {
        String sql = "SELECT u.id, u.name, u.email, u.password, u.registered,u.enabled, u.calories_per_day, r.role " +
                "FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE u.id = ?";

        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            User user = null;
            Set<Role> roles = new HashSet<>();
            while (rs.next()) {
                if (user == null) {
                    user = mapUser(rs);
                }
                mapRole(rs, roles);
            }
            if (user != null) {
                user.setRoles(roles);
            }
            return user;
        });
    }

    @Override
    public User getByEmail(@NotBlank @Size(max = 128) @Email String email) {
        String sql = "SELECT u.id, u.name, u.email, u.password, u.registered,u.enabled, u.calories_per_day, r.role " +
                "FROM users u LEFT JOIN user_role r ON u.id = r.user_id WHERE u.email = ?";

        return jdbcTemplate.query(sql, new Object[]{email}, rs -> {
            User user = null;
            Set<Role> roles = new HashSet<>();
            while (rs.next()) {
                if (user == null) {
                    user = mapUser(rs);
                }
                mapRole(rs, roles);
            }
            if (user != null) {
                user.setRoles(roles);
            }
            return user;
        });
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, r.role " +
                "FROM users u " +
                "LEFT JOIN  user_role r ON u.id = r.user_id" +
                " ORDER BY u.name ";
        return jdbcTemplate.query(sql, resultSet -> {
            Set<User> users = new HashSet<>();
            while (resultSet.next()) {
                User user = mapUser(resultSet);
                Set<Role> roles = new HashSet<>();
                mapRole(resultSet, roles);
                user.setRoles(roles);
                users.add(user);
            }
            return new ArrayList<>(users);
        });
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRegistered(rs.getTimestamp("registered"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setCaloriesPerDay(rs.getInt("calories_per_day"));
        user.setRoles(new HashSet<>());
        return user;
    }

    private void mapRole(ResultSet rs, Set<Role> roles) throws SQLException {
        String role = rs.getString("role");
        try {
            switch (role) {
                case "USER":
                    roles.add(USER);
                    break;
                case "ADMIN":
                    roles.add(ADMIN);
                    break;
            }
        } catch (NullPointerException e) {
        }
    }
}
