package DAO;

import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;

public class AccountDAO implements AccountDAOInterface {
    
    @Override
    public Account createAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        Account createdAccount = null;

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            int rows = preparedStatement.executeUpdate();

            if(rows > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if(rs.next()) {
                    int id = rs.getInt(1);
                    createdAccount = new Account(id, account.getUsername(), account.getPassword());
                }
                rs.close();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
    }
        return createdAccount;
    }

    @Override
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;

        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public Account getAccountByUsernameAndPassword(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;

        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
            
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }
}
