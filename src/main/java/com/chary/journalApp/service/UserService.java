package com.chary.journalApp.service;

import com.chary.journalApp.entity.User;
import com.chary.journalApp.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepository,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }
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

    public User saveNewEntry(User user){

        if (userRepository.findByUserName(user.getUserName()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));

        User savedUser = userRepository.save(user);

        log.info("User saved successfully. username={}", user.getUserName());

        return savedUser;


    }
    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER", "ADMIN"));
        userRepository.save(user);

    }
    public void saveUser(User user){
        userRepository.save(user);

    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }
    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
}
