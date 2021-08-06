package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.AddOrEditClientDTO;
import model.Client;
import utility.ConnectionUtility;



public class ClientDAOImp implements ClientDAO {


	@Override
	public List<Client> getAllClients() throws SQLException {
		List<Client> clients = new ArrayList<>();
		try (Connection con = ConnectionUtility.getConnection()) {
		Statement stmts = con.createStatement();
		
		String sql = "SELECT * FROM project0.client";
		ResultSet rs = stmts.executeQuery(sql);
	
		while (rs.next()) {
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			
			Client client = new Client(id, firstName, lastName);
			
			clients.add(client);
		}
	}
	
	return clients;
	}

	@Override
	public Client getClientByid(int clientid) throws SQLException {
		try (Connection con = ConnectionUtility.getConnection()) {
			String sql = "SELECT * FROM project0.client where id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientid);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int client_id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				Client client = new Client(client_id, firstName, lastName);
				return client;
			}else {
				return null;
			}
			
		}
		
	}

	@Override
	public Client addClient(AddOrEditClientDTO client) throws SQLException {
		try (Connection con = ConnectionUtility.getConnection()){
			String sql = "INSERT INTO project0.client (first_name, last_name) VALUES (?, ?)";
			PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, client.getFirstName());
			pstmt.setString(2, client.getLastName());
			int recordUpdate = pstmt.executeUpdate();
			if(recordUpdate != 1) {
				throw new SQLException();
			}
			ResultSet generatedKey = pstmt.getGeneratedKeys();
			if(generatedKey.next()) {
				Client newClient = new Client(generatedKey.getInt(1), client.getFirstName(), client.getLastName());
				return newClient;
			}
			
		}
		return null;
	}

	@Override
	public Client editClientByid(int clientid, AddOrEditClientDTO client) throws SQLException {
		try (Connection con = ConnectionUtility.getConnection()){
			String sql = "UPDATE project0.client SET first_name=?, last_name=? WHERE id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, client.getFirstName());
			pstmt.setString(2, client.getLastName());
			pstmt.setInt(3, clientid);
			int recordUpdate = pstmt.executeUpdate();
			if(recordUpdate != 1) {
				
			}
				Client newClient = new Client(clientid, client.getFirstName(), client.getLastName());
				return newClient;
		
			
		}
	}

	@Override
	public void deleteClientByid(int client_id) throws SQLException {
		try(Connection con = ConnectionUtility.getConnection()){
			String sql = "DELETE FROM project0.client WHERE id=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, client_id);
			int recordUpdate = pstmt.executeUpdate();
			if(recordUpdate != 1) {
				
			}
		}
		
	}
	
	


}
