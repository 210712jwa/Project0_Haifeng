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
import model.Client;
import utility.ConnectionUtility;

public class AccountDAOImp implements AccountDAO {

	@Override
	public Account addAccount(int clientid, double accountBalance) throws SQLException {
		try (Connection con = ConnectionUtility.getConnection()) {
			String sql = "INSERT INTO project0.account (account_balance, client_id) VALUES (?, ?)";

			PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, accountBalance);
			pstmt.setInt(2, clientid);
			int recordUpdate = pstmt.executeUpdate();
			if (recordUpdate != 1) {
				throw new SQLException();
			}
			ResultSet generatedKey = pstmt.getGeneratedKeys();
			if (generatedKey.next()) {
				Account newAccount = new Account(generatedKey.getInt(1), accountBalance, clientid);
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
				int cid = rs.getInt("client_id");
				Account account = new Account(id, accountBalance, cid);
				accounts.add(account);
			}

			return accounts;
		}
	}

	@Override
	public Account getAccountByBalance(int clientid, double min, double max) throws SQLException {
		// TODO Auto-generated method stub
		return null;
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
				int cid = rs.getInt("client_id");
				return new Account(aid, accountBalance, cid);
			}else {
				return null;
			}
		}
		
	}

	@Override
	public Account editAccountByids(int clientid, int accountid, double accountBalance) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()){
			String sql = "UPDATE project0.account SET account_balance=? WHERE account_id=? AND client_id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setDouble(1, accountBalance);
			pstmt.setInt(2, accountid);
			pstmt.setInt(3, clientid);
			int recordUpdate = pstmt.executeUpdate();
			if(recordUpdate != 1) {
				throw new SQLException();
			}
			return new Account(accountid, accountBalance, clientid);
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
				throw new SQLException();
			}
		}

	}

}