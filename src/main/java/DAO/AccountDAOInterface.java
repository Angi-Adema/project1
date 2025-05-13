package DAO;

import Model.Account;

// Provide for decoupling, scalability and readability by using an interface.
public interface AccountDAOInterface {

    // Create and insert a new account through registration.
    Account createAccount(Account account);

    // Lookup usernames to be sure that username is not already in use.
    Account getAccountByUsername(String username);

    // Validate login credentials.
    Account getAccountByUsernameAndPassword(String username, String password);
    
}
