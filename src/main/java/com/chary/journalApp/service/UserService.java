package com.chary.journalApp.service;

import com.chary.journalApp.entity.User;
import com.chary.journalApp.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    public void processUser(String username){
//        logger.info("Intializling user processing stream.");
//        try{
//            if("ADMIN".equals(username)){
//                throw new IllegalArgumentException("System modification restricted");
//            }
//        } catch (Exception e) {
//            logger.error("Execution failed for context user: {}", username, e);
//
//        }
//    }

    public boolean saveNewEntry(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getUserName()));
            user.setRoles(Arrays.asList("User"));
            userRepo.save(user);

            log.info("################ USER SAVED ################");

            return true;
        }catch (Exception e){
            log.info("ohhhhhhhhhhh shitttttttttttttttt");
            log.error("Error occurred for {} :",user.getUserName(),e);
            log.debug("User creation started");
            log.warn("ohhhhhhhhhhh shitttttttttttttttt");
            //log.debug("ohhhhhhhhhhh shitttttttttttttttt");
            log.trace("ohhhhhhhhhhh shitttttttttttttttt");
            return false;
        }

    }
    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getUserName()));
        user.setRoles(Arrays.asList("User","ADMIN"));
        userRepo.save(user);

    }
    public void saveUser(User user){
        userRepo.save(user);

    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepo.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepo.deleteById(id);
    }
    public User findByUserName(String userName){
        return userRepo.findByUserName(userName);
    }
}
