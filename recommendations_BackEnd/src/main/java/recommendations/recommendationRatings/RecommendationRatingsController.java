package recommendations.recommendationRatings;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import recommendations.recommendation.dto.RecommendationRequest;
import recommendations.recommendationRatings.dto.RatingRequest;
import recommendations.recommendationRatings.dto.RatingResponse;

@Controller
@RestController
@RequestMapping("/api/v1/recommendation")
public class RecommendationRatingsController {
	
	@Autowired
	RecommendationRatingsService recommendationRatingsService;
	
	//create rating
	@PostMapping("/{id}/rating")
	public ResponseEntity<RatingResponse> createOrUpdateRating(@RequestBody RatingRequest ratingRequest,@RequestHeader("Authorization") String jwt,@PathVariable Integer id){
		RatingResponse recommendationRatings = recommendationRatingsService.create(ratingRequest,jwt,id);
		return ResponseEntity.ok(recommendationRatings);
	}
	
	
}
