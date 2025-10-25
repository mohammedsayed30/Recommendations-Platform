package recommendations.recommendation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import recommendations.config.JwtService;
import recommendations.recommendation.dto.RecommendationRequest;
import recommendations.recommendation.dto.RecommendationUpdateRequest;
import recommendations.recommendation.dto.RecommendationUpdatedResponse;
import recommendations.recommendation.dto.RecommendationsResponse;
import recommendations.recommendationCategory.Category;
import recommendations.recommendationCategory.CategoryRepository;
import recommendations.recommendationCategory.CategoryService;
import recommendations.recommendationType.Type;
import recommendations.recommendationType.TypeRepository;
import recommendations.recommendationType.TypeService;
import recommendations.user.User;
import recommendations.user.UserRepository;
import recommendations.user.UserService;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private JwtService jwtService;
    
    
    //get all the recommendations 
    public Page<RecommendationsResponse> getAllRecommendations(int page,int size) {
        Pageable pageable = PageRequest.of(page, size); // Fixed at 20 per page
        return recommendationRepository.findAllRecommendationsWithDetails(pageable);
    }
    
    //get only recommendation by its id
    public RecommendationsResponse getRecommendationById(Integer id) {
        return recommendationRepository.findRecommendationDetailsById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation not found with id: " + id));
    }
    
    //get only recommendation by its id
    public Recommendation getRecommendation(Integer id) {
        return recommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation not found with id: " + id));
    }

    public Recommendation create(RecommendationRequest recommendationRequest, String JwtToken) {
    	//get the value of the token
    	String jwtToken = JwtToken.replace("Bearer ", "").trim();
        // Extract email from JWT token
        String userEmail = jwtService.extractUsername(jwtToken);
        System.out.println(jwtToken);
        System.out.println(userEmail);
        // Find the user by email
        User user = (User) userService.loadUserByUsername(userEmail);
          
        // Validate description
        if (recommendationRequest.getDescription() == null || recommendationRequest.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is mandatory");
        }

        // Validate category
        if (recommendationRequest.getCat_id() == null) {
            throw new IllegalArgumentException("Category is required");
        }
        
        //get the category by its id 
        Category category = categoryService.getCategory(recommendationRequest.getCat_id());

        //  Validate type
        if (recommendationRequest.getType_id() == null) {
            throw new IllegalArgumentException("Type is required");
        }
        //get the type for this recommendation
        Type type = typeService.getType(recommendationRequest.getType_id());

        Recommendation recommendation = new Recommendation();
        //set all the required fields 
        recommendation.setDescription(recommendationRequest.getDescription());
        recommendation.setUser(user);
        recommendation.setCategory(category);
        recommendation.setType(type);
        

        // Save and return
        return recommendationRepository.save(recommendation);
    }
    
    //update recommendations
    public RecommendationUpdatedResponse updateRecommendation(RecommendationUpdateRequest recommendationUpdateRequest, String JwtToken,int recId) {
    	String jwtToken = JwtToken.replace("Bearer ", "").trim();
    	// Extract email from JWT token
        String userEmail = jwtService.extractUsername(jwtToken);

        // Find the user by email
        User user = (User) userService.loadUserByUsername(userEmail);
     
        //get the user-id
        Integer userId = user.getId();
        
        
        
        //find the recommendation of that id and get the use id
        Recommendation recommendation = recommendationRepository.findById(recId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found with id: " + recId));
        
        //get the owner user
        Integer ownerId = recommendation.getUser().getId();
        
    	//get the user_id of that recommendation and make sure the owner one is the requested one
        if(!userId.equals(ownerId)) {
        	//unauthorized action
        	throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to perform this action,it is not your damn recommendation");
        }
        
         
        // Update only provided fields
        if (recommendationUpdateRequest.getDescription() != null) {
            recommendation.setDescription(recommendationUpdateRequest.getDescription());
        }

        // Check for non-zero integers before updating
        if (recommendationUpdateRequest.getType_id() != 0) {
        	recommendation.setType(typeService.getType(recommendationUpdateRequest.getType_id()));
        }

        if (recommendationUpdateRequest.getCat_id() != 0) {
            recommendation.setCategory(categoryService.getCategory(recommendationUpdateRequest.getCat_id()));
        }

        // Save changes to that recommendation
        Recommendation saved =  recommendationRepository.save(recommendation);
        
        // Convert to DTO
        return new RecommendationUpdatedResponse(
    		saved.getId(),
            saved.getDescription(),
            saved.getType() != null ? saved.getType().getName() : null,
            saved.getCategory() != null ? saved.getCategory().getName() : null
        );
        
       
    }
    
    //delete a recommendation
    
    public void deleteRecommendation(String JwtToken,int recId) {
    	String jwtToken = JwtToken.replace("Bearer ", "").trim();
    	// Extract email from JWT token
        String userEmail = jwtService.extractUsername(jwtToken);

        // Find the user by email
        User user = (User) userService.loadUserByUsername(userEmail);
     
        //get the user-id
        Integer userId = user.getId();
        
        
        
        //find the recommendation of that id and get the use id
        Recommendation recommendation = recommendationRepository.findById(recId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found with id: " + recId));
        
        //get the owner user
        Integer ownerId = recommendation.getUser().getId();
        
    	//get the user_id of that recommendation and make sure the owner one is the requested one
        if(!userId.equals(ownerId)) {
        	//unauthorized action
        	throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to perform this action,it is not your damn recommendation");
        }
        

        // delete that recommendation
        recommendationRepository.deleteById(recId);
    }
    
    
    
    
}
