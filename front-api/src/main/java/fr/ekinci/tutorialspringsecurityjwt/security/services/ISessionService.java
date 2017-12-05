package fr.ekinci.tutorialspringsecurityjwt.security.services;

import fr.ekinci.tutorialspringsecurityjwt.security.models.Session;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

/**
 * @author Gokan EKINCI
 */
public interface ISessionService {

	/**
	 * Return user's session
	 *
	 * @return		User's session. It's never null.
	 */
	Session getSession();

	/**
	 * Extract roles from given JSON string subject
	 *
	 * @param subject
	 *
	 * @return Subject's roles
	 */
	Collection<? extends GrantedAuthority> extractSubjectRoles(String subject);
}
