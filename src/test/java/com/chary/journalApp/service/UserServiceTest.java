package com.chary.journalApp.service;

import com.chary.journalApp.entity.User;
import com.chary.journalApp.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @ParameterizedTest
    @ArgumentsSource(userArgumentProvider.class)
    public void testSaveNewUser(User user){

        assertTrue(userService.saveNewEntry(user));
    }
}
