package recommendations.recommendation;

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

import io.jsonwebtoken.lang.Arrays;
import recommendations.recommendation.dto.RecommendationRequest;
import recommendations.recommendation.dto.RecommendationUpdateRequest;
import recommendations.recommendation.dto.RecommendationUpdatedResponse;
import recommendations.recommendation.dto.RecommendationsResponse;


@RestController
@RequestMapping("/api/v1/recommendation")
public class RecommendationController {
	
	@Autowired
	private RecommendationService recommendationService;
	
	//get all the recommendations with pagination
	@GetMapping
    public Page<RecommendationsResponse> getAllRecommendations(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "20") int size) {
           return recommendationService.getAllRecommendations(page, size);
    }
	
	//get a specific recommendation
	@GetMapping("/{id}")
	public RecommendationsResponse getRecommendationById(@PathVariable Integer id) {
	    return recommendationService.getRecommendationById(id);
	}
	
	//create recommendation
	@PostMapping
	public ResponseEntity<?> create(@RequestBody RecommendationRequest request
			,@RequestHeader("Authorization") String jwt) {
			
		//call the create  function in the service provider
		Recommendation rec = recommendationService.create(request,jwt);
	
		return ResponseEntity.ok(rec);
	}
	
	//update recommendation
	@PutMapping("/{id}")
	public ResponseEntity<RecommendationUpdatedResponse> updateRecommendationById(@RequestBody RecommendationUpdateRequest request
			,@RequestHeader("Authorization") String jwt,@PathVariable int id) {
		
		//call the update function in the service provider
		RecommendationUpdatedResponse rec =  recommendationService.updateRecommendation(request,jwt,id);
		
		return ResponseEntity.ok(rec);
	}
	
	//delete recommendation
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRecommendationById(@RequestHeader("Authorization") String jwt,@PathVariable Integer id) {
		//call the update function in the service provider
		recommendationService.deleteRecommendation(jwt,id);
		return ResponseEntity.noContent().build();
	}

}
