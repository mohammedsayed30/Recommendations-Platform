package recommendations.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserValidationTest {
    //to check the rules
    private Validator validator;

    //start function before tests for all rules
    @BeforeEach
    public void setUp(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
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

    //user should has no violations
    @Test
    @DisplayName("TC_01: Validate user has zero violations")
    public void validateUserHasZeroViolations(){
        //build valid user
        User user = buildValidUser();

        //get the violdations if there is existing
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        //check there is no violations
        assertTrue(violations.isEmpty());
    }

    //ensure that user enter his full name
    @Test
    @DisplayName("TC_02: Missing Full Name Should Fail the test")
    public void missingFullNameShouldFailTheRegisteration(){
        //build valid user
        User user = buildValidUser();

        user.setFullName("");

        //get the violdations if there is existing
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        //check there is no violations
        assertFalse(violations.isEmpty());

        //check if the violation come from full name
        assertTrue(violations
                .stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fullName"))
        );
    }

    //ensure that user enter valid mail
    @Test
    @DisplayName("TC_03: InValid Email Should Fail the test")
    public void nonValidEmailShouldFailTheRegisteration(){
        //build valid user
        User user = buildValidUser();

        user.setEmail("not-an-email");

        //get the violdations if there is existing
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        //check there is no violations
        assertFalse(violations.isEmpty());

        //check if the violation come from Email
        assertTrue(violations
                .stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"))
        );
    }

    //validate that password has at least  6 characters
    @Test
    @DisplayName("TC_04")
    public void shortPasswordShouldTrigerViolation(){
        User user = buildValidUser();

        user.setPassword("12345");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());

        assertTrue(violations
                .stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"))
        );
    }

    //validate that password has at least  6 characters
    @Test
    @DisplayName("TC_05: Negative yearsOfExperience currently PASSES")
    public void nigativeYOFShouldTrigerViolation(){
        User user = buildValidUser();

        user.setYearsOfExperience(-2);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());

        assertTrue(violations
                .stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("yearsOfExperience"))
        );
    }
}
