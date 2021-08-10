package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dto.ExceptionMessageDTO;
import exception.*;
import io.javalin.Javalin;

public class ExceptionController implements Controller {
	
	private Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.exception(AccountNotFoundException.class, (e, ctx) -> {
			logger.info("ShipNotFoundException occurred from " + ctx.method() + " " + ctx.path() +  ". Message is " + e.getMessage());

			ctx.status(404); // 404 is "Not Found"
			
			ExceptionMessageDTO messageDTO = new ExceptionMessageDTO();
			messageDTO.setMessage(e.getMessage());
			
			ctx.json(messageDTO);
		});
		
		app.exception(BadAccountTypeException.class, (e, ctx) -> {
			logger.info("AccountTypeException occurred from " + ctx.method() + " " + ctx.path() +  ". Message is " + e.getMessage());

			ctx.status(400); // 404 is "Not Found"
			
			ExceptionMessageDTO messageDTO = new ExceptionMessageDTO();
			messageDTO.setMessage(e.getMessage());
			
			ctx.json(messageDTO);
		});
		
		app.exception(BadDecimalException.class, (e, ctx) -> {
			logger.info("BadDecimalException occurred from " + ctx.method() + " " + ctx.path() +  ". Message is " + e.getMessage());

			ctx.status(400); // 404 is "Not Found"
			
			ExceptionMessageDTO messageDTO = new ExceptionMessageDTO();
			messageDTO.setMessage(e.getMessage());
			
			ctx.json(messageDTO);
		});
		
		app.exception(BadParameterException.class, (e, ctx) -> {
			logger.info("BadParameterException occurred from " + ctx.method() + " " + ctx.path() +  ". Message is " + e.getMessage());

			ctx.status(400); // 404 is "Not Found"
			
			ExceptionMessageDTO messageDTO = new ExceptionMessageDTO();
			messageDTO.setMessage(e.getMessage());
			
			ctx.json(messageDTO);
		});
		
		app.exception(ClientNotFoundException.class, (e, ctx) -> {
			logger.info("ClientNotFoundException occurred from " + ctx.method() + " " + ctx.path() +  ". Message is " + e.getMessage());

			ctx.status(404); // 404 is "Not Found"
			
			ExceptionMessageDTO messageDTO = new ExceptionMessageDTO();
			messageDTO.setMessage(e.getMessage());
			
			ctx.json(messageDTO);
		});
		
		app.exception(DatabaseException.class, (e, ctx) -> {
			logger.info("DatabaseException occurred from " + ctx.method() + " " + ctx.path() +  ". Message is " + e.getMessage());

			ctx.status(500); // 404 is "Not Found"
			
			ExceptionMessageDTO messageDTO = new ExceptionMessageDTO();
			messageDTO.setMessage(e.getMessage());
			
			ctx.json(messageDTO);
		});
		
	}
	
}
