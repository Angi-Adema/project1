package DAO;

import Model.Account;

public interface AccountDAOInterface {
    Account createAccount(Account account);
    Account getAccountByUsername(String username);
    Account getAccountByUsernameAndPassword(String username, String password);
    
}
