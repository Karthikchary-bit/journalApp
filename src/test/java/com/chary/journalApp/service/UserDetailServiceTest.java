package com.chary.journalApp.service;

import com.chary.journalApp.entity.User;
import com.chary.journalApp.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
//@ActiveProfiles("dev")
public class UserDetailServiceTest {
    @InjectMocks
    private  UserDetailServiceImpl userDetailService;
    @Mock
    private UserRepo userRepo;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
                
    }
    @Test
    void loadByUserNameTest(){
       when(userRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(User.builder().userName("karthik").password("asdfghjkl").roles(new ArrayList<>()).build());
        UserDetails user =userDetailService.loadUserByUsername("karthik");
        assertEquals("karthik", user.getUsername());

    }

}
