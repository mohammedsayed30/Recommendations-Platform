package recommendations.recommendationType;



import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import recommendations.recommendationCategory.Category;


@RequiredArgsConstructor
@Service
public class TypeService {
   private final TypeRepository typeRepository;
   
   @Autowired
   private Validator validator;
   
   public List<Type> getAllTypes(){
	   //return all the available types in the system
	   return typeRepository.findAll();
   }
   
   public Type create(Type type){
		Set<ConstraintViolation<Type>> violations = validator.validate(type);

       if (!violations.isEmpty()) {
           // 2️⃣ Collect all error messages
           StringBuilder errors = new StringBuilder();
           for (ConstraintViolation<Type> v : violations) {
               errors.append(v.getMessage()).append("; ");
           }

           // 3️⃣ Throw an exception or custom error
           throw new IllegalArgumentException("Validation failed: " + errors.toString());
       }
       //save this category
		return typeRepository.save(type);
	}
   
 //get category by its id
 	public Type getType(int id) {
 		return typeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Type not found"));
 	}
}
