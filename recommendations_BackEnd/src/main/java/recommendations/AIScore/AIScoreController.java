package recommendations.AIScore;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import recommendations.recommendation.RecommendationService;
import recommendations.recommendation.dto.RecommendationsResponse;


@RestController
@RequestMapping("/api/v1/score")
@Slf4j
public class AIScoreController {
    @Autowired
    AIScoreService aiScoreService;
    @Autowired
    RecommendationService recommendationService;

    @GetMapping("/{id}")
    public ResponseEntity<?> GetRecommendationAIScore(@PathVariable Integer id){

        try {
            //get the required recommendation
            RecommendationsResponse recommendation = recommendationService.getRecommendationById(id);

            //get the content of the recommendation
            String content = recommendation.getDescription();

            //get the score of the AI for that recommendation
            Integer AIScoreResponse = aiScoreService.getRecommendationAIScore(content);

            //return the AI response
            return ResponseEntity.ok(AIScoreResponse);
        }
        catch (Exception e) {

                log.error("Error getting AI score: {}", e.getMessage());
                return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
