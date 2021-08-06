package utility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import org.mariadb.jdbc.Driver;

public class ConnectionUtility {
	
	private ConnectionUtility() {
		
	}

	public static Connection getConnection() throws SQLException {
		
		DriverManager.registerDriver(new Driver());

		
		String url = "jdbc:mariadb://database-1.cu1xmacuozvv.us-east-2.rds.amazonaws.com:3306/project0";
		String username = "admin";
		String password = "somepass";

		Connection connection = DriverManager.getConnection(url, username, password);

		return connection;
	}

}
