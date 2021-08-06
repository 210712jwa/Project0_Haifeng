import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//
//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

import dao.ClientDAO;
import exception.DatabaseException;
import model.Client;
import service.ClientService;

public class ServiceUintTest {

//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}


		
		private ClientService clientService;
		private ClientDAO clientDao;
		
		@Before
		public void setUp() throws Exception {
			this.clientDao = mock(ClientDAO.class); 
			
			this.clientService = new ClientService(clientDao); 
		}

		@Test
		public void test_getAllClients_positive() throws DatabaseException, SQLException {

			List<Client> mockReturnValues = new ArrayList<>();
			mockReturnValues.add(new Client(1000, "Adam" , "Apple"));
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

}
