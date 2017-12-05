package fr.ekinci.tutorialspringsecurityjwt.authentication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ekinci.Application;
import fr.ekinci.tutorialspringsecurityjwt.authentication.models.LoginRequest;
import fr.ekinci.tutorialspringsecurityjwt.authentication.models.LoginResponse;
import fr.ekinci.tutorialspringsecurityjwt.authentication.services.ILoginService;
import fr.ekinci.tutorialspringsecurityjwt.authentication.services.LoginServiceMock;
import fr.ekinci.tutorialspringsecurityjwt.commons.configurations.dozer.DozerConfiguration;
import fr.ekinci.tutorialspringsecurityjwt.commons.models.Profile;
import fr.ekinci.tutorialspringsecurityjwt.security.configurations.WebSecurityConfig;
import fr.ekinci.tutorialspringsecurityjwt.security.models.Role;
import fr.ekinci.tutorialspringsecurityjwt.security.services.IJwtService;
import fr.ekinci.tutorialspringsecurityjwt.security.services.ISessionService;
import fr.ekinci.tutorialspringsecurityjwt.security.services.JwtHmacService;
import fr.ekinci.tutorialspringsecurityjwt.security.services.SessionService;
import io.jsonwebtoken.SignatureAlgorithm;
import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Note: {@link TestPropertySource} does not work in {@link ContextConfiguration} classes
 *
 * @author Gokan EKINCI
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
@ActiveProfiles("jwtHmac")
@ContextConfiguration(classes = {
	AuthenticationControllerTest_jwtHmac.DependenciesConfig.class
})
@TestPropertySource(properties = {"jwt.hmac.secret.key=MTIzc2Z2MWU2djFlcnYxOThlcjF2NXYxOWU4YjFlNjViMTY1ZWYxYnY5OGU0ZmI"})
public class AuthenticationControllerTest_jwtHmac {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private ISessionService sessionService;

	@Test
	public void testLogin() throws Exception {
		final String route = "/authentication/login";
		final String sentBody = sentBody_ok();
		final String expectedResult = expectedResult_ok();

		this.mvc.perform(
			post(route)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(sentBody)
		)
			.andExpect(status().isOk())
			.andExpect(content().json(expectedResult));
	}

	private String sentBody_ok() throws JsonProcessingException {
		final LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("password");
		return mapper.writeValueAsString(request);
	}

	private String expectedResult_ok() throws JsonProcessingException {
		final String guid = "MOCK_GUID";
		final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJndWlkXCI6XCJNT0NLX0dVSURcIixcInJvbGVzXCI6W1wiUk9MRV9BRFZJU09SXCJdLFwicGFzc3dvcmRFeHBpcmVkXCI6ZmFsc2UsXCJwcm9maWxlXCI6e1wiaWRcIjpudWxsLFwiZmlyc3ROYW1lXCI6XCJKb2huXCIsXCJsYXN0TmFtZVwiOlwiTEVOTk9OXCIsXCJlbWFpbFwiOlwiam9obi5sZW5ub25AZ21haWwuY29tXCJ9fSJ9.dvCKc8Rl73HwwEn3ARjZXAIbSdZfSVdL47QIz84hr-hIge3u50_EsZHFOpZWii2KWwgLXOw8fU9G3xMdoutRCw";

		return mapper.writeValueAsString(
			LoginResponse.builder()
				.guid(guid)
				.passwordExpired(false)
				.roles(Arrays.asList(Role.ADVISOR))
				.profile(
					Profile.builder()
						.email("john.lennon@gmail.com")
						.firstName("John")
						.lastName("LENNON")
						.build())
				.token(token)
				.build()
		);
	}

	/**
	 * Tip for importing WebSecurityConfig class : https://github.com/spring-projects/spring-boot/issues/6514
	 */
	@Import({
		DozerConfiguration.class,
		SessionService.class,

		// Needed for setting WebSecurityConfig: https://stackoverflow.com/a/39427897
		WebSecurityConfig.class,
		Application.class,
	})
	public static class DependenciesConfig {

		@Bean
		public IJwtService jwtService(
			ISessionService sessionService,
			@Value("${jwt.hmac.algorithm:HS512}") SignatureAlgorithm algorithm,
			@Value("${jwt.duration:#{null}}") Long duration,
			@Value("${jwt.hmac.secret.key}") String secretKey
		) {
			return new JwtHmacService(sessionService, algorithm, duration, secretKey);
		}

		@Bean
		public ILoginService loginService(Mapper dozer, ObjectMapper mapper, IJwtService jwtService) {
			return new LoginServiceMock(null, dozer, mapper, jwtService);
		}
	}
}
