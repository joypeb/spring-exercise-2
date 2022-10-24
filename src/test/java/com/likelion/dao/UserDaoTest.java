package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Factory.class)
class UserDaoTest {
    @Autowired
    ApplicationContext context;

    UserDao userDao;
    User u1;
    User u2;
    User u3;
    @BeforeEach
    public void setUp() {
        userDao = context.getBean("awsUserDao",UserDao.class);
        this.u1 = new User("1","박은빈","pw1");
        this.u2 = new User("2","박은빈2","pw2");
        this.u3 = new User("3","ParkEunBin","pw3");
    }

    @Test
    public void test() throws SQLException, ClassCastException {
        userDao.deleteAll();

        userDao.add(u1);

        User user = userDao.findById(this.u1.getId());

        assertNotNull(user);
        assertEquals(this.u1.getId(),user.getId());
        assertEquals(this.u1.getName(),user.getName());
        assertEquals(1,userDao.getCount());
    }


    @Test
    public void testCount() throws SQLException {


        userDao.deleteAll();
        assertEquals(0,userDao.getCount());

        userDao.add(u1);
        assertEquals(1,userDao.getCount());

        userDao.add(u2);
        assertEquals(2,userDao.getCount());

        userDao.add(u3);
        assertEquals(3,userDao.getCount());
    }
}