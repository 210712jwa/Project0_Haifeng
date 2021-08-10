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
			
			//make sure splitter[1] will not return null when balance doesn't have a decimal
			if(!balanceString.contains(".")) {
				balanceString = balanceString.concat(".00");
				double temp = Double.parseDouble(balanceString);
				account.setAccountBalance(temp);
			}
			
			//check if balance is positive
			if(accountBalance < 0) {
				throw new BadParameterException("cannot have negative balance");
			}
			
			//check number of decimal place for balance
			accountBalance = account.getAccountBalance();
			String[] splitter = Double.toString(accountBalance).split("\\.");
			int decimalLength = splitter[1].length();
			if(decimalLength > 2) {
				throw new BadDecimalException("cannot have more than two decimal place for balance");		
			}
			String type = account.getAccountType();
			
			//make sure account type is either checking or saving
			if(!(type.equalsIgnoreCase("checking") ^ type.equalsIgnoreCase("saving"))) {
				throw new BadAccountTypeException("Not a vaild Account Type");
			}
			int cid = Integer.parseInt(clientid);
			
			//check client id is positive
			if(cid < 0) {
				throw new BadParameterException("Client id can not be negative");
			}
			
			//make sure not request for a client not exist
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			account.setClientid(cid);
			newAccount = accountDao.addAccount(account);
			return newAccount;
		} catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + "is not an acceptable input");
		}
		
	}
	
	
	public List<Account> getAllAccount(String clientid) throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		List<Account> accounts;
		try {
			int cid = Integer.parseInt(clientid);
			
			//check client id is positive
			if(cid < 0) {
				throw new BadParameterException("Client id can not be negative");
			}
			
			//make sure requesting a existing client 
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			
			// if no account returned print out Client don't have any account
			accounts = accountDao.getAllAccount(cid);
			return accounts;
		}catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		}catch(NumberFormatException e) {
			throw new BadParameterException(clientid + " is not an acceptable input");
		}
		
	}
	
	//get accounts balance that is greater than and less than
	public List<Account> getAccountByBalance(String clientid, String min, String max) throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		List<Account> accounts;
		try {
			int cid = Integer.parseInt(clientid);
			
			//test client id is positive
			if(cid < 0) {
				throw new BadParameterException("Client id can not be negative");
			}
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			
			//make sure splitter[1] will not return null and throw off the application
			if(!min.contains(".")) {
				min = min.concat(".00");
			}
			if(!max.contains(".")) {
				max = max.concat(".00");
			}
			
			//make sure balance will at most have two decimal place
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
				throw new BadDecimalException("cannot have more than two decimal place for balance");		
			}
			double minBalance = Double.parseDouble(min);
			double maxBalance = Double.parseDouble(max);
			
			//check if balance is positive
			if(minBalance < 0 || maxBalance < 0) {
				throw new BadParameterException("cannot have negative for balance");
			}
			if(minBalance > maxBalance) {
				throw new BadParameterException("AmountLessThan cannot greater than amountGreaterThan");
			}
			accounts = accountDao.getAccountByBalance(cid, minBalance, maxBalance);
			
			//when no SQLexception get thrown then there is no account with match balance
			if(accounts.size() == 0) {
				throw new AccountNotFoundException("Client don't have account with balance between " + min + " and "+ max);
			}
			return accounts;
		} catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		}catch(NumberFormatException e) {
			throw new BadParameterException("Not an acceptable request input");
		}
	}
	
	public Account getAccountByid(String clientid, String accountid) throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		
		try {
			int cid = Integer.parseInt(clientid);
			//check client id positive 
			if(cid < 0) {
				throw new BadParameterException("Client id can not be negative");
			}
			int aid = Integer.parseInt(accountid);
			//check account id positive
			if(aid < 0) {
				throw new BadParameterException("Account id can not be negative");
			}
			//check client exist before getting account for client
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			Account account = accountDao.getAccountByids(cid, aid);
			//if not SQLexception get thrown then there is no account for this client
			if(account == null) {
				throw new AccountNotFoundException("client " + clientid + " don't have account with id: " + accountid);
			}
			return account;
		}catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		}catch(NumberFormatException e) {
			throw new BadParameterException("Input for client id or account id s not an valid");
		}
	}
	
	public Account editAccount(String clientid, String accountid, Account account) throws DatabaseException, BadDecimalException, BadParameterException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException {
		Account newaccount;
		try {
				double accountBalance = account.getAccountBalance();
				String balanceString = Double.toString(accountBalance);
				//make sure splitter[1] will not return null and format balance
				if(!balanceString.contains(".")) {
					balanceString = balanceString.concat(".00");
					double temp = Double.parseDouble(balanceString);
					account.setAccountBalance(temp);
				}
				//check if input balance is positive
				if(accountBalance < 0) {
					throw new BadParameterException("cannot have negative balance");
				}
				
				//check balance is at most two decimal place
				accountBalance = account.getAccountBalance();
				String[] splitter = Double.toString(accountBalance).split("\\.");
				int decimalLength = splitter[1].length();
				if(decimalLength > 2) {
					throw new BadDecimalException("cannot have more than two decimal place for balance");		
				}
				String type = account.getAccountType();
				if(!(type.equalsIgnoreCase("checking") ^ type.equalsIgnoreCase("saving"))) {
					throw new BadAccountTypeException("Not a vaild Account Type");
				}
				int cid = Integer.parseInt(clientid);
				if(cid < 0) {
					throw new BadParameterException("Client id can not be negative");
				}
				int aid = Integer.parseInt(accountid);
				if(aid < 0) {
					throw new BadParameterException("Account id can not be negative");
				}
				
				//check if client exist before edit account
				if(clientDao.getClientByid(cid) == null) {
					throw new ClientNotFoundException("Client not exist");
				}
				account.setClientid(cid);
				account.setId(aid);
				newaccount = accountDao.editAccountByids(account);
				return newaccount;
			
		}catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		}catch(NumberFormatException e) {
			throw new BadParameterException("Not a vaild input for client id or account id");
		}
	}
	
	public void deleteAccount(String clientid, String accountid) throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			int cid = Integer.parseInt(clientid);
			//check client id positive
			if(cid < 0) {
				throw new BadParameterException("Client id can not be negative");
			}
			int aid = Integer.parseInt(accountid);
			
			//check account id positive
			if(aid < 0) {
				throw new BadParameterException("Account id can not be negative");
			}
			
			//check client exist before delete
			if(clientDao.getClientByid(cid) == null) {
				throw new ClientNotFoundException("Client not exist");
			}
			//check account exist before delete
			if(accountDao.getAccountByids(cid, aid) == null) {
				throw new AccountNotFoundException("Account not exist");
			}
			accountDao.deleteAccountByids(cid, aid);
		}catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		}catch(NumberFormatException e) {
			throw new BadParameterException("Not a vaild input for client id or account id");
		}
	}
	
	public void deleteAllAccount(String clientid) throws SQLException, ClientNotFoundException, DatabaseException, BadParameterException {
	try {
		int cid = Integer.parseInt(clientid);
		//check client id positive
		if(cid < 0) {
			throw new BadParameterException("Client id can not be negative");
		}
		
		//check client exist before delete
		if(clientDao.getClientByid(cid) == null) {
			throw new ClientNotFoundException("Client not exist");
		}
	
		//check if client have any account before delete
		if(accountDao.getAllAccount(cid).size() != 0) {
			accountDao.deleteAllAccount(cid);
		}
		
	}catch(SQLException e) {
		throw new DatabaseException("Something went wrong with our DAO operations");
	}catch(NumberFormatException e) {
		throw new BadParameterException("Not a vaild input for client id or account id");
	}

}
}
