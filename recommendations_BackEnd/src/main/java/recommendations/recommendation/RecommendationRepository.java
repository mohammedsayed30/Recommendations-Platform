package recommendations.recommendation;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;  

import recommendations.recommendation.dto.RecommendationsResponse;



public interface RecommendationRepository extends JpaRepository<Recommendation, Integer>{
	//return all recommendations with user/category/type informations
	@Query("""
			SELECT new recommendations.recommendation.dto.RecommendationsResponse(
			    r.id,
			    u.fullName,
			    u.jobTitle,
			    u.yearsOfExperience,
			    c.name,
			    t.name,
			    r.description,
			    COUNT(rr.id),
			    COALESCE(AVG(rr.stars), 0)
			)
			FROM Recommendation r
			JOIN r.user u
			JOIN r.category c
			JOIN r.type t
			LEFT JOIN r.ratings rr
			GROUP BY r.id, u.fullName, u.jobTitle, u.yearsOfExperience, c.name, t.name, r.description
			""")
	Page<RecommendationsResponse> findAllRecommendationsWithDetails(Pageable pageable);

	
	//return single recommendation with user/category/type informations
	@Query("""
			SELECT new recommendations.recommendation.dto.RecommendationsResponse(
			    r.id,
			    u.fullName,
			    u.jobTitle,
			    u.yearsOfExperience,
			    c.name,
			    t.name,
			    r.description,
			    COUNT(rr.id),
			    COALESCE(AVG(rr.stars), 0)
			)
			FROM Recommendation r
			JOIN r.user u
			JOIN r.category c
			JOIN r.type t
			LEFT JOIN r.ratings rr
			WHERE r.id = :id
			GROUP BY r.id, u.fullName, u.jobTitle, u.yearsOfExperience, c.name, t.name, r.description
			""")
    Optional<RecommendationsResponse> findRecommendationDetailsById(@Param("id") Integer id);
	
}
