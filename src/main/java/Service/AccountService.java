package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO dao;

    public AccountService() {
        this.dao = new AccountDAO();
    }

    public AccountService(Account account) {
        this.dao = new AccountDAO();
    }

    // Account must have username & password set and must return the persisted Account
    // with generated account_id, or null if username already exists.
    public Account register(Account account) {

        if(dao.getAccountByUsername(account.getUsername()) != null) {
            return null;
        } else {
            return dao.createAccount(account);
        }
    } 
    
    // Login using username and password, return Account if found.
    public Account login(String username, String password) {
        return dao.getAccountByUsernameAndPassword(username, password);
    }

    

}

