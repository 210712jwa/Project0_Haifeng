package service;

import java.sql.SQLException;
import java.util.List;

import dao.AccountDAO;
import dao.AccountDAOImp;
import dao.ClientDAO;
import dao.ClientDAOImp;
import dto.AddOrEditClientDTO;
import exception.BadParameterException;
import exception.DatabaseException;
import model.Client;


public class ClientService {

	private ClientDAO clientDao;
	private AccountDAO accountDao;
	

	public ClientService() {
		this.clientDao = new ClientDAOImp();
		this.accountDao = new AccountDAOImp();
	}
	
	public ClientService(ClientDAO mockedDaoObject) {
		this.clientDao = mockedDaoObject;
	}
	

	public List<Client> getAllClients() throws DatabaseException {
		List<Client> clients;
		try {
			clients = clientDao.getAllClients();
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} 
		
		return clients;
	}
	
	public Client getClientById(String clientid) throws DatabaseException, BadParameterException {
		Client client;
		try {
			int id = Integer.parseInt(clientid);
			client = clientDao.getClientByid(id);
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + "was pass in, but it is not a integer");
		}
		
		return client;
	}
	
	public Client addClient(AddOrEditClientDTO client) throws DatabaseException {
		try {
			Client newClient = clientDao.addClient(client);
			return newClient;
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} 
		
		
	}
	
	public Client editClient(String clientid, AddOrEditClientDTO client) throws DatabaseException, BadParameterException {
		try {
			int id = Integer.parseInt(clientid);
			Client editedClient = clientDao.editClientByid(id, client);
			return editedClient;
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + "was pass in, but it is not a integer");
		}
	}
	
	public void deleteClient(String clientid) throws DatabaseException, BadParameterException {
		try {
			int id = Integer.parseInt(clientid);
			if(accountDao.getAllAccount(id).size() != 0) {
				throw new DatabaseException("Cannot delete client before delete accounts belong to the client");
			}
			clientDao.deleteClientByid(id);
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + "was pass in, but it is not a integer");
		}
	}
}
