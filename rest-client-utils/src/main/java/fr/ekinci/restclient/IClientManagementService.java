package fr.ekinci.restclient;

import fr.ekinci.client.models.account.AccountDto;
import fr.ekinci.client.models.user.UserDto;

import java.util.List;

/**
 * @author Gokan EKINCI
 */
public interface IClientManagementService {

	/**
	 * Create a new user (costumer) in the database
	 * @param user
	 * @return
	 */
	UserDto create(UserDto user);

	/**
	 * Update some information for given user id
	 *
	 * @param user
	 */
	void updateInfo(UserDto user);

	/**
	 * Get user by id
	 *
	 * @param userId
	 * @return
	 */
	UserDto getUserById(String userId);

	/**
	 * Get users by name
	 *
	 * @param name
	 * @return
	 */
	List<UserDto> getUsersByName(String name);

	/**
	 * Create a new account for user
	 *
	 * @param account
	 * @return
	 */
	AccountDto addAccount(AccountDto account);
}
