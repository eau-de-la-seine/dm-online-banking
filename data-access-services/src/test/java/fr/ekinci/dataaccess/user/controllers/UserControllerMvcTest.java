package fr.ekinci.dataaccess.user.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ekinci.App;
import fr.ekinci.client.models.user.UserDto;
import fr.ekinci.dataaccess.configurations.dozer.DozerConfiguration;
import fr.ekinci.dataaccess.user.entities.UserEntity;
import fr.ekinci.dataaccess.user.repositories.UserRepository;
import fr.ekinci.dataaccess.user.services.UserDAO;
import fr.ekinci.restserver.configurations.JacksonConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Gokan EKINCI
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {
	UserControllerMvcTest.Context.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // => For Spring Data
public class UserControllerMvcTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void before() {
		// Inserting data to database
		final UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("Billie");
		userEntity.setLastName("Jean");
		userEntity.setAddress("mock_initialValue");
		userRepository.save(userEntity);
	}

	@Test
	public void test_patch() throws Exception {
		final String route = "/users/{id}";
		final String id = "1";
		final String sentBody = patch_sentBody_ok();

		// WHEN
		this.mvc.perform(
				patch(route, id)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(sentBody)
		)
		// THEN
			.andExpect(status().isOk());

		final UserEntity reponse = userRepository.findOne(Long.parseLong(id));
		Assert.assertEquals(1L, (long) reponse.getId());
		Assert.assertEquals("mock_afterPatchMethodExecutedValue", reponse.getAddress());
	}

	@Test
	public void test_findByName() throws Exception {
		final String route = "/users/research";
		final String expected = findByName_responseBody_expected();
		// WHEN
		this.mvc.perform(
			get(route)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.param("name", "bill")
		)
		// THEN
			.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

	private String patch_sentBody_ok() throws JsonProcessingException {
		final UserDto request = new UserDto();
		request.setAddress("mock_afterPatchMethodExecutedValue");
		return mapper.writeValueAsString(request);
	}

	private String findByName_responseBody_expected() throws JsonProcessingException {
		final List<UserDto> response = new ArrayList<>();
		final UserDto userDto = new UserDto();
		userDto.setId("1");
		userDto.setFirstName("Billie");
		userDto.setLastName("Jean");
		response.add(userDto);
		return mapper.writeValueAsString(response);
	}

	@Import({
		App.class,
		UserDAO.class,
		JacksonConfiguration.class,
		DozerConfiguration.class
	})
	@DataJpaTest // => For Spring Data
	public static class Context {


	}
}
