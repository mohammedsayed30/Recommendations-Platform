package recommendations.recommendationCategory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
	
	private final CategoryService categoryService;
	
	
	@GetMapping
    public ResponseEntity<?> Categories(){
		List<Category> categories = categoryService.getAllCategories();
	    return ResponseEntity.ok(categories);
    }
	//only for testing it suppose be hidden for normal users not only for admins
	@PostMapping("/admin")
    public ResponseEntity<?> createCategory(@RequestBody Category RequestCategory){
		Category category  = categoryService.create(RequestCategory);
	    return ResponseEntity.ok(category);
    }
}
