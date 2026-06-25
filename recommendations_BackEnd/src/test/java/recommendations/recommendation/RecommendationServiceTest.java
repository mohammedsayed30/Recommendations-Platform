package recommendations.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import recommendations.recommendation.dto.RecommendationRequest;
import recommendations.recommendation.dto.RecommendationsResponse;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RecommendationServiceTest {

    @InjectMocks
    RecommendationService recommendationService;

    @Mock
    RecommendationRepository recommendationRepository;


    //create recommendation request object
    public RecommendationRequest createRecommendationRequestObject()
    {
        RecommendationRequest recommendationMock = new RecommendationRequest();

        recommendationMock.setDescription("DDA is the most powerful book for software engineering");
        recommendationMock.setCat_id(10);
        recommendationMock.setType_id(3);

        return  recommendationMock;

    }

    //create recommendation response object
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
    @DisplayName("TC_01 : Should return the paginated recommendations")
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

    @Test
    @DisplayName("TC_02 : Should throw an exception for nagative page number")
    public void getAllRecommendations_ShouldThrowPageIsNagativeException() {

       assertThrows(IllegalArgumentException.class, () -> {
                 recommendationService.getAllRecommendations(-1,20);
       });

    }

    @Test
    @DisplayName("TC_03 : Should throw an exception for nagative size number")
    public void getAllRecommendations_ShouldThrowSizeIsZeroOrNagativeException() {

        assertThrows(IllegalArgumentException.class, () -> {
            recommendationService.getAllRecommendations(1,-20);
        });

    }

    @Test
    @DisplayName("TC_04 : Should return recommendation")
    public void getRecommendationById_ShouldReturnRecommendation() {

        RecommendationsResponse recommendation = createRecommendationResponseObject();

        Mockito.when(recommendationRepository.findRecommendationDetailsById(Mockito.anyInt()))
                        .thenReturn(Optional.ofNullable(recommendation));

        RecommendationsResponse result= recommendationService.getRecommendationById(1);

        assertEquals("klay",result.getUserFullName());
        assertEquals(1,result.getRecommendationId());

    }


    @Test
    @DisplayName("TC_05 : Should return RuntimeException if not found")
    public void getRecommendationById_ShouldReturnRuntimeException() {

        int id = 0;  //id that will never exist

        Mockito.when(recommendationRepository.findRecommendationDetailsById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        //throw a runtime exception if the recommendation not found
        assertThrows(RuntimeException.class, () -> {
            recommendationService.getRecommendationById(id);
        });


    }

    @Test
    @DisplayName("TC_06 : should save recommendation into the database without any errors")
    public void saveRecommendation_ShouldSaveTheRecommendationToDB() {




    }

}
