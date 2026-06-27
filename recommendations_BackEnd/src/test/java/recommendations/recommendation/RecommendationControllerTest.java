package recommendations.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import recommendations.config.SecurityConfig;
import recommendations.recommendation.dto.RecommendationRequest;
import recommendations.recommendation.dto.RecommendationUpdateRequest;
import recommendations.recommendation.dto.RecommendationUpdatedResponse;
import recommendations.recommendation.dto.RecommendationsResponse;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import recommendations.recommendationCategory.Category;
import recommendations.recommendationType.Type;
import recommendations.user.User;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RecommendationControllerTest {

    @MockBean
    private RecommendationService recommendationService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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

    private Category createCategoryObject()
    {
        Category categoryMock = new Category();

        categoryMock.setId(1);
        categoryMock.setName("Book");

        return  categoryMock;

    }

    private Type createTypeObject()
    {
        Type typeMock = new Type();

        typeMock.setId(1);
        typeMock.setName("Book");

        return  typeMock;

    }

    //create recommendation request object
    private RecommendationRequest createRecommendationRequestObject()
    {
        RecommendationRequest recommendationMock = new RecommendationRequest();

        recommendationMock.setDescription("DDA book is the most powerful book for software engineering");
        recommendationMock.setCat_id(10);
        recommendationMock.setType_id(3);

        return  recommendationMock;

    }

    private RecommendationUpdatedResponse createRecommendationUpdateResponseObject()
    {
        RecommendationUpdatedResponse updatedRecommendationMock = new RecommendationUpdatedResponse();

        updatedRecommendationMock.setId(1);
        updatedRecommendationMock.setDescription("updated description");
        updatedRecommendationMock.setCategoryName("advice");
        updatedRecommendationMock.setTypeName("Software Engineer");

        return  updatedRecommendationMock;

    }

    public RecommendationUpdateRequest createRecommendationUpdateRequestObject()
    {
        RecommendationUpdateRequest recommendationMock = new RecommendationUpdateRequest();

        recommendationMock.setDescription("DDA book is the most powerful book for software engineeringg");
        recommendationMock.setCat_id(10);
        recommendationMock.setType_id(3);

        return  recommendationMock;

    }

    private User createValidUser(){
        User user = new User();
        user.setId(1);
        user.setFullName("aliklay");
        user.setEmail("mohamedsayedshaaban2023@gmail.com");
        user.setPassword("123456");
        user.setJobTitle("Software engineer");
        user.setLinkedinProfile("https://www.linkedin.com/in/mohammed-shaaban-573038254/");
        user.setYearsOfExperience(5);
        return user;
    }

    private Recommendation createRecommendationWithUserAndCatAndTypeResponseObject()
    {
        Recommendation recommendationRes = new Recommendation();
        recommendationRes.setId(1);
        recommendationRes.setDescription("DSA the most important thing in software engineer");
        recommendationRes.setUser(createValidUser());
        recommendationRes.setCategory(createCategoryObject());
        recommendationRes.setType(createTypeObject());
        recommendationRes.setRatings(new ArrayList<>());

        return  recommendationRes;

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

    @Test
    @DisplayName("TC_03 : hit the create recommendation end point")
    public void createRecommendationEndPointTest() throws Exception {

        //response expectation
        Recommendation recommendationMock = createRecommendationWithUserAndCatAndTypeResponseObject();


        //to isolate service from the test
        Mockito.when(recommendationService.create(Mockito.any(),Mockito.anyString()))
                .thenReturn(recommendationMock);



        mockMvc.perform(post("/api/v1/recommendation")
                        .header("Authorization", "Bearer faketoken123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecommendationRequestObject())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("DSA the most important thing in software engineer"));

    }

    @Test
    @DisplayName("TC_04 : hit the update recommendation end point")
    public void updateRecommendationEndPointTest() throws Exception {

        //response expectation
        RecommendationUpdatedResponse recommendationMock = createRecommendationUpdateResponseObject();


        //to isolate service from the test
        Mockito.when(recommendationService.updateRecommendation(Mockito.any(),Mockito.anyString(),Mockito.anyInt()))
                .thenReturn(recommendationMock);



        mockMvc.perform(put("/api/v1/recommendation/{id}",1)
                        .header("Authorization", "Bearer faketoken123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecommendationUpdateRequestObject())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("updated description"));

    }

    @Test
    @DisplayName("TC_05 : hit the delete recommendation end point")
    public void deleteRecommendationEndPointTest() throws Exception {


        //to isolate service from the test
        Mockito.doNothing().when(recommendationService)
                .deleteRecommendation(Mockito.anyString(), Mockito.anyInt());


        mockMvc.perform(delete("/api/v1/recommendation/{id}",1)
                        .header("Authorization", "Bearer faketoken123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
}
