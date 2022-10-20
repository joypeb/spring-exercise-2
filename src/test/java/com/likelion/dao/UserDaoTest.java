package com.likelion.dao;

import com.likelion.domain.User;
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

    @Test
    public void test() throws SQLException, ClassCastException {
        UserDao userDao = context.getBean("awsUserDao",UserDao.class);

        userDao.deleteAll();
        String id = "1";
        userDao.add(new User(id,"test1","passwordTest"));

        User user = userDao.findById(id);

        assertEquals(id,user.getId());
        assertEquals("test1",user.getName());
        assertEquals(1,userDao.getCount());
    }


    @Test
    public void testCount() {
        UserDao userDao = context.getBean("awsUserDao", UserDao.class);

        User u1 = new User("1","박은빈","pw1");
        User u2 = new User("2","박은빈2","pw2");
        User u3 = new User("3","ParkEunBin","pw3");

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