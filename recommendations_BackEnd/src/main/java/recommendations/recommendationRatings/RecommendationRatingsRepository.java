package recommendations.recommendationRatings;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import recommendations.recommendation.Recommendation;
import recommendations.user.User;


public interface RecommendationRatingsRepository extends JpaRepository<RecommendationRatings, Long> {
    Optional<RecommendationRatings> findByUserAndRecommendation(User user, Recommendation recommendation);
}
