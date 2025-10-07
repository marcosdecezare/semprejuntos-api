package com.sempremjuntos.api.repositories;

import com.sempremjuntos.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, full_name, email, password_hash, role, created_at FROM semprejuntos.users WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, this::mapRow).stream().findFirst();
    }

    public void save(String fullName, String email, String passwordHash) {
        String sql = "INSERT INTO semprejuntos.users (full_name, email, password_hash, role, created_at) VALUES (?, ?, ?, 'USER', NOW())";
        jdbcTemplate.update(sql, fullName, email, passwordHash);
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("role"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
