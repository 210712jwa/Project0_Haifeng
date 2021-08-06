package service;

import java.sql.SQLException;
import java.util.List;

import dao.AccountDAO;
import exception.DatabaseException;
import model.Account;
import dao.AccountDAOImp;
import dto.AddOrEditAccountDTO;

public class AccountService {
	private AccountDAO accountDao;
	public AccountService() {
		this.accountDao = new AccountDAOImp();
	}
	
	public Account addAccount(AddOrEditAccountDTO account) throws DatabaseException {
		try {
			Account newAccount = accountDao.addAccount(account);
			return newAccount;
		}catch(SQLException e) {
			throw new DatabaseException();
		}
		
	}
	
	public List<Account> getAllAccount(String clientid) throws DatabaseException {
		List<Account> accounts;
		try {
			int id = Integer.parseInt(clientid);
			accounts = accountDao.getAllAccount(id);
			
		}catch(SQLException e) {
			throw new DatabaseException();
		}
		return accounts;
	}

}
