package fr.ekinci.tutorialspringsecurityjwt.security.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

/**
 * @author Gokan EKINCI
 */
@Data
@Builder
public class AuthorizationRoute {
	private String route;
	private Collection<HttpMethod> requestMethods;
	private Collection<? extends GrantedAuthority> authorities;
}
