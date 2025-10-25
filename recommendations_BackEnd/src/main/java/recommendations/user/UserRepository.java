package recommendations.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
	//to get the user by his email for login functionality
	Optional<User> findByEmail(String email);
}
