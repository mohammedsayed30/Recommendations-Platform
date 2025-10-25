package recommendations.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationUpdatedResponse {
	private Integer id;
    private String description;
    private String typeName;
    private String categoryName;
}
