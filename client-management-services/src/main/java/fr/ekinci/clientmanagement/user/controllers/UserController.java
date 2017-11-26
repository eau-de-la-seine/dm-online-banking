package fr.ekinci.clientmanagement.user.controllers;

import fr.ekinci.client.models.user.UserDto;
import fr.ekinci.restclient.IClientManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * @author Gokan EKINCI
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

	private final IClientManagementService clientManagementService;

	@Autowired
	public UserController(IClientManagementService clientManagementService) {
		this.clientManagementService = clientManagementService;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserDto> get(@PathVariable("id") String id) {
		final Optional<UserDto> dtoOpt = Optional.of(clientManagementService.getUserById(id));
		return (dtoOpt.isPresent()) ?
			new ResponseEntity<>(dtoOpt.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(path = "/research", method = RequestMethod.GET)
	public ResponseEntity<List<UserDto>> findByName(@RequestParam("name") String name) {
		final List<UserDto> dtoOpt = clientManagementService.getUsersByName(name);
		return (dtoOpt.isEmpty()) ?
			new ResponseEntity<>(HttpStatus.NO_CONTENT) :
			new ResponseEntity<>(dtoOpt, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<UserDto> create(@RequestBody UserDto user) {
		return new ResponseEntity<>(clientManagementService.create(user), HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<?> update(@PathVariable("id") String userId, @RequestBody UserDto user) {
		user.setId(userId);
		clientManagementService.updateInfo(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
