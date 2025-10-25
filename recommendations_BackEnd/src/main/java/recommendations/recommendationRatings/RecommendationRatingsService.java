package recommendations.recommendationRatings;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import recommendations.config.JwtService;
import recommendations.recommendation.Recommendation;
import recommendations.recommendation.RecommendationService;
import recommendations.recommendation.dto.RecommendationUpdatedResponse;
import recommendations.recommendationRatings.dto.RatingRequest;
import recommendations.recommendationRatings.dto.RatingResponse;
import recommendations.user.User;
import recommendations.user.UserService;

@Service
public class RecommendationRatingsService {
	
	@Autowired
	private RecommendationRatingsRepository recommendationRatingsRepository;
	@Autowired
	private RecommendationService recommendationService;
	@Autowired
	private UserService  userService;
	@Autowired
	private JwtService  jwtService;

	public RatingResponse create(RatingRequest ratingRequest,String jwtToken,Integer id) {
		//validate the stars
		if (ratingRequest.getStars() < 0 || ratingRequest.getStars() > 5) {
		    throw new IllegalArgumentException("Stars must be between 0 and 5");
		}
		
		// Extract email from JWT token
        String userEmail = jwtService.extractUsername(jwtToken);
        // Find the user by email
        User user = (User) userService.loadUserByUsername(userEmail);
        
        //get the recommendation
        Recommendation recommendation = recommendationService.getRecommendation(id);
        
        //find the rating is exist or not
        
        Optional<RecommendationRatings> existing=recommendationRatingsRepository.findByUserAndRecommendation(user, recommendation);
        
        //check if it is update or create
        
        if (existing.isPresent()) {
            // Update existing rating
            RecommendationRatings rating = existing.get();
            rating.setStars(ratingRequest.getStars());
            recommendationRatingsRepository.save(rating);
               
        } else {
            // Create new rating
            RecommendationRatings rating = new RecommendationRatings();
            rating.setUser(user);
            rating.setRecommendation(recommendation);
            rating.setStars(ratingRequest.getStars());
            recommendationRatingsRepository.save(rating);
            
        }
       // Convert to DTO
        return new RatingResponse(
        		user.getId(),
        		id,
        		ratingRequest.getStars()
        );
		
	}
}
