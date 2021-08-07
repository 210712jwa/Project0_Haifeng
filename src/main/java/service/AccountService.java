package service;

import java.sql.SQLException;
import java.util.List;

import dao.AccountDAO;
import exception.BadDecimalException;
import exception.BadParameterException;
import exception.DatabaseException;
import model.Account;
import dao.AccountDAOImp;
import dto.AddAccountDTO;

public class AccountService {
	private AccountDAO accountDao;
	public AccountService() {
		this.accountDao = new AccountDAOImp();
	}
	
	public Account addAccount(String clientid, String accountBalance) throws DatabaseException, BadDecimalException, BadParameterException {
		Account newAccount;
		try {
			String[] splitter = accountBalance.split("\\.");
			int decimalLength = splitter[1].length();
			if(decimalLength > 2) {
				throw new BadDecimalException("cannot have more than two decimal for balance");		
			}
			int id = Integer.parseInt(clientid);
			double balance = Double.parseDouble(accountBalance);
			newAccount = accountDao.addAccount(id, balance);
			return newAccount;
		} catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + " or " + accountBalance + " is not an acceptable input");
		}
		
	}
	
	public List<Account> getAllAccount(String clientid) throws DatabaseException, BadParameterException {
		List<Account> accounts;
		try {
			int id = Integer.parseInt(clientid);
			accounts = accountDao.getAllAccount(id);
			return accounts;
		}catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		}catch(NumberFormatException e) {
			throw new BadParameterException(clientid + " was pass in, but it is not a integer");
		}
		
	}
	
	public Account editAccount(String clientid, String accountid, String accountBalance) throws DatabaseException, BadDecimalException, BadParameterException {
		Account account;
		try {
			String[] splitter = accountBalance.split("\\.");
			int decimalLength = splitter[1].length();
			if(decimalLength > 2) {
				throw new BadDecimalException("cannot have more than two decimal for balance");		
			}
			
				int id = Integer.parseInt(clientid);
				int aid = Integer.parseInt(accountid);
				double balance = Double.parseDouble(accountBalance);
				account = accountDao.editAccountByids(id, aid, balance);
				return account;
			
		}catch(SQLException e) {
			throw new DatabaseException();
		}catch(NumberFormatException e) {
			throw new BadParameterException("Not a vaild input for either client id, account id, or account balance");
		}
	}
	
	public void deleteAccount(String clientid, String accountid) throws DatabaseException, BadParameterException {
		try {
			int id = Integer.parseInt(clientid);
			int aid = Integer.parseInt(accountid);
			accountDao.deleteAccountByids(id, aid);
		}catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		}catch(NumberFormatException e) {
			throw new BadParameterException();
		}
	}

}
