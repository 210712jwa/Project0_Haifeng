package service;

import java.sql.SQLException;
import java.util.List;

import dao.AccountDAO;
import exception.AccountNotFoundException;
import exception.BadAccountTypeException;
import exception.BadDecimalException;
import exception.BadParameterException;
import exception.ClientNotFoundException;
import exception.DatabaseException;
import model.Account;
import dao.AccountDAOImp;
import dao.ClientDAO;
import dao.ClientDAOImp;
import dto.AddAccountDTO;

public class AccountService {
	private AccountDAO accountDao;
	private ClientDAO clientDao;
	
	public AccountService() {
		this.accountDao = new AccountDAOImp();
		this.clientDao = new ClientDAOImp();
		
	}
	
	public AccountService(AccountDAO mockAccountObject, ClientDAO mockClientObject) {
		this.accountDao = mockAccountObject;
		this.clientDao = mockClientObject;

	}
	
	public Account addAccount(String clientid, AddAccountDTO account) throws DatabaseException, BadDecimalException, BadParameterException, BadAccountTypeException, ClientNotFoundException {
		Account newAccount;
		try {
			double accountBalance = account.getAccountBalance();
			String balanceString = Double.toString(accountBalance);
			if(!balanceString.contains(".")) {
				balanceString = balanceString.concat(".00");
				double temp = Double.parseDouble(balanceString);
				account.setAccountBalance(temp);
			}
			accountBalance = account.getAccountBalance();
			String[] splitter = Double.toString(accountBalance).split("\\.");
			int decimalLength = splitter[1].length();
			if(decimalLength > 2) {
				throw new BadDecimalException("cannot have more than two decimal for balance");		
			}
			String type = account.getAccountType();
			if(!(type.equalsIgnoreCase("checking") ^ type.equalsIgnoreCase("saving"))) {
				throw new BadAccountTypeException("Not a vaild Account Type");
			}
			int cid = Integer.parseInt(clientid);
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			account.setClientid(cid);
			newAccount = accountDao.addAccount(account);
			return newAccount;
		} catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + "is not an acceptable input");
		}
		
	}
	
	public List<Account> getAllAccount(String clientid) throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		List<Account> accounts;
		try {
			int cid = Integer.parseInt(clientid);
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			accounts = accountDao.getAllAccount(cid);
			if(accounts.size() == 0) {
				throw new AccountNotFoundException("Client don't have any account");
			}
			return accounts;
		}catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		}catch(NumberFormatException e) {
			throw new BadParameterException(clientid + " is not an acceptable input");
		}
		
	}
	
	public List<Account> getAccountByBalance(String clientid, String min, String max) throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		List<Account> accounts;
		try {
			if(min == "" || max == "") {
				throw new BadParameterException("cannot have null value for balance");
			}
			int cid = Integer.parseInt(clientid);
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			if(!min.contains(".")) {
				min = min.concat(".00");
			}
			if(!max.contains(".")) {
				max = max.concat(".00");
			}
			String[] minSplitter = min.split("\\.");
			if(minSplitter.length > 2) {
				throw new BadDecimalException("Can not have more than one decimal for amount greater than balance");
			}
			int minDecimalLength = minSplitter[1].length();
			String[] maxSplitter = max.split("\\.");
			if(maxSplitter.length > 2) {
				throw new BadDecimalException("Can not have more than one decimal for amount less than balance");
			}
			int maxDecimalLength = maxSplitter[1].length();
			if(minDecimalLength > 2 || maxDecimalLength > 2) {
				throw new BadDecimalException("cannot have more than two decimal for balance");		
			}
			double minBalance = Double.parseDouble(min);
			double maxBalance = Double.parseDouble(max);
			accounts = accountDao.getAccountByBalance(cid, minBalance, maxBalance);
			if(accounts.size() == 0) {
				throw new AccountNotFoundException("Client don't have account with balance between " + min + " and "+ max);
			}
			return accounts;
		} catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		}catch(NumberFormatException e) {
			throw new BadParameterException("Not an acceptable request input");
		}
	}
	
	public Account getAccountByid(String clientid, String accountid) throws DatabaseException, BadParameterException, ClientNotFoundException {
		Account account;
		try {
			int cid = Integer.parseInt(clientid);
			int aid = Integer.parseInt(accountid);
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			account = accountDao.getAccountByids(cid, aid);
			return account;
		}catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		}catch(NumberFormatException e) {
			throw new BadParameterException("Input for client id or account id s not an valid");
		}
	}
	
	public Account editAccount(String clientid, String accountid, Account account) throws DatabaseException, BadDecimalException, BadParameterException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException {
		Account newaccount;
		try {
				double accountBalance = account.getAccountBalance();
				String balanceString = Double.toString(accountBalance);
				if(!balanceString.contains(".")) {
					balanceString = balanceString.concat(".00");
					double temp = Double.parseDouble(balanceString);
					account.setAccountBalance(temp);
				}
				accountBalance = account.getAccountBalance();
				String[] splitter = Double.toString(accountBalance).split("\\.");
				int decimalLength = splitter[1].length();
				if(decimalLength > 2) {
					throw new BadDecimalException("cannot have more than two decimal for balance");		
				}
				String type = account.getAccountType();
				if(!(type.equalsIgnoreCase("checking") ^ type.equalsIgnoreCase("saving"))) {
					throw new BadAccountTypeException("Not a vaild Account Type");
				}
				int cid = Integer.parseInt(clientid);
				int aid = Integer.parseInt(accountid);
				if(clientDao.getClientByid(cid) == null) {
					throw new ClientNotFoundException("Client not exist");
				}
				account.setClientid(cid);
				account.setId(aid);
				newaccount = accountDao.editAccountByids(account);
				return newaccount;
			
		}catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		}catch(NumberFormatException e) {
			throw new BadParameterException("Not a vaild input for client id or account id");
		}
	}
	
	public void deleteAccount(String clientid, String accountid) throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			int cid = Integer.parseInt(clientid);
			int aid = Integer.parseInt(accountid);
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			if(accountDao.getAccountByids(cid, aid) == null) {
				throw new AccountNotFoundException("Account not exist");
			}
			accountDao.deleteAccountByids(cid, aid);
		}catch(SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		}catch(NumberFormatException e) {
			throw new BadParameterException("Not a vaild input for client id or account id");
		}
	}
	
	public void deleteAllAccount(String clientid) throws SQLException, ClientNotFoundException, DatabaseException, BadParameterException {
	try {
		int cid = Integer.parseInt(clientid);
		if(clientDao.getClientByid(cid) == null) {
			throw new ClientNotFoundException("Client not exist");
		}
		accountDao.deleteAllAccount(cid);
	}catch(SQLException e) {
		throw new DatabaseException("Something went wrong with our DAO operations");
	}catch(NumberFormatException e) {
		throw new BadParameterException("Not a vaild input for client id or account id");
	}

}
}
