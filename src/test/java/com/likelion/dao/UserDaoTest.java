package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.Assertions;
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
}