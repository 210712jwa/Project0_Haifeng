package dao;

import java.sql.SQLException;
import java.util.List;

import dto.AddOrEditAccountDTO;

import model.Account;

public interface AccountDAO {
	
	public abstract Account addAccount(AddOrEditAccountDTO account) throws SQLException;
	
	public abstract List<Account> getAllAccount(int clientid) throws SQLException;
	
	public abstract Account getAccountByBalance(int clientid, double min, double max) throws SQLException;
	
	public abstract Account getAccountByids(int clientid, int accountid) throws SQLException;
	
	public abstract Account editAccountByids(int clientid, int accountid) throws SQLException;
	
	public abstract void deleteAccountByids(AddOrEditAccountDTO account) throws SQLException; 
	
	
}
