package recommendations.recommendationRatings;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import recommendations.recommendation.Recommendation;
import recommendations.user.User;

@Data
@Entity
@Table(name ="recommendation_ratings",
                            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "recommendation_id"})})

public class RecommendationRatings {
	
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private Recommendation recommendation;

    @Column(nullable = false)
    @Min(0)
    @Max(5)
    private int stars;

}
