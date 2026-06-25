package recommendations.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import recommendations.recommendation.dto.RecommendationsResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RecommendationServiceTest {

    @InjectMocks
    RecommendationService recommendationService;

    @Mock
    RecommendationRepository recommendationRepository;


    public RecommendationsResponse createRecommendationResponseObject()
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

    @Test
    public void getAllRecommendations_ShouldReturnRecommendations(){
        //pagination setup
        Pageable pageable = PageRequest.of(0, 20);

        //response expectation
        RecommendationsResponse recommendationMock = createRecommendationResponseObject();
        List<RecommendationsResponse>  recommendationsMock = List.of(recommendationMock);

        //wrap the response in a page
        Page<RecommendationsResponse> expectedPage =
                new PageImpl<>(recommendationsMock, pageable, recommendationsMock.size());

        //mock the repository
        Mockito.when(recommendationRepository.findAllRecommendationsWithDetails(pageable))
                .thenReturn(expectedPage);

        //recieve the result from the service
        Page<RecommendationsResponse> result = recommendationService.getAllRecommendations(0,20);

        //validate the response
        assertEquals("klay",result.getContent().get(0).getUserFullName());
        assertEquals(1,result.getTotalElements());

    }
}
