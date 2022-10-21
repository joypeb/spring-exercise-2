package com.likelion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStrategy implements StatementStrategy{
    @Override
    public PreparedStatement makePreparedStatement(Connection connection) {
        try {
            return connection.prepareStatement("DELETE FROM likelionDB.users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
