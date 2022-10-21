package com.likelion.dao;

import com.likelion.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStrategy implements StatementStrategy{
    User user;

    public AddStrategy(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO likelionDB.users(id, name, password) VALUES(?,?,?)");

            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            return ps;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
