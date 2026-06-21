package recommendations.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    public void simulateSavingUserInDB()
    {
        User user = new User();
        user.setFullName("aliklay");
        user.setEmail("test@example.com");
        user.setPassword("123456");
        user.setJobTitle("Software engineer");
        user.setLinkedinProfile("https://linkedin.com/in/test");
        user.setYearsOfExperience(5);

        User saved = userRepo.save(user);

        Optional<User> result = userRepo.findById(saved.getId());

        assertTrue(result.isPresent());

        //check if the name is true or not
        assertEquals(user.getFullName(),result.get().getFullName());

    }
}
