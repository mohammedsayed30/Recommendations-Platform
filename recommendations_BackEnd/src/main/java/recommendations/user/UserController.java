package recommendations.user;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import recommendations.user.dto.LoginRequest;


@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
	
	@PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user){
		User userCreated = userService.create(user);
	    return ResponseEntity.ok(userCreated);
    }
	
	@PostMapping("/login")
    public ResponseEntity<?> verifyUser(@RequestBody LoginRequest loginRequest){
		Map<String, Object> userVerifyed = userService.verifyUser(loginRequest);
		
	    return ResponseEntity.ok(userVerifyed);
    }
	
	
}


