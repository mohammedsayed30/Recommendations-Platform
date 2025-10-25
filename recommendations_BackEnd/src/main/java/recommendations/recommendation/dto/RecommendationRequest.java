package recommendations.recommendation.dto;

import lombok.Data;


@Data
public class RecommendationRequest {
	private String description;
    private Integer type_id;
    private Integer cat_id;
	
}


