package main;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.AccountController;
import controller.ClientController;
import controller.Controller;
import exception.DatabaseException;
import io.javalin.Javalin;
import model.Client;
import service.ClientService;;

public class Application {
	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);

		
		
		
		public static void main(String[] args) {
		
			app = Javalin.create();
			mapControllers(new ClientController(), new AccountController());
			
			app.start(7000); 
		}
		
		public static void mapControllers(Controller... controllers) {
			for (Controller c : controllers) {
				c.mapEndpoints(Application.app);
			}
		
		

	}

}
