package recommendations.config;

import java.io.IOException;


import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import recommendations.user.User;
import recommendations.user.UserService;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtService JwtService;
	private final UserService userService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String AuthHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String userEmail;
		
		//check if the token sended or not 
		if(AuthHeader==null || !AuthHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//get the JWT and user Email
		jwtToken = AuthHeader.substring(7).trim();
		userEmail = JwtService.extractUsername(jwtToken);
		if(userEmail !=null && SecurityContextHolder.getContext().getAuthentication() ==null) {
			User userDetails =  (User) userService.loadUserByUsername(userEmail);
			
			if(JwtService.isTokenValid(jwtToken, userDetails)) {
				//create authentication object
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				//It tells Spring Security This request is now authenticated as this user
				SecurityContextHolder.getContext().setAuthentication(authToken);
				//from now  any controller or service can get the authenticated user. 
			}
			
		}
		filterChain.doFilter(request, response);
	}

}
