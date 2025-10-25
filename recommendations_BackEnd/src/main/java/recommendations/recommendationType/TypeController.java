package recommendations.recommendationType;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import recommendations.recommendationCategory.Category;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/type")
public class TypeController {

	private final TypeService typeService;
	
	@GetMapping()
	public ResponseEntity<?> Types(){
	  List<Type> types= typeService.getAllTypes();
	  
	  return ResponseEntity.ok(types);
	}
	
	@PostMapping("/admin")
    public ResponseEntity<?> createType(@RequestBody Type RequestType){
		Type type  = typeService.create(RequestType);
	    return ResponseEntity.ok(type);
    }
}
