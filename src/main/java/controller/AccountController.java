package controller;



import java.util.List;

import dto.AddAccountDTO;
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
		String clientid = ctx.pathParam(":clientid");
		String accountBalance = ctx.body();
		Account newAccount = accountService.addAccount(clientid, accountBalance);
		ctx.json(newAccount);
	};

	private Handler getAllAccount = (ctx) -> {
		String clientid = ctx.pathParam("clientid");
		List<Account> accounts = accountService.getAllAccount(clientid);
		ctx.status(200);
		ctx.json(accounts);
		
	};
	
	private Handler editAccount = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		String accountid = ctx.pathParam(":accountid");
		String accountBalance = ctx.body();
		Account editedAccount = accountService.editAccount(clientid, accountid, accountBalance);
		ctx.json(editedAccount);
	};
	
	private Handler deleteAccount = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		String accountid = ctx.pathParam(":accountid");
		accountService.deleteAccount(clientid, accountid);
	};
	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/client/:clientid/account", getAllAccount);
		app.post("/client/:clientid/account", addAccount );
		app.put("/client/:clientid/account/:accountid", editAccount);
		app.delete("/client/:clientid/account/:accountid", deleteAccount);
	}

}
