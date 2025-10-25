package recommendations.recommendationCategory;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;


@RequiredArgsConstructor
@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	@Autowired
    private Validator validator;
	
	//return all the categories
	public List<Category> getAllCategories(){
		return categoryRepository.findAll();
	}
	
	//create category
	public Category create(Category category){
		Set<ConstraintViolation<Category>> violations = validator.validate(category);

        if (!violations.isEmpty()) {
            // 2️⃣ Collect all error messages
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<Category> v : violations) {
                errors.append(v.getMessage()).append("; ");
            }

            // 3️⃣ Throw an exception or custom error
            throw new IllegalArgumentException("Validation failed: " + errors.toString());
        }
        //save this category
		return categoryRepository.save(category);
	}
	
	//get category by its id
	public Category getCategory(int id) {
		return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
	}
	
}
