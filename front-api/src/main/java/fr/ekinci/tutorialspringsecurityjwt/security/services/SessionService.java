package fr.ekinci.tutorialspringsecurityjwt.security.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ekinci.tutorialspringsecurityjwt.security.exceptions.SessionException;
import fr.ekinci.tutorialspringsecurityjwt.security.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Gokan EKINCI
 */
@Service
public class SessionService implements ISessionService {
	private final static String LOG_HEADER = "[SESSION][SERVICE]";
	private final ObjectMapper mapper;

	@Autowired
	public SessionService(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Session getSession() {
		final String subject = (String) SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();

		return convertToSession(subject);
	}

	@Override
	public Collection<? extends GrantedAuthority> extractSubjectRoles(String subject) {
		return convertToSession(subject)
			.getRoles()
			.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

	private Session convertToSession(String subject) {
		try {
			return mapper.readValue(
				subject,
				Session.class
			);
		} catch (IOException e) {
			throw new SessionException(
				String.format("%s An error happened when reading subject", LOG_HEADER),
				e
			);
		}
	}
}
