package recommendations.recommendationType;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotBlank;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Data;
import recommendations.recommendation.Recommendation;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name ="recom_type")
public class Type {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@NotBlank(message = "name of the type is mendatory")
	@Column(nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Recommendation> recommendations;
	
	@CreationTimestamp
	@JsonIgnore
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@JsonIgnore
	private LocalDateTime updatedAt;

}
