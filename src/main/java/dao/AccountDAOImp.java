package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.AddAccountDTO;

import model.Account;
import utility.ConnectionUtility;

public class AccountDAOImp implements AccountDAO {

	@Override
	public Account addAccount(AddAccountDTO account) throws SQLException {
		try (Connection con = ConnectionUtility.getConnection()) {
			String sql = "INSERT INTO project0.account (account_balance, account_type, client_id) VALUES (?, ?, ?)";

			PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, account.getAccountBalance());
			pstmt.setString(2, account.getAccountType().toUpperCase());
			pstmt.setInt(3, account.getClientid());
			int recordUpdate = pstmt.executeUpdate();
			if (recordUpdate != 1) {
				throw new SQLException("fail to update account");
			}
			ResultSet generatedKey = pstmt.getGeneratedKeys();
			if (generatedKey.next()) {
				Account newAccount = new Account(generatedKey.getInt(1), account.getAccountBalance(), account.getAccountType().toUpperCase(), account.getClientid());
				return newAccount;
			}

		}
		return null;

	}

	@Override
	public List<Account> getAllAccount(int clientid) throws SQLException {
		List<Account> accounts = new ArrayList<>();
		try (Connection con = ConnectionUtility.getConnection()) {
			

			String sql = "SELECT * FROM project0.account WHERE client_id=?";
			PreparedStatement pstmts = con.prepareStatement(sql);
			pstmts.setInt(1, clientid);
			ResultSet rs = pstmts.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("account_id");
				double accountBalance = rs.getDouble("account_balance");
				String accountType = rs.getString("account_type");
				int cid = rs.getInt("client_id");
				Account account = new Account(id, accountBalance, accountType, cid);
				accounts.add(account);
			}

			return accounts;
		}
	}

	@Override
	public List<Account> getAccountByBalance(int clientid, double min, double max) throws SQLException {
		List<Account> accounts = new ArrayList<>();
		try(Connection con = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM project0.account WHERE client_id=? AND account_balance BETWEEN ? AND ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientid);
			pstmt.setDouble(2, min);
			pstmt.setDouble(3, max);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("account_id");
				double accountBalance = rs.getDouble("account_balance");
				String accountType = rs.getString("account_type");
				int cid = rs.getInt("client_id");
				Account account = new Account(id, accountBalance, accountType, cid);
				accounts.add(account);
			}
		}
		return accounts;
	}

	@Override
	public Account getAccountByids(int clientid, int accountid) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM project0.account WHERE account_id=? AND client_id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, accountid);
			pstmt.setInt(2, clientid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int aid = rs.getInt("account_id");
				double accountBalance = rs.getDouble("account_balance");
				String accountType = rs.getString("account_type");
				int cid = rs.getInt("client_id");
				return new Account(aid, accountBalance, accountType, cid);
			}else {
				return null;
			}
		}
		
	}

	@Override
	public Account editAccountByids(Account account) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()){
			String sql = "UPDATE project0.account SET account_balance=?, account_type=? WHERE account_id=? AND client_id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setDouble(1, account.getAccountBalance());
			pstmt.setString(2, account.getAccountType().toUpperCase());
			pstmt.setInt(3, account.getId());
			pstmt.setInt(4, account.getClientid());
			int recordUpdate = pstmt.executeUpdate();
			if(recordUpdate != 1) {
				throw new SQLException("fail to edit account");
			}
			return new Account(account.getId(), account.getAccountBalance(), account.getAccountType().toUpperCase(), account.getClientid());
		}
	}

	@Override
	public void deleteAccountByids(int clientid, int accountid) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()) {
			String sql = "DELETE FROM project0.account WHERE account_id=? AND client_id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, accountid);
			pstmt.setInt(2, clientid);
			int recordUpdate = pstmt.executeUpdate();
			if(recordUpdate != 1) {
				throw new SQLException("fail to delete account");
			}
		}

	}
	
	@Override
	public void deleteAllAccount(int clientid) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()){
			String sql = "DELETE FROM account WHERE client_id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientid);
			int recordUpdate = pstmt.executeUpdate();
			if(!(recordUpdate >= 1)) {
				throw new SQLException("fail to delete all account associate with client " + clientid + " Client may not exist");
			}
		}
	}


	
	

}
