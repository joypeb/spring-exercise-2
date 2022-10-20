package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.*;
import java.util.Map;

public class UserDao {
    private ConnectionMaker connectionMaker = new AWSConnectionMaker();

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public static void main(String[] args) {
    }

    public void add(User user) {
        try {
            // DB접속 (ex sql workbeanch실행)
            Connection c = connectionMaker.makeConnection();

            // Query문 작성
            PreparedStatement pstmt = c.prepareStatement("INSERT INTO likelionDB.users(id, name, password) VALUES(?,?,?);");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());

            // Query문 실행
            pstmt.executeUpdate();

            pstmt.close();
            c.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        try {
            Connection c = connectionMaker.makeConnection();

            PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM likelionDB.users"
            );

            ps.executeUpdate();

            ps.close();
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCount() {
        try {
            Connection c = connectionMaker.makeConnection();

            PreparedStatement ps = c.prepareStatement(
                    "SELECT COUNT(*) FROM likelionDB.users"
            );

            ResultSet rs = ps.executeQuery();

            rs.next();
            int count = rs.getInt(1);

            rs.close();
            ps.close();
            c.close();

            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(String id) {
        try {
            Connection c = connectionMaker.makeConnection();
            // Query문 작성
            PreparedStatement pstmt = c.prepareStatement("SELECT * FROM likelionDB.users WHERE id = ?");
            pstmt.setString(1, id);

            // Query문 실행
            ResultSet rs = pstmt.executeQuery();

            User user = null;

            if (rs.next()) {
                user = new User(rs.getString("id"), rs.getString("name"),
                        rs.getString("password"));
            }

            rs.close();
            pstmt.close();
            c.close();

            //데이터가 없을시 에러처리
            if(user == null) throw new EmptyResultDataAccessException(1);

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
