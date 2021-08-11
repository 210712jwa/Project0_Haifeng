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
	
	
	//get all client from bank
	public List<Client> getAllClients() throws DatabaseException, ClientNotFoundException {
		List<Client> clients;
		try {
			clients = clientDao.getAllClients();
			//check if there any client in bank
			if(clients.size() == 0) {
				throw new ClientNotFoundException("No client in the bank");
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} 
		
		return clients;
	}
	
	//get client with client id
	public Client getClientById(String clientid) throws DatabaseException, BadParameterException, ClientNotFoundException {
		Client client;
		try {
			int id = Integer.parseInt(clientid);
			//check client id positive
			if(id < 0) {
				throw new BadParameterException("Client id cannot be negative");
			}
			client = clientDao.getClientByid(id);
			
			//if no client return then there is no client in the bank
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
			
			//check name is alphabetic
			if(!client.getFirstName().matches("[a-zA-Z]+")) {
				throw new BadParameterException("input for first name contain non alphabetic characters or null");
			}
			if(!client.getLastName().matches("[a-zA-Z]+")) {
				throw new BadParameterException("input for last name contain non alphabetic characters or null");
			}
			
			//Capitalize the name
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
	
	//edit client name
	public Client editClient(String clientid, AddOrEditClientDTO client) throws DatabaseException, BadParameterException {
		try {
			//check name alphabetic
			if(!client.getFirstName().matches("[a-zA-Z]+")) {
				throw new BadParameterException("input for first name contain non alphabetic characters or null");
			}
			if(!client.getLastName().matches("[a-zA-Z]+")) {
				throw new BadParameterException("input for last name contain non alphabetic characters or null");
			}
			
			//capitalize name
			String tempf = client.getFirstName();
			String firstName = tempf.substring(0, 1).toUpperCase() + tempf.substring(1).toLowerCase();
			client.setFirstName(firstName);
			String templ = client.getFirstName();
			String lastName = templ.substring(0, 1).toUpperCase() + templ.substring(1).toLowerCase();
			client.setFirstName(lastName);
			int id = Integer.parseInt(clientid);
			
			//check client id positive
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
			//check id positive
			if(id < 0) {
				throw new BadParameterException("Client id cannot be negative");
			}
			
			if(accountDao.getAllAccount(id).size() > 0){
				accountDao.deleteAllAccount(id);
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
			
			//check id positive
			if(id < 0) {
				throw new BadParameterException("Client id cannot be negative");
			}
			clientWithAccount = clientDao.clientWithAccounts(id);
			
			//check client have any account
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
