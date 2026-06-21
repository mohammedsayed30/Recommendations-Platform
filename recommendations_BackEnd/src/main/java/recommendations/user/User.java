package recommendations.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import jakarta.validation.constraints.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendations.recommendation.Recommendation;
import recommendations.recommendationRatings.RecommendationRatings;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name ="users")
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	
	
	@NotBlank(message = "full Name Is Mandatory")
	@Column(nullable = false)
	private String fullName;
	
	
	
	@NotBlank(message = "Email Is Mandatory")
	@Email(message = "Email should be valid")
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotBlank(message = "Password Is Mandatory")
	@Size(min = 6, max = 90, message = "Password must be between 8 and 20 characters")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	@Column(nullable = false)
	@NotBlank(message = "job_title Is Mandatory")
	private String jobTitle;
	
	@Column(nullable = false, unique = true)
	@NotBlank(message = "LinkedIn profile or similar website for integrity Is Mandatory")
	private String linkedinProfile;
	
	@Column(nullable = false)
	@NotNull(message = "Years of experience is mandatory")
    @Positive(message = "Years of experience must be a positive number")
	private Integer yearsOfExperience;
	
	 // One user can have many recommendations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Recommendation> recommendations;
    
   
	
	@CreationTimestamp
	@JsonIgnore
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@JsonIgnore
	private LocalDateTime updatedAt;
	
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of();
	}
	
	@Override
	@JsonIgnore
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}
	
	@Override
	@JsonIgnore
    public String getPassword() {
        return password;
    }
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}


}
