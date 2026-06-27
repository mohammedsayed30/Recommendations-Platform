package recommendations.recommendation;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import recommendations.recommendation.dto.RecommendationsResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;


import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class RecommendationControllerTest {

    @MockBean
    private RecommendationService recommendationService;

    @Autowired
    MockMvc mockMvc;

    //create recommendation response object
    private RecommendationsResponse createRecommendationResponseObject()
    {
        RecommendationsResponse recommendationMock = new RecommendationsResponse();

        recommendationMock.setRecommendationId(1);
        recommendationMock.setUserFullName("ali klay");
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
    @DisplayName("TC_01 : hit the get all recommendation end point")
    public void getAllRecommendationEndPointTest() throws Exception {

        //pagination setup
        Pageable pageable = PageRequest.of(0, 20);

        //response expectation
        RecommendationsResponse recommendationMock = createRecommendationResponseObject();
        List<RecommendationsResponse> recommendationsMock = List.of(recommendationMock);

        //wrap the response in a page
        Page<RecommendationsResponse> expectedPage =
                new PageImpl<>(recommendationsMock, pageable, recommendationsMock.size());

        //to isolate service from the test
        Mockito.when(recommendationService.getAllRecommendations(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(expectedPage);

        mockMvc.perform(get("/api/v1/recommendation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].recommendationId").value(1))
                .andExpect(jsonPath("$.content[0].userFullName").value("ali klay"))
                .andExpect(jsonPath("$.content[0].description").value("DSA the most important thing in software engineer"));

    }

    @Test
    @DisplayName("TC_02 : hit the get one recommendation end point")
    public void getOneRecommendationEndPointTest() throws Exception {

        //response expectation
        RecommendationsResponse recommendationMock = createRecommendationResponseObject();


        //to isolate service from the test
        Mockito.when(recommendationService.getRecommendationById(Mockito.anyInt()))
                .thenReturn(recommendationMock);

        mockMvc.perform(get("/api/v1/recommendation/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationId").value(1))
                .andExpect(jsonPath("$.userFullName").value("ali klay"))
                .andExpect(jsonPath("$.description").value("DSA the most important thing in software engineer"));

    }
}
