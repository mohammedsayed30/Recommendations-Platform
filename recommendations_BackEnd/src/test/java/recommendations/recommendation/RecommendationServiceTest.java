package recommendations.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import recommendations.config.JwtService;
import recommendations.recommendation.dto.RecommendationRequest;
import recommendations.recommendation.dto.RecommendationsResponse;
import recommendations.recommendationCategory.Category;
import recommendations.recommendationCategory.CategoryService;
import recommendations.recommendationType.Type;
import recommendations.recommendationType.TypeService;
import recommendations.user.User;
import recommendations.user.UserService;

import java.util.ArrayList;
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

    @Mock
    private CategoryService categoryService;
    @Mock
    private TypeService typeService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;


    //build valid fake user for simulation
    private User createValidUser(){
        User user = new User();
        user.setFullName("aliklay");
        user.setEmail("mohamedsayedshaaban2023@gmail.com");
        user.setPassword("123456");
        user.setJobTitle("Software engineer");
        user.setLinkedinProfile("https://www.linkedin.com/in/mohammed-shaaban-573038254/");
        user.setYearsOfExperience(5);
        return user;
    }

    //create recommendation request object
    public RecommendationRequest createRecommendationRequestObject()
    {
        RecommendationRequest recommendationMock = new RecommendationRequest();

        recommendationMock.setDescription("DDA book is the most powerful book for software engineering");
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

    public Category createCategoryObject()
    {
        Category categoryMock = new Category();

        categoryMock.setId(1);
        categoryMock.setName("Book");

        return  categoryMock;

    }

    public Type createTypeObject()
    {
        Type typeMock = new Type();

        typeMock.setId(1);
        typeMock.setName("Book");

        return  typeMock;

    }

    public Recommendation createRecommendationWithUserAndCatAndTypeResponseObject()
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
    @DisplayName("TC_06 : should build and return the recommendation with correct user and description")
    public void saveRecommendation_ShouldSaveTheRecommendationToDB() {
        //create the recommendation object
        RecommendationRequest recommendation = createRecommendationRequestObject();
        //create the fake user
        User user = createValidUser();
        //create the fake category
        Category category = createCategoryObject();
        //create the fake type
        Type type =  createTypeObject();

        //create the recommendation
        Recommendation recommendationRes = createRecommendationWithUserAndCatAndTypeResponseObject();
        //fake email
        String userEmail = "mohamedsayedshaaban2022@gmail.com";

        //return fake email for isolation
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(userEmail);

        //return fake user for isolation
        Mockito.when(userService.loadUserByUsername(Mockito.anyString())).thenReturn(user);

        //return fake category for isolation
        Mockito.when(categoryService.getCategory(Mockito.anyInt())).thenReturn(category);

        //return fake type for isolation
        Mockito.when(typeService.getType(Mockito.anyInt())).thenReturn(type);

        //return fake recommendation when recommendation  got save it
        Mockito.when(recommendationRepository.save(Mockito.any())).thenReturn(recommendationRes);

        //call the actual service
        Recommendation result= recommendationService.create(recommendation,"anythingstring");

        assertEquals("aliklay",result.getUser().getFullName());
        assertEquals("DSA the most important thing in software engineer",result.getDescription());
        assertEquals(category, result.getCategory());
        assertEquals(type, result.getType());
    }

    @Test
    @DisplayName("TC_07 : should throw  an error due to the empty description")
    public void saveRecommendation_ShouldThrowAnErrorDueToEmptyDescription() {
        //create the recommendation object
        RecommendationRequest recommendation = createRecommendationRequestObject();

        recommendation.setDescription("");

        //create the fake user
        User user = createValidUser();
        //create the fake category
        Category category = createCategoryObject();
        //create the fake type
        Type type =  createTypeObject();

        //create the recommendation
        Recommendation recommendationRes = createRecommendationWithUserAndCatAndTypeResponseObject();
        //fake email
        String userEmail = "mohamedsayedshaaban2022@gmail.com";

        //return fake email for isolation
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(userEmail);

        //return fake user for isolation
        Mockito.when(userService.loadUserByUsername(Mockito.anyString())).thenReturn(user);

        //return fake category for isolation
        Mockito.when(categoryService.getCategory(Mockito.anyInt())).thenReturn(category);

        //return fake type for isolation
        Mockito.when(typeService.getType(Mockito.anyInt())).thenReturn(type);

        //return fake recommendation when recommendation  got save it
        Mockito.when(recommendationRepository.save(Mockito.any())).thenReturn(recommendationRes);

        assertThrows(IllegalArgumentException.class, () -> {
            recommendationService.create(recommendation,"anythingstring");
        });
    }

}
