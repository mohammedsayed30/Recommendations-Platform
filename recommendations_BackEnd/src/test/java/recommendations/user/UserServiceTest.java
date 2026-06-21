package recommendations.user;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import recommendations.config.JwtService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "validator", validator);
    }

    private User buildValidUser(){
        User user = new User();
        user.setFullName("aliklay");
        user.setEmail("mohamedsayedshaaban2023@gmail.com");
        user.setPassword("123456");
        user.setJobTitle("Software engineer");
        user.setLinkedinProfile("https://www.linkedin.com/in/mohammed-shaaban-573038254/");
        user.setYearsOfExperience(5);
        return user;
    }

    @Test
    @DisplayName("TC_01: create User and save it to the db")
    public void createUserAndSaveItToTheDbWithHashedPassword(){
        User user =  buildValidUser();

        String rawPassword  = user.getPassword();

        String hashedPassword = "hashed_123456";

        when(validator.validate(user)).thenReturn(Set.of());
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertEquals(hashedPassword, result.getPassword());
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Invalid user: throws ConstraintViolationException and never saves")
    void createUser_withInvalidData_shouldThrowAndNotSave() {

        User invalidUser = buildValidUser();
        invalidUser.setFullName("");

        ConstraintViolation<User> fakeViolation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<User>> violations = Set.of(fakeViolation);

        when(validator.validate(invalidUser)).thenReturn(violations);


        assertThrows(ConstraintViolationException.class, () -> {
            userService.create(invalidUser);
        });

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(any(String.class));
    }
}
