package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import controller.AccountController;
import controller.ClientController;
import controller.Controller;
import controller.ExceptionController;
import io.javalin.Javalin;

public class Application {
	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);

		
		
		
		public static void main(String[] args) {
		
			app = Javalin.create();
			mapControllers(new ClientController(), new AccountController(), new ExceptionController());
			
			app.start(7000); 
		}
		
		public static void mapControllers(Controller... controllers) {
			for (Controller c : controllers) {
				c.mapEndpoints(Application.app);
			}
		
		

	}

}
