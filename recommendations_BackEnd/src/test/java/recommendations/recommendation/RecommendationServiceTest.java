package recommendations.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import recommendations.recommendation.dto.RecommendationsResponse;

@SpringBootTest
public class RecommendationServiceTest {

    @InjectMocks
    RecommendationService recommendationService;

    @Mock
    RecommendationRepository recommendationRepository;


    public RecommendationsResponse setUp()
    {
        RecommendationsResponse recommendationMock = new RecommendationsResponse();

        recommendationMock.setRecommendationId(1);
        recommendationMock.setUserFullName("klay");
        recommendationMock.setCategoryName("advice");
        recommendationMock.setUserTitle("Software Engineer");
        recommendationMock.setUserYearsOfExperience(5);
        recommendationMock.setTypeName("Software Engineer");
        recommendationMock.setDescription("DSA the most important thing in software engineer");
        recommendationMock.setReviews((long)4);
        recommendationMock.setRating(4.5);

        return  recommendationMock;

    }

    public void getAllRecommendations_ShouldReturnRecommendations(){

    }
}
