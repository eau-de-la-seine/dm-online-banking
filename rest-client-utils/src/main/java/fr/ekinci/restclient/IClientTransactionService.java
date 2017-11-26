package fr.ekinci.restclient;

import fr.ekinci.client.models.usertransaction.TransactionDto;
import org.springframework.data.domain.Page;

/**
 * @author Gokan EKINCI
 */
public interface IClientTransactionService {


	/**
	 * Get history of transactions
	 *
	 * @return
	 */
	Page<TransactionDto> getTransactions();

	/**
	 * Credit money
	 *
	 * @param transaction
	 * @return
	 */
	TransactionDto credit(TransactionDto transaction);

	/**
	 * Debit money
	 *
	 * @param transaction
	 * @return
	 */
	TransactionDto debit(TransactionDto transaction);
}
