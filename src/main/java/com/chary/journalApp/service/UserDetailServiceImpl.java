package com.chary.journalApp.service;


import com.chary.journalApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.chary.journalApp.entity.User;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       User user = userRepo.findByUserName(userName);
        if(user!=null){
            return org.springframework.security.core.userdetails.User.
                    builder().
                    username(user.getUserName()).
                    password(user.getPassword()).
                    roles(user.getRoles().toArray(new String[0])).
                    build();

        }
        throw new UsernameNotFoundException("User not found with username: "+userName);
    }
}
