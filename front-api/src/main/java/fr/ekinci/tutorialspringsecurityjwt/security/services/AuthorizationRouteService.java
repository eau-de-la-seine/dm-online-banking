package fr.ekinci.tutorialspringsecurityjwt.security.services;

import fr.ekinci.tutorialspringsecurityjwt.security.models.AuthorizationRoute;
import fr.ekinci.tutorialspringsecurityjwt.security.models.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Gokan EKINCI
 */
@Service
public class AuthorizationRouteService implements IAuthorizationRouteService {

	private Map<String, AuthorizationRoute> authorizationRoutes;

	@PostConstruct
	public void init() {
		initAuthorizationRoutes();
	}

	@Override
	public boolean isAuthorizedRoute() {
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
			.getRequest();

		// Check if route exists
		final String path = request.getRequestURI();
		final AuthorizationRoute authorizationRoute = authorizationRoutes.get(path);
		if (authorizationRoute == null) {
			return false;
		}

		// Check if HTTP method is authorized
		HttpMethod method = HttpMethod.resolve(request.getMethod());
		if (!authorizationRoute.getRequestMethods().contains(method)) {
			return false;
		}

		// Check if HTTP user role is authorized
		final List<String> userRoles = SecurityContextHolder.getContext()
			.getAuthentication()
			.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());

		final boolean isAuthorizedRole = authorizationRoute.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(userRoles::contains);

		return isAuthorizedRole;
	}

	private void initAuthorizationRoutes() {
		this.authorizationRoutes = new HashMap<>();

		final String route1 = "/client-management/users";
		authorizationRoutes.put(
			route1,
			AuthorizationRoute.builder()
				.route(route1)
				.requestMethods(Arrays.asList(HttpMethod.POST))
				.authorities(Arrays.asList(new SimpleGrantedAuthority(Role.ADVISOR)))
				.build()
		);

		// TODO: Add more authorization routes below
	}
}
