package service;

import java.sql.SQLException;
import java.util.List;

import dao.AccountDAO;
import dao.AccountDAOImp;
import dao.ClientDAO;
import dao.ClientDAOImp;
import dto.AddOrEditClientDTO;
import exception.AccountNotFoundException;
import exception.BadParameterException;
import exception.ClientNotFoundException;
import exception.DatabaseException;
import model.Client;
import model.ClientWithAccount;


public class ClientService {

	private ClientDAO clientDao;
	private AccountDAO accountDao;
	

	public ClientService() {
		this.clientDao = new ClientDAOImp();
		this.accountDao = new AccountDAOImp();
	}
	
	public ClientService(ClientDAO mockedClientDaoObject, AccountDAO mockedAccountDaoObject) {
		this.clientDao = mockedClientDaoObject;
		this.accountDao = mockedAccountDaoObject;
	}
	

	public List<Client> getAllClients() throws DatabaseException, ClientNotFoundException {
		List<Client> clients;
		try {
			clients = clientDao.getAllClients();
			if(clients.size() == 0) {
				throw new ClientNotFoundException("No client in the bank");
			}
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with our DAO operations");
		} 
		
		return clients;
	}
	
	public Client getClientById(String clientid) throws DatabaseException, BadParameterException, ClientNotFoundException {
		Client client;
		try {
			int id = Integer.parseInt(clientid);
			if(id < 0) {
				throw new BadParameterException("Client id cannot be negative");
			}
			client = clientDao.getClientByid(id);
			if(client == null) {
				throw new ClientNotFoundException("Client not found wiht id: " + id);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + " was pass in, but it is not a integer");
		}
		
		return client;
	}
	
	public Client addClient(AddOrEditClientDTO client) throws DatabaseException, BadParameterException {
		try {
			if(!client.getFirstName().matches("[a-zA-Z]+")) {
				throw new BadParameterException("input for first name contain non alphabetic characters or null");
			}
			if(!client.getLastName().matches("[a-zA-Z]+")) {
				throw new BadParameterException("input for last name contain non alphabetic characters or null");
			}
			String tempf = client.getFirstName();
			String firstName = tempf.substring(0, 1).toUpperCase() + tempf.substring(1).toLowerCase();
			client.setFirstName(firstName);
			String templ = client.getFirstName();
			String lastName = templ.substring(0, 1).toUpperCase() + templ.substring(1).toLowerCase();
			client.setFirstName(lastName);
			Client newClient = clientDao.addClient(client);
			return newClient;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} 
		
		
	}
	
	public Client editClient(String clientid, AddOrEditClientDTO client) throws DatabaseException, BadParameterException {
		try {
			if(!client.getFirstName().matches("[a-zA-Z]+")) {
				throw new BadParameterException("input for first name contain non alphabetic characters or null");
			}
			if(!client.getLastName().matches("[a-zA-Z]+")) {
				throw new BadParameterException("input for last name contain non alphabetic characters or null");
			}
			String tempf = client.getFirstName();
			String firstName = tempf.substring(0, 1).toUpperCase() + tempf.substring(1).toLowerCase();
			client.setFirstName(firstName);
			String templ = client.getFirstName();
			String lastName = templ.substring(0, 1).toUpperCase() + templ.substring(1).toLowerCase();
			client.setFirstName(lastName);
			int id = Integer.parseInt(clientid);
			if(id < 0) {
				throw new BadParameterException("Client id cannot be negative");
			}
			Client editedClient = clientDao.editClientByid(id, client);
			return editedClient;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + "was pass in, but it is not a integer");
		}
	}
	
	public void deleteClient(String clientid) throws DatabaseException, BadParameterException {
		try {
			int id = Integer.parseInt(clientid);
			if(id < 0) {
				throw new BadParameterException("Client id cannot be negative");
			}
			if(accountDao.getAllAccount(id).size() != 0) {
				throw new DatabaseException("Cannot delete client before delete accounts belong to the client");
			}
			clientDao.deleteClientByid(id);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} catch(NumberFormatException e) {
			throw new BadParameterException(clientid + "was pass in, but it is not a integer");
		}
	}
	
	public List<ClientWithAccount> clientWithAccount(String clientid) throws DatabaseException, BadParameterException, AccountNotFoundException {
		List<ClientWithAccount> clientWithAccount;
		try {
			int id = Integer.parseInt(clientid);
			if(id < 0) {
				throw new BadParameterException("Client id cannot be negative");
			}
			clientWithAccount = clientDao.clientWithAccounts(id);
			if(clientWithAccount.size() == 0) {
				throw new AccountNotFoundException("Client don't have any account");
			}
			return clientWithAccount;
		}catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		}catch(NumberFormatException e) {
			throw new BadParameterException(clientid + "was pass in, but it is not a integer");
		}
	}
}
