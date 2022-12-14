package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private DataSource datasource;
    private JdbcContext jdbcContext;

    public UserDao(DataSource datasource) {
        this.datasource = datasource;
        this.jdbcContext = new JdbcContext(datasource);
    }

    public void add(final User user) throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) {
                PreparedStatement pstmt = null;
                try {
                    pstmt = c.prepareStatement("INSERT INTO likelionDB.users(id, name, password) VALUES(?,?,?)");

                    pstmt.setString(1, user.getId());
                    pstmt.setString(2, user.getName());
                    pstmt.setString(3, user.getPassword());

                    return pstmt;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
                // Query??? ??????
                ps = c.prepareStatement("SELECT * FROM likelionDB.users WHERE id = ?");
                ps.setString(1, id);

                // Query??? ??????
                rs = ps.executeQuery();

                User user = null;

                if (rs.next()) {
                    user = new User(rs.getString("id"), rs.getString("name"),
                            rs.getString("password"));
                }

                //???????????? ????????? ????????????
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
