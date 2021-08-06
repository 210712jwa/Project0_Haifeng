package controller;



import java.util.List;

import dto.AddOrEditAccountDTO;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import service.AccountService;
import model.Account;

public class AccountController implements Controller {
	private AccountService accountService;
	public AccountController() {
		this.accountService = new AccountService();
	}
	
	private Handler addAccount = (ctx) -> {
		AddOrEditAccountDTO account = ctx.bodyAsClass(AddOrEditAccountDTO.class);
		Account newAccount = accountService.addAccount(account);
		ctx.json(newAccount);
	};

	private Handler getAllAccount = (ctx) -> {
		String clientid = ctx.pathParam("clientid");
		List<Account> accounts = accountService.getAllAccount(clientid);
		ctx.status(200);
		ctx.json(accounts);
		
	};
	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/client/:clientid/account", getAllAccount);
		app.post("/client/account", addAccount );
		
	}

}
