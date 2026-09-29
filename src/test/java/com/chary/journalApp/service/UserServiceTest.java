package com.chary.journalApp.service;


import com.chary.journalApp.entity.User;
import com.chary.journalApp.repository.UserRepo;
import com.chary.journalApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

//    @InjectMocks
//    private UserRepo userRepo;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserName("john");
        user.setPassword("rawPassword");
    }

    @Test
    void shouldSaveNewUserSuccessfully() {

        // Arrange
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUserName("john"))
                .thenReturn(null);

        when(passwordEncoder.encode("rawPassword"))
                .thenReturn(encodedPassword);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.saveNewEntry(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("john", savedUser.getUserName());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(List.of("USER"), savedUser.getRoles());

        verify(userRepository).findByUserName("john");
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        // Arrange
        User existingUser = new User();
        existingUser.setUserName("john");

        when(userRepository.findByUserName("john"))
                .thenReturn(existingUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.saveNewEntry(user)
        );

        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository).findByUserName("john");
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldSaveAdminWithUserAndAdminRoles() {

        // Arrange
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode("rawPassword"))
                .thenReturn(encodedPassword);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        userService.saveAdmin(user);

        // Assert
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(List.of("USER", "ADMIN"), user.getRoles());

        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(user);
    }

    @Test
    void shouldReturnAllUsers() {

        // Arrange
        List<User> users = List.of(user);

        when(userRepository.findAll())
                .thenReturn(users);

        // Act
        List<User> result = userService.getAll();

        // Assert
        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void shouldFindUserById() {

        // Arrange
        ObjectId id = new ObjectId();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenUserIdDoesNotExist() {

        // Arrange
        ObjectId id = new ObjectId();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(id);

        // Assert
        assertTrue(result.isEmpty());

        verify(userRepository).findById(id);
    }

    @Test
    void shouldDeleteUserById() {

        // Arrange
        ObjectId id = new ObjectId();

        // Act
        userService.deleteById(id);

        // Assert
        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldFindUserByUsername() {

        // Arrange
        when(userRepository.findByUserName("john"))
                .thenReturn(user);

        // Act
        User result = userService.findByUserName("john");

        // Assert
        assertNotNull(result);
        assertEquals("john", result.getUserName());

        verify(userRepository).findByUserName("john");
    }
}