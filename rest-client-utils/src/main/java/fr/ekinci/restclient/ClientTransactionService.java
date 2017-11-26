package fr.ekinci.restclient;

import fr.ekinci.client.models.usertransaction.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Gokan EKINCI
 */
@Service
public class ClientTransactionService implements IClientTransactionService {

	private final String URL;
	private final RestTemplate restTemplate;

	@Autowired
	public ClientTransactionService(
		@Value("${client.transaction.services.url}") String url,
		RestTemplate restTemplate
	) {
		this.URL = url;
		this.restTemplate = restTemplate;
	}

	@Override
	public Page<TransactionDto> getTransactions() {
		return null;
	}

	@Override
	public TransactionDto credit(TransactionDto transaction) {
		return restTemplate.exchange(
			URL + "/transactions",
			HttpMethod.POST,
			new HttpEntity<>(transaction),
			TransactionDto.class)
			.getBody();
	}

	@Override
	public TransactionDto debit(TransactionDto transaction) {
		return restTemplate.exchange(
			URL + "/transactions",
			HttpMethod.POST,
			new HttpEntity<>(transaction),
			TransactionDto.class)
			.getBody();
	}
}
