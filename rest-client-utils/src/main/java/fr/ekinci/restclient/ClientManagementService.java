package fr.ekinci.restclient;

import fr.ekinci.client.models.account.AccountDto;
import fr.ekinci.client.models.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author Gokan EKINCI
 */
@Service
public class ClientManagementService implements IClientManagementService {

	private final String URL;
	private final RestTemplate restTemplate;

	@Autowired
	public ClientManagementService(
		@Value("${client.management.services.url}") String url,
		RestTemplate restTemplate
	) {
		this.URL = url;
		this.restTemplate = restTemplate;
	}

	@Override
	public UserDto create(UserDto user) {
		return restTemplate.exchange(
			URL + "/users",
			HttpMethod.POST,
			new HttpEntity<>(user),
			UserDto.class)
			.getBody();
	}

	@Override
	public void updateInfo(UserDto user) {
		final UserDto u = UserDto.builder()
			.id(user.getId())
			.address(user.getAddress())
			.homePhone(user.getHomePhone())
			.cellPhone(user.getCellPhone())
			.build();

		restTemplate.exchange(
			URL + "/users/{id}",
			HttpMethod.PATCH,
			new HttpEntity<>(u),
			UserDto.class,
			u.getId()
		);
	}

	@Override
	public UserDto getUserById(String userId) {
		return restTemplate.exchange(
			URL + "/users/{id}",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			UserDto.class,
			userId
		)
		.getBody();
	}

	@Override
	public List<UserDto> getUsersByName(String name) {
		final List<UserDto> result = restTemplate.exchange(
			URL + "/users/research?name={name}",
			HttpMethod.GET,
			HttpEntity.EMPTY,
			new ParameterizedTypeReference<List<UserDto>>(){},
			name
		)
		.getBody();

		return (result == null) ? Collections.emptyList() : result;
	}

	@Override
	public AccountDto addAccount(AccountDto account) {
		return restTemplate.exchange(
			URL + "/accounts",
			HttpMethod.POST,
			new HttpEntity<>(account),
			AccountDto.class
		)
		.getBody();
	}
}
