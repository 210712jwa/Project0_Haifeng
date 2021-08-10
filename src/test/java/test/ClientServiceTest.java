package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import org.junit.Test;
import org.mockito.Mockito;

import dao.AccountDAO;
import dao.ClientDAO;
import dto.AddOrEditClientDTO;
import exception.AccountNotFoundException;
import exception.BadParameterException;
import exception.ClientNotFoundException;
import exception.DatabaseException;
import model.Account;
import model.Client;
import model.ClientWithAccount;
import service.ClientService;

public class ClientServiceTest {


	private ClientService clientService;
	private ClientDAO clientDao;
	private AccountDAO accountDao;

	@Before
	public void setUp() throws Exception {
		this.clientDao = mock(ClientDAO.class);
		this.accountDao = mock(AccountDAO.class);
		this.clientService = new ClientService(clientDao, accountDao);
	}

	@Test
	public void test_getAllClients_positive() throws DatabaseException, SQLException, ClientNotFoundException {

		List<Client> mockReturnValues = new ArrayList<>();
		mockReturnValues.add(new Client(1000, "Adam", "Apple"));
		mockReturnValues.add(new Client(1001, "Bob", "Banana"));
		mockReturnValues.add(new Client(1002, "Chalie", "Cherry"));
		mockReturnValues.add(new Client(1003, "Dylan", "Dates"));
		mockReturnValues.add(new Client(1004, "Ella", "Elderberry"));

		when(clientDao.getAllClients()).thenReturn(mockReturnValues);

		List<Client> actual = clientService.getAllClients();

		List<Client> expected = new ArrayList<>();
		expected.add(new Client(1000, "Adam", "Apple"));
		expected.add(new Client(1001, "Bob", "Banana"));
		expected.add(new Client(1002, "Chalie", "Cherry"));
		expected.add(new Client(1003, "Dylan", "Dates"));
		expected.add(new Client(1004, "Ella", "Elderberry"));

		assertEquals(expected, actual);
	}

	@Test
	public void test_get_all_clients_no_client() throws DatabaseException {
		try {
			clientService.getAllClients();
			fail();
		} catch (ClientNotFoundException e) {
			assertEquals("No client in the bank", e.getMessage());
		}
	}

	@Test(expected = DatabaseException.class)
	public void test_get_all_clients_databaseException()
			throws SQLException, DatabaseException, ClientNotFoundException {
		when(clientDao.getAllClients()).thenThrow(SQLException.class);
		clientService.getAllClients();
	}

	/*
	 * getClientByid
	 */
	@Test
	public void test_get_client_by_id_postive()
			throws SQLException, DatabaseException, BadParameterException, ClientNotFoundException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Rob", "Bob"));
		Client actual = clientService.getClientById("10");
		Client expected = new Client(10, "Rob", "Bob");
		assertEquals(actual, expected);
	}

	@Test
	public void test_get_client_by_id_id_null() throws DatabaseException, ClientNotFoundException {
		try {
			clientService.getClientById("");
			fail();
		} catch (BadParameterException e) {
			assertEquals(" was pass in, but it is not a integer", e.getMessage());
		}
	}

	@Test
	public void test_get_client_by_id_id_string() throws DatabaseException, ClientNotFoundException {
		try {
			clientService.getClientById("dawda");
			fail();
		} catch (BadParameterException e) {
			assertEquals("dawda was pass in, but it is not a integer", e.getMessage());
		}
	}

	@Test
	public void test_get_client_by_id_id_space() throws DatabaseException, ClientNotFoundException {
		try {
			clientService.getClientById(" ");
			fail();
		} catch (BadParameterException e) {
			assertEquals("  was pass in, but it is not a integer", e.getMessage());
		}
	}

	@Test
	public void test_get_client_by_id_id_negative() throws DatabaseException, ClientNotFoundException {
		try {
			clientService.getClientById("-1");
			fail();
		} catch (BadParameterException e) {
			assertEquals("Client id cannot be negative", e.getMessage());
		}
	}

	@Test
	public void test_get_client_by_id_id_client_not_exist()
			throws DatabaseException, SQLException, BadParameterException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(null);
			clientService.getClientById("10");
			fail();
		} catch (ClientNotFoundException e) {
			assertEquals("Client not found wiht id: 10", e.getMessage());
		}
	}

	@Test(expected = DatabaseException.class)
	public void test_get_client_by_id_negative()
			throws DatabaseException, BadParameterException, SQLException, ClientNotFoundException {
		when(clientDao.getClientByid(isA(Integer.class))).thenThrow(SQLException.class);
		clientService.getClientById("10");

	}

	@Test
	public void test_add_client_positive() throws DatabaseException, BadParameterException, SQLException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setFirstName("Jack");
		dto.setLastName("Rose");
		when(clientDao.addClient(dto)).thenReturn(new Client(10, "Jack", "Rose"));
		Client actual = clientService.addClient(dto);
		Client expected = clientService.addClient(dto);
		assertEquals(actual, expected);
	}

	@Test
	public void test_add_client_first_name_null() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("");
			dto.setLastName("Rose");
			clientService.addClient(dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("input for first name contain non alphabetic characters or null", e.getMessage());
		}
	}

	@Test
	public void test_add_client_first_name_number() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("21sajd");
			dto.setLastName("Rose");
			clientService.addClient(dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("input for first name contain non alphabetic characters or null", e.getMessage());
		}
	}

	@Test
	public void test_add_client_last_name_number() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("Rose");
			dto.setLastName("219");
			clientService.addClient(dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("input for last name contain non alphabetic characters or null", e.getMessage());
		}
	}

	@Test
	public void test_add_client_last_name_null() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("Rose");
			dto.setLastName("");
			clientService.addClient(dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("input for last name contain non alphabetic characters or null", e.getMessage());
		}
	}

	@Test(expected = DatabaseException.class)
	public void test_add_client_database_exception() throws SQLException, DatabaseException, BadParameterException {
		when(clientDao.addClient(any())).thenThrow(SQLException.class);
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setFirstName("some");
		dto.setLastName("string");
		clientService.addClient(dto);
	}

	@Test
	public void test_edit_client_positive() throws SQLException, DatabaseException, BadParameterException {
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setFirstName("Some");
		dto.setLastName("String");
		when(clientDao.editClientByid(10, dto)).thenReturn(new Client(10, "Some", "String"));
		Client expected = clientService.editClient("10", dto);
		Client actual = new Client(10, "Some", "String");
		assertEquals(expected, actual);
	}

	@Test
	public void test_edit_client_first_name_null() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("");
			dto.setLastName("String");
			clientService.editClient("10", dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("input for first name contain non alphabetic characters or null", e.getMessage());
		}
	}

	@Test
	public void test_edit_client_client_id_not_int() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("Some");
			dto.setLastName("String");
			clientService.editClient("o", dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("o" + "was pass in, but it is not a integer", e.getMessage());
		}
	}

	@Test
	public void test_edit_client_client_id_null() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("Some");
			dto.setLastName("String");
			clientService.editClient("", dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("" + "was pass in, but it is not a integer", e.getMessage());
		}
	}

	@Test
	public void test_edit_client_client_id_negative() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("Some");
			dto.setLastName("String");
			clientService.editClient("-1", dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("Client id cannot be negative", e.getMessage());
		}
	}

	@Test
	public void test_edit_client_first_name_non_alpha() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("9wq./?");
			dto.setLastName("String");
			clientService.editClient("10", dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("input for first name contain non alphabetic characters or null", e.getMessage());
		}
	}

	@Test
	public void test_edit_client_last_name_null() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("Some");
			dto.setLastName("");
			clientService.editClient("10", dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("input for last name contain non alphabetic characters or null", e.getMessage());
		}
	}

	@Test
	public void test_edit_client_last_name_non_alphabetic() throws DatabaseException {
		try {
			AddOrEditClientDTO dto = new AddOrEditClientDTO();
			dto.setFirstName("Some");
			dto.setLastName("212.dw0");
			clientService.editClient("10", dto);
			fail();
		} catch (BadParameterException e) {
			assertEquals("input for last name contain non alphabetic characters or null", e.getMessage());
		}
	}

	@Test(expected = DatabaseException.class)
	public void test_edit_client_database_exception() throws DatabaseException, BadParameterException, SQLException {
		when(clientDao.editClientByid(anyInt(), any())).thenThrow(SQLException.class);
		AddOrEditClientDTO dto = new AddOrEditClientDTO();
		dto.setFirstName("Some");
		dto.setLastName("String");
		clientService.editClient("10", dto);
	}

	@Test
	public void test_delete_client_positive() throws SQLException, DatabaseException, BadParameterException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Fox", "Say"));
		Mockito.doNothing().when(accountDao).deleteAccountByids(anyInt(), anyInt());
		clientService.deleteClient("10");
		Mockito.verify(clientDao).deleteClientByid(10);
	}

	@Test
	public void test_delete_client_id_string() throws DatabaseException {
		try {
			clientService.deleteClient("dd");
			fail();
		} catch (BadParameterException e) {
			assertEquals("dd" + "was pass in, but it is not a integer", e.getMessage());
		}
	}

	@Test
	public void test_delete_client_id_null() throws DatabaseException {
		try {
			clientService.deleteClient("");
			fail();
		} catch (BadParameterException e) {
			assertEquals("" + "was pass in, but it is not a integer", e.getMessage());
		}
	}

	@Test
	public void test_delete_client_account_exist() throws SQLException, BadParameterException {
		try {
			List<Account> mockAccounts = new ArrayList<>();
			mockAccounts.add(new Account(20, 1000.32, "CHECKING", 10));
			when(accountDao.getAllAccount(eq(10))).thenReturn(mockAccounts);
			clientService.deleteClient("10");
			fail();
		} catch (DatabaseException e) {
			assertEquals("Cannot delete client before delete accounts belong to the client", e.getMessage());
		}

	}

	@Test(expected = DatabaseException.class)
	public void test_delete_client_database_exception() throws SQLException, DatabaseException, BadParameterException {
		List<Account> mockAccounts = new ArrayList<>();
		mockAccounts.add(new Account(20, 1000.32, "CHECKING", 10));
		when(accountDao.getAllAccount(eq(10))).thenReturn(mockAccounts);
		Mockito.doThrow(new SQLException()).when(clientDao).deleteClientByid(anyInt());
		clientService.deleteClient("10");
		Mockito.verify(clientDao).deleteClientByid(10);
		}
	
	@Test
	public void test_client_with_account_positive() throws SQLException, DatabaseException, BadParameterException, AccountNotFoundException {
		List<ClientWithAccount> mockClientWithAccount = new ArrayList<>();
		mockClientWithAccount.add(new ClientWithAccount(10, "Good", "Night", 20, 1000.32, "CHECKING"));
		mockClientWithAccount.add(new ClientWithAccount(10, "Good", "Night", 21, 13300.32, "SAVING"));
		when(clientDao.clientWithAccounts(eq(10))).thenReturn(mockClientWithAccount);
		List<ClientWithAccount> actual = clientService.clientWithAccount("10");
		List<ClientWithAccount> expected = new ArrayList<>();
		expected.add(new ClientWithAccount(10, "Good", "Night", 20, 1000.32, "CHECKING"));
		expected.add(new ClientWithAccount(10, "Good", "Night", 21, 13300.32, "SAVING"));
		assertEquals(actual, expected);
	}
	
	@Test
	public void test_client_with_account_int_string() throws DatabaseException, AccountNotFoundException {
		try {
			clientService.clientWithAccount("dasdde");
			fail();
		}catch(BadParameterException e) {
			assertEquals("dasdde" + "was pass in, but it is not a integer", e.getMessage());
		}
	}
	
	@Test
	public void test_client_with_account_int_null() throws DatabaseException, AccountNotFoundException {
		try {
			clientService.clientWithAccount("");
			fail();
		}catch(BadParameterException e) {
			assertEquals("" + "was pass in, but it is not a integer", e.getMessage());
		}
	}
	
	@Test
	public void test_client_with_account_int_negative() throws DatabaseException, AccountNotFoundException {
		try {
			clientService.clientWithAccount("-1");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id cannot be negative", e.getMessage());
		}
	}
	
	@Test
	public void test_client_with_account_no_account() throws DatabaseException, AccountNotFoundException, SQLException, BadParameterException {
		try {
			List<ClientWithAccount> mockclientWithAccount = new ArrayList<>();
			when(clientDao.clientWithAccounts(eq(10))).thenReturn(mockclientWithAccount);
			clientService.clientWithAccount("10");
			fail();
		}catch(AccountNotFoundException e) {
			assertEquals("Client don't have any account", e.getMessage());
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void test_client_with_account_negative() throws SQLException, DatabaseException, BadParameterException, AccountNotFoundException {
		when(clientDao.clientWithAccounts(anyInt())).thenThrow(SQLException.class);
		clientService.clientWithAccount("10");
	}
	

}
