package recommendations.recommendation;



import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


import lombok.Data;
import recommendations.recommendationCategory.Category;
import recommendations.recommendationRatings.RecommendationRatings;
import recommendations.recommendationType.Type;
import recommendations.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Data
@Entity
@Table(name ="recommendation")

public class Recommendation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@NotBlank(message = "the description is mendatory")
	@Column(columnDefinition = "TEXT", nullable = false)
	private String description;
	
	
	
	// Many recommendations belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // FK column
    private User user;
    
    // Many recommendations belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id", nullable = false)  // FK column
    private Category category ;
    
    // Many recommendations belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)  // FK column
    private Type type;
    
    //one recommendation has many ratings 
    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendationRatings> ratings = new ArrayList<>();
	
	@CreationTimestamp
	@JsonIgnore
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@JsonIgnore
	private LocalDateTime updatedAt;

}
