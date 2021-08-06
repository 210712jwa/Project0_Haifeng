package dao;
import java.sql.SQLException;
import java.util.List;

import dto.AddOrEditClientDTO;
import model.Client;


public interface ClientDAO{
	
	public abstract List<Client> getAllClients() throws SQLException;
	
	public abstract Client getClientByid(int clientid) throws SQLException;
	
	public abstract Client addClient(AddOrEditClientDTO client) throws SQLException;
	
	public abstract Client editClientByid(int clientid, AddOrEditClientDTO client) throws SQLException;
	
	public abstract void deleteClientByid(int clientid) throws SQLException;
	
}
