package recommendations.recommendationRatings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
	private int userId;
	private int recommendationId;
	private int stars;
}
