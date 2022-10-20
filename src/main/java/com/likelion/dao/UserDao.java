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
        Connection c = null;
        PreparedStatement ps =  null;
        try {
            // DB접속 (ex sql workbeanch실행)
            c = connectionMaker.makeConnection();

            // Query문 작성
            ps = c.prepareStatement("INSERT INTO likelionDB.users(id, name, password) VALUES(?,?,?);");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            // Query문 실행
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteAll() {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = connectionMaker.makeConnection();

            ps = c.prepareStatement(
                    "DELETE FROM likelionDB.users"
            );

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public int getCount() {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = connectionMaker.makeConnection();

            ps = c.prepareStatement(
                    "SELECT COUNT(*) FROM likelionDB.users"
            );

            rs = ps.executeQuery();

            rs.next();
            int count = rs.getInt(1);

            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public User findById(String id) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        {
            try {
                c = connectionMaker.makeConnection();
                // Query문 작성
                ps = c.prepareStatement("SELECT * FROM likelionDB.users WHERE id = ?");
                ps.setString(1, id);

                // Query문 실행
                rs = ps.executeQuery();

                User user = null;

                if (rs.next()) {
                    user = new User(rs.getString("id"), rs.getString("name"),
                            rs.getString("password"));
                }

                //데이터가 없을시 에러처리
                if (user == null) throw new EmptyResultDataAccessException(1);
                return user;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                    }
                }
                if (c != null) {
                    try {
                        c.close();
                    } catch (SQLException e) {
                    }
                }
            }
        }
    }
}
