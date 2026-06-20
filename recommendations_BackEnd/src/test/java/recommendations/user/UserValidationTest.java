package recommendations.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserValidationTest {
    //to check the rules
    private Validator validator;

    //start function before tests for all rules
    @BeforeEach
    public void setUp(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private User buildValidateUser(){
        User user = new User();
        user.setFullName("aliklay");
        user.setEmail("mohamedsayedshaaban2023@gmail.com");
        user.setPassword("123456");
        user.setJobTitle("Software engineer");
        user.setLinkedinProfile("https://www.linkedin.com/in/mohammed-shaaban-573038254/");
        user.setYearsOfExperience(5);
        return user;
    }

    //
    @Test
    @DisplayName("TC_01: Validate user has zero violations")
    public void validateUserHasZeroViolations(){
        //build valid user
        User user = buildValidateUser();

        //get the violdations if there is existing
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        //check there is no violations
        assertTrue(violations.isEmpty());
    }


}
