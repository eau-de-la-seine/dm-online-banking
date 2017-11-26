package ekinci.clienttransaction.user.controllers;

import fr.ekinci.client.models.usertransaction.TransactionDto;
import fr.ekinci.restclient.IClientTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Gokan EKINCI
 */
@RestController
@RequestMapping(path = "/users")
public class UserTransactionController {

	private final IClientTransactionService clientTransactionService;

	@Autowired
	public UserTransactionController(IClientTransactionService clientTransactionService) {
		this.clientTransactionService = clientTransactionService;
	}

	/**
	 * For client-service
	 */
	@RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<Page<TransactionDto>> getTransactions() {
		return new ResponseEntity<>(clientTransactionService.getTransactions(), HttpStatus.OK);
	}

	/**
	 * For client-service
	 */
	@RequestMapping(path = "/credit/users/{id}", method = RequestMethod.POST)
	public ResponseEntity<TransactionDto> credit(@RequestBody TransactionDto transactionDto) {
		return new ResponseEntity<>(clientTransactionService.credit(transactionDto), HttpStatus.OK);
	}

	/**
	 * For client-service
	 */
	@RequestMapping(path = "/debit/users/{id}", method = RequestMethod.POST)
	public ResponseEntity<TransactionDto> debit(@RequestBody TransactionDto transactionDto) {
		return new ResponseEntity<>(clientTransactionService.debit(transactionDto), HttpStatus.OK);
	}
}
