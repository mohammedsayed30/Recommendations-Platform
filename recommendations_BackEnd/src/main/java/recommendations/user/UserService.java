package recommendations.user;

import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import jakarta.validation.Validator;
import recommendations.config.JwtService;
import recommendations.user.dto.LoginRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private Validator validator;
	
	//for password hashing
    private PasswordEncoder passwordEncoder;
	
    private JwtService jwtService;
    
	private  UserRepository userRepo;

	@Autowired
    public UserService(UserRepository userRepo,@Lazy PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder=passwordEncoder;
        this.jwtService = jwtService;
    }
	 
	public User create(User user){
		    //get all the  unique violations against the UserEntity Constraints 
		    Set<ConstraintViolation<User>> violations = validator.validate(user);
		    //check for validation violations
		    if (!violations.isEmpty()) {
	            StringBuilder sb = new StringBuilder();
	            for (ConstraintViolation<User> violation : violations) {
	            	sb.append(violation.getPropertyPath())
	                .append(": ")
	                .append(violation.getMessage())
	                .append("; ");
	            }
	            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
	        }
		    System.out.println("User registered successfully!");
		    //hash the password
		    user.setPassword(passwordEncoder.encode(user.getPassword()));
		    //return that saved user 
	        return userRepo.save(user);
	 }
	
	 public Map<String, Object> verifyUser(LoginRequest loginRequest) {
		 String email = loginRequest.getEmail();
		 String password = loginRequest.getPassword();
		 User user = (User) loadUserByUsername(email);

	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new RuntimeException("Invalid email or password");
	    }
	    
	    //generate token
	    String token = jwtService.generateToken(user);

	    //make response that contain user and his token to make authentication responses
	    Map<String, Object> response = new HashMap<>();
	    
	    //build the new response and his token
	    response.put("token", token);
	    response.put("user", user);
	    
	    return response;
	 }
	 
	 
	 @Override
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
	 }
}
