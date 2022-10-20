package com.likelion.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Factory {
    @Bean
    public UserDao awsUserDao() {
        AWSConnectionMaker awsConnectionMaker = new AWSConnectionMaker();
        return new UserDao(awsConnectionMaker);
    }
}
