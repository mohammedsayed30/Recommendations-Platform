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

    @Autowired
    private MemcachedClient memcachedClient;

    @GetMapping("/{id}")
    public ResponseEntity<?> GetRecommendationAIScore(@PathVariable Integer id){

        try {
            //get the required recommendation
            RecommendationsResponse recommendation = recommendationService.getRecommendationById(id);

            //build the key
            String cacheKey = "ai_score_" + id;

            //check if it exists or not
            Integer AIScoreResponse = (Integer) memcachedClient.get(cacheKey);

            if(AIScoreResponse == null) { //not exists in the cache

                //get the content of the recommendation
                String content = recommendation.getDescription();

                //get the score of the AI for that recommendation
                AIScoreResponse = aiScoreService.getRecommendationAIScore(content);

                // store in cache for 12 hour (43200 seconds)
                memcachedClient.set(cacheKey, 43200, AIScoreResponse);

            }

            //return the AI response
            return ResponseEntity.ok(AIScoreResponse);
        }
        catch (Exception e) {

                log.error("Error getting AI score: {}", e.getMessage());
                return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
