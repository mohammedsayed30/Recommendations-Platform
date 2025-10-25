package recommendations.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationsResponse {
	private Integer recommendationId;
    private String userFullName;
    private String userTitle;
    private Integer userYearsOfExperience;
    private String categoryName;
    private String typeName;
    private String description;
    private Long reviews;
    private Double  rating;
}
