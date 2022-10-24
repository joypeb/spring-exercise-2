package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private DataSource datasource;

    public UserDao(DataSource datasource) {
        this.datasource = datasource;
    }

    public void add(User user) {
        StatementStrategy st = c -> {
            try {
                PreparedStatement ps = c.prepareStatement("INSERT INTO likelionDB.users(id, name, password) VALUES(?,?,?)");

                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        jdbcContextWithStatementStrategy(st);
    }

    public void deleteAll() {
        StatementStrategy st = c -> {
            try {
                return c.prepareStatement("DELETE FROM likelionDB.users");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        jdbcContextWithStatementStrategy(st);
    }

    public int getCount() {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = datasource.getConnection();

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
                c = datasource.getConnection();
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

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = datasource.getConnection();

            ps = stmt.makePreparedStatement(c);

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

    public static void main(String[] args) {
        Factory factory = new Factory();
        UserDao u = factory.awsUserDao();
        System.out.println(u.getCount());
    }
}
