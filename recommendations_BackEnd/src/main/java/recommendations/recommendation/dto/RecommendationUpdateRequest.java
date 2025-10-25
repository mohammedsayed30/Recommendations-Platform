package recommendations.recommendation.dto;

import lombok.Data;


@Data
public class RecommendationUpdateRequest {
	private String description;
    private int type_id;
    private int cat_id;
}
