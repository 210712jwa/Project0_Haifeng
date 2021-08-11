package test;
import static org.junit.Assert.*;

import org.junit.Before;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.AccountDAO;
import dao.ClientDAO;
import dto.AddAccountDTO;
import exception.AccountNotFoundException;
import exception.BadAccountTypeException;
import exception.BadDecimalException;
import exception.BadParameterException;
import exception.ClientNotFoundException;
import exception.DatabaseException;
import model.Account;
import model.Client;
import service.AccountService;

public class AccountServiceTest {
	
	private AccountService accountService;
	private AccountDAO accountDao;
	private ClientDAO clientDao;
	

	@Before
	public void setUp() throws Exception {
		this.accountDao = mock(AccountDAO.class);
		this.clientDao = mock(ClientDAO.class);
		this.accountService = new AccountService(accountDao, clientDao);
	}

	@Test
	public void add_account_positive() throws SQLException, DatabaseException, BadParameterException, BadDecimalException, BadAccountTypeException, ClientNotFoundException {
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1023.33);
		dto.setAccountType("CHECKING");
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Bob", "Banna"));;
		when(accountDao.addAccount(dto)).thenReturn(new Account(10, 1023.33, "CHECKING", 10));
		
		Account actual = accountService.addAccount("10", dto);
		
		Account expect = new Account(10, 1023.33, "CHECKING", 10);
		assertEquals(actual, expect);
	}
	
	@Test
	public void add_account_negative_balance() throws DatabaseException, BadParameterException, BadDecimalException, BadAccountTypeException, ClientNotFoundException {
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(-1000.222);
		try {
			accountService.addAccount("10", dto);
			fail();
		}catch(BadParameterException e) {
			assertEquals("cannot have negative balance", e.getMessage());
		}
	}
	
	@Test
	public void add_account_bad_balance_decimal_number() throws DatabaseException, BadParameterException, BadDecimalException, BadAccountTypeException, ClientNotFoundException {
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1000.222);
		try {
			accountService.addAccount("10", dto);
			fail();
		}catch(BadDecimalException e) {
			assertEquals("cannot have more than two decimal place for balance", e.getMessage());
		}
	}
	
	@Test
	public void add_account_null_account_type() throws DatabaseException, BadDecimalException, BadParameterException, ClientNotFoundException {
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1000.00);
		dto.setAccountType("");
		try {
			accountService.addAccount("10", dto);
			fail();
		}catch(BadAccountTypeException e) {
			assertEquals("Not a vaild Account Type", e.getMessage());
		}
	}
	
	@Test
	public void add_account_bad_account_type() throws DatabaseException, BadDecimalException, BadParameterException, ClientNotFoundException {
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1000.00);
		dto.setAccountType("GOLD");
		try {
			accountService.addAccount("10", dto);
			fail();
		}catch(BadAccountTypeException e) {
			assertEquals("Not a vaild Account Type", e.getMessage());
		}
	}
	
	@Test
	public void add_account_client_id_not_int() throws DatabaseException, BadDecimalException, BadAccountTypeException, ClientNotFoundException{
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1000.00);
		dto.setAccountType("CHECKING");
		try {
			accountService.addAccount("a", dto);
			fail();
		}catch(BadParameterException e) {
			assertEquals("a" + "is not an acceptable input", e.getMessage());
		}
	}
	
	
	@Test
	public void add_account_client_id_null() throws DatabaseException, BadDecimalException, BadAccountTypeException, ClientNotFoundException{
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1000.00);
		dto.setAccountType("CHECKING");
		try {
			accountService.addAccount("", dto);
			fail();
		}catch(BadParameterException e) {
			assertEquals("is not an acceptable input", e.getMessage());
		}
	}
	
	@Test
	public void add_account_client_id_negative() throws DatabaseException, BadDecimalException, BadAccountTypeException, ClientNotFoundException{
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1000.00);
		dto.setAccountType("CHECKING");
		try {
			accountService.addAccount("-1", dto);
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id can not be negative", e.getMessage());
		}
	}
	
	@Test
	public void add_account_client_not_exist() throws DatabaseException, BadDecimalException, BadParameterException, BadAccountTypeException {
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1000.00);
		dto.setAccountType("CHECKING");
		try {
			accountService.addAccount("10", dto);
			fail();
		}catch(ClientNotFoundException e) {
			assertEquals("Client not exist", e.getMessage());
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void test_addAccount_SQLExceptionEncountered() throws SQLException, DatabaseException, BadParameterException, BadDecimalException, BadAccountTypeException, ClientNotFoundException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Bob", "Banna"));;
		when(accountDao.addAccount(any())).thenThrow(SQLException.class);
		AddAccountDTO dto = new AddAccountDTO();
		dto.setAccountBalance(1000.00);
		dto.setAccountType("CHECKING");
		accountService.addAccount("10", dto);
	}
	
	@Test
	public void get_all_account_positive() throws SQLException, DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		List<Account> mockAccountValue = new ArrayList<>();
		when(clientDao.getClientByid(eq(11))).thenReturn(new Client(11, "Bob", "Banana"));
		mockAccountValue.add(new Account(10, 1234.56, "CHECKING", 11));
		mockAccountValue.add(new Account(12, 234.56, "SAVING",11));
		mockAccountValue.add(new Account(14, 34.56, "SAVING",11));
		when(accountDao.getAllAccount(eq(11))).thenReturn(mockAccountValue);
		List<Account> actual = accountService.getAllAccount("11");
		List<Account> expected = new ArrayList<>();
		expected.add(new Account(10, 1234.56, "CHECKING", 11));
		expected.add(new Account(12, 234.56, "SAVING", 11));
		expected.add(new Account(14, 34.56, "SAVING",11));
		assertEquals(actual, expected);
	}
	
	@Test
	public void get_all_account_client_not_exist() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAllAccount("12");
			fail();
		}catch(ClientNotFoundException e) {
			assertEquals("Client not exist", e.getMessage());
		}
	}

	
	@Test(expected = DatabaseException.class)
	public void get_all_account_SQLExceptionEncounter() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException, SQLException {
		when(accountDao.getAllAccount(anyInt())).thenThrow(SQLException.class);
		when(clientDao.getClientByid(eq(11))).thenReturn(new Client(11, "Adam", "Apple"));
		accountService.getAllAccount("11");	
	}
	
	@Test
	public void get_all_account_integer_not_int() throws DatabaseException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAllAccount("wdjaiw");
			fail();
		}catch(BadParameterException e) {
			assertEquals("wdjaiw is not an acceptable input", e.getMessage());
		}
	}
	
	
	@Test
	public void get_all_account_integer_null() throws DatabaseException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAllAccount("");
			fail();
		}catch(BadParameterException e) {
			assertEquals(" is not an acceptable input", e.getMessage());
		}
	}
	
	@Test
	public void get_account_Greater_than_positive() throws SQLException, AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		List<Account> mockAccounts = new ArrayList<Account>();
		mockAccounts.add(new Account(2, 500.00, "CHECKING", 10));
		mockAccounts.add(new Account(3, 501.23, "SAVING", 10));
		mockAccounts.add(new Account(4, 1999.56, "CHECKING", 10));
		mockAccounts.add(new Account(5, 2000.00, "SAVING", 10));
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Bob", "Banna"));
		when(accountDao.getAccountGreaterThan(10, 2000.00)).thenReturn(mockAccounts);
		List<Account> actual = accountService.getAccountGreaterThan("10", "2000.00");
		List<Account> expected = new ArrayList<Account>();
		expected.add(new Account(2, 500.00, "CHECKING", 10));
		expected.add(new Account(3, 501.23, "SAVING", 10));
		expected.add(new Account(4, 1999.56, "CHECKING", 10));
		expected.add(new Account(5, 2000.00, "SAVING", 10));
		assertEquals(actual, expected);
	}
	
	@Test (expected = DatabaseException.class)
	public void get_account_greater_than_DatabaseException() throws SQLException, AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walter", "Melon"));
		when(accountDao.getAccountGreaterThan(10, 2000)).thenThrow(SQLException.class);
		accountService.getAccountGreaterThan("10", "2000.00");
	}
	
	@Test
	public void get_account_greater_than_max_string() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountGreaterThan("10", "wadad");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_Greater_than_client_id_string() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			accountService.getAccountGreaterThan("1was", "2000");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_Greater_than_client_id_null() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			accountService.getAccountGreaterThan("", "2000");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_Greater_than_max_negative() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountGreaterThan("10", "-2000");
			fail();
		}catch(BadParameterException e) {
			assertEquals("cannot have negative for balance", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_Greater_than_client_id_negative() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			accountService.getAccountGreaterThan("-10", "2000");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id can not be negative", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_less_than_positive() throws SQLException, AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		List<Account> mockAccounts = new ArrayList<Account>();
		mockAccounts.add(new Account(2, 500.00, "CHECKING", 10));
		mockAccounts.add(new Account(3, 501.23, "SAVING", 10));
		mockAccounts.add(new Account(4, 1999.56, "CHECKING", 10));
		mockAccounts.add(new Account(5, 2000.00, "SAVING", 10));
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Bob", "Banna"));
		when(accountDao.getAccountLessThan(10, 2000.00)).thenReturn(mockAccounts);
		List<Account> actual = accountService.getAccountLessThan("10", "2000.00");
		List<Account> expected = new ArrayList<Account>();
		expected.add(new Account(2, 500.00, "CHECKING", 10));
		expected.add(new Account(3, 501.23, "SAVING", 10));
		expected.add(new Account(4, 1999.56, "CHECKING", 10));
		expected.add(new Account(5, 2000.00, "SAVING", 10));
		assertEquals(actual, expected);
	}
	
	@Test (expected = DatabaseException.class)
	public void get_account_less_than_DatabaseException() throws SQLException, AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walter", "Melon"));
		when(accountDao.getAccountLessThan(10, 2000)).thenThrow(SQLException.class);
		accountService.getAccountLessThan("10", "2000.00");
	}
	
	@Test
	public void get_account_less_than_max_string() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountLessThan("10", "wadad");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_less_than_client_id_string() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			accountService.getAccountLessThan("1was", "2000");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_less_than_client_id_null() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			accountService.getAccountLessThan("", "2000");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_less_than_max_negative() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountLessThan("10", "-2000");
			fail();
		}catch(BadParameterException e) {
			assertEquals("cannot have negative for balance", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_less_than_client_id_negative() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			accountService.getAccountLessThan("-10", "2000");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id can not be negative", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_by_balance_positive() throws SQLException, AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		List<Account> mockAccounts = new ArrayList<Account>();
		mockAccounts.add(new Account(2, 500.00, "CHECKING", 10));
		mockAccounts.add(new Account(3, 501.23, "SAVING", 10));
		mockAccounts.add(new Account(4, 1999.56, "CHECKING", 10));
		mockAccounts.add(new Account(5, 2000.00, "SAVING", 10));
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Bob", "Banna"));
		when(accountDao.getAccountByBalance(10, 500.00, 2000.00)).thenReturn(mockAccounts);
		List<Account> actual = accountService.getAccountByBalance("10", "500.00", "2000.00");
		List<Account> expected = new ArrayList<Account>();
		expected.add(new Account(2, 500.00, "CHECKING", 10));
		expected.add(new Account(3, 501.23, "SAVING", 10));
		expected.add(new Account(4, 1999.56, "CHECKING", 10));
		expected.add(new Account(5, 2000.00, "SAVING", 10));
		assertEquals(actual, expected);
	}
	
	@Test (expected = DatabaseException.class)
	public void get_account_by_balance_DatabaseException() throws SQLException, AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walter", "Melon"));
		when(accountDao.getAccountByBalance(10, 1000, 2000)).thenThrow(SQLException.class);
		accountService.getAccountByBalance("10", "1000.00", "2000.00");
	}
	
	
	@Test
	public void get_account_by_balance_min_string() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountByBalance("10", "wadad", "2000.32");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_by_balance_min_negative() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountByBalance("10", "-200", "2000.32");
			fail();
		}catch(BadParameterException e) {
			assertEquals("cannot have negative for balance", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_by_balance_max_negative() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountByBalance("10", "200", "-2000.32");
			fail();
		}catch(BadParameterException e) {
			assertEquals("cannot have negative for balance", e.getMessage());
		}
		
	}

	
	@Test
	public void get_account_by_balance_max_string() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountByBalance("10", "1000.00", "2ewa");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_by_balance_with_no_match_account() throws SQLException, AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, BadParameterException {
		try {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
		accountService.getAccountByBalance("10", "1000.00", "2000.00");
		fail();
		}catch(AccountNotFoundException e) {
			assertEquals("Client don't have account with balance between " + "1000.00" + " and "+ "2000.00", e.getMessage());
		}
	}
	
	
	@Test
	public void get_account_by_balance_min_bad_decimal_exception() throws AccountNotFoundException, ClientNotFoundException, DatabaseException, BadParameterException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "name", "last"));
			accountService.getAccountByBalance("10", "1000.023", "2000.00");
			fail();
		}catch(BadDecimalException e) {
			assertEquals("cannot have more than two decimal place for balance", e.getMessage());
		}
	}
	
	@Test
	public void get_account_by_balance_max_bad_decimal_exception() throws AccountNotFoundException, ClientNotFoundException, DatabaseException, BadParameterException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "name", "last"));
			accountService.getAccountByBalance("10", "1000.02", "2000.223");
		}catch(BadDecimalException e) {
			assertEquals("cannot have more than two decimal place for balance", e.getMessage());
		}
	}
	
	@Test
	public void get_account_by_balance_min_max_bad_decimal_exception() throws AccountNotFoundException, ClientNotFoundException, DatabaseException, BadParameterException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "name", "last"));
			accountService.getAccountByBalance("10", "1000.0232", "2000.223");
		}catch(BadDecimalException e) {
			assertEquals("cannot have more than two decimal place for balance", e.getMessage());
		}
	}
	
	@Test
	public void get_account_by_balance_max_more_than_one_decimal() throws AccountNotFoundException, ClientNotFoundException, DatabaseException, BadParameterException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "name", "last"));
			accountService.getAccountByBalance("10", "1000.02", "2000.22.23.3");
		}catch(BadDecimalException e) {
			assertEquals("Can not have more than one decimal for amount less than balance", e.getMessage());
		}
	}
	
	@Test
	public void get_account_by_balance_min_more_than_one_decimal() throws AccountNotFoundException, ClientNotFoundException, DatabaseException, BadParameterException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "name", "last"));
			accountService.getAccountByBalance("10", "1000.023.2", "2000.22");
		}catch(BadDecimalException e) {
			assertEquals("Can not have more than one decimal for amount greater than balance", e.getMessage());
		}
	}
	
	@Test
	public void get_account_by_balance_min_greater_than_max() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException, SQLException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Walmart", "Walgreen"));
			accountService.getAccountByBalance("10", "2000", "200");
			fail();
		}catch(BadParameterException e) {
			assertEquals("AmountGreaterThan cannot greater than amountLessThan", e.getMessage());
		}
		
	}
	
	
	@Test
	public void get_account_by_id_positive() throws SQLException, DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Ben", "Ten"));
		when(accountDao.getAccountByids(eq(10), eq(20))).thenReturn(new Account(20, 123.23, "SAVING", 10));
		Account actual = accountService.getAccountByid("10", "20");
		Account expected = new Account(20, 123.23, "SAVING", 10);
		assertEquals(actual, expected);
	}
	

	@Test
	public void get_account_by_client_id_null() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException {
		try {
			accountService.getAccountByBalance("", "1000.00", "2000.32");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_by_client_id_negative() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException {
		try {
			accountService.getAccountByBalance("-1", "1000.00", "2000.32");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id can not be negative", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_by_client_id_string() throws AccountNotFoundException, ClientNotFoundException, BadDecimalException, DatabaseException {
		try {
			accountService.getAccountByBalance("weoadp0", "1000.00", "2000.32");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not an acceptable request input", e.getMessage());
		}
		
	}
	
	@Test
	public void get_account_by_id_client_id_not_int() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAccountByid("wda2", "20");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Input for client id or account id s not an valid", e.getMessage());
		}
	}
	
	@Test
	public void get_account_by_id_client_id_negative() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAccountByid("-1", "20");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id can not be negative", e.getMessage());
		}
	}
	
	@Test
	public void get_accountByid_client_id_null() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAccountByid("", "20");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Input for client id or account id s not an valid", e.getMessage());
		}
	}
	
	@Test
	public void get_accountByid_account_id_negative() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAccountByid("10", "-20");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Account id can not be negative", e.getMessage());
		}
	}
	
	@Test
	public void get_accountByid_account_id_not_int() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAccountByid("20", "wwew");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Input for client id or account id s not an valid", e.getMessage());
		}
	}
	
	@Test
	public void get_accountByid_account_id_null() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.getAccountByid("20", "");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Input for client id or account id s not an valid", e.getMessage());
		}
	}
	
	@Test
	public void get_accountByid_null() throws DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException, SQLException {
		try {
			when(clientDao.getClientByid(20)).thenReturn(new Client(20, "some", "String"));
			accountService.getAccountByid("20", "10");
			fail();
		}catch(AccountNotFoundException e) {
			assertEquals("client 20 don't have account with id: 10", e.getMessage());
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void get_accountByid_no_account() throws DatabaseException, BadParameterException, ClientNotFoundException, SQLException, AccountNotFoundException {
		when(accountDao.getAccountByids(anyInt(),anyInt())).thenThrow(SQLException.class);
		when(clientDao.getClientByid(anyInt())).thenReturn(new Client(10, "April", "Apple"));
		accountService.getAccountByid("10", "20");
	}
	
	@Test
	public void edit_account_positive() throws SQLException, DatabaseException, BadDecimalException, BadParameterException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Abby", "Apple"));
		Account account = new Account();
		account.setAccountBalance(1000);
		account.setAccountType("CHECKING");
		when(accountDao.editAccountByids(account)).thenReturn(new Account(20, 1000, "CHECKING", 10));
		Account actual = accountService.editAccount("10", "20", account);
		Account expected = new Account(20, 1000, "CHECKING", 10);
		assertEquals(actual, expected);
	}
	
	@Test(expected = DatabaseException.class)
	public void edit_account_DatabaseException() throws SQLException, DatabaseException, BadDecimalException, BadParameterException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException {
		when(accountDao.editAccountByids(any())).thenThrow(SQLException.class);
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Abby", "Apple"));
		Account account = new Account();
		account.setAccountBalance(1000);
		account.setAccountType("CHECKING");
		accountService.editAccount("10", "20", account);	
	}
	
	@Test
	public void edit_account_client_id_null() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, SQLException {
		try {
			Account account = new Account();
			account.setAccountBalance(1000);
			account.setAccountType("CHECKING");
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "client", "some"));
			accountService.editAccount("", "20", account);
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	

	@Test
	public void edit_account_client_id_negative() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, SQLException {
		try {
			Account account = new Account();
			account.setAccountBalance(1000);
			account.setAccountType("CHECKING");
			accountService.editAccount("-10", "20", account);
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id can not be negative", e.getMessage());
		}
	}
	
	@Test
	public void edit_account_client_id_string() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, SQLException {
		try {
			Account account = new Account();
			account.setAccountBalance(1000);
			account.setAccountType("CHECKING");
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "client", "some"));
			accountService.editAccount("dawda", "20", account);
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	
	@Test
	public void edit_account_account_id_null() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, SQLException {
		try {
			Account account = new Account();
			account.setAccountBalance(1000);
			account.setAccountType("CHECKING");
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "client", "some"));
			accountService.editAccount("10", "", account);
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	
	@Test
	public void edit_account_account_id_negative() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, SQLException {
		try {
			Account account = new Account();
			account.setAccountBalance(1000);
			account.setAccountType("CHECKING");
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "client", "some"));
			accountService.editAccount("10", "-20", account);
			fail();
		}catch(BadParameterException e) {
			assertEquals("Account id can not be negative", e.getMessage());
		}
	}

	@Test
	public void edit_account_account_id_stringl() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, SQLException {
		try {
			Account account = new Account();
			account.setAccountBalance(1000);
			account.setAccountType("CHECKING");
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "client", "some"));
			accountService.editAccount("10", "wadaaw", account);
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	
	
	@Test
	public void edit_account_balance_decimal_more_than_two() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, BadParameterException, SQLException {
		try {
			Account account = new Account();
			account.setAccountBalance(1000.2134);
			account.setAccountType("CHECKING");
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "client", "some"));
			accountService.editAccount("10", "20", account);
			fail();
		}catch(BadDecimalException e) {
			assertEquals("cannot have more than two decimal place for balance", e.getMessage());
		}
	}

	
	@Test
	public void edit_account_account_type_unknown() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, BadParameterException, SQLException {
		try {
			Account account = new Account();
			account.setAccountBalance(1000.24);
			account.setAccountType("BEST");
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "client", "some"));
			accountService.editAccount("10", "20", account);
			fail();
		}catch(BadAccountTypeException e) {
			assertEquals("Not a vaild Account Type", e.getMessage());
		}
	}
	
	@Test
	public void edit_account_account_type_null() throws DatabaseException, BadDecimalException, ClientNotFoundException, AccountNotFoundException, BadAccountTypeException, BadParameterException, SQLException {
		try {
		
			Account account = new Account();
			account.setAccountBalance(1000.24);
			account.setAccountType("");
			accountService.editAccount("10", "20", account);
			fail();
		}catch(BadAccountTypeException e) {
			assertEquals("Not a vaild Account Type", e.getMessage());
		}
	}
	
	@Test
	public void delete_account_positive() throws SQLException, DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Fox", "Say"));
		when(accountDao.getAccountByids(eq(10), eq(20))).thenReturn(new Account(20, 1000, "CHECKING", 10));
		Mockito.doNothing().when(accountDao).deleteAccountByids(anyInt(), anyInt());
		accountService.deleteAccount("10", "20");
		Mockito.verify(accountDao).deleteAccountByids(10, 20);
	}
	
	@Test
	public void delete_account_client_id_null() throws DatabaseException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.deleteAccount("", "20");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	
	@Test
	public void delete_account_client_id_negative() throws DatabaseException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.deleteAccount("-10", "20");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id can not be negative", e.getMessage());
		}
	}
	
	@Test
	public void delete_account_client_id_string() throws DatabaseException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.deleteAccount("wessl", "20");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	
	@Test
	public void delete_account_account_id_string() throws DatabaseException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.deleteAccount("10", "dawdiw");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	
	@Test
	public void delete_account_account_id_null() throws DatabaseException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.deleteAccount("10", "");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	
	@Test
	public void delete_account_account_id_negative() throws DatabaseException, ClientNotFoundException, AccountNotFoundException {
		try {
			accountService.deleteAccount("10", "-20");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Account id can not be negative", e.getMessage());
		}
	}
	
	@Test
	public void delete_account_client_not_exist() throws DatabaseException, BadParameterException, AccountNotFoundException {
		try {
			accountService.deleteAccount("10", "20");
			fail();
		}catch(ClientNotFoundException e) {
			assertEquals("Client not exist", e.getMessage());
		}
	}
	
	@Test
	public void delete_account_not_exist() throws DatabaseException, BadParameterException, AccountNotFoundException, SQLException, ClientNotFoundException {
		try {
			when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Jill", "Gill"));
			accountService.deleteAccount("10", "20");
			fail();
		}catch(AccountNotFoundException e) {
			assertEquals("Account not exist", e.getMessage());
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void delete_account_databaseException() throws SQLException, DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Bobby", "Banana"));
		when(accountDao.getAccountByids(eq(10), eq(20))).thenReturn(new Account(20, 1000, "CHECKING", 10));
		Mockito.doThrow(new SQLException()).when(accountDao).deleteAccountByids(anyInt(), anyInt());
		accountService.deleteAccount("10", "20");
		Mockito.verify(accountDao).deleteAccountByids(10, 20);
	}
	
	@Test
	public void delete_all_account_positive() throws SQLException, ClientNotFoundException, DatabaseException, BadParameterException {
		List<Account> mockAccounts = new ArrayList<>();
		mockAccounts.add(new Account(20, 1000.23, "CHECKING", 10));
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Bobby", "Banana"));
		when(accountDao.getAllAccount(eq(10))).thenReturn(mockAccounts);
		Mockito.doNothing().when(accountDao).deleteAllAccount(anyInt());
		accountService.deleteAllAccount("10");
		Mockito.verify(accountDao).deleteAllAccount(10);
	}
	
	@Test(expected = DatabaseException.class)
	public void delete_all_account_databaseException() throws SQLException, DatabaseException, BadParameterException, ClientNotFoundException, AccountNotFoundException {
		List<Account> mockAccounts = new ArrayList<>();
		mockAccounts.add(new Account(20, 1000.23, "CHECKING", 10));
		when(clientDao.getClientByid(eq(10))).thenReturn(new Client(10, "Bobby", "Banana"));
		when(accountDao.getAllAccount(eq(10))).thenReturn(mockAccounts);
		Mockito.doThrow(new SQLException()).when(accountDao).deleteAllAccount(anyInt());
		accountService.deleteAllAccount("10");
		Mockito.verify(accountDao).deleteAllAccount(10);
	}
	
	@Test
	public void delete_all_account_client_id_string() throws SQLException, ClientNotFoundException, DatabaseException {
		try {
			accountService.deleteAllAccount("wdawd");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
	
	@Test
	public void delete_all_account_client_id_negative() throws SQLException, ClientNotFoundException, DatabaseException {
		try {
			accountService.deleteAllAccount("-10");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Client id can not be negative", e.getMessage());
		}
	}
	
	@Test
	public void delete_all_account_client_id_null() throws SQLException, ClientNotFoundException, DatabaseException {
		try {
			accountService.deleteAllAccount("");
			fail();
		}catch(BadParameterException e) {
			assertEquals("Not a vaild input for client id or account id", e.getMessage());
		}
	}
}
