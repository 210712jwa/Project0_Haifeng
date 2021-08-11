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
		AddAccountDTO account = ctx.bodyAsClass(AddAccountDTO.class);
		Account newAccount = accountService.addAccount(clientid, account);
		ctx.json(newAccount);
	};

	private Handler getAllAccount = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		if(ctx.queryParam("amountGreaterThan") != null && ctx.queryParam("amountLessThan") == null) {
			String min = ctx.queryParam("amountGreaterThan", "0");
			List<Account> accounts = accountService.getAccountGreaterThan(clientid, min);
			ctx.status(200);
			ctx.json(accounts);
		}else if(ctx.queryParam("amountGreaterThan") == null && ctx.queryParam("amountLessThan") != null) {
			String min = ctx.queryParam("amountLessThan", "0");
			List<Account> accounts = accountService.getAccountLessThan(clientid, min);
			ctx.status(200);
			ctx.json(accounts);
		}else if (ctx.queryParam("amountGreaterThan") != null || ctx.queryParam("amountLessThan") != null) {
			String min = ctx.queryParam("amountGreaterThan", "0");
			String max = ctx.queryParam("amountLessThan", "0");
			List<Account> accounts = accountService.getAccountByBalance(clientid, min, max);
			ctx.status(200);
			ctx.json(accounts);
		} else {
			List<Account> accounts = accountService.getAllAccount(clientid);
			if (accounts.size() == 0) {
				ctx.json("Client dont have any account");
				ctx.status(404);
			} else {
				ctx.status(200);
				ctx.json(accounts);
			}
		}

	};

	private Handler getSpecificAccount = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		String accountid = ctx.pathParam(":accountid");
		Account account = accountService.getAccountByid(clientid, accountid);
		ctx.json(account);
	};

	private Handler editAccount = (ctx) -> {
		Account account = ctx.bodyAsClass(Account.class);
		String clientid = ctx.pathParam(":clientid");
		String accountid = ctx.pathParam(":accountid");
		Account editedAccount = accountService.editAccount(clientid, accountid, account);
		ctx.json(editedAccount);
	};

	private Handler deleteAccount = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		String accountid = ctx.pathParam(":accountid");
		accountService.deleteAccount(clientid, accountid);
		ctx.json("deleted account for client " + clientid + " successfully");
	};

	private Handler deleteAllAccount = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		accountService.deleteAllAccount(clientid);
		ctx.json("deleted all account for client " + clientid + " successfully");
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/client/:clientid/account", getAllAccount);
		app.get("/client/:clientid/account/:accountid", getSpecificAccount);
		app.post("/client/:clientid/account", addAccount);
		app.put("/client/:clientid/account/:accountid", editAccount);
		app.delete("/client/:clientid/account/:accountid", deleteAccount);
		app.delete("/client/:clientid/accounts/all", deleteAllAccount);
	}

}
