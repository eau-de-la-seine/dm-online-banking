package fr.ekinci.tutorialspringsecurityjwt.security.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;
import fr.ekinci.tutorialspringsecurityjwt.security.services.IAuthorizationRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Gokan EKINCI
 */
@Slf4j
@Component
public class ZuulFilterImplementation extends ZuulFilter {

	private final IAuthorizationRouteService authorizationRouteService;

	@Autowired
	public ZuulFilterImplementation(IAuthorizationRouteService authorizationRouteService) {
		this.authorizationRouteService = authorizationRouteService;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	/**
	 * This method is executed after {@link AuthenticationFilter#doFilter(ServletRequest, ServletResponse, FilterChain)}
	 * Either this method return `true` or `false`, routing process is done. To avoid routing process, set {@link RequestContext#setSendZuulResponse(boolean)} to `false`.
	 * If this method `true` then {@link com.netflix.zuul.IZuulFilter#run()} method will be executed.
	 */
	@Override
	public boolean shouldFilter() {
		final boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

		final boolean isAuthorizedRoute = isAuthenticated && authorizationRouteService.isAuthorizedRoute();

		RequestContext.getCurrentContext()
			.setSendZuulResponse(
				isAuthorizedRoute
			);

		return isAuthorizedRoute;
	}

	@Override
	public Object run() {
		final RequestContext ctx = RequestContext.getCurrentContext();
		final HttpServletRequest request = ctx.getRequest();

		// Log called route
		log.info(String.format("[%s][%s][%s]",
			ZuulFilterImplementation.class.getSimpleName(),
			request.getMethod(),
			request.getRequestURL().toString()
		));

		// Return value is not used by Zuul yet
		return null;
	}
}
