package fr.ekinci.clientmanagement.user.controllers;

import fr.ekinci.client.models.account.AccountDto;
import fr.ekinci.restclient.IClientManagementService;
import fr.ekinci.restserver.exceptions.ExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gokan EKINCI
 */
@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

	private final IClientManagementService clientManagementService;

	@Autowired
	public AccountController(IClientManagementService clientManagementService) {
		this.clientManagementService = clientManagementService;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto dto) {
		return new ResponseEntity<>(clientManagementService.addAccount(dto), HttpStatus.OK);
	}

}
